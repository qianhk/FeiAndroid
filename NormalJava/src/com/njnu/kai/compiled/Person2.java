package com.njnu.kai.compiled;

import java.util.Locale;

/**
 * Created by kai
 * since 2017/5/12
 */
public class Person2 {

    public long mUserId;
    public String mName;

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "compiled Person uid=%d name=%s", mUserId, mName);
    }
}

