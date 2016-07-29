package com.njnu.kai.plugin.mapper;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.njnu.kai.plugin.mapper.form.SettingDialog;
import com.njnu.kai.plugin.mapper.model.WaitPOItem;
import com.njnu.kai.plugin.mapper.model.WaitPOManager;
import com.njnu.kai.plugin.mapper.processor.MapperProcessor;
import com.njnu.kai.plugin.util.Utils;

import java.awt.*;

public class MapperCodeGenAction extends AnAction {

    private static final String VO_CLASS_CANONICAL_NAME = "voClassCanonicalName";
    private static final String MAPPER_CLASS_CANONICAL_NAME = "mapperClassCanonicalName";
    private static final String IS_SUPPORT_LIST = "isSupportList";
    private static final String BOUND_X = "bound_x";
    private static final String BOUND_Y = "bound_y";
    private static final String BOUND_WIDTH = "bound_width";
    private static final String BOUND_HEIGHT = "bound_height";

    private SettingDialog mDialog;

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiClass originClass = Utils.getEditedClass(editor, project, false);
        if (originClass == null) {
            Utils.showErrorNotification(project, "没检索出要转换的类, 注意光标或焦点位置");
            return;
        }

        final TmpRuntimeParams params = new TmpRuntimeParams(new TmpRuntimeParams.Action() {
            @Override
            public void run(TmpRuntimeParams params) {
                final Rectangle bounds = mDialog.getBounds();
                PropertiesComponent.getInstance().setValue(BOUND_X, bounds.x, 100);
                PropertiesComponent.getInstance().setValue(BOUND_Y, bounds.y, 100);
                PropertiesComponent.getInstance().setValue(BOUND_WIDTH, bounds.width, 800);
                PropertiesComponent.getInstance().setValue(BOUND_HEIGHT, bounds.height, 200);
                saveProperties(params);
                new MapperProcessor(params).execute();
            }
        }, project, originClass);
        loadProperties(params);
        mDialog = new SettingDialog(params);

        mDialog.setBounds(PropertiesComponent.getInstance().getInt(BOUND_X, 100), PropertiesComponent.getInstance().getInt(BOUND_Y, 100)
                , PropertiesComponent.getInstance().getInt(BOUND_WIDTH, 800), PropertiesComponent.getInstance().getInt(BOUND_HEIGHT, 200));
        mDialog.setVisible(true);
    }

    private void loadProperties(TmpRuntimeParams context) {
        context.setVoClassCanonicalName(PropertiesComponent.getInstance().getValue(VO_CLASS_CANONICAL_NAME, "com.example.app.model.NewObject"));
        context.setMapperClassCanonicalName(PropertiesComponent.getInstance().getValue(MAPPER_CLASS_CANONICAL_NAME, "com.example.app.mapper.NewObjectMapper"));
        context.setSupportList(PropertiesComponent.getInstance().getBoolean(IS_SUPPORT_LIST));
    }

    private void saveProperties(TmpRuntimeParams context) {
        PropertiesComponent.getInstance().setValue(VO_CLASS_CANONICAL_NAME, context.getVoClassCanonicalName());
        PropertiesComponent.getInstance().setValue(MAPPER_CLASS_CANONICAL_NAME, context.getMapperClassCanonicalName());
        PropertiesComponent.getInstance().setValue(IS_SUPPORT_LIST, context.isSupportList());
    }

}
