package com.njnu.kai.plugin.viewgenerator.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.njnu.kai.plugin.viewgenerator.common.InjectWriter;
import com.njnu.kai.plugin.viewgenerator.common.Utils;
import com.njnu.kai.plugin.viewgenerator.form.EntryList;
import com.njnu.kai.plugin.viewgenerator.iface.ICancelListener;
import com.njnu.kai.plugin.viewgenerator.iface.IConfirmListener;
import com.njnu.kai.plugin.viewgenerator.model.Element;
import com.njnu.kai.plugin.viewgenerator.model.VGContext;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by liquanmin on 16/2/24.
 */
public class ViewGenerateAction extends AnAction implements IConfirmListener, ICancelListener {
    protected JFrame mDialog;
    protected VGContext context;

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        actionPerformedImpl(project, editor);
    }

    private void actionPerformedImpl(Project project, Editor editor) {
        PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (file == null) {
            return;
        }

        PsiFile layout = Utils.getLayoutFileFromCaret(editor, file);
        if (layout == null) {
            Utils.showErrorNotification(project, "No layout found");
            return;
        }

        PsiClass clazz = Utils.getEditedClass(editor, file);
        if (clazz == null) {
            Utils.showErrorNotification(project, "No class found");
            return;
        }

        ArrayList<Element> elements = Utils.getIDsFromLayout(layout);
        if (elements.isEmpty()) {
            Utils.showErrorNotification(project, "No IDs found in layout");
            return;
        }

        context = new VGContext(project, file, layout, clazz);
        context.parseClass();
        context.preDealWithElements(elements);
        showDialog(elements);
    }

    protected void showDialog(ArrayList<Element> elements) {
        EntryList panel = new EntryList(context, elements, this, this);
        mDialog = new JFrame();
        mDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mDialog.getRootPane().setDefaultButton(panel.getConfirmButton());
        mDialog.getContentPane().add(panel);
        mDialog.pack();
        mDialog.setLocationRelativeTo(null);
        mDialog.setVisible(true);
    }

    @Override
    public void onCancel() {
        closeDialog();
    }

    @Override
    public void onConfirm(VGContext context, ArrayList<Element> elements, String fieldNamePrefix) {
        closeDialog();
        Utils.dealElementList(elements);
        if (Utils.getInjectCount(context, elements) > 0 || Utils.getClickCount(context, elements) > 0) { // generate injections
            new InjectWriter(context, "Generate Injections", elements, fieldNamePrefix).execute();
        } else { // just notify user about no element selected
            Utils.showInfoNotification(context.getProject(), "No injection was selected");
        }
    }

    protected void closeDialog() {
        if (mDialog == null) {
            return;
        }

        mDialog.setVisible(false);
        mDialog.dispose();
    }
}
