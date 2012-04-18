package com.njnu.kai.fordevd;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class MainFrm {

	private JFrame frmFordevd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
				txtFriendResult.setText("Start......\r\n");
				if (!IsValidDevdCookie()) {
					txtFriendResult.setText("Cookie无效了，重新登录......\r\n");
					if (LoginDevdBBs()) {
						AddLineToResult("登录成功......");
					} else {
						AddLineToResult("登录失败，Over！");
						return;
					}
				} else {
					txtFriendResult.setText("Cookie有效，无需重新登录!\r\n");
				}
			}
		});
		btnFriendStart.setBounds(656, 13, 117, 29);
		panel.add(btnFriendStart);

		txtFriendResult = new JTextArea();
		panel.add(txtFriendResult);
		txtFriendResult.setText("welcome.");
		txtFriendResult.setColumns(64);
		txtFriendResult.setRows(10);
		txtFriendResult.setBounds(2, 2, 425, 309);

		JScrollPane scrollPane = new JScrollPane(txtFriendResult);
		scrollPane.setBounds(0, 54, 773, 434);
		panel.add(scrollPane);

		tabbedPane.addTab("Tab 2", new JLabel("Tab 2 Content"));
		tabbedPane.addTab("Tab 3", new JLabel("Tab 3 Content"));

		JMenuBar menuBar = new JMenuBar();
		frmFordevd.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmItem = new JMenuItem("Item1");
		mnFile.add(mntmItem);
	}

	private boolean LoginDevdBBs() {
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/member.php?mod=logging&action=login");
		if (_lastContent.indexOf("登录   DEVDIV.COM") < 0) {
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
		String postdata = String
				.format("formhash=%s&referer=&username=%s&password=%s&questionid=0&answer=&cookietime=%s",
						formhash, devUserAccount, md5pw, cookietime);
//		AddLineToResult(posturl + " " + postdata);
//		_lastContent = _hh.PostUseAutoEncoding("http://www.devdiv.com/" + posturl, postdata);

		return true;
	}

	private boolean IsValidDevdCookie() {
		String needStr = String
				.format(".com/home.php?mod=space&amp;uid=%s\" target=\"_blank\" title=\"访问我的空间\">%s</a></strong>",
						devUid, devUserAccount);
		_lastContent = HttpUtility
				.GetUseAutoEncoding("http://www.devdiv.com/forum-154-1.html");
		return _lastContent.indexOf(needStr) >= 0;
		// System.out.println("\r\n\r\n------------\r\n\r\n" + _lastContent);
		// txtFriendResult.setText(String.format("indexofs=%d", indexOfStr));
	}

	private void AddLineToResult(String text) {
		String oriText = txtFriendResult.getText();
		txtFriendResult.setText(oriText + text + "\r\n");
	}

	private String _lastContent = null;
	private JTextArea txtFriendResult = null;
	private JButton btnFriendStart = null;

	private final static String devUserAccount = "waring1983";
	private final static String devUserPW = "qwertasdf";
	private final static String devUid = "215055";
}
