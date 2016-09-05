package com.njnu.kai.plugin.mvp.processor;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElementFactory;
import com.njnu.kai.plugin.mvp.MvpRuntimeParams;
import com.njnu.kai.plugin.util.PsiFileUtils;
import com.njnu.kai.plugin.util.VirtualFileUtils;

import java.io.IOException;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class GenerateMvpFile {

    private MvpRuntimeParams mRuntimeParams;
    private Project mProject;
    private PsiElementFactory mFactory;

    private String mPackageName;
    private String mClassName;
    private String mEntityName;
    private String mTemplate;

    public GenerateMvpFile(MvpRuntimeParams params, String packageName, String className, String entityName, String template) {
        mRuntimeParams = params;
        mProject = params.getProject();
        mFactory = JavaPsiFacade.getElementFactory(mProject);

        mPackageName = packageName;
        mClassName = className;
        mEntityName = entityName;
        mTemplate = template;
    }

    public void execute() throws IOException {
        PsiDirectory directory = PsiFileUtils.createDirectory(mProject, mPackageName);
//        PsiClass psiClass = PsiFileUtils.createClass(mProject, mPackageName, mClassName, false);
        String fileName = mClassName + ".java";
        VirtualFile virtualFile = VirtualFileUtils.createOrFindFile(mProject, fileName, "file://" + directory.getVirtualFile().getCanonicalPath() + "/");
        virtualFile.setBinaryContent(mTemplate.getBytes());
    }
}
