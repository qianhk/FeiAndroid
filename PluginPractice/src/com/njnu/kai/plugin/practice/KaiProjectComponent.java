package com.njnu.kai.plugin.practice;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
public class KaiProjectComponent implements ProjectComponent {
    public KaiProjectComponent(Project project) {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "KaiProjectComponent";
    }

    @Override
    public void projectOpened() {
        // called when project is opened
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}
