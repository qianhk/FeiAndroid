package com.njnu.kai.plugin.practice;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
@State(name = "com.njnu.kai.plugin.practice.KaiAppComponent", storages = {@Storage(file = "$WORKSPACE_FILE$"
)})
public class KaiAppComponent implements ApplicationComponent, PersistentStateComponent<HelloworldMsg> {

    private HelloworldMsg mHelloworldMsg = new HelloworldMsg();

    public KaiAppComponent() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here

    }

    @Override
    @NotNull
    public String getComponentName() {
        return "KaiAppComponent";
    }

    public void sayHi() {
        Messages.showMessageDialog(mHelloworldMsg.getMsg(), mHelloworldMsg.getTitle(), Messages.getInformationIcon());
    }

    @Nullable
    @Override
    public HelloworldMsg getState() {
        return mHelloworldMsg;
    }

    @Override
    public void loadState(HelloworldMsg helloworldMsg) {
        XmlSerializerUtil.copyBean(helloworldMsg, mHelloworldMsg);
    }

}
