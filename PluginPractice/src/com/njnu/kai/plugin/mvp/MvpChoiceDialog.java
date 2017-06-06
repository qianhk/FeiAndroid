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
    private JCheckBox mCbListFragment;
    private JCheckBox mCbListAdapter;
    private JTextField mEdtListFragment;
    private JTextField mEdtListAdapter;
    private JTextField mEdtEntityName;
    private JCheckBox mCbListPesenter;
    private JTextField mEdtListPresenter;

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

        mEdtActivity.setText(params.getListActivityPackageName());
        mEdtListFragment.setText(params.getListFragmentPackageName());
        mEdtListAdapter.setText(params.getListAdapterPackageName());
        mEdtListPresenter.setText(params.getListPresenterPackageName());

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
        mParams.setListActivityPackageName(getFullPackageName(mEdtActivity));
        mParams.setListFragmentPackageName(getFullPackageName(mEdtListFragment));
        mParams.setListAdapterPackageName(getFullPackageName(mEdtListAdapter));
        mParams.setListPresenterPackageName(getFullPackageName(mEdtListPresenter));
        mParams.setCheckActivity(mCbActivity.isSelected());
        mParams.setCheckListFragment(mCbListFragment.isSelected());
        mParams.setCheckListAdapter(mCbListAdapter.isSelected());
        mParams.setCheckListPresenter(mCbListPesenter.isSelected());
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
