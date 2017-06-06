package com.njnu.kai.plugin.mvp;

import com.njnu.kai.plugin.util.Utils;

import javax.swing.*;
import java.awt.event.*;

public class TemplateMvpChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mEdtEntityName;
    private JCheckBox mCbNuwa;
    private MvpRuntimeParams mParams;

    public TemplateMvpChoiceDialog(MvpRuntimeParams params) {
        mParams = params;
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
    }

    private void onOK() {
        mParams.setEntityName(mEdtEntityName.getText().trim());
        if (Utils.isEmptyString(mParams.getEntityName())) {
            Utils.showErrorNotification(mParams.getProject(), "请填写页面名称");
            return;
        }
        mParams.setUseNuwa(mCbNuwa.isSelected());

        mParams.run();

        dispose();
    }

    private void onCancel() {
        dispose();
    }

//    public static void main(String[] args) {
//        TemplateMvpChoiceDialog dialog = new TemplateMvpChoiceDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
