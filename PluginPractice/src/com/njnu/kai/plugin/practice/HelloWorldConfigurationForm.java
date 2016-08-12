package com.njnu.kai.plugin.practice;

import javax.swing.*;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 16-7-16
 */
public class HelloWorldConfigurationForm {
    private JTextField mEdtTitle;
    private JTextField mEdtMsg;
    private JPanel mRootView;

    public void setData(HelloworldMsg data) {
        mEdtTitle.setText(data.getTitle());
        mEdtMsg.setText(data.getMsg());
    }

    public void getData(HelloworldMsg data) {
        data.setTitle(mEdtTitle.getText());
        data.setMsg(mEdtMsg.getText());
    }

    public void setTitleText(String title) {
        if (mEdtTitle != null) {
            mEdtTitle.setText(title);
        }
    }

    public void setMsgText(String msg) {
        if (mEdtMsg != null) {
            mEdtMsg.setText(msg);
        }
    }

    public JPanel getRootView() {
        return mRootView;
    }

    public boolean isModified(HelloworldMsg data) {
        if (mEdtTitle.getText() != null ? !mEdtTitle.getText().equals(data.getTitle()) : data.getTitle() != null) return true;
        if (mEdtMsg.getText() != null ? !mEdtMsg.getText().equals(data.getMsg()) : data.getMsg() != null) return true;
        return false;
    }
}
