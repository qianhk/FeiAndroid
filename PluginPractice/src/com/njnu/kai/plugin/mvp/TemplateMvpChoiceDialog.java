package com.njnu.kai.plugin.mvp;

import com.njnu.kai.plugin.util.Utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class TemplateMvpChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField mEdtEntityName;
    private JCheckBox mCbNuwa;
    private JCheckBox mCbNuwaBinder;

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

        mCbNuwa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass().getSimpleName() + " actionPerformed: 33" + 33);
                mCbNuwaBinder.setEnabled(mCbNuwa.isSelected());
                mCbNuwaBinder.setSelected(mCbNuwa.isSelected());
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
        mCbNuwaBinder.setEnabled(false);
    }

    private void onOK() {
        mParams.setEntityName(mEdtEntityName.getText().trim());
        if (Utils.isEmptyString(mParams.getEntityName())) {
            Utils.showErrorNotification(mParams.getProject(), "请填写页面名称");
            return;
        }
        mParams.setUseNuwa(mCbNuwa.isSelected());
        mParams.setUserCreateNuwaBinder(mCbNuwaBinder.isSelected());
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
