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
            if (mParams.isUserCreateNuwaBinder()) {
                properties.put("NUWAVO", mParams.getFullNuwaVOClassName());
                properties.put("NUWA_BINDER", mParams.getFullNuwaBinderClassName());
                properties.put("USE_NUWA_BINDER", "TRUE");
            }
        }

        PsiDirectory directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListActivityPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListActivityClassName(), "TTMvpListActivity", properties);

        directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListFragmentPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListFragmentClassName(), "TTMvpListFragment", properties);

        directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListPresenterPackageName());
        PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListPresenterClassName(), "TTMvpListPresenter", properties);

        if (!mParams.isUseNuwa()) {
            directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getListAdapterPackageName());
            PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getListAdapterClassName(), "TTMvpListAdapter", properties);
        } else {
            if (mParams.isUserCreateNuwaBinder()) {
                directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getNuwaBinderPackageName());
                PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getNuwaBinderClassName(), "TTNuwaBinder", properties);

                directory = PsiFileUtils.createDirectory(mParams.getProject(), mParams.getNuwaVOPackageName());
                PsiFileUtils.createClassUseJavaDirectoryService(directory, mParams.getNuwaVOClassName(), "TTNuwaVO", properties);
            }
        }
    }
}
