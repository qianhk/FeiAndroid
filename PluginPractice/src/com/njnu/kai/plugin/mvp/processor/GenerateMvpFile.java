package com.njnu.kai.plugin.mvp.processor;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/9/3
 */
public class GenerateMvpFile {

    private String mPackageName;
    private String mClassName;
    private String mEntityName;
    private String mTemplate;

    public GenerateMvpFile(String packageName, String className, String entityName, String template) {
        mPackageName = packageName;
        mClassName = className;
        mEntityName = entityName;
        mTemplate = template;
    }

    public void execute() {

    }
}
