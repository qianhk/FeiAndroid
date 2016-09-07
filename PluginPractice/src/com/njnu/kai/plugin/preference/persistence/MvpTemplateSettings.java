package com.njnu.kai.plugin.preference.persistence;

import com.google.common.collect.Maps;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.njnu.kai.plugin.mvp.MvpConstant;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/8/13
 */
@State(
        name = "MvpTemplateSettings",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/mvp_template_settings.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class MvpTemplateSettings implements PersistentStateComponent<MvpTemplateSettings> {

    private Map<String, String> mTemplateValues = Maps.newHashMap();

//    private TemplatesProvider templatesProvider = new CodeGeneratorFactory.ResourceTemplateProvider();

//    private final Project mProject;

//    public MvpTemplateSettings(Project project) {
//        mProject = project;
//    }

    public static MvpTemplateSettings getInstance(final Project project) {
        return ServiceManager.getService(project, MvpTemplateSettings.class);
    }

    public Map<String, String> getTemplateValues() {
        return mTemplateValues;
    }

    public void setTemplateValues(Map<String, String> templateValues) {
        mTemplateValues = templateValues;
    }

    @Nullable
    @Override
    public MvpTemplateSettings getState() {
        return this;
    }

    @Override
    public void loadState(MvpTemplateSettings mvpTemplateSettings) {
        XmlSerializerUtil.copyBean(mvpTemplateSettings, this);
    }

    public String provideTemplateForName(String templateName) {
        if (isUsingCustomTemplateForName(templateName)) {
            return mTemplateValues.get(templateName);
        }
        if (MvpConstant.TEMPLATE_NAME_ACTIVITY.equals(templateName)) {
            return MvpDefaultTemplate.ACTIVITY_DEFAULT_TEMPLATE;
        } else if (MvpConstant.TEMPLATE_NAME_LIST_ADAPTER.equals(templateName)) {
            return MvpDefaultTemplate.LIST_ADAPTER_DEFAULT_TEMPLATE;
        } else if (MvpConstant.TEMPLATE_NAME_LIST_FRAGMENT.equals(templateName)) {
            return MvpDefaultTemplate.LIST_FRAGMENT_DEFAULT_TEMPLATE;
        } else if (MvpConstant.TEMPLATE_NAME_LIST_PRESENTER.equals(templateName)) {
            return MvpDefaultTemplate.LIST_PRESENTER_DEFAULT_TEMPLATE;
        } else {
            return String.format("Not setting default template for %s.", templateName);
        }
//        return templatesProvider.provideTemplateForName(templateName);
    }

    public void removeTemplateForName(String templateName) {
        mTemplateValues.remove(templateName);
    }

    public boolean isUsingCustomTemplateForName(String templateName) {
        return mTemplateValues.containsKey(templateName);
    }

    public void setTemplateForName(String templateName, String template) {
        mTemplateValues.put(templateName, template);
    }

}
