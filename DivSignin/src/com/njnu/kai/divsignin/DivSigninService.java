package com.njnu.kai.divsignin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.njnu.kai.divsignin.common.DivConst;
import com.njnu.kai.divsignin.common.HttpUtility;

public class DivSigninService extends Service {

	private static String PREFIX = "DivSigninService";
	private boolean mThreadContinue = true;
	private String mDevAccountUid;
	private String mDevAccountName;
	private String mDevAccountPw;
	private String mQiangLouTitle;
	private String mQiangLouContent;

	private Thread mThreadQiangLou = new Thread(new Runnable() {

		@Override
		public void run() {
			Log.i(PREFIX, "run");
			doThreadQiangLou();
		}
	});

	private void doThreadQiangLou() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		mDevAccountUid = settings.getString("div_user_uid", "");
		mDevAccountName = settings.getString("div_user_name", "");
		mDevAccountPw = settings.getString("div_user_pw", "");
		mQiangLouTitle = settings.getString("divsignin_qianglou_title", "");
		mQiangLouContent = settings.getString("divsignin_qianglou_content", "");
		if (TextUtils.isEmpty(mDevAccountUid) || TextUtils.isEmpty(mDevAccountName) || TextUtils.isEmpty(mDevAccountPw)) {
			sendNotifyBroadcast(1, "Error: Check your account.");
			stopSelf();
			return;
		}
		if (TextUtils.isEmpty(mQiangLouTitle) || TextUtils.isEmpty(mQiangLouContent)) {
			sendNotifyBroadcast(1, "Error: Need Qianglou title and content.");
			stopSelf();
			return;
		}

		sendNotifyBroadcast("Start，Check Cookie ? [" + mDevAccountName + "]");
		if (IsValidDevdCookie()) {
			sendNotifyBroadcast("Sucess, Cookie Valid.");
		} else {
			sendNotifyBroadcast("need login, login...");
			boolean loginSucess = loginDevdBBs();
			if (loginSucess) {
				sendNotifyBroadcast("login Sucess.");
			} else {
				sendNotifyBroadcast(1, "Error: Login Failed.");
				stopSelf();
				return;
			}
		}

		if (!mThreadContinue)
			return;

		String _lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/forum.php?mod=post&action=newthread&fid=151&specialextra=donglin8_signin");
		if (_lastContent.indexOf(mDevAccountName) < 0) {
			sendNotifyBroadcast(1, "Error: Open qianglou url failed.");
			stopSelf();
			return;
		} else {
			sendNotifyBroadcast("Open qianglou url Sucess.");
		}

