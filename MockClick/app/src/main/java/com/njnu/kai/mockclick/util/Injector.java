package com.njnu.kai.mockclick.util;

import android.os.SystemClock;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Locale;

/**
 * @author Juergen Punz
 *         Injects InputEvents to Android-Device
 *         Needs root-access to Device and App needs superuser-rights (you will be asked for that at execution-time).
 */
public class Injector {

    private static final String TAG = "Injector";

    /**
     * Injects Swipe-Event from right to left
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean swipeRightLeft() throws IOException, InterruptedException {
        return executeCommand("input swipe 300 500 50 500 100");
    }

    /**
     * Injects Swipe-Event from left to right
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean swipeLeftRight() throws IOException, InterruptedException {
        return executeCommand("input swipe 50 500 300 500 100");
    }

    /**
     * Injects Touch-Event at x- and y-coordinates of screen
     *
     * @param x x-coordinate of screen
     * @param y y-coordinate of screen
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean touch(int x, int y) throws IOException, InterruptedException {
        return executeCommand(String.format(Locale.getDefault(), "input tap %d %d", x, y));
    }

    /**
     * Injects Unlock-Event for unlocking the device's screen
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean unlockDevice() throws IOException, InterruptedException {
        return executeCommand("input keyevent 82");
    }

    /**
     * Injects Powerbutton-Event for locking or activate the device's screen
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean pressPowerButton() throws IOException, InterruptedException {
        return executeCommand("input keyevent 26");
    }

    /**
     * Injects Homebutton-Event
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean pressHomeButton() throws IOException, InterruptedException {
        return executeCommand("input keyevent 3");
    }

    /**
     * Injects Backbutton-Event
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean pressBackButton() throws IOException, InterruptedException {
        return executeCommand("input keyevent 4");
    }

    /**
     * Injects Swipe-Event (up to down) for opening the Notificationcenter
     *
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    public static boolean showNotificationCenter() throws IOException, InterruptedException {
        return executeCommand("input swipe 10 10 10 1000");
    }

    /**
     * Runs given command in shell as superuser
     *
     * @param command Command to execute
     * @return If execution of shell-command was successful or not
     * @throws IOException
     * @throws InterruptedException
     */
    private static boolean executeCommand(String command) throws IOException, InterruptedException {
        long beginTime = SystemClock.elapsedRealtime();
        Process suShell = Runtime.getRuntime().exec("su");
        DataOutputStream commandLine = new DataOutputStream(suShell.getOutputStream());
//
        commandLine.writeBytes(command + '\n');
        commandLine.flush();
        commandLine.writeBytes("exit\n");
        commandLine.flush();

        long middleTime = SystemClock.elapsedRealtime();

//        String result = StringUtils.stringFromInputStream(suShell.getInputStream());
//        Log.i(TAG, "resultFromBash " + result);
        int waitResult = suShell.waitFor();
        long elapsed = SystemClock.elapsedRealtime() - beginTime;
        LogUtils.i(TAG, "executeCommand totalTime:%d mid:%d", elapsed, middleTime - beginTime);
        return waitResult == 0;
    }

    private static Process sSuShell;
    private static DataOutputStream sDataOutputStream;

    public static boolean canLargeExecuteCommand() {
        return sSuShell == null;
    }

    public static void beginCommand2() throws IOException, InterruptedException {
        if (sSuShell != null) {
            throw new InterruptedIOException("shell exists");
        }
        sSuShell = Runtime.getRuntime().exec("su");
        sDataOutputStream = new DataOutputStream(sSuShell.getOutputStream());
    }

    public static void executeCommand2(String command) throws IOException {
        long beginTime = SystemClock.elapsedRealtime();
        sDataOutputStream.writeBytes(command + '\n');
        sDataOutputStream.flush();
        LogUtils.i(TAG, "executeCommand2 time:%d", SystemClock.elapsedRealtime() - beginTime);
    }

    public static void touch2(int x, int y) throws IOException, InterruptedException {
        executeCommand2(String.format(Locale.getDefault(), "input tap %d %d", x, y));
    }

    public static boolean endCommand2() throws InterruptedException, IOException {
        if (sSuShell != null) {
            sDataOutputStream.writeBytes("exit\n");
            sDataOutputStream.flush();
            int waitResult = sSuShell.waitFor();
            sDataOutputStream = null;
            sSuShell = null;
            return waitResult == 0;
        } else {
            return true;
        }
    }
}
