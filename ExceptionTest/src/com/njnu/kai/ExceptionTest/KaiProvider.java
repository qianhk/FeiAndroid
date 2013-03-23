package com.njnu.kai.ExceptionTest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 13-3-23
 */
public class KaiProvider extends ContentProvider {
    private static final String LOG_TAG = "KaiProvider";

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        Log.d(LOG_TAG, "query uri=" + uri.toString() + " s=" + s);
        if (s == null) {
            Log.d(LOG_TAG, "will crash In Provider:");
            int i = 1 / 0;
        } else {
            Runnable runnable = new Runnable() {
                public void run() {
                    Log.d(LOG_TAG, "will crash In Provider Thread:");
                    int i = 1 / 0;
                }
            };
            new Thread(runnable).start();
        }
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
