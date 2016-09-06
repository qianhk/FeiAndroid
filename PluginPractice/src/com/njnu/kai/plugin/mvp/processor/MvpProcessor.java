package com.njnu.kai.plugin.mvp.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.njnu.kai.plugin.mvp.MvpRuntimeParams;
import com.njnu.kai.plugin.preference.persistence.MvpTemplateSettings;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class MvpProcessor extends WriteCommandAction.Simple {

    private static final String KEY_DATAITEMTYPE = "${DataItemType}";

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
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getActivityCanonicalName(), entityName + "Activity", entityName
                    , settings.provideTemplateForName("Activity"));
            generate.execute();
        }
        if (mRuntimeParams.isCheckFragment()) {
            String fragmentStr = settings.provideTemplateForName("Fragment");
            fragmentStr = fragmentStr.replace(KEY_DATAITEMTYPE, entityName);
            GenerateMvpFile generate = new GenerateMvpFile(mRuntimeParams, mRuntimeParams.getFragmentCanonicalName(), entityName + "Fragment", entityName
                    , fragmentStr);
            generate.execute();
        }
    }
}
