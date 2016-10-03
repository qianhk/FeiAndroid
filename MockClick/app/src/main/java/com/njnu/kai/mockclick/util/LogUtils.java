/**
 * @(#)LogUtils.java     2011-8-15
 */
package com.njnu.kai.mockclick.util;

import android.util.Log;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class description here
 *
 */
public final class LogUtils {
    private static final SimpleDateFormat LOG_DATE_TIME_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static boolean sLogEnable = true;
    private static boolean sWriteToFile = false;
    private static String sLogFileName;

    private static String sPrefix = "MockClick:";

    /**
     * 设置Log开关
     *
     * @param enable 开关项(默认为开).
     */
    public static void setEnable(boolean enable) {
        sLogEnable = enable;
    }

    /**
     * 设置写入log到文件
     *
     * @param writeToFile 开关项(默认开)
     */
    public static void setWriteToFile(boolean writeToFile) {
        sWriteToFile = writeToFile;
    }

    /**
     * log for debug
     *
     * @param message log message
     * @param tag     tag
     * @see android.util.Log#d(String, String)
     */
    public static void d(String tag, String message) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for debug
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see android.util.Log#d(String, String, Throwable)
     */
    public static void d(String tag, String message, Throwable throwable) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.d(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for debug
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see android.util.Log#d(String, String)
     */
    public static void d(String tag, String format, Object... params) {
        if (sLogEnable) {
            String msg = String.format(sPrefix + format, params);
            Log.d(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for warning
     *
     * @param message log message
     * @param tag     tag
     * @see android.util.Log#w(String, String)
     */
    public static void w(String tag, String message) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.w(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for warning
     *
     * @param tag       tag
     * @param throwable throwable
     * @see android.util.Log#w(String, Throwable)
     */
    public static void w(String tag, Throwable throwable) {
        if (sLogEnable) {
            Log.w(tag, throwable);
            writeToFile(tag, Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for warning
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see android.util.Log#w(String, String, Throwable)
     */
    public static void w(String tag, String message, Throwable throwable) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.w(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for warning
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see android.util.Log#w(String, String)
     */
    public static void w(String tag, String format, Object... params) {
        if (sLogEnable) {
            String msg = String.format(sPrefix + format, params);
            Log.w(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for error
     *
     * @param message message
     * @param tag     tag
     * @see android.util.Log#i(String, String)
     */
    public static void e(String tag, String message) {
        String msg = sPrefix + message;
        Log.e(tag, msg);
        writeToFile(tag, msg);
    }

    /**
     * log for error
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see android.util.Log#i(String, String, Throwable)
     */
    public static void e(String tag, String message, Throwable throwable) {
        String msg = sPrefix + message;
        Log.e(tag, msg, throwable);
        writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
    }

    /**
     * log for error
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see android.util.Log#e(String, String)
     */
    public static void e(String tag, String format, Object... params) {
        String msg = String.format(sPrefix + format, params);
        Log.e(tag, msg);
        writeToFile(tag, msg);
    }

    /**
     * log for information
     *
     * @param message message
     * @param tag     tag
     * @see android.util.Log#i(String, String)
     */
    public static void i(String tag, String message) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.i(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for information
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see android.util.Log#i(String, String, Throwable)
     */
    public static void i(String tag, String message, Throwable throwable) {
        if (sLogEnable) {
            String msg = message;
            Log.i(tag, sPrefix + msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for information
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see android.util.Log#i(String, String)
     */
    public static void i(String tag, String format, Object... params) {
        if (sLogEnable) {
            String msg = String.format(sPrefix + format, params);
            Log.i(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for verbos
     *
     * @param message log message
     * @param tag     tag
     * @see android.util.Log#v(String, String)
     */
    public static void v(String tag, String message) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.v(tag, msg);
            writeToFile(tag, msg);
        }
    }

    /**
     * log for verbose
     *
     * @param message   log message
     * @param throwable throwable
     * @param tag       tag
     * @see android.util.Log#v(String, String, Throwable)
     */
    public static void v(String tag, String message, Throwable throwable) {
        if (sLogEnable) {
            String msg = sPrefix + message;
            Log.v(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
        }
    }

    /**
     * log for verbose
     *
     * @param tag    tag
     * @param format message format, such as "%d ..."
     * @param params message content params
     * @see android.util.Log#v(String, String)
     */
    public static void v(String tag, String format, Object... params) {
        if (sLogEnable) {
            String msg = String.format(sPrefix + format, params);
            Log.v(tag, msg);
            writeToFile(tag, msg);
        }
    }

    private static void writeToFile(String tag, String msg) {
        if (sWriteToFile) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(sLogFileName, true);
                fileWriter.write(String.format("%s pid=%d %s: %s\n", LOG_DATE_TIME_FORMAT.format(new Date()), android.os.Process.myPid(), tag, msg));
                fileWriter.flush();
                fileWriter.close();
            } catch (Throwable t) {
                t.printStackTrace();
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
