package com.njnu.kai.plugin.mapper.form;

import com.intellij.openapi.ui.Messages;
import com.njnu.kai.plugin.mapper.MapperRuntimeParams;
import com.njnu.kai.plugin.util.Utils;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.awt.event.*;

public class SettingDialog extends JDialog {
    private final MapperRuntimeParams mMapperRuntimeParams;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mVoClass;
    private JTextField mMapperClass;
    private JCheckBox mCbCascade;

    public SettingDialog(MapperRuntimeParams mapperRuntimeParams) {
        mMapperRuntimeParams = mapperRuntimeParams;
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

//        System.err.println("SettingDialog mapperRuntimeParams=" + mapperRuntimeParams);
        final String voClassFullName = Utils.replaceFullPkgWithGivenClass(mapperRuntimeParams.getVoClassCanonicalName(), mapperRuntimeParams.getOriginClass().getName()) + "VO";
        mVoClass.setText(voClassFullName);
        mMapperClass.setText(Utils.replaceFullPkgWithGivenClass(mapperRuntimeParams.getMapperClassCanonicalName(), mapperRuntimeParams.getOriginClass().getQualifiedName()) + "Mapper");
    }

    private void onOK() {
        String objectClassCanonicalName = getClassCanonicalName(mVoClass);
        if (objectClassCanonicalName == null) return;
        mMapperRuntimeParams.setVoClassCanonicalName(objectClassCanonicalName);

        String mapperClassCanonicalName = getClassCanonicalName(mMapperClass);
        if (mapperClassCanonicalName == null) return;
        mMapperRuntimeParams.setMapperClassCanonicalName(mapperClassCanonicalName);

        mMapperRuntimeParams.setCascadeMapper(mCbCascade.isSelected());

        mMapperRuntimeParams.getAction().run(mMapperRuntimeParams);

        dispose();
    }

    private String getClassCanonicalName(JTextField textField) {
        String canonicalName = textField.getText().replaceAll(" ", "").replaceAll(".java$", "");

        if (TextUtils.isEmpty(canonicalName) || canonicalName.endsWith(".")) {
            Messages.showMessageDialog(mMapperRuntimeParams.getProject(), "error", "the class name is not allowed", Messages.getErrorIcon());
            return null;
        }
        return canonicalName;
    }

    private void onCancel() {
        dispose();
    }
}
