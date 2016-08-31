package com.njnu.kai.plugin.mvp;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by kai
 * since 16/8/31
 */
public class MvpGenerateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        MvpChoiceDialog dialog = new MvpChoiceDialog();
        dialog.pack();
        dialog.setVisible(true);
    }
}
