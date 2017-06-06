package com.njnu.kai.plugin.mvp.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiDirectory;
import com.njnu.kai.plugin.mvp.MvpRuntimeParams;
import com.njnu.kai.plugin.util.PsiFileUtils;

import java.util.HashMap;

/**
 * Created by kai
 * since 2017/6/6
 */
public class TemplateMvpProcessor extends WriteCommandAction.Simple {

    private MvpRuntimeParams mParams;

    public TemplateMvpProcessor(MvpRuntimeParams params) {
        super(params.getProject(), "template-mvp-generator-command");
        mParams = params;
    }

    @Override
    protected void run() throws Throwable {
        HashMap<String, String> properties = new HashMap<>();
        PsiDirectory directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getActivityCanonicalName());
        properties.put("ENTITY", mParams.getEntityName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListActivityClassName(), "MvpListActivity", properties);
    }
}
