package com.njnu.kai.plugin.mvp;

import com.njnu.kai.plugin.util.Utils;

import javax.swing.*;
import java.awt.event.*;

public class MvpChoiceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox mCbActivity;
    private JTextField mEdtActivity;
    private JCheckBox mCbFragment;
    private JCheckBox mCbAdapter;
    private JTextField mEdtFragment;
    private JTextField mEdtAdapter;
    private JTextField mEdtEntityName;

    private MvpRuntimeParams mParams;

    public MvpChoiceDialog(MvpRuntimeParams params) {
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

        mEdtActivity.setText(params.getActivityCanonicalName());
        mEdtFragment.setText(params.getFragmentCanonicalName());
        mEdtAdapter.setText(params.getAdapterCanonicalName());

        mEdtEntityName.requestFocus();
    }

    private String getFullPackageName(JTextField field) {
        String packageName = field.getText().trim();
        int length = packageName.length();
        if (length > 0 && packageName.endsWith(".")) {
            packageName = packageName.substring(0, length -1 );
        }
        return packageName;
    }

    private void onOK() {
        mParams.setActivityCanonicalName(getFullPackageName(mEdtActivity));
        mParams.setFragmentCanonicalName(getFullPackageName(mEdtFragment));
        mParams.setAdapterCanonicalName(getFullPackageName(mEdtAdapter));
        mParams.setCheckActivity(mCbActivity.isSelected());
        mParams.setCheckFragment(mCbFragment.isSelected());
        mParams.setCheckAdapter(mCbAdapter.isSelected());
        mParams.setEntityName(mEdtEntityName.getText().trim());

        if (Utils.isEmptyString(mParams.getEntityName())) {
            Utils.showErrorNotification(mParams.getProject(), "请填写页面实体名称");
            return;
        }

        mParams.run();

        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
