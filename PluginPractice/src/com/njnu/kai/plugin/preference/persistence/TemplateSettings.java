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

    private Map<String, String> mTemplateValues = Maps.newHashMap();

//    private TemplatesProvider templatesProvider = new CodeGeneratorFactory.ResourceTemplateProvider();

    public static TemplateSettings getInstance() {
        return ServiceManager.getService(TemplateSettings.class);
    }

    public Map<String, String> getTemplateValues() {
        return mTemplateValues;
    }

    public void setTemplateValues(Map<String, String> templateValues) {
        mTemplateValues = templateValues;
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

    public String provideTemplateForName(String templateName) {
        if (isUsingCustomTemplateForName(templateName)) {
            return mTemplateValues.get(templateName);
        }
        if (templateName.contains("Activity")) {
            return ACTIVITY_DEFAULT_TEMPLATE;
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

    private static final String ACTIVITY_DEFAULT_TEMPLATE = "import android.os.Bundle;\n" +
            "import android.app.Activity;\n" +
            "${IMPORTS}\n" +
            "\n" +
            "public class ${CLASS_NAME}Activity extends Activity ${INTERFACES} {\n" +
            "\n" +
            "    ${FIELDS}\n" +
            "\n" +
            "    @Override\n" +
            "    protected void onCreate(Bundle savedInstanceState) {\n" +
            "        super.onCreate(savedInstanceState);\n" +
            "        setContentView(R.layout.${RESOURCE_NAME});\n" +
            "\n" +
            "        ${ASSIGNMENTS}\n" +
            "    }\n" +
            "\n" +
            "    ${METHODS}\n" +
            "}";
}
