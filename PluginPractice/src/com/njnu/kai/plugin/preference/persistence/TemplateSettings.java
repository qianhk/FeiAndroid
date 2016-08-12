package com.njnu.kai.plugin.preference.persistence;

import com.google.common.collect.Maps;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/8/13
 */
@State(
        name = "PluginPracticeTemplateSettings",
        storages = {
                @Storage(file = StoragePathMacros.APP_CONFIG + "/plugin_practice_template_settings.xml")
        }
)
public class TemplateSettings implements PersistentStateComponent<TemplateSettings> {

    private Map<String, String> templateValues = Maps.newHashMap();

//    private TemplatesProvider templatesProvider = new CodeGeneratorFactory.ResourceTemplateProvider();

    public static TemplateSettings getInstance() {
        return ServiceManager.getService(TemplateSettings.class);
    }

    public Map<String, String> getTemplateValues() {
        return templateValues;
    }

    public void setTemplateValues(Map<String, String> templateValues) {
        this.templateValues = templateValues;
    }

    @Nullable
    @Override
    public TemplateSettings getState() {
        return this;
    }

    @Override
    public void loadState(TemplateSettings templateSettings) {
        XmlSerializerUtil.copyBean(templateSettings, this);
    }
}