		if (!mThreadContinue)
			return;

		Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"formhash\".+? value=\"(\\w+?)\" />");
		Matcher match = pattern.matcher(_lastContent);
		match.find();
		String formhash = match.group(1);

		pattern = Pattern.compile("<input type=\"hidden\" name=\"posttime\".+? value=\"(\\w+?)\" />");
		match = pattern.matcher(_lastContent);
		match.find();
		String posttime = match.group(1);
		
		String postData = String
				.format("formhash=%s&posttime=%s&wysiwyg=0&special=127&specialextra=donglin8_signin&subject=%s&checkbox=0&message=%s&replycredit_extcredits=0&replycredit_times=1&replycredit_membertimes=1&replycredit_random=100&readperm=&tags=&rushreplyfrom=&rushreplyto=&rewardfloor=&stopfloor=&save=&uploadalbum=307&newalbum=&usesig=1&allownoticeauthor=1",
						formhash, posttime, mQiangLouTitle, mQiangLouContent);
		String postUrl = "http://www.devdiv.com/forum.php?mod=post&action=newthread&fid=151&extra=&topicsubmit=yes";
		_lastContent = HttpUtility.PostUseAutoEncoding(postUrl, postData, "GBK");
		if (_lastContent.indexOf("对不起，已有人先你") > 0) {
			Log.i(PREFIX, "Sorry, Too Late.");
		} else if (_lastContent.indexOf("未到\u62a2\u697c时间") > 0) {
			Log.i(PREFIX, "QiangLou not begin.");
		} else if (_lastContent.indexOf(mQiangLouContent) > 0) {
			Log.i(PREFIX, "QiangLou Sucess.");
		} else {
			Log.i(PREFIX, _lastContent);
		}

		sendNotifyBroadcast(2, "Operate Finish.");
		stopSelf();

		/*
		 * L_BEGINQIANG: //抢楼签到 if (bBegin==-1) { goto L_BEGINQIANG_END; } // goto L_BEGINQIANG_END; //goto L_GUANSHUI2; //
		 * http://www.devdiv.net/bbs/post.php?action=newthread&fid=151&specialextra=grab_signin content =
		 * _hh.Get("http://www.devdiv.net/bbs/post.php?action=newthread&fid=151&specialextra=grab_signin");
		 * 
		 * //txtQianglouResult.Text += content; if (content.IndexOf(devzh) != -1) { AddMessageToTxt("打开抢楼签到发贴页面成功.....\r\n"); } else {
		 * AddMessageToTxt("打开抢楼签到发贴页面失败!\r\n\r\n"); Thread.Sleep(2000); goto L_BEGINQIANG; } string Pattern =
		 * "<input type=\"hidden\" name=\"formhash\" id=\"formhash\" value=\"(\\w*)\" />"; Match verifyVar = Regex.Match(content, Pattern,
		 * RegexOptions.Multiline); string formhash = verifyVar.Result("$1").ToString().Trim(); //txtQianglouResult.Text += formhash;
		 * //txtQianglouResult.Text += "\r\n";
		 * 
		 * Pattern = "<input type=\"hidden\" name=\"posttime\" id=\"posttime\" value=\"(\\w*)\" />"; verifyVar = Regex.Match(content, Pattern,
		 * RegexOptions.Multiline); string posttime = verifyVar.Result("$1").ToString().Trim(); //txtQianglouResult.Text += posttime;
		 * //txtQianglouResult.Text += "\r\n";
		 * 
		 * //string posttime = "1270482003"; //string secanswer= "9"; //string formhash = "63e73374"; command = "formhash=" + formhash + "&posttime="
		 * + posttime + "&wysiwyg=0&special=127&subject=" + HttpHelper.ChineseEncode(txtQianglouTitle.Text) + "&checkbox=0&message=" +
		 * HttpHelper.ChineseEncode(txtQianglouContent.Text) + "&tags=&readperm=&attention_add=1&addfeed=1"; //&secanswer=" + secanswer;
		 * //txtQianglouResult.Text += command; //txtQianglouResult.Text += "\r\n\r\n"; ijs++;
		 * //formhash=04411b82&posttime=1341674824&wysiwyg=0&special
		 * =127&specialextra=donglin8_signin&subject=uiui&checkbox=0&message=abcdef&replycredit_extcredits
		 * =0&replycredit_times=1&replycredit_membertimes
		 * =1&replycredit_random=100&readperm=&tags=&rushreplyfrom=&rushreplyto=&rewardfloor=&stopfloor=&
		 * save=&uploadalbum=307&newalbum=&usesig=1&allownoticeauthor=1 content =
		 * _hh.Post("http://www.devdiv.com/forum.php?mod=post&action=newthread&fid=151&extra=&topicsubmit=yes", command);
		 * 
		 * 
		 * if (content.IndexOf("对不起，已有人先你抢到今日楼主了，请返回明日继续。") != -1) { Pattern = @"GMT\+8, (.+) (\d\d:\d\d)\."; verifyVar = Regex.Match(content,
		 * Pattern, RegexOptions.Multiline); string nowtime = verifyVar.Result("$2").ToString().Trim();
		 * AddMessageToTxt("对不起，已有人先你抢到今日楼主了，请返回明日继续。"+nowtime+"\r\n\r\n"); TipNotify("Devdiv：已有人先你抢到今日楼主了 "+ijs.ToString()); string st1 = "23:59";
		 * string st2 = "00:02"; DateTime dtNowtime = DateTime.Parse(nowtime); DateTime dttime1 = DateTime.Parse(st1); DateTime dttime2 =
		 * DateTime.Parse(st2); if (DateTime.Compare(dtNowtime, dttime2) <= 0) { //被其他人抢了，转抢楼跟帖流程 AddMessageToTxt("转抢楼跟帖流程....\r\n\r\n");
		 * TipNotify("Devdiv转抢楼跟帖流程...."); goto L_QIANLOUGENTIAN; } if (DateTime.Compare(dtNowtime, dttime1) >= 0) { //sleeptime = 1000; sleeptime =
		 * (sleeptime > 1000) ? 1000 : sleeptime; sleeptime -= 40; sleeptime = (sleeptime < 30) ? 30 : sleeptime; }
		 * 
		 * //AddMessageToTxt(content); //return;
		 * 
		 * Thread.Sleep(sleeptime); goto L_BEGINQIANG; } else if (content.IndexOf(txtQianglouTitle.Text) != -1) {
		 * AddMessageToTxt("发帖子抢楼签到成功....\r\n\r\n"); TipNotify("Devdiv发帖子抢楼签到成功"); goto L_GUANSHUI; } else if (content.IndexOf("验证问答回答错误，无法提交，请返回修改。")
		 * > -1) { AddMessageToTxt("验证问答回答错误，无法提交，请返回修改。\r\n停止操作！！！！！\r\n\r\n"); goto L_BEGINQIANG_END; } else { string tmpstr = "未知错误重试…………\r\n\r\n";
		 * TipNotify("Devdiv未知错误重试"); tmpstr += content; tmpstr += "\r\n\r\n"; AddMessageToTxt(tmpstr); Thread.Sleep(2000); goto L_BEGINQIANG; }
		 * L_QIANLOUGENTIAN: //抢楼跟帖 content = _hh.Get("http://www.devdiv.net/bbs/forum-151-1.html"); if (content.IndexOf("版块主题") == -1) {
		 * AddMessageToTxt("打开抢楼签到板块失败，重试....\r\n\r\n"); Thread.Sleep(1000); goto L_QIANLOUGENTIAN; } Pattern =
		 * 
		 * @"版块主题<(.+?)<a href=""thread([-\d]+).html"" title=""新窗口打开"" target=""_blank"">"; verifyVar = Regex.Match(content, Pattern,
		 * RegexOptions.Singleline); string urlnew = verifyVar.Result("$2").ToString().Trim(); string thistianziid = urlnew.Substring(1,5); urlnew =
		 * "http://www.devdiv.net/bbs/thread" + urlnew + ".html"; L_QLST: AddMessageToTxt("打开抢楼签到首贴:"+urlnew+"....\r\n"); content = _hh.Get(urlnew);
		 * if (content.IndexOf("感谢大家对论坛支持决定举办每日签到活动") == -1) { AddMessageToTxt("打开抢楼签到首贴失败，重试....\r\n\r\n"); Thread.Sleep(1000); goto L_QLST; }
		 * Pattern = @"<form method=""post"" id=""fastpostform"" action="
		 * "post\.php\?action=reply&amp;fid=(.+)&amp;replysubmit=yes&amp;infloat=yes&amp;handlekey=fastpost"" onSubmit=""return
		 * fastpostvalidate\(this\)"">"; verifyVar = Regex.Match(content, Pattern, RegexOptions.Singleline); string posturl =
		 * verifyVar.Result("$1").ToString().Trim(); posturl = "http://www.devdiv.net/bbs/post.php?action=reply&fid=" + posturl +
		 * "&replysubmit=yes&infloat=yes&handlekey=fastpost&inajax=1"; string postparam; Pattern =
		 * 
		 * @"<input type=""hidden"" name=""formhash"" value=""(.+?)"" />"; verifyVar = Regex.Match(content, Pattern, RegexOptions.Singleline);
		 * postparam = verifyVar.Result("$1").ToString().Trim(); postparam = "formhash=" + postparam + "&subject=&usesig=0&addfeed=1&message=" +
		 * HttpHelper.ChineseEncode(txtQianglouGentian.Text); posturl = posturl.Replace("&amp;", "&"); L_QLSTFT: content = _hh.Post(posturl,
		 * postparam); if (content.IndexOf("非常感谢，您的回复已经发布，现在将转入主题页") == -1) { AddMessageToTxt("发帖到抢楼签到首贴失败，重试....\r\n\r\n"); Thread.Sleep(1000); goto
		 * L_QLSTFT; } posturl = "http://www.devdiv.net/bbs/plugin.php?id=grab_signin:submit&tid=" + thistianziid; L_QLSTLF: content =
		 * _hh.Get(posturl); if (content.IndexOf(devzh) == -1) { AddMessageToTxt("领分失败， 重试....\r\n\r\n"); Thread.Sleep(1000); goto L_QLSTLF; }
		 * AddMessageToTxt("领分成功....\r\n"); TipNotify("Devdiv抢楼签到跟帖并领分成功");
		 */
		// int i = 0;
		// while (mThreadContinue) {
		// ++i;
		// // Log.i(PREFIX, "doThreadQiangLou " + (++i));
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

	private boolean loginDevdBBs() {
		String _lastContent = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/member.php?mod=logging&action=login");
		if (_lastContent == null || _lastContent.indexOf("登录 -  DEVDIV.COM") < 0) {
			return false;
		}
		// AddLineToResult(_lastContent);
		Pattern pattern = Pattern.compile("<input type=\"hidden\" name=\"formhash\" value=\"(\\w+?)\" />");
		Matcher match = pattern.matcher(_lastContent);
		match.find();
		String formhash = match.group(1);

		pattern = Pattern.compile("action=\"(member.php\\?mod=logging.+?)\">");
		match = pattern.matcher(_lastContent);
		match.find();
		String posturl = match.group(1).replace("&amp;", "&");

		pattern = Pattern.compile("value=\"(\\d+?)\"  />自动登录</label>");
		match = pattern.matcher(_lastContent);
		match.find();
		String cookietime = match.group(1);

		String md5pw = HttpUtility.getMD5(mDevAccountPw.getBytes());
		// List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		// formparams.add(new BasicNameValuePair("formhash", formhash));
		// formparams.add(new BasicNameValuePair("referer", ""));
		// formparams.add(new BasicNameValuePair("username", devUserAccount));
		// formparams.add(new BasicNameValuePair("password", md5pw));
		// formparams.add(new BasicNameValuePair("questionid", "0"));
		// formparams.add(new BasicNameValuePair("answer", ""));
		// formparams.add(new BasicNameValuePair("cookietime", cookietime));
		String postdata = String.format("formhash=%s&referer=&username=%s&password=%s&questionid=0&answer=&cookietime=%s", formhash, mDevAccountName,
				md5pw, cookietime);
		// AddLineToResult(posturl + " " + postdata);
		Log.i(PREFIX, "postData=" + postdata);
		_lastContent = HttpUtility.PostUseAutoEncoding("http://www.devdiv.com/" + posturl, postdata, HTTP.UTF_8);
		// AddLineToResult(_lastContent);
		return _lastContent != null && _lastContent.indexOf("欢迎您回来") >= 0;
	}

	private boolean IsValidDevdCookie() {
		String needStr = String.format("<a href=\"http://www.devdiv.com/space-uid-%s.html\" target=\"_blank\" title=\"访问我的空间\">%s</a>",
				mDevAccountUid, mDevAccountName);
		String _lastContent = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/forum-154-1.html");
		// Log.i(PREFIX, _lastContent);
		// sendNotifyBroadcast(_lastContent);
		return _lastContent != null && _lastContent.indexOf(needStr) >= 0;
	}

	private void sendNotifyBroadcast(String message) {
		sendNotifyBroadcast(0, message);
	}

	private void sendNotifyBroadcast(int type, String message) {
		Intent intent = new Intent(DivConst.ACTION_QIANGLOU_NOTIFY);
		intent.putExtra("type", type);
		intent.putExtra("message", message);
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(PREFIX, "onCreate");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread.State state = mThreadQiangLou.getState();
		Log.i(PREFIX, "thread state=" + state);
		if (mThreadQiangLou.getState() == Thread.State.NEW) {
			mThreadQiangLou.start();
		} else {
			// mThreadContinue = false;
		}
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(PREFIX, "onDestroy");
		mThreadContinue = false;
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(PREFIX, "onUnbind");
		return super.onUnbind(intent);
	}

}
