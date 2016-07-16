package com.njnu.kai.plugin.practice;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
public class KaiAppComponent implements ApplicationComponent {
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
        Messages.showMessageDialog("Hello world, Plugin! from " + getClass().getSimpleName(), "Kai Plugin", Messages.getInformationIcon());
    }
}
