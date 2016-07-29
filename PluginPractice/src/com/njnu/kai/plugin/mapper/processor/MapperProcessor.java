package com.njnu.kai.plugin.mapper.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.mapper.TmpRuntimeParams;
import com.njnu.kai.plugin.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MapperProcessor extends WriteCommandAction.Simple {

    private PsiElementFactory mFactory;
    private final PsiClass mOriginClass;
    private PsiClass mObjectClass;
    private PsiClass mMapperClass;
    private boolean mGenerateObjectListTransformMethod;

    private TmpRuntimeParams mTmpRuntimeParams;

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


    public MapperProcessor(final TmpRuntimeParams params) {
        super(params.getProject(), null);
        mTmpRuntimeParams = params;
        mFactory = JavaPsiFacade.getElementFactory(getProject());
        mOriginClass = params.getOriginClass();
        mGenerateObjectListTransformMethod = params.isSupportList();
    }

    @Override
    protected void run() {
        mObjectClass = createClass(mTmpRuntimeParams.getVoClassCanonicalName(), false);
        mMapperClass = createClass(mTmpRuntimeParams.getMapperClassCanonicalName(), true);
        generateObjectClass();
        generateMapperClass();
    }

    private void generateObjectClass() {
        generateFields(mObjectClass);
        generateMethods(mObjectClass);

        optimizeStyle(mObjectClass);
    }

    private void generateMapperClass() {
        generateImports(mMapperClass);
        generateObjectTransformMethod(mMapperClass);
        if (mGenerateObjectListTransformMethod) {
            generateObjectListTransformMethod(mMapperClass);
        }
        optimizeStyle(mMapperClass);
    }

    private void generateObjectTransformMethod(PsiClass mapperClass) {
        String originClassName = mOriginClass.getName();
        String mappedClassName = mObjectClass.getName();

        String methodText = OBJECT_TRANSFORM_METHOD.replace("$ORIGIN_CLASS$", originClassName)
                .replace("$MAPPED_CLASS$", mappedClassName)
                .replace("$ENTITY$", Utils.getClassEntityName(originClassName))
                .replace("$BODY$", methodBody());
        System.out.println("methodText is: " + methodText);
        mapperClass.add(mFactory.createMethodFromText(methodText, mapperClass));
    }

    private void generateObjectListTransformMethod(PsiClass mapperClass) {
        String originClassName = mOriginClass.getName();
        String mappedClassName = mObjectClass.getName();

        String methodText = OBJECT_LIST_TRANSFORM_METHOD.replace("$ORIGIN_CLASS$", originClassName)
                .replace("$ENTITY$", Utils.getClassEntityName(originClassName))
                .replace("$MAPPED_CLASS$", mappedClassName);
        mapperClass.add(mFactory.createMethodFromText(methodText, mapperClass));
    }

    private void generateImports(PsiClass mapperClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(getProject());
        PsiJavaFile file = (PsiJavaFile) mapperClass.getContainingFile();

        styleManager.addImport(file, mOriginClass);
        styleManager.addImport(file, mObjectClass);

        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(getProject());
        GlobalSearchScope searchScope = GlobalSearchScope.allScope(getProject());
        styleManager.addImport(file, javaPsiFacade.findClass("java.util.List", searchScope));
        styleManager.addImport(file, javaPsiFacade.findClass("java.util.ArrayList", searchScope));
    }

    private String methodBody() {
        StringBuilder methodBody = new StringBuilder();

        for (PsiField field : mOriginClass.getFields()) {
            if (field.hasModifierProperty("static")) {
                continue;
            }

            String property = field.getName();
            if (property.startsWith("m")) {
                property = property.substring(1);
            }
            if (PsiType.BOOLEAN.equals(field.getType())) {
                String getter = "get" + property;
                PsiMethod[] methodsByName = mOriginClass.findMethodsByName(getter, false);
                if (methodsByName.length == 0) {
                    getter = "is" + property;
                    methodsByName = mOriginClass.findMethodsByName(getter, false);
                }
                if (methodsByName.length > 0) {
                    final String methodItem = METHOD_BODY_ITEM
                            .replace("$GETTER$", getter)
                            .replace("$PROPERTY$", property);
                    methodBody.append(methodItem);
                }
            } else {
                String getter = "get" + property;
                final PsiMethod[] methodsByName = mOriginClass.findMethodsByName(getter, false);
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
        for (PsiField field : mOriginClass.getFields()) {
            if (!field.hasModifierProperty("static")) {
                objectClass.add(mFactory.createField(field.getName(), field.getType()));
            }
        }
    }

    private void generateMethods(PsiClass objectClass) {
        for (PsiMethod method : mOriginClass.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("set") || methodName.startsWith("get") || methodName.startsWith("is")) {
                String methodText = method.getText().replace("\b" + mOriginClass.getName() + "\b", objectClass.getName());
                objectClass.add(mFactory.createMethodFromText(methodText, objectClass));
            }
        }
    }

    private void optimizeStyle(PsiClass clazz) {
//        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(getProject());
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
        PsiPackage pkg = JavaPsiFacade.getInstance(getProject()).findPackage(packagePath);
        List<String> packageSegments = new ArrayList();
        while (pkg == null) {
            int i = packagePath.lastIndexOf(".");
            packageSegments.add(packagePath.substring(i + 1));
            packagePath = packagePath.substring(0, i);
            pkg = JavaPsiFacade.getInstance(getProject()).findPackage(packagePath);
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

}
