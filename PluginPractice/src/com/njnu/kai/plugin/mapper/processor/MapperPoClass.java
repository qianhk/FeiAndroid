package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-29
 */
public class MapperPoClass {

    private Project mProject;
    private MapperPoClassListener mMapperPoClassListener;
    private WaitPOItem mWaitPOItem;

    private PsiElementFactory mFactory;
    private PsiClass mPoClass;
    private PsiClass mVoClass;
    private PsiClass mMapperClass;

    private static String OBJECT_TRANSFORM_METHOD = "/**\n" +
            "        * @param po persist object\n" +
            "        * @return view object\n" +
            "        */\n" +
            "        public static $MAPPED_CLASS$ transform($ORIGIN_CLASS$ po) {\n" +
            "        $MAPPED_CLASS$  vo = new $MAPPED_CLASS$();\n\n" +
            "        $BODY$\n" +
            "        return vo;\n" +
            "    }\n\n";

    private static String OBJECT_LIST_TRANSFORM_METHOD = "/**\n" +
            "        * @param poList list of persist object \n" +
            "        * @return list of view object \n" +
            "        */ \n" +
            "        public static List<$MAPPED_CLASS$> transform$ENTITY$List(List<$ORIGIN_CLASS$> poList) {\n" +
            "        List<$MAPPED_CLASS$>  voList = new ArrayList<>();\n\n" +
            "        if (poList != null) {" +
            "        for ($ORIGIN_CLASS$ po : poList) {\n" +
            "        if (po != null) {\n" +
            "        voList.add(transform(po));\n" +
            "        }\n" +
            "        }\n" +
            "        }\n" +
            "        return voList;\n" +
            "    }\n\n";

    private static String METHOD_BODY_ITEM = "vo.set$PROPERTY$(po.$GETTER$());\n";
    private static String METHOD_BODY_PO_ITEM = "vo.set$PROPERTY$(transform(po.$GETTER$()));\n";
    private static String METHOD_BODY_ITEM_LIST = "vo.set$PROPERTY$List(po.$GETTER$());\n";
    private static String METHOD_BODY_PO_ITEM_LIST = "vo.set$PROPERTY$List(transform$ENTITY$List(po.$GETTER$()));\n";

    public MapperPoClass(Project project, MapperPoClassListener mapperPoClassListener, WaitPOItem waitPOItem) {
        mProject = project;
        mMapperPoClassListener = mapperPoClassListener;
        mWaitPOItem = waitPOItem;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
    }

    public void execute(boolean newMapperClass) {
        mPoClass = mWaitPOItem.getPoClass();
        mVoClass = createClass(mWaitPOItem.getVoClassCanonicalName(), false);
        mMapperClass = createClass(mWaitPOItem.getMapperClassCanonicalName(), newMapperClass);
        generateObjectClass();
        generateMapperClass();
    }

    private void generateObjectClass() {
        generateFields(mVoClass);
        generateMethods(mVoClass);

        optimizeStyle(mVoClass);
    }

    private void generateMapperClass() {
        generateImports(mMapperClass);
        generateObjectTransformMethod(mMapperClass);
        generateObjectListTransformMethod(mMapperClass);
        optimizeStyle(mMapperClass);
    }

    private void generateObjectTransformMethod(PsiClass mapperClass) {
        String originClassName = mPoClass.getName();
        String mappedClassName = mVoClass.getName();

        String methodText = OBJECT_TRANSFORM_METHOD.replace("$ORIGIN_CLASS$", originClassName)
                .replace("$MAPPED_CLASS$", mappedClassName)
                .replace("$ENTITY$", Utils.getClassEntityName(originClassName))
                .replace("$BODY$", methodBody());
//        System.out.println("methodText is: " + methodText);
        mapperClass.add(mFactory.createMethodFromText(methodText, mapperClass));
    }

    private void generateObjectListTransformMethod(PsiClass mapperClass) {
        String originClassName = mPoClass.getName();
        String mappedClassName = mVoClass.getName();

        String methodText = OBJECT_LIST_TRANSFORM_METHOD.replace("$ORIGIN_CLASS$", originClassName)
                .replace("$ENTITY$", Utils.getClassEntityName(originClassName))
                .replace("$MAPPED_CLASS$", mappedClassName);
        mapperClass.add(mFactory.createMethodFromText(methodText, mapperClass));
    }

