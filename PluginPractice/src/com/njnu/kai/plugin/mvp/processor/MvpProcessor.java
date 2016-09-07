package com.njnu.kai.plugin.mvp.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.njnu.kai.plugin.mvp.MvpConstant;
import com.njnu.kai.plugin.mvp.MvpRuntimeParams;
import com.njnu.kai.plugin.preference.persistence.MvpTemplateSettings;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class MvpProcessor extends WriteCommandAction.Simple {

    private static final String KEY_DATA_ITEM_TYPE = "${DataItemType}";
    private static final String KEY_FRAGMENT_CLASS = "${FragmentClass}";

    private MvpRuntimeParams mRuntimeParams;

    public MvpProcessor(MvpRuntimeParams params) {
        super(params.getProject(), "mvp-generator-command");
        mRuntimeParams = params;
    }

    @Override
    protected void run() throws Throwable {
        String entityName = mRuntimeParams.getEntityName();
        MvpTemplateSettings settings = MvpTemplateSettings.getInstance(mRuntimeParams.getProject());
        if (mRuntimeParams.isCheckActivity()) {
            String activityStr = settings.provideTemplateForName(MvpConstant.TEMPLATE_NAME_ACTIVITY);
            activityStr = replaceTemplateString(activityStr);
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getActivityCanonicalName()
                    , mRuntimeParams.getActivityClassName(), entityName, activityStr);
            generate.execute();
        }
        if (mRuntimeParams.isCheckListFragment()) {
            String fragmentStr = settings.provideTemplateForName(MvpConstant.TEMPLATE_NAME_LIST_FRAGMENT);
            fragmentStr = replaceTemplateString(fragmentStr);
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getListFragmentCanonicalName()
                    , mRuntimeParams.getListFragmentClassName(), entityName, fragmentStr);
            generate.execute();
        }
        if (mRuntimeParams.isCheckListAdapter()) {
            String adapterStr = settings.provideTemplateForName(MvpConstant.TEMPLATE_NAME_LIST_ADAPTER);
            adapterStr = replaceTemplateString(adapterStr);
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getListAdapterCanonicalName()
                    , mRuntimeParams.getListAdapterClassName(), entityName, adapterStr);
            generate.execute();
        }
        if (mRuntimeParams.isCheckListPresenter()) {
            String presenterStr = settings.provideTemplateForName(MvpConstant.TEMPLATE_NAME_LIST_PRESENTER);
            presenterStr = replaceTemplateString(presenterStr);
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getListPresenterCanonicalName()
                    , mRuntimeParams.getListPresenterClassName(), entityName, presenterStr);
            generate.execute();
        }
    }

    private String replaceTemplateString(String text) {
        text = text.replace(KEY_DATA_ITEM_TYPE, mRuntimeParams.getEntityName());
        text = text.replace(KEY_FRAGMENT_CLASS, mRuntimeParams.getFullListFragmentClassName());
        return text;
    }
}
