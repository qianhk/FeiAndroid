package com.njnu.kai.plugin.mapper.form;

import com.intellij.openapi.ui.Messages;
import com.njnu.kai.plugin.mapper.TmpRuntimeParams;
import com.njnu.kai.plugin.util.Utils;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.event.*;

public class SettingDialog extends JDialog {
    private final TmpRuntimeParams mTmpRuntimeParams;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mVoClass;
    private JCheckBox mCbSupportList;
    private JTextField mMapperClass;

    public SettingDialog(TmpRuntimeParams tmpRuntimeParams) {
        mTmpRuntimeParams = tmpRuntimeParams;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

//        System.err.println("SettingDialog tmpRuntimeParams=" + tmpRuntimeParams);
        final String voClassFullName = Utils.replaceFullPkgWithGivenClass(tmpRuntimeParams.getVoClassCanonicalName(), tmpRuntimeParams.getOriginClass().getName()) + "VO";
        mVoClass.setText(voClassFullName);
        mMapperClass.setText(Utils.replaceFullPkgWithGivenClass(tmpRuntimeParams.getMapperClassCanonicalName(), tmpRuntimeParams.getOriginClass().getQualifiedName()) + "Mapper");
        mCbSupportList.setSelected(tmpRuntimeParams.isSupportList());
    }

    private void onOK() {
        String objectClassCanonicalName = getClassCanonicalName(mVoClass);
        if (objectClassCanonicalName == null) return;
        mTmpRuntimeParams.setVoClassCanonicalName(objectClassCanonicalName);

        String mapperClassCanonicalName = getClassCanonicalName(mMapperClass);
        if (mapperClassCanonicalName == null) return;
        mTmpRuntimeParams.setMapperClassCanonicalName(mapperClassCanonicalName);

        mTmpRuntimeParams.setSupportList(mCbSupportList.isSelected());

        mTmpRuntimeParams.getAction().run(mTmpRuntimeParams);

        dispose();
    }

    private String getClassCanonicalName(JTextField textField) {
        String canonicalName = textField.getText().replaceAll(" ", "").replaceAll(".java$", "");

        if (TextUtils.isEmpty(canonicalName) || canonicalName.endsWith(".")) {
            Messages.showMessageDialog(mTmpRuntimeParams.getProject(), "error", "the class name is not allowed", Messages.getErrorIcon());
            return null;
        }
        return canonicalName;
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
