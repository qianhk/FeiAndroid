package com.njnu.kai.plugin.preference.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.njnu.kai.plugin.mvp.MvpConstant;
import com.njnu.kai.plugin.preference.configurable.template.TemplateConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
public class MvpProjectConfigurable implements Configurable, Configurable.Composite {

    private Configurable[] mConfigurables;

    private final Project mProject;

    public MvpProjectConfigurable(Project project) {
        mProject = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "MVP Framework";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "kai help topic";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("<html>Here you can edit 'MVP Framework' settings. In children pages you can edit template for each code generation method.</html>");
        label.setVerticalAlignment(SwingConstants.TOP);
        panel.add(label, BorderLayout.PAGE_START);
        return panel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }

    @Override
    public Configurable[] getConfigurables() {
        if (mConfigurables == null) {
            mConfigurables = new Configurable[]{
                    new TemplateConfigurable(mProject, "Activity Template", "生成Activity的模板:", MvpConstant.TEMPLATE_NAME_ACTIVITY),
                    new TemplateConfigurable(mProject, "List Adapter Template", "生成列表adapter的模板:", MvpConstant.TEMPLATE_NAME_LIST_ADAPTER),
                    new TemplateConfigurable(mProject, "List Fragment Template", "生成列表Fragment的模板:", MvpConstant.TEMPLATE_NAME_LIST_FRAGMENT),
                    new TemplateConfigurable(mProject, "List Presenter Template", "生成列表Presenter的模板:", MvpConstant.TEMPLATE_NAME_LIST_PRESENTER)
            };
        }
        return mConfigurables;
    }
}
