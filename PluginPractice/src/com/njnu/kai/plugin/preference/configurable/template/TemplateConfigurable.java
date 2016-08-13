package com.njnu.kai.plugin.preference.configurable.template;

import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.JavaFileHighlighter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SeparatorFactory;
import com.intellij.ui.roots.ToolbarPanel;
import com.njnu.kai.plugin.preference.persistence.MvpTemplateSettings;
import com.njnu.kai.plugin.util.DialogsFactory;
import com.njnu.kai.plugin.util.StringResources;
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

    private final Project mProject;

    private JPanel mEditorPanel = new JPanel(new GridLayout());
    private Editor mEditor;

    private MvpTemplateSettings mMvpTemplateSettings;

    private final String mTemplateName;
    private final String mTemplateHeaderText;
    private final String mDisplayName;

    public TemplateConfigurable(Project project, String displayName, String templateHeaderText, String templateName) {
        mProject = project;
        mDisplayName = displayName;
        mTemplateHeaderText = templateHeaderText;
        mTemplateName = templateName;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mMvpTemplateSettings = MvpTemplateSettings.getInstance(mProject);
        if (mMvpTemplateSettings == null) {
            return new JLabel("TemplateSettings.getInstance() return null : " + mTemplateHeaderText);
        }
        mEditor = createEditorInPanel(mMvpTemplateSettings.provideTemplateForName(mTemplateName));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 300));
        panel.add(SeparatorFactory.createSeparator(mTemplateHeaderText, null), BorderLayout.PAGE_START);
        panel.add(new ToolbarPanel(mEditorPanel, new DefaultActionGroup(new ResetToDefaultAction())), BorderLayout.CENTER);
        return panel;
    }

    private Editor createEditorInPanel(String string) {
        EditorFactory editorFactory = EditorFactory.getInstance();
        Editor editor = editorFactory.createEditor(editorFactory.createDocument(string));

        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setVirtualSpace(false);
        editorSettings.setLineMarkerAreaShown(false);
        editorSettings.setIndentGuidesShown(false);
        editorSettings.setLineNumbersShown(false);
        editorSettings.setFoldingOutlineShown(false);
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);

        editor.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void documentChanged(DocumentEvent e) {
                onTextChanged();
            }
        });

        ((EditorEx) editor).setHighlighter(getEditorHighlighter());

        addEditorToPanel(editor);

        return editor;
    }

    private LexerEditorHighlighter getEditorHighlighter() {
        return new LexerEditorHighlighter(new JavaFileHighlighter(), EditorColorsManager.getInstance().getGlobalScheme());
    }

    private void onTextChanged() {
        myModified = true;
    }

    private void addEditorToPanel(Editor editor) {
        mEditorPanel.removeAll();
        mEditorPanel.add(editor.getComponent());
    }

    @Override
    public void disposeUIResources() {
        if (mEditor != null) {
            EditorFactory.getInstance().releaseEditor(mEditor);
            mEditor = null;
        }
        mMvpTemplateSettings = null;
    }

    @Override
    public void apply() throws ConfigurationException {
        mMvpTemplateSettings.setTemplateForName(mTemplateName, mEditor.getDocument().getText());
        setUnmodified();
    }

    @Override
    public void reset() {
        if (mEditor != null) {
            EditorFactory.getInstance().releaseEditor(mEditor);
            mEditor = createEditorInPanel(mMvpTemplateSettings.provideTemplateForName(mTemplateName));
        }
        setUnmodified();
    }

    private void setUnmodified() {
        setModified(false);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mEditorPanel;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    private class ResetToDefaultAction extends AnAction {

        ResetToDefaultAction() {
            super(StringResources.RESET_TO_DEFAULT_ACTION_TITLE, StringResources.RESET_TO_DEFAULT_ACTION_DESCRIPTION, AllIcons.Actions.Reset);
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
            if (DialogsFactory.openResetTemplateDialog()) {
                mMvpTemplateSettings.removeTemplateForName(mTemplateName);
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        mEditor.getDocument().setText(mMvpTemplateSettings.provideTemplateForName(mTemplateName));
                        setUnmodified();
                    }
                });
            }
        }

        @Override
        public void update(AnActionEvent e) {
            super.update(e);
            e.getPresentation().setEnabled(mMvpTemplateSettings.isUsingCustomTemplateForName(mTemplateName));
        }
    }
}