    private void generateImports(PsiClass mapperClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        PsiJavaFile file = (PsiJavaFile) mapperClass.getContainingFile();

        styleManager.addImport(file, mPoClass);
        styleManager.addImport(file, mVoClass);

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(mProject);
        GlobalSearchScope searchScope = GlobalSearchScope.allScope(mProject);
        styleManager.addImport(file, javaPsiFacade.findClass("java.util.List", searchScope));
        styleManager.addImport(file, javaPsiFacade.findClass("java.util.ArrayList", searchScope));
    }

    private void addImport(PsiClass whichClass, String canonicalText) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        PsiJavaFile file = (PsiJavaFile) whichClass.getContainingFile();
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(mProject);
        GlobalSearchScope searchScope = GlobalSearchScope.allScope(mProject);
        styleManager.addImport(file, javaPsiFacade.findClass(canonicalText, searchScope));
    }

    private String methodBody() {
        StringBuilder methodBody = new StringBuilder();

        for (PsiField field : mPoClass.getFields()) {
            if (field.hasModifierProperty("static")) {
                continue;
            }

            String fieldName = field.getName();
            if (fieldName.startsWith("m")) {
                fieldName = fieldName.substring(1);
            }
            final String canonicalText = field.getType().getCanonicalText();

            if (PsiType.BOOLEAN.equals(field.getType())) {
                String getter = "get" + fieldName;
                PsiMethod[] methodsByName = mPoClass.findMethodsByName(getter, false);
                if (methodsByName.length == 0) {
                    getter = "is" + fieldName;
                    methodsByName = mPoClass.findMethodsByName(getter, false);
                }
                if (methodsByName.length > 0) {
                    final String methodItem = METHOD_BODY_ITEM
                            .replace("$GETTER$", getter)
                            .replace("$PROPERTY$", fieldName);
                    methodBody.append(methodItem);
                }
            } else {
                String getter = "get" + fieldName;
                final PsiMethod[] methodsByName = mPoClass.findMethodsByName(getter, false);
                if (methodsByName.length > 0) {
                    if (canonicalText.startsWith(Utils.JAVA_LIST_TYPE)) {
                        String itemCanonicalText = Utils.getListInnerType(canonicalText);
                        final String amendFieldName = Utils.getListMethodEntifyNameWithoutPrefix(fieldName);
                        if (Utils.isJavaInnerClass(itemCanonicalText)) {
                            final String methodItem = METHOD_BODY_ITEM_LIST
                                    .replace("$PROPERTY$", amendFieldName)
                                    .replace("$GETTER$", getter);
                            methodBody.append(methodItem);
                        } else {
                            final String methodItem = METHOD_BODY_PO_ITEM_LIST
                                    .replace("$PROPERTY$", amendFieldName)
                                    .replace("$ENTITY$", Utils.getClassEntityName(itemCanonicalText))
                                    .replace("$GETTER$", getter);
                            methodBody.append(methodItem);
                        }
                    } else {
                        //补充list的mapper set
                        final String amendFieldName = Utils.amendFieldNameWithoutPrefix(fieldName);
                        if (Utils.isJavaInnerClass(canonicalText)) {
                            final String methodItem = METHOD_BODY_ITEM
                                    .replace("$PROPERTY$", amendFieldName)
                                    .replace("$GETTER$", getter);
                            methodBody.append(methodItem);
                        } else {
                            final String methodItem = METHOD_BODY_PO_ITEM
                                    .replace("$PROPERTY$", amendFieldName)
                                    .replace("$GETTER$", getter);
                            methodBody.append(methodItem);
                        }
                    }
                }
            }
        }

        return methodBody.toString();
    }


    private static final String FIELD_ITEM_LIST = "private java.util.List<%s> %s;"; //原来的field已经有前缀如m,不用再加了
    private static final String FIELD_ITEM = "private %s %s;";

    private void generateFields(PsiClass objectClass) {
        for (PsiField field : mPoClass.getFields()) {
            if (!field.hasModifierProperty("static")) {
                final String name = field.getName();
                final String canonicalText = field.getType().getCanonicalText();
                if (canonicalText.startsWith(Utils.JAVA_LIST_TYPE)) {
                    String itemCanonicalText = Utils.getListInnerType(canonicalText);
                    if (Utils.isJavaInnerClass(itemCanonicalText)) {
                        String filedStr = String.format(FIELD_ITEM_LIST, itemCanonicalText, Utils.amendListFieldName(name));
                        objectClass.add(mFactory.createFieldFromText(filedStr, objectClass));
                        continue;
                    } else {
                        final String voTypeCanonicalName = getVoFieldTypeNameFromPoText(itemCanonicalText);
                        String filedStr = String.format(FIELD_ITEM_LIST, voTypeCanonicalName, Utils.amendListFieldName(name));
                        objectClass.add(mFactory.createFieldFromText(filedStr, objectClass));
                        mMapperPoClassListener.notifyFoundFieldInPoClass(itemCanonicalText);
                        continue;
                    }
                } else if (Utils.isJavaInnerClass(canonicalText)) {
                    objectClass.add(mFactory.createField(name, field.getType()));
                } else {
                    final String voTypeCanonicalName = getVoFieldTypeNameFromPoField(field);
                    String filedStr = String.format(FIELD_ITEM, voTypeCanonicalName, Utils.amendFieldName(name));
//                    objectClass.add(mFactory.createImportStatementOnDemand(voTypeCanonicalName));
                    objectClass.add(mFactory.createFieldFromText(filedStr, objectClass));
                    mMapperPoClassListener.notifyFoundFieldInPoClass(field);
                }
            }
        }
    }

    private String getVoFieldTypeNameFromPoField(PsiField field) {
        final String canonicalText = field.getType().getCanonicalText();
        return getVoFieldTypeNameFromPoText(canonicalText);
    }

    @NotNull
    private String getVoFieldTypeNameFromPoText(String canonicalText) {
        return Utils.replaceFullPkgWithGivenClass(mWaitPOItem.getVoClassCanonicalName(), canonicalText) + "VO";
    }

    private static final String METHOD_GET_FIELD_ITEM = "public {TYPE} get{ENTITY}() {\nreturn m{ENTITY};\n}";
    private static final String METHOD_SET_FIELD_ITEM = "public void set{ENTITY}({TYPE} value) {\nm{ENTITY} = value;\n}";

    private static final String METHOD_GET_FIELD_LIST_ITEM = "public java.util.List<{TYPE}> get{ENTITY}List() {\nreturn m{ENTITY}List;\n}";
    private static final String METHOD_SET_FIELD_LIST_ITEM = "public void set{ENTITY}List(java.util.List<{TYPE}> list) {\nm{ENTITY}List = list;\n}";

    private void generateMethods(PsiClass objectClass) {
        for (PsiMethod method : mPoClass.getMethods()) {
            String fieldName = Utils.getFieldNameFromMethod(method.getText());
            String methodName = method.getName();
            if (methodName.startsWith("set") || methodName.startsWith("get") || methodName.startsWith("is")) {
                if (methodName.startsWith("get")) {
                    final String canonicalText = method.getReturnType().getCanonicalText();
                    if (canonicalText.startsWith(Utils.JAVA_LIST_TYPE)) {
                        String itemCanonicalText = Utils.getListInnerType(canonicalText);
                        if (Utils.isJavaInnerClass(itemCanonicalText)) {
                            if (methodName.endsWith("s")) {
                                String itemEntity = methodName.substring(3, methodName.length() - 1);
                                final String oriMethodText = method.getText();
                                String methodText = oriMethodText.replace(itemEntity + "s", itemEntity + "List").replace("this.", "");
                                objectClass.add(mFactory.createMethodFromText(methodText, objectClass));
                            } else {
                                addNormalMethod(objectClass, method);
                            }
                        } else {
                            String itemEntity = Utils.getClassEntityName(itemCanonicalText);
                            final String amendFieldName = Utils.getListMethodEntifyNameWithoutPrefix(fieldName);
                            String methodGetStr = METHOD_GET_FIELD_LIST_ITEM.replace("{TYPE}", itemEntity + "VO").replace("{ENTITY}", amendFieldName);
                            objectClass.add(mFactory.createMethodFromText(methodGetStr, objectClass));
                        }
                    } else if (!Utils.isJavaInnerClass(canonicalText)) {
                        String entity = Utils.getClassEntityName(canonicalText);
                        final String amendFieldName = Utils.amendFieldNameWithoutPrefix(fieldName);
                        String methodGetStr = METHOD_GET_FIELD_ITEM.replace("{TYPE}", entity + "VO").replace("{ENTITY}", amendFieldName);
                        objectClass.add(mFactory.createMethodFromText(methodGetStr, objectClass));
                    } else {
                        addNormalMethod(objectClass, method);
                    }
                } else if (methodName.startsWith("set")) {
                    final String paramText = method.getParameterList().getParameters()[0].getType().getCanonicalText();
                    if (paramText.startsWith(Utils.JAVA_LIST_TYPE)) {
                        String itemCanonicalText = Utils.getListInnerType(paramText);
                        if (Utils.isJavaInnerClass(itemCanonicalText)) {
                            if (methodName.endsWith("s")) {
                                String itemEntity = methodName.substring(3, methodName.length() - 1);
                                final String oriMethodText = method.getText();
                                String methodText = oriMethodText.replace(itemEntity + "s", itemEntity + "List").replace("this.", "");
                                objectClass.add(mFactory.createMethodFromText(methodText, objectClass));
                            } else {
                                addNormalMethod(objectClass, method);
                            }
                        } else {
                            String itemEntity = Utils.getClassEntityName(itemCanonicalText);
                            final String amendFieldName = Utils.getListMethodEntifyNameWithoutPrefix(fieldName);
                            String methodGetStr = METHOD_SET_FIELD_LIST_ITEM.replace("{TYPE}", itemEntity + "VO").replace("{ENTITY}", amendFieldName);
                            objectClass.add(mFactory.createMethodFromText(methodGetStr, objectClass));
                        }
                    } else if (!Utils.isJavaInnerClass(paramText)) {
                        String entity = Utils.getClassEntityName(paramText);
                        final String amendFieldName = Utils.amendFieldNameWithoutPrefix(fieldName);
                        String methodGetStr = METHOD_SET_FIELD_ITEM.replace("{TYPE}", entity + "VO").replace("{ENTITY}", amendFieldName);
                        objectClass.add(mFactory.createMethodFromText(methodGetStr, objectClass));

                    } else {
                        addNormalMethod(objectClass, method);
                    }
                } else {
                    addNormalMethod(objectClass, method);
                }
            }
        }
    }

    private void addNormalMethod(PsiClass objectClass, PsiMethod method) {
        final String oriMethodText = method.getText();
        String methodText = oriMethodText.replace("\b" + mPoClass.getName() + "\b", objectClass.getName());
        methodText = methodText.replace("this.", "");
        objectClass.add(mFactory.createMethodFromText(methodText, objectClass));
    }

    private void optimizeStyle(PsiClass clazz) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
        PsiJavaFile file = (PsiJavaFile) clazz.getContainingFile();
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(clazz);
    }

    private PsiClass createClass(String classPath, boolean keepFile) {
        int i = classPath.lastIndexOf(".");
        String packagePath = classPath.substring(0, i);
        String className = classPath.substring(i + 1);

        PsiDirectory directory = createDirectory(packagePath);
        PsiFile file = directory.findFile(className + ".java");
        if (keepFile && file != null) {
            PsiJavaFile psiJavaFile = (PsiJavaFile) file;
            final PsiClass[] classes = psiJavaFile.getClasses();
            if (classes.length > 0) {
                return classes[0];
            } else {
                file.delete();
                return JavaDirectoryService.getInstance().createClass(directory, className);
            }
        } else {
            if (file != null) {
                file.delete();
            }
            return JavaDirectoryService.getInstance().createClass(directory, className);
        }
    }

    private PsiDirectory createDirectory(String packagePath) {
        PsiPackage pkg = JavaPsiFacade.getInstance(mProject).findPackage(packagePath);
        List<String> packageSegments = new ArrayList();
        while (pkg == null) {
            int i = packagePath.lastIndexOf(".");
            packageSegments.add(packagePath.substring(i + 1));
            packagePath = packagePath.substring(0, i);
            pkg = JavaPsiFacade.getInstance(mProject).findPackage(packagePath);
        }
        PsiDirectory directory = createDirectory(pkg);
        for (int i = packageSegments.size() - 1; i >= 0; --i) {
            directory = directory.createSubdirectory(packageSegments.get(i));
        }
        return directory;
    }

    private PsiDirectory createDirectory(PsiPackage pkg) {
        PsiDirectory[] directories = pkg.getDirectories();
        if (directories.length == 1) {
            return directories[0];
        }
        for (PsiDirectory directory : directories) {
            final String canonicalPath = directory.getVirtualFile().getCanonicalPath();
            if (canonicalPath.contains("/src/")) {
                return directory;
            }
        }
        return null;
    }

    public interface MapperPoClassListener {

        void notifyFoundFieldInPoClass(PsiField field);

        void notifyFoundFieldInPoClass(String poCanonicalText);
    }
}
