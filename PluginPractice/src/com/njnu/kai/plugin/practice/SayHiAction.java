package com.njnu.kai.plugin.practice;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-14
 */
public class SayHiAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Messages.showMessageDialog("Hello world, Plugin!", "Kai Plugin", Messages.getInformationIcon());
    }
}
