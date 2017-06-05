package com.njnu.kai.plugin.mvp;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.njnu.kai.plugin.mvp.processor.MvpProcessor;

import java.awt.*;

/**
 * Created by kai
 * since 16/8/31
 */
public class TemplateMvpGenerateAction extends AnAction {

    private static final String BOUND_X = "mvp_bound_x";
    private static final String BOUND_Y = "mvp_bound_y";
    private static final String BOUND_WIDTH = "mvp_bound_width";
    private static final String BOUND_HEIGHT = "mvp_bound_height";

    private TemplateMvpChoiceDialog mDialog;

    @Override
    public void actionPerformed(AnActionEvent event) {

        final Project project = event.getData(PlatformDataKeys.PROJECT);

        final MvpRuntimeParams params = new MvpRuntimeParams(new MvpRuntimeParams.Action() {
            @Override
            public void run(MvpRuntimeParams params) {
                final Rectangle bounds = mDialog.getBounds();
                PropertiesComponent.getInstance().setValue(BOUND_X, bounds.x, 100);
                PropertiesComponent.getInstance().setValue(BOUND_Y, bounds.y, 100);
                PropertiesComponent.getInstance().setValue(BOUND_WIDTH, bounds.width, 800);
                PropertiesComponent.getInstance().setValue(BOUND_HEIGHT, bounds.height, 300);
                new MvpProcessor(params).execute();
            }
        }, project);

        mDialog = new TemplateMvpChoiceDialog(params);

        mDialog.setBounds(PropertiesComponent.getInstance().getInt(BOUND_X, 100), PropertiesComponent.getInstance().getInt(BOUND_Y, 100)
                , PropertiesComponent.getInstance().getInt(BOUND_WIDTH, 800), PropertiesComponent.getInstance().getInt(BOUND_HEIGHT, 300));
        mDialog.setVisible(true);
    }

}
