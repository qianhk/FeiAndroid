package com.njnu.kai.plugin.mvp.processor;

import com.intellij.openapi.command.WriteCommandAction;
import com.njnu.kai.plugin.mvp.MvpRuntimeParams;

/**
 * Created by kai
 * since 2017/6/6
 */
public class TemplateMvpProcessor extends WriteCommandAction.Simple {

    private MvpRuntimeParams mRuntimeParams;

    protected TemplateMvpProcessor(MvpRuntimeParams params) {
        super(params.getProject(), "template-mvp-generator-command");
        mRuntimeParams = params;
    }

    @Override
    protected void run() throws Throwable {

    }
}
