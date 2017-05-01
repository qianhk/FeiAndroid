package com.njnu.kai.plugin.practice;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.njnu.kai.plugin.util.PsiFileUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-14
 */
public class TestTemplateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final Project project = anActionEvent.getProject();
        new WriteCommandAction.Simple(project, "test-template-operate") {

            @Override
            protected void run() throws Throwable {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("ENTITY", "Abc");
                PsiFileUtils.createClass(project, "codegen.template", properties.get("ENTITY") + "Activity", false, "MvpActivity", properties);
            }
        }.execute();
    }
}
