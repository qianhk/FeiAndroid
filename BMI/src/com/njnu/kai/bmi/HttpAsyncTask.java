/**
 * @(#)HttpAsyncTask.java		2012-10-31
 *
 */
package com.njnu.kai.bmi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 *
 * @version 1.0.0
 * @since 2012-10-31
 */

interface HttpAsyncTaskNotify {
	public void notifyResult(int id, String result);
}

public class HttpAsyncTask extends AsyncTask<Object, Void, String> {
	private static final String LOG_TAG = "HttpAsyncTask";
	public static final String TEST_URL_BLUE = "";
	public static final String TEST_URL_YELLOW = "";
	public static final String TEST_URL_PINK = "";
	public static final String TEST_URL_GRAY = "";
	public static final String TEST_URL_GREEN = "";
	private HttpAsyncTaskNotify mNotify;
	private int mId;
	private Context mContext;


	public HttpAsyncTask(Context context, HttpAsyncTaskNotify notify) {
		super();
		mContext = context;
		mNotify = notify;
	}

	@Override
	protected String doInBackground(Object... params) {
		String result = "empty";
		mId = (Integer)params[0];
		switch (mId) {
		case R.id.iv_color_blue:
			result = HttpUtility.WriteToFile(TEST_URL_BLUE, "/sdcard/downTestBlue.file");
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_BLUE, "/sdcard/downTestBlue.file");
			break;

		case R.id.iv_color_yellow:
//			result = HttpUtility.WriteToFile(TEST_URL_YELLOW, "/sdcard/downTestYellow.file");
			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_YELLOW, "/sdcard/downTestYellow.file");
			break;

		case R.id.iv_color_pink:
//			result = HttpUtility.WriteToFile(TEST_URL_PINK, "/sdcard/downTestPink.file");
			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_PINK, "/sdcard/downTestPink.file");
			break;

		case R.id.iv_color_gray:
//			result = HttpUtility.WriteToFile(TEST_URL_GRAY, "/sdcard/downTestGray.file");
			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_GRAY, "/sdcard/downTestGray.file");
			break;
//			result = HttpSocketByGet.GetUseAutoEncoding(mContext, HttpUtility.TTLRCMIME);
//			break;

		case R.id.iv_color_green:
//			result = HttpUtility.WriteToFile(TEST_URL_GREEN, "/sdcard/downTestGreen.file");
			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_GREEN, "/sdcard/downTestGreen.file");
			break;
//			result = HttpSocketByConnect.GetUseAutoEncoding(mContext, HttpUtility.TTLRCMIME);
//			break;

		case 1:
			result = new SendEmail2().sendEmail((String)params[1]);
			break;

		case 2:
//			testPictureDecord();
			break;

		default:
			break;
		}
		return result;
	}

//	private void testPictureDecord() {
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(result, options);
//		boolean check = (options.outMimeType != null);
//	}

	@Override
	protected void onPostExecute(String result) {
		mNotify.notifyResult(mId, result);
	}

}
