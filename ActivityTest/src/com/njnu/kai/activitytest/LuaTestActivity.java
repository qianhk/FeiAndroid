package com.njnu.kai.activitytest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @version 1.0.0
 * @since 13-7-6
 */
public class LuaTestActivity extends Activity {

    private LuaState mLuaState;
    private TextView mDisplay;
    private LinearLayout mLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lua_test);

        mLayout = (LinearLayout)findViewById(R.id.layout);
        mDisplay = (TextView)mLayout.findViewById(R.id.display);

        mLuaState = LuaStateFactory.newLuaState();
        mLuaState.openLibs();

        runStatement(mDisplay);
    }

    public void runStatement(View v) {
        mLuaState.LdoString(" varSay = 'This is string in lua script statement.'");
        mLuaState.getGlobal("varSay");
        mDisplay.setText(mLuaState.toString(-1));
    }

    public void runFile(View v) {
        mLuaState.LdoString(readStream(getResources().openRawResource(R.raw.test)));
        mLuaState.getField(LuaState.LUA_GLOBALSINDEX, "functionInLuaFile");
        mLuaState.pushString("从java中传递参数");

        int paramCount = 1;
        int resultCount = 1;
        mLuaState.call(paramCount, resultCount);
        mLuaState.setField(LuaState.LUA_GLOBALSINDEX, "resultKey");
        mLuaState.getGlobal("resultKey");
        mDisplay.setText(mLuaState.toString(-1));
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int c = is.read();
            while (c != -1) {
                bo.write(c);
                c = is.read();
            }
            return bo.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public void callAndroidClick(View v) {
        mLuaState.LdoString(readStream(getResources().openRawResource(R.raw.test)));
        mLuaState.getField(LuaState.LUA_GLOBALSINDEX, "callAndroidApi");
        mLuaState.pushJavaObject(getApplicationContext());
        mLuaState.pushJavaObject(mLayout);
        mLuaState.pushString("push到textview");
        mLuaState.call(3, 0);
    }

}