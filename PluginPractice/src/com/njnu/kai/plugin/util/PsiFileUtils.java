package com.njnu.kai.plugin.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

import java.util.ArrayList;
import java.util.List;

/**
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

    public static PsiClass createClass(Project project, String packagePath, String className, boolean keepFile) {
        PsiDirectory directory = createDirectory(project, packagePath);
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
