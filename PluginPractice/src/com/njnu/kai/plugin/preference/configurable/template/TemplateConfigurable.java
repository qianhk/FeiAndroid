package com.njnu.kai.plugin.preference.configurable.template;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.njnu.kai.plugin.practice.HelloWorldConfigurationForm;
import com.njnu.kai.plugin.preference.persistence.TemplateSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16/8/13
 */
public class TemplateConfigurable extends BaseConfigurable {

    private JPanel editorPanel = new JPanel(new GridLayout());

    private Editor editor;

    private TemplateSettings templateSettings;

    private final String templateName;
    private final String templateHeaderText;
    private final String displayName;

    public TemplateConfigurable(String displayName, String templateHeaderText, String templateName) {
        this.displayName = displayName;
        this.templateHeaderText = templateHeaderText;
        this.templateName = templateName;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
//        templateSettings = TemplateSettings.getInstance();
//        editor = createEditorInPanel(templateSettings.provideTemplateForName(templateName));
//
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setPreferredSize(new Dimension(400, 300));
//        panel.add(SeparatorFactory.createSeparator(templateHeaderText, null), BorderLayout.PAGE_START);
//        panel.add(new ToolbarPanel(editorPanel, new DefaultActionGroup(new ResetToDefaultAction())), BorderLayout.CENTER);
//        return panel;

        final HelloWorldConfigurationForm helloWorldConfigurationForm = new HelloWorldConfigurationForm();
        helloWorldConfigurationForm.setTitleText(templateName);
        helloWorldConfigurationForm.setMsgText(templateHeaderText);
        return helloWorldConfigurationForm.getRootView();
    }

//    private Editor createEditorInPanel(String string) {
//        EditorFactory editorFactory = EditorFactory.getInstance();
//        Editor editor = editorFactory.createEditor(editorFactory.createDocument(string));
//
//        EditorSettings editorSettings = editor.getSettings();
//        editorSettings.setVirtualSpace(false);
//        editorSettings.setLineMarkerAreaShown(false);
//        editorSettings.setIndentGuidesShown(false);
//        editorSettings.setLineNumbersShown(false);
//        editorSettings.setFoldingOutlineShown(false);
//        editorSettings.setAdditionalColumnsCount(3);
//        editorSettings.setAdditionalLinesCount(3);
//
//        editor.getDocument().addDocumentListener(new DocumentAdapter() {
//            @Override
//            public void documentChanged(DocumentEvent e) {
//                onTextChanged();
//            }
//        });
//
//        ((EditorEx) editor).setHighlighter(getEditorHighlighter());
//
//        addEditorToPanel(editor);
//
//        return editor;
//    }
//
//    private LexerEditorHighlighter getEditorHighlighter() {
//        return new LexerEditorHighlighter(new JavaFileHighlighter(), EditorColorsManager.getInstance().getGlobalScheme());
//    }
//
//    private void onTextChanged() {
//        myModified = true;
//    }
//
//    private void addEditorToPanel(Editor editor) {
//        editorPanel.removeAll();
//        editorPanel.add(editor.getComponent());
//    }

    @Override
    public void disposeUIResources() {
//        if (editor != null) {
//            EditorFactory.getInstance().releaseEditor(editor);
//            editor = null;
//        }
//        templateSettings = null;
    }

    @Override
    public void apply() throws ConfigurationException {
//        templateSettings.setTemplateForName(templateName, editor.getDocument().getText());
//        setUnmodified();
    }

    @Override
    public void reset() {
//        EditorFactory.getInstance().releaseEditor(editor);
//        editor = createEditorInPanel(templateSettings.provideTemplateForName(templateName));
//        setUnmodified();
    }

    private void setUnmodified() {
        setModified(false);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return editorPanel;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

//    class ResetToDefaultAction extends AnAction {
//
//        ResetToDefaultAction() {
//            super(StringResources.RESET_TO_DEFAULT_ACTION_TITLE, StringResources.RESET_TO_DEFAULT_ACTION_DESCRIPTION, AllIcons.Actions.Reset);
//        }
//
//        @Override
//        public void actionPerformed(AnActionEvent anActionEvent) {
//            if (DialogsFactory.openResetTemplateDialog()) {
//                templateSettings.removeTemplateForName(templateName);
//                ApplicationManager.getApplication().runWriteAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        editor.getDocument().setText(templateSettings.provideTemplateForName(templateName));
//                        setUnmodified();
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void update(AnActionEvent e) {
//            super.update(e);
//            e.getPresentation().setEnabled(templateSettings.isUsingCustomTemplateForName(templateName));
//        }
//    }
}
