package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.util.Utils;

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
            "        * @param originObject origin object\n" +
            "        * @return mapped object\n" +
            "        */\n" +
            "        public static $MAPPED_CLASS$ transform($ORIGIN_CLASS$ originObject) {\n" +
            "        $MAPPED_CLASS$  mappedObject = new $MAPPED_CLASS$();\n\n" +
            "        $BODY$\n" +
            "        return mappedObject;\n" +
            "    }\n\n";

    private static String OBJECT_LIST_TRANSFORM_METHOD = "/**\n" +
            "        * @param originObjectList list of origin object\n" +
            "        * @return list of mapped object \n" +
            "        */ \n" +
            "        public static List<$MAPPED_CLASS$> transform$ENTITY$List(List<$ORIGIN_CLASS$> originObjectList) {\n" +
            "        List<$MAPPED_CLASS$>  mappedObjects = new ArrayList<>();\n\n" +
            "        if (originObjectList != null) {" +
            "        for ($ORIGIN_CLASS$ originObject : originObjectList) {\n" +
            "        if (originObject != null) {\n" +
            "        mappedObjects.add(transform(originObject));\n" +
            "        }\n" +
            "        }\n" +
            "        }\n" +
            "        return mappedObjects;\n" +
            "    }\n\n";

    private static String METHOD_BODY_ITEM = "mappedObject.set$PROPERTY$(originObject.$GETTER$());\n";

    public MapperPoClass(Project project, MapperPoClassListener mapperPoClassListener, WaitPOItem waitPOItem) {
        mProject = project;
        mMapperPoClassListener = mapperPoClassListener;
        mWaitPOItem = waitPOItem;
        mFactory = JavaPsiFacade.getElementFactory(mProject);
    }

    public void execute() {
        mPoClass = mWaitPOItem.getPoClass();
        mVoClass = createClass(mWaitPOItem.getVoClassCanonicalName(), false);
        mMapperClass = createClass(mWaitPOItem.getMapperClassCanonicalName(), true);
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

    private String methodBody() {
        StringBuilder methodBody = new StringBuilder();

        for (PsiField field : mPoClass.getFields()) {
            if (field.hasModifierProperty("static")) {
                continue;
            }

            String property = field.getName();
            if (property.startsWith("m")) {
                property = property.substring(1);
            }
            if (PsiType.BOOLEAN.equals(field.getType())) {
                String getter = "get" + property;
                PsiMethod[] methodsByName = mPoClass.findMethodsByName(getter, false);
                if (methodsByName.length == 0) {
                    getter = "is" + property;
                    methodsByName = mPoClass.findMethodsByName(getter, false);
                }
                if (methodsByName.length > 0) {
                    final String methodItem = METHOD_BODY_ITEM
                            .replace("$GETTER$", getter)
                            .replace("$PROPERTY$", property);
                    methodBody.append(methodItem);
                }
            } else {
                String getter = "get" + property;
                final PsiMethod[] methodsByName = mPoClass.findMethodsByName(getter, false);
                if (methodsByName.length > 0) {
                    final String methodItem = METHOD_BODY_ITEM
                            .replace("$GETTER$", getter)
                            .replace("$PROPERTY$", property);
                    methodBody.append(methodItem);
                }
            }
        }

        return methodBody.toString();
    }

    private void generateFields(PsiClass objectClass) {
        StringBuilder injection = new StringBuilder();
        for (PsiField field : mPoClass.getFields()) {
            if (!field.hasModifierProperty("static")) {
                final String name = field.getName();
                if (name.endsWith("PO")) {
                    injection.setLength(0);
                    injection.append("private ");
                    final String voTypeCanonicalName = getVoFieldTypeNameFromPoField(field);
                    injection.append(voTypeCanonicalName);
                    injection.append(" ");
                    injection.append(name.substring(0, name.length() - 2));
                    injection.append(";");
//                    objectClass.add(mFactory.createImportStatementOnDemand(voTypeCanonicalName));
                    objectClass.add(mFactory.createFieldFromText(injection.toString(), objectClass));
                    mMapperPoClassListener.notifyFoundFieldInPoClass(field);
                } else {
                    objectClass.add(mFactory.createField(name, field.getType()));
                }
            }
        }
    }

    private String getVoFieldTypeNameFromPoField(PsiField field) {
        final String canonicalText = field.getType().getCanonicalText();
        return Utils.replaceFullPkgWithGivenClass(mWaitPOItem.getVoClassCanonicalName(), canonicalText) + "VO";
    }

    private void generateMethods(PsiClass objectClass) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (PsiMethod method : mPoClass.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("set") || methodName.startsWith("get") || methodName.startsWith("is")) {
                if (methodName.startsWith("get")) {
                    final String canonicalText = method.getReturnType().getCanonicalText();
                    if (canonicalText.endsWith("PO")) {
                        String entity = Utils.getClassEntityName(canonicalText);
                        stringBuilder.setLength(0);
                        stringBuilder.append("public ");
                        stringBuilder.append(entity + "VO get");
                        stringBuilder.append(entity + "() {\nreturn m");
                        stringBuilder.append(entity + ";\n}");
                        objectClass.add(mFactory.createMethodFromText(stringBuilder.toString(), objectClass));
                    } else {
                        addNormalMethod(objectClass, method);
                    }
                } else if (methodName.startsWith("set") && methodName.endsWith("PO")) {
                    String entity = methodName.substring(3, methodName.length() - 2);
                    String smallEntity = entity.toLowerCase().substring(0, 1) + entity.substring(1);
                    stringBuilder.setLength(0);
                    stringBuilder.append("public void set");
                    stringBuilder.append(entity + "(");
                    stringBuilder.append(entity + "VO " + smallEntity + ") {\n");
                    stringBuilder.append("m" + entity + " = " + smallEntity + ";\n}");
                    objectClass.add(mFactory.createMethodFromText(stringBuilder.toString(), objectClass));
                } else {
                    addNormalMethod(objectClass, method);
                }
            }
        }
    }

    private void addNormalMethod(PsiClass objectClass, PsiMethod method) {
        final String oriMethodText = method.getText();
        String methodText = oriMethodText.replace("\b" + mPoClass.getName() + "\b", objectClass.getName());
        objectClass.add(mFactory.createMethodFromText(methodText, objectClass));
    }

    private void optimizeStyle(PsiClass clazz) {
//        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(mProject);
//        PsiJavaFile file = (PsiJavaFile) clazz.getContainingFile();
//        styleManager.optimizeImports(file);
//        styleManager.shortenClassReferences(clazz);
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
    }
}
