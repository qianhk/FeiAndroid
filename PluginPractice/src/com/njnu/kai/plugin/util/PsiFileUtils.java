package com.njnu.kai.plugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.*;

/**
 * 通用方法
 * FilenameIndex.getFilesByName()通过给定名称（不包含具体路径）搜索对应文件
 * ReferencesSearch.search()类似于IDE中的Find Usages操作
 * RefactoringFactory.createRename()重命名
 * FileContentUtil.reparseFiles()通过VirtualFile重建PSI
 * <p>
 * Java专用方法
 * ClassInheritorsSearch.search()搜索一个类的所有子类
 * JavaPsiFacade.findClass()通过类名查找类
 * PsiShortNamesCache.getInstance().getClassesByName()通过一个短名称（例如LogUtil）查找类
 * PsiClass.getSuperClass()查找一个类的直接父类
 * JavaPsiFacade.getInstance().findPackage()获取Java类所在的Package
 * OverridingMethodsSearch.search()查找被特定方法重写的方法
 *
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class PsiFileUtils {

    public static void optimizeStyle(Project project, PsiClass clazz) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        PsiJavaFile file = (PsiJavaFile) clazz.getContainingFile();
        styleManager.optimizeImports(file);
        styleManager.shortenClassReferences(clazz);
    }

    public static PsiClass createClass(Project project, String classPath, boolean keepFile) {
        int i = classPath.lastIndexOf(".");
        String packagePath = classPath.substring(0, i);
        String className = classPath.substring(i + 1);
        return createClass(project, packagePath, className, keepFile);
    }

    public static PsiClass createClass(Project project, String packagePath, String className, boolean keepFile
            , String templateName, Map<String, String> properties) {
        PsiDirectory directory = createDirectory(project, packagePath);
        PsiFile file = directory.findFile(className + ".java");
        if (keepFile && file != null) {
            PsiJavaFile psiJavaFile = (PsiJavaFile) file;
            final PsiClass[] classes = psiJavaFile.getClasses();
            if (classes.length > 0) {
                return classes[0];
            } else {
                file.delete();
                return createClassUseJavaDirectoryService(directory, className, templateName, properties);
            }
        } else {
            if (file != null) {
                file.delete();
            }
            return createClassUseJavaDirectoryService(directory, className, templateName, properties);
        }
    }

    public static PsiClass createClass(Project project, String packagePath, String className, boolean keepFile) {
        return createClass(project, packagePath, className, keepFile, "Class", null);
    }

    public static PsiClass createClassUseJavaDirectoryService(PsiDirectory directory, String className
            , String templateName, Map<String, String> properties) {
        if (properties == null || properties == Collections.EMPTY_MAP) {
            properties = new HashMap<>();
        }
        if (!properties.containsKey("VISIBILITY")) {
            properties.put("VISIBILITY", "PUBLIC");
        }
        return JavaDirectoryService.getInstance().createClass(directory, className, templateName, false, properties);
    }

    public static PsiDirectory createDirectory(Project project, String packagePath) {
        PsiPackage pkg = JavaPsiFacade.getInstance(project).findPackage(packagePath);
        List<String> packageSegments = new ArrayList();
        while (pkg == null) {
            int i = packagePath.lastIndexOf(".");
            packageSegments.add(packagePath.substring(i + 1));
            packagePath = packagePath.substring(0, i);
            pkg = JavaPsiFacade.getInstance(project).findPackage(packagePath);
        }
        PsiDirectory directory = createDirectory(pkg);
        for (int i = packageSegments.size() - 1; i >= 0; --i) {
            directory = directory.createSubdirectory(packageSegments.get(i));
        }
        return directory;
    }

    public static PsiDirectory createDirectory(PsiPackage pkg) {
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
