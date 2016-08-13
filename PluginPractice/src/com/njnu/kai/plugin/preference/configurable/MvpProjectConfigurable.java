package com.njnu.kai.plugin.preference.configurable;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
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
                    new TemplateConfigurable(mProject, "Activity Template", "Setup Template for Activity code generation:", "Activity_template"),
                    new TemplateConfigurable(mProject, "Adapter Template", "Setup Template for Adapter code generation:", "Adapter_template"),
                    new TemplateConfigurable(mProject, "Fragment Template", "Setup Template for Fragment code generation:", "Fragment_template"),
                    new TemplateConfigurable(mProject, "Menu Template", "Setup Template for Menu code generation:", "Menu_template")
            };
        }
        return mConfigurables;
    }
}
