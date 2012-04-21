package com.njnu.kai.fordevd;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class MainFrm {

	private JFrame frmFordevd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("param1", "value1"));
//		formparams.add(new BasicNameValuePair("param2", "value2"));
//		try {
//			System.out.println(URLEncodedUtils.format(formparams, "UTF-8"));
////			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
//			StringEntity entity = new StringEntity("param1=value1&param2=value2", "UTF-8");
//			entity.setContentType(URLEncodedUtils.CONTENT_TYPE + HTTP.CHARSET_PARAM  + "UTF-8");
//			System.out.println(entity.getContentLength());
//			System.out.println(entity.getContentType());
//			System.out.println(entity.getContentEncoding());
//			System.out.println(entity.getClass());
//			byte[] datahah = new byte[32];
//			System.out.println(datahah);
//			InputStream ins = entity.getContent();
//			System.out.println(ins.available());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
////			System.out.println(ins.read());
//			ins.read(datahah);
//			System.out.println(new String(datahah));
//			System.out.println(entity.getContent());
//
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrm window = new MainFrm();
					window.frmFordevd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFordevd = new JFrame();
		frmFordevd.setResizable(false);
		frmFordevd.setTitle("ForDevD");
		frmFordevd.setBounds(100, 100, 800, 600);
		frmFordevd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblStatusbar = new JLabel("  Kai Java Gui Application");
		frmFordevd.getContentPane().add(lblStatusbar, BorderLayout.SOUTH);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmFordevd.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Friend", null, panel, null);
		panel.setLayout(null);

		btnFriendStart = new JButton("Start");
		btnFriendStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				devUid = txtDevUID.getText().trim();
				devUserAccount = txtDevAccount.getText().trim();
				devUserPW = String.valueOf(pwUidPw.getPassword()).trim();
				if (devUid.length() == 0 || devUserAccount.length() == 0 || devUserPW.length() == 0) {
					return;
				}

				btnFriendStart.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						threadDoAddFriendTask();
					}
				}).start();
			}
		});
		btnFriendStart.setBounds(656, 13, 117, 29);
		panel.add(btnFriendStart);

		txtFriendResult = new JTextArea();
		panel.add(txtFriendResult);
		txtFriendResult.setText("welcome.\r\n");
		txtFriendResult.setColumns(64);
		txtFriendResult.setRows(10);
		txtFriendResult.setBounds(2, 2, 425, 309);

		JScrollPane scrollPane = new JScrollPane(txtFriendResult);
		scrollPane.setBounds(0, 54, 773, 434);
		panel.add(scrollPane);

		txtDevUID = new JTextField();
		txtDevUID.setBounds(57, 12, 61, 28);
		panel.add(txtDevUID);
		txtDevUID.setColumns(10);

		JLabel UID = new JLabel("UID:");
		UID.setBounds(9, 18, 36, 16);
		panel.add(UID);

		JLabel lblNewLabel_1 = new JLabel("Account:");
		lblNewLabel_1.setBounds(143, 18, 61, 16);
		panel.add(lblNewLabel_1);

		txtDevAccount = new JTextField();
		txtDevAccount.setBounds(216, 12, 94, 28);
		panel.add(txtDevAccount);
		txtDevAccount.setColumns(10);

		JLabel lblNewLabel = new JLabel("PW:");
		lblNewLabel.setBounds(330, 18, 36, 16);
		panel.add(lblNewLabel);

		pwUidPw = new JPasswordField();
		pwUidPw.setColumns(16);
		pwUidPw.setBounds(366, 12, 104, 28);
		panel.add(pwUidPw);

		tabbedPane.addTab("Tab 2", new JLabel("Tab 2 Content"));
		tabbedPane.addTab("Tab 3", new JLabel("Tab 3 Content"));

		JMenuBar menuBar = new JMenuBar();
		frmFordevd.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmItem = new JMenuItem("Item1");
		mnFile.add(mntmItem);
	}

	private void threadDoAddFriendTask() {
		AddLineToResult("Start，祝好运，检查cookie是否有效......");
		boolean operateSucess = true;
		if (!IsValidDevdCookie()) {
			AddLineToResult("Cookie无效了，重新登录......");
			if (LoginDevdBBs()) {
				AddLineToResult("登录成功......");
			} else {
				AddLineToResult("登录失败，Over！");
				operateSucess = false;
			}
		} else {
			AddLineToResult("Cookie有效，无需重新登录!");
		}

		if (operateSucess) {
			checkFriendTimes = 0;
			AddLineToResult("开始每5分钟检查一次!");

			while(true) {
				++checkFriendTimes;
				AddLineToResult(String.format("第 %d 次检查开始.....", checkFriendTimes));
				try {
					String ls_task = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=task&do=apply&id=70");
					if (ls_task == null) {
						AddLineToResult("网络链接超时，本次忽略");
					} else if (ls_task.indexOf("抱歉，本期您已") >= 0) {
						AddLineToResult("No Task, 本次忽略");
						ls_task = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=task&do=draw&id=70");
					} else {
						AddLineToResult("Have Task, Doing...");
						Thread.sleep(2000);
						ls_task = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=task&do=view&id=70");
						Thread.sleep(2000);

						countDownLatch = new CountDownLatch(doTaskThreadAmount);
						taskSucessTimes = 0;
						Thread[] arrThread = new Thread[doTaskThreadAmount];
						for (int i = 0; i < doTaskThreadAmount; ++i) {
							arrThread[i] = new Thread(new Runnable() {
								@Override
								public void run() {
									String returnContent = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=task&do=draw&id=70");
									if (returnContent == null) {
//										AddLineToResult("Do Task: Timeout.");
									} else if (returnContent.indexOf("不是进行中的") >= 0) {
//										AddLineToResult("Do Task: 不是进行中的");
									} else {
//										AddLineToResult("Do Task: Ok");
										++taskSucessTimes;
									}
									countDownLatch.countDown();
								}
							});
						}
						for (Thread thread : arrThread) {
							thread.start();
						}
						countDownLatch.await();

						operateSucess = false;
						String leiAmount = null;
						String ls_seeMagic = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=magic&action=mybox");
						if (ls_seeMagic != null) {
							Pattern pattern = Pattern.compile("我的道具包容量: <span class=\"xi1\">(\\d+)</span>/(\\d+).+?<strong>雷鸣之声.+?数量: <font class=\"xi1 xw1\">(\\d+)", Pattern.DOTALL);
							Matcher match = pattern.matcher(ls_seeMagic);
							if (match.find()) {
								int curMagicAmount = Integer.parseInt(match.group(1));
								int canMagicAmount = Integer.parseInt(match.group(2));
								leiAmount = match.group(3);
								if ((curMagicAmount + 20) >= canMagicAmount) {
									String ls_sellMagic = HttpUtility.GetUseAutoEncoding("http://www.devdiv.com/home.php?mod=magic&action=mybox&operation=sell&magicid=14");
									if (ls_sellMagic != null) {
										pattern = Pattern.compile("name=\"formhash\" value=\"(\\w+?)\"");
										match = pattern.matcher(ls_sellMagic);
										if (match.find()) {
											String formHash = match.group(1);
											String sellPostUrl = String.format("http://www.devdiv.com/home.php?mod=magic&action=mybox&infloat=yes&inajax=1&formhash=%s&handlekey=magics&magicid=14&magicnum=%s&operatesubmit=yes&operation=sell", formHash, leiAmount);
											String sellPostData = String.format("formhash=%s&handlekey=magics&operation=sell&magicid=14&magicnum=%s&operatesubmit=yes", formHash, leiAmount);
											ls_sellMagic = HttpUtility.PostUseAutoEncoding(sellPostUrl, sellPostData, HTTP.UTF_8);
											if (ls_sellMagic != null && ls_sellMagic.indexOf("雷鸣之声") >= 0) {
												operateSucess = true;
											}
										}
									}
								}
							}
						}
						ls_seeMagic = ".";
						if (operateSucess) {
							ls_seeMagic = String.format(", and Sell %s.", leiAmount);
						}
						AddLineToResult(String.format("本次任务完成, %d of %d%s", taskSucessTimes, doTaskThreadAmount, ls_seeMagic));
					}
					Thread.sleep(doTaskInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
//		EnableButton(true);
	}

	private boolean LoginDevdBBs() {
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/member.php?mod=logging&action=login");
		if (_lastContent == null || _lastContent.indexOf("登录   DEVDIV.COM") < 0) {
			return false;
		}
		// AddLineToResult(_lastContent);
		Pattern pattern = Pattern
				.compile("<input type=\"hidden\" name=\"formhash\" value=\"(\\w+?)\" />");
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

		String md5pw = HttpUtility.getMD5(devUserPW.getBytes());
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//		formparams.add(new BasicNameValuePair("formhash", formhash));
//		formparams.add(new BasicNameValuePair("referer", ""));
//		formparams.add(new BasicNameValuePair("username", devUserAccount));
//		formparams.add(new BasicNameValuePair("password", md5pw));
//		formparams.add(new BasicNameValuePair("questionid", "0"));
//		formparams.add(new BasicNameValuePair("answer", ""));
//		formparams.add(new BasicNameValuePair("cookietime", cookietime));
		String postdata = String.format("formhash=%s&referer=&username=%s&password=%s&questionid=0&answer=&cookietime=%s", formhash, devUserAccount, md5pw, cookietime);
//		AddLineToResult(posturl + " " + postdata);
		_lastContent = HttpUtility.PostUseAutoEncoding("http://www.devdiv.com/" + posturl, postdata, HTTP.UTF_8);
//		AddLineToResult(_lastContent);
		return _lastContent != null && _lastContent.indexOf("欢迎您回来") >= 0;
	}

	private boolean IsValidDevdCookie() {
		String needStr = String
				.format(".com/home.php?mod=space&amp;uid=%s\" target=\"_blank\" title=\"访问我的空间\">%s</a></strong>",
						devUid, devUserAccount);
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/forum-154-1.html");
		return _lastContent != null && _lastContent.indexOf(needStr) >= 0;
		// System.out.println("\r\n\r\n------------\r\n\r\n" + _lastContent);
		// txtFriendResult.setText(String.format("indexofs=%d", indexOfStr));
	}

	private void AddLineToResult(String lineText) {
		uiOperator.lineText = lineText;
		try {
			SwingUtilities.invokeAndWait(uiOperator);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

//	private void EnableButton(boolean enable) {
//		uiOperator.btnEnable = enable;
//		try {
//			SwingUtilities.invokeAndWait(uiOperator);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		}
//	}

	private UIOperator uiOperator = new UIOperator();

	private String _lastContent = null;
	private JTextArea txtFriendResult = null;
	private JButton btnFriendStart = null;

	private int checkFriendTimes = 0;
	private CountDownLatch countDownLatch = null;
	private int taskSucessTimes = 0;

	private String devUserAccount = "";
	private String devUserPW = "";
	private String devUid = "";

	private final static int doTaskThreadAmount = 20;
	private final static int doTaskInterval = 5 * 60 * 1000;
	private JTextField txtDevUID;
	private JTextField txtDevAccount;
	private JPasswordField pwUidPw;

	private class UIOperator implements Runnable {
		@Override
		public void run() {
			if (lineText != null) {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]: ");
			String oriText = txtFriendResult.getText();
			txtFriendResult.setText(oriText + dateFormat.format(date) + lineText + "\r\n");
			lineText = null;
			} else {
				btnFriendStart.setEnabled(btnEnable);
			}
		}
		public String lineText = null;
		public boolean btnEnable;
	}
}
