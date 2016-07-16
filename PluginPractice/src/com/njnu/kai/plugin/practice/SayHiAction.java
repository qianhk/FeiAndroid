package com.njnu.kai.plugin.practice;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-14
 */
public class SayHiAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final Application application = ApplicationManager.getApplication();
        final KaiAppComponent kaiAppComponent = application.getComponent(KaiAppComponent.class);
        kaiAppComponent.sayHi();
//        Messages.showMessageDialog("Hello world, Plugin!", "Kai Plugin", Messages.getInformationIcon());
    }
}
