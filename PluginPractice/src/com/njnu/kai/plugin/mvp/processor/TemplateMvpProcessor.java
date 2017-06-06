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
        properties.put("ENTITY", mParams.getEntityName());
        properties.put("FRAGMENT", mParams.getFullListFragmentClassName());
        properties.put("PRESENTER", mParams.getFullListPresenterClassName());
        properties.put("ADAPTER", mParams.getFullListAdapterClassName());

        if (mParams.isUseNuwa()) {
            properties.put("USE_NUWA", "TRUE");
        }

        PsiDirectory directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListActivityPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListActivityClassName(), "MvpListActivity", properties);

        directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListFragmentPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListFragmentClassName(), "MvpListFragment", properties);

        directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListPresenterPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListPresenterClassName(), "MvpListPresenter", properties);

        if (!mParams.isUseNuwa()) {
            directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListAdapterPackageName());
            PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListAdapterClassName(), "MvpListAdapter", properties);
        }
    }
}
