package com.njnu.kai.plugin.mapper;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.njnu.kai.plugin.util.Utils;

import java.util.ArrayList;
import java.util.List;

class MapperProcessor extends WriteCommandAction.Simple {

    private PsiElementFactory mFactory;
    private final PsiClass mOriginClass;
    private PsiClass mObjectClass;
    private PsiClass mMapperClass;
    private boolean mGenerateObjectListTransformMethod;

    private static String OBJECT_TRANSFORM_METHOD =
            "        /** \n" +
                    "        * @param originObject origin object \n" +
                    "        * @return mapped object \n" +
                    "        */ \n" +
                    "        public static $MAPPED_CLASS$ transform(@NonNull $ORIGIN_CLASS$ originObject) {\n" +
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


    public MapperProcessor(final TmpRuntimeParams context) {
        super(context.getProject(), null);
        mFactory = JavaPsiFacade.getElementFactory(getProject());
        mOriginClass = context.getOriginClass();
        mObjectClass = createClass(context.getVoClassCanonicalName(), false);
        mMapperClass = createClass(context.getMapperClassCanonicalName(), true);
        mGenerateObjectListTransformMethod = context.isSupportList();
    }

    @Override
    protected void run() {
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
        mapperClass.add(mFactory.createMethodFromText(methodText, mapperClass));
    }

    private void generateObjectListTransformMethod(PsiClass mapperClass) {
        String originClassName = mOriginClass.getName();
        String mappedClassName = mObjectClass.getName();

        String methodText = OBJECT_LIST_TRANSFORM_METHOD.replace("$ORIGIN_CLASS$", originClassName)
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
        styleManager.addImport(file, javaPsiFacade.findClass("android.support.annotation.NonNull", searchScope));
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
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(getProject());
        PsiJavaFile file = (PsiJavaFile) clazz.getContainingFile();
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(clazz);
    }

    private PsiClass createClass(String classPath, boolean keepFile) {
        int i = classPath.lastIndexOf(".");
        String packagePath = classPath.substring(0, i);
        String className = classPath.substring(i + 1);

        PsiDirectory directory = createDirectory(packagePath);
        if (!keepFile) {
            PsiFile file = directory.findFile(className + ".java");
            if (file != null) {
                file.delete();
            }
        }

        return JavaDirectoryService.getInstance().createClass(directory, className);
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
        for (PsiDirectory directory : directories) {
            if (getPath(directory).contains("src/main/java")) {
                return directory;
            }
        }
        return null;
    }

    private String getPath(PsiDirectory directory) {
        StringBuilder stringBuilder = new StringBuilder(directory.getName());
        while (directory.getParent() != null) {
            directory = directory.getParent();
            stringBuilder.insert(0, directory.getName() + "/");
        }
        return stringBuilder.toString();
    }
}
