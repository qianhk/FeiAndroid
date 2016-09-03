package com.njnu.kai.plugin.util;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.awt.RelativePoint;
import com.njnu.kai.plugin.viewgenerator.Settings.Settings;
import com.njnu.kai.plugin.viewgenerator.model.Element;
import com.njnu.kai.plugin.viewgenerator.model.VGContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final ArrayList<String> INNER_CLASS_PREFIX_LIST;

    static {
        INNER_CLASS_PREFIX_LIST = new ArrayList<String>();
        INNER_CLASS_PREFIX_LIST.add("android.");
        INNER_CLASS_PREFIX_LIST.add("java.");
        INNER_CLASS_PREFIX_LIST.add("org.");
        INNER_CLASS_PREFIX_LIST.add("javax.");
        INNER_CLASS_PREFIX_LIST.add("com.android.");
        INNER_CLASS_PREFIX_LIST.add("junit.");
        INNER_CLASS_PREFIX_LIST.add("dalvik.");
    }

    public static boolean isJavaInnerClass(String className) {
        if (className == null || className.indexOf('.') < 0) {
            return true;
        }
        for (String classPrefix : INNER_CLASS_PREFIX_LIST) {
            if (className.startsWith(classPrefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is using Android SDK?
     */
    public static Sdk findAndroidSDK() {
        Sdk[] allJDKs = ProjectJdkTable.getInstance().getAllJdks();
        for (Sdk sdk : allJDKs) {
            if (sdk.getSdkType().getName().toLowerCase().contains("android")) {
                return sdk;
            }
        }

        return null; // no Android SDK found
    }

    /**
     * Try to find layout XML file in current source on cursor's position
     *
     * @param editor
     * @param file
     * @return
     */
    public static PsiFile getLayoutFileFromCaret(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();

        PsiElement candidateA = file.findElementAt(offset);
        PsiElement candidateB = file.findElementAt(offset - 1);

        PsiFile layout = findLayoutResource(candidateA);
        if (layout != null) {
            return layout;
        }

        return findLayoutResource(candidateB);
    }

    /**
     * Try to find layout XML file in selected element
     *
     * @param element
     * @return
     */
    public static PsiFile findLayoutResource(PsiElement element) {
        if (element == null) {
            return null; // nothing to be used
        }
        if (!(element instanceof PsiIdentifier)) {
            return null; // nothing to be used
        }

        PsiElement layout = element.getParent().getFirstChild();
        if (layout == null) {
            return null; // no file to process
        }
        if (!"R.layout".equals(layout.getText())) {
            return null; // not layout file
        }

        Project project = element.getProject();
        String name = String.format("%s.xml", element.getText());
        return resolveLayoutResourceFile(element, project, name);


    }

    private static PsiFile resolveLayoutResourceFile(PsiElement element, Project project, String name) {
        // restricting the search to the current module - searching the whole project could return wrong layouts
        Module module = ModuleUtil.findModuleForPsiElement(element);
        PsiFile[] files = null;
        if (module != null) {
            GlobalSearchScope moduleScope = module.getModuleWithDependenciesAndLibrariesScope(false);
            files = FilenameIndex.getFilesByName(project, name, moduleScope);
        }
        if (files == null || files.length <= 0) {
            // fallback to search through the whole project
            // useful when the project is not properly configured - when the resource directory is not configured
            files = FilenameIndex.getFilesByName(project, name, new EverythingGlobalScope(project));
            if (files.length <= 0) {
                return null; //no matching files
            }
        }

        // TODO - we have a problem here - we still can have multiple layouts (some coming from a dependency)
        // we need to resolve R class properly and find the proper layout for the R class
        return files[0];
    }

    /**
     * Try to find layout XML file by name
     *
     * @param file
     * @param project
     * @param fileName
     * @return
     */
    public static PsiFile findLayoutResource(PsiFile file, Project project, String fileName) {
        String name = String.format("%s.xml", fileName);
        // restricting the search to the module of layout that includes the layout we are seaching for
        return resolveLayoutResourceFile(file, project, name);
    }

    /**
     * Obtain all IDs from layout
     *
     * @param file
     * @return
     */
    public static ArrayList<Element> getIDsFromLayout(final PsiFile file, Project project) {
        final ArrayList<Element> elements = new ArrayList<Element>();

        return getIDsFromLayout(file, elements, project);
    }

    /**
     * Obtain all IDs from layout
     *
     * @param file
     * @return
     */
    public static ArrayList<Element> getIDsFromLayout(final PsiFile file, final ArrayList<Element> elements, final Project project) {
        file.accept(new XmlRecursiveElementVisitor() {

            @Override
            public void visitElement(final PsiElement element) {
                super.visitElement(element);

                if (element instanceof XmlTag) {
                    XmlTag tag = (XmlTag) element;

                    if (tag.getName().equalsIgnoreCase("include")) {
                        XmlAttribute layout = tag.getAttribute("layout", null);

                        if (layout != null) {
                            Project project = file.getProject();
                            PsiFile include = findLayoutResource(file, project, getLayoutName(layout.getValue()));

                            if (include != null) {
                                getIDsFromLayout(include, elements, project);

                                return;
                            }
                        }
                    }

                    // get element ID
                    XmlAttribute id = tag.getAttribute("android:id", null);
                    if (id == null) {
                        return; // missing android:id attribute
                    }
                    String value = id.getValue();
                    if (value == null) {
                        return; // empty value
                    }

                    // check if there is defined custom class
                    String name = tag.getName();
                    XmlAttribute clazz = tag.getAttribute("class", null);
                    if (clazz != null) {
                        name = clazz.getValue();
                    }

                    try {
                        elements.add(new Element(name, value, project));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return elements;
    }

    /**
     * Get layout name from XML identifier (@layout/....)
     *
     * @param layout
     * @return
     */
    public static String getLayoutName(String layout) {
        if (layout == null || !layout.startsWith("@") || !layout.contains("/")) {
            return null; // it's not layout identifier
        }

        String[] parts = layout.split("/");
        if (parts.length != 2) {
            return null; // not enough parts
        }

        return parts[1];
    }

    /**
     * Display simple notification - information
     *
     * @param project
     * @param text
     */
    public static void showInfoNotification(Project project, String text) {
        showNotification(project, MessageType.INFO, text);
    }

    /**
     * Display simple notification - error
     *
     * @param project
     * @param text
     */
    public static void showErrorNotification(Project project, String text) {
        showNotification(project, MessageType.ERROR, text);
    }

    /**
     * Display simple notification of given type
     *
     * @param project
     * @param type
     * @param text
     */
    public static void showNotification(Project project, MessageType type, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);

        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(text, type, null)
                .setFadeoutTime(7500)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
    }

    /**
     * 直接在状态栏上显示文本
     *
     * @param project project
     * @param text    text
     */
    public static void showSimpleInfo(Project project, String text) {
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        statusBar.setInfo(text);
    }

    /**
     * Load field name prefix from code style
     *
     * @return
     */
    public static String getPrefix(Project project) {
        if (PropertiesComponent.getInstance().isValueSet(Settings.PREFIX)) {
            String value = PropertiesComponent.getInstance().getValue(Settings.PREFIX);
            logInfo("getPrefix has butterknifezelezny_prefix: " + value);
            return value;
        } else {
            CodeStyleSettings settings;
            if (project == null) {
                CodeStyleSettingsManager manager = CodeStyleSettingsManager.getInstance();
                settings = manager.getCurrentSettings();
                logInfo("getPrefix use current code style setting: " + settings.FIELD_NAME_PREFIX);
            } else {
                settings = CodeStyleSettingsManager.getSettings(project);
                logInfo("getPrefix use project code style setting: " + settings.FIELD_NAME_PREFIX);
            }
            return settings.FIELD_NAME_PREFIX;
        }
    }

    public static String getViewHolderClassName() {
        return PropertiesComponent.getInstance().getValue(Settings.VIEWHOLDER_CLASS_NAME, "ViewHolder");
    }

    /**
     * 删除不需要处理的Element
     */
    public static void dealElementList(ArrayList<Element> elements) {
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            if (!element.needDeal)
                iterator.remove();
        }
    }

    public static int getInjectCount(VGContext context, ArrayList<Element> elements) {
        int cnt = 0;
        for (Element element : elements) {
            if (!context.getFieldNameList().contains(element.fieldName)) {
                cnt++;
            }
        }
        return cnt;
    }

    public static int getClickCount(VGContext context, ArrayList<Element> elements) {
        int cnt = 0;
        for (Element element : elements) {
            if (!context.getClickIdsList().contains(element.getFullID())
                    && element.isClick) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Easier way to check if string is empty
     *
     * @param text
     * @return
     */
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().length() == 0);
    }

    /**
     * Check whether classpath of a module that corresponds to a {@link PsiElement} contains given class.
     *
     * @param project    Project
     * @param psiElement Element for which we check the class
     * @param className  Class name of the searched class
     * @return True if the class is present on the classpath
     * @since 1.3
     */
    public static boolean isClassAvailableForPsiFile(@NotNull Project project, @NotNull PsiElement psiElement, @NotNull String className) {
        Module module = ModuleUtil.findModuleForPsiElement(psiElement);
        if (module == null) {
            return false;
        }
        GlobalSearchScope moduleScope = module.getModuleWithDependenciesAndLibrariesScope(false);
        PsiClass classInModule = JavaPsiFacade.getInstance(project).findClass(className, moduleScope);
        return classInModule != null;
    }

    /**
     * Check whether classpath of a the whole project contains given class.
     * This is only fallback for wrongly setup projects.
     *
     * @param project   Project
     * @param className Class name of the searched class
     * @return True if the class is present on the classpath
     * @since 1.3.1
     */
    public static boolean isClassAvailableForProject(@NotNull Project project, @NotNull String className) {
        PsiClass classInModule = JavaPsiFacade.getInstance(project).findClass(className,
                new EverythingGlobalScope(project));
        return classInModule != null;
    }

    /**
     * 去除空格换行等字符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isLayoutStatement(PsiStatement statement) {
        return statement instanceof PsiExpressionStatement && statement.getText().contains("R.layout.");
    }

    public static String getIdFromLayoutStatement(String layoutStatement) {
        StringBuilder stringBuilder = new StringBuilder(layoutStatement);
        stringBuilder.delete(0, stringBuilder.indexOf("R.layout."));

        if (stringBuilder.indexOf(")") > 0) {
            stringBuilder.delete(stringBuilder.indexOf(")"), stringBuilder.length());
        }

        if (stringBuilder.indexOf(";") > 0) {
            stringBuilder.delete(stringBuilder.indexOf(";"), stringBuilder.length());
        }

        return replaceBlank(stringBuilder.toString());
    }

    public static void clearStringBuilder(StringBuilder builder) {
        if (builder == null)
            return;

        builder.delete(0, builder.length());
    }

    public static boolean ifClassContainsMethod(PsiClass psiClass, String methodName) {
        return psiClass.findMethodsByName(methodName, false).length != 0;
    }

    public static PsiClass getEditedClass(Editor editor, Project project, boolean strict) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        if (element != null) {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class, strict);
            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }

    public static PsiClass getEditedClass(Editor editor, PsiFile file, boolean strict) {
        PsiElement element = file.findElementAt(editor.getCaretModel().getOffset());
        if (element != null) {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class, strict);
            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }

    public static String getClassEntityName(String className) {
        int pos = className.lastIndexOf('.');
        if (pos > 0) {
            className = className.substring(pos + 1);
        }
        if (className.endsWith("PO") || className.endsWith("VO")) {
            className = className.substring(0, className.length() - 2);
        }
        return className;
    }

    public static String replaceFullPkgWithGivenClass(String fullPkg, String oriClass) {
        final int pos = fullPkg.lastIndexOf('.');
        if (pos > 0) {
            return fullPkg.substring(0, pos + 1) + getClassEntityName(oriClass);
        }
        return fullPkg;
    }

    private static final Logger sLOG = Logger.getInstance(Utils.class);

    public static void logInfo(String text) {
        sLOG.info(text);
    }

    public static String amendFieldName(String name) {
        if (name.endsWith("PO") || name.endsWith("VO")) {
            name = name.substring(0, name.length() - 2);
        }
        return name;
    }

    public static String amendFieldNameWithoutPrefix(String name) {
        return removeFieldPrefix(amendFieldName(name));
    }

    public static String amendListFieldName(String name) {
        if (name.endsWith("s")) {
            name = name.substring(0, name.length() - 1);
        }
        if (name.endsWith("List")) {
            name = name.substring(0, name.length() - 4);
        }
        if (name.endsWith("PO") || name.endsWith("VO")) {
            name = name.substring(0, name.length() - 2);
        }
        return name + "List";
    }

    public static String amendListFieldNameWithoutPrefix(String name) {
        return removeFieldPrefix(amendListFieldName(name));
    }


    public static String removeFieldPrefix(String name) {
        if (name.startsWith("m")) {
            name = name.substring(1);
        }
        return name;
    }

    public static String getListMethodEntifyNameWithoutPrefix(String name) {
        return removeFieldPrefix(getListMethodEntifyName(name));
    }

    public static String getListMethodEntifyName(String name) {
        if (name.endsWith("s")) {
            name = name.substring(0, name.length() - 1);
        }
        if (name.endsWith("List")) {
            name = name.substring(0, name.length() - 4);
        }
        if (name.endsWith("PO") || name.endsWith("VO")) {
            name = name.substring(0, name.length() - 2);
        }
        return name;
    }

    public static String getFieldNameFromMethod(String methodText) {
        String resultStr;
        final int returnPos = methodText.indexOf("return");
        if (returnPos > 0) {
            final int endPos = methodText.indexOf(';', returnPos);
            if (endPos > 0) {
                resultStr = methodText.substring(returnPos + 6 + 1, endPos).trim();
            } else {
                resultStr = "GetFieldError";
            }
        } else {
            int endPos = methodText.indexOf('=');
            if (endPos > 0) {
                int startPos = methodText.indexOf('{');
                String filedName = methodText.substring(startPos + 1, endPos);
                filedName = filedName.replace('\n', ' ');
                resultStr = filedName.trim();
            } else {
                resultStr = "SetFieldError";
            }
        }

        if (resultStr.startsWith("this.")) {
            resultStr = resultStr.substring(5);
        }
        return resultStr;
    }

    public static final String JAVA_LIST_TYPE = "java.util.List";

    public static String getListInnerType(String canonicalText) {
        String itemCanonicalText = canonicalText.substring(JAVA_LIST_TYPE.length() + 1, canonicalText.length() - 1);
        return itemCanonicalText;
    }
}
