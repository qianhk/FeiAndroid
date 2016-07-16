package com.njnu.kai.plugin.practice;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
public class KaiAppConfigurable implements Configurable {

    private HelloWorldConfigurationForm mHelloWorldConfigurationForm;
    private HelloworldMsg mHelloworldMsg = new HelloworldMsg();

    @Nls
    @Override
    public String getDisplayName() {
        return "Kai插件设置中的名称";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "kai help topic";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (mHelloWorldConfigurationForm == null) {
            mHelloWorldConfigurationForm = new HelloWorldConfigurationForm();
            mHelloWorldConfigurationForm.setData(mHelloworldMsg);
        }
        return mHelloWorldConfigurationForm.getRootView();
    }

    @Override
    public boolean isModified() {
        return mHelloWorldConfigurationForm != null && mHelloWorldConfigurationForm.isModified(mHelloworldMsg);
    }

    @Override
    public void apply() throws ConfigurationException {
        mHelloWorldConfigurationForm.getData(mHelloworldMsg);
    }

    @Override
    public void reset() {
        mHelloWorldConfigurationForm.setData(mHelloworldMsg);
    }

    @Override
    public void disposeUIResources() {
        mHelloWorldConfigurationForm = null;
    }
}
