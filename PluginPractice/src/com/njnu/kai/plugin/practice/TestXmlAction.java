package com.njnu.kai.plugin.practice;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.njnu.kai.plugin.util.Utils;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-14
 */
public class TestXmlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false);
        VirtualFile virtualFile = FileChooser.chooseFile(descriptor, project, null);

        XmlFile xmlFile = null;
        if (virtualFile != null) {
            if (!virtualFile.isDirectory() && virtualFile.getName().endsWith("xml")) {
                xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(virtualFile);
            }
        }
        if (xmlFile != null) {
            XmlDocument document = xmlFile.getDocument();
            if (document != null) {
                final XmlTag rootTag = document.getRootTag();
                if (rootTag != null) {
                    XmlTag[] subTags = rootTag.getSubTags();
                    for (XmlTag tag : subTags) {
                        XmlAttribute[] attributes = tag.getAttributes();
                        if (attributes.length > 0) {
                            XmlAttribute attribute = attributes[0];
                            Utils.logInfo(String.format("xml tag: %s, first attribute: %s=%s", tag.getName(), attribute.getName(), attribute.getValue()));
                        } else {
                            Utils.logInfo(String.format("xml tag: %s, no attribute.", tag.getName()));
                        }
                    }

                    //Throwable: Threading assertion.  Under write: false
//                    rootTag.setAttribute("timeK", String.valueOf(System.currentTimeMillis()));

                    new WriteCommandAction.Simple(project, "kai-write-xml") {
                        @Override
                        protected void run() throws Throwable {
                            rootTag.setAttribute("timeK", String.valueOf(System.currentTimeMillis()));
                        }
                    }.execute();
                }
            }
        }
    }
}
