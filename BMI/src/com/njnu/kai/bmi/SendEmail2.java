/**
 * @(#)SendEmail2.java		2012-11-1
 *
 */
package com.njnu.kai.bmi;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.text.TextUtils;

class MyAuthenticator extends javax.mail.Authenticator {
     protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication("\u0074\u0074\u0070\u006f\u0064\u006c\u006f\u0067", "\u0074\u0074\u0061\u0062\u0063\u0031\u0032\u0033");
     }
 }

/**
 *
 * @version 1.0.0
 * @since 2012-11-1
 */
public class SendEmail2 {
	private static final String LOG_TAG = "SendEmail2";

	public String sendEmail(String text) {
		String smtpHost = "\u0073\u006d\u0074\u0070\u002e\u0031\u0036\u0033\u002e\u0063\u006f\u006d";
		String from = "\u0074\u0074\u0070\u006f\u0064\u006c\u006f\u0067\u0040\u0031\u0036\u0033\u002e\u0063\u006f\u006d";
		String[] toWho = {
				"\u0068\u006f\u006e\u0067\u006b\u0061\u0069\u002e\u0071\u0069\u0061\u006e\u0040\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d",
				"\u007a\u0068\u0065\u006e\u0068\u0075\u0061\u002e\u0067\u0061\u006f\u0040\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d",
				"\u0061\u006e\u0070\u0069\u006e\u0067\u002e\u0079\u0069\u006e\u0040\u0074\u0074\u0070\u006f\u0064\u002e\u0063\u006f\u006d"};
		String result = "邮件发送成功";
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, new MyAuthenticator());
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from, "\u5929\u5929\u004c\u006f\u0067"));
			InternetAddress[] address = new InternetAddress[toWho.length];
			for (int idx = 0; idx < toWho.length; ++idx) {
				address[idx] = new InternetAddress(toWho[idx]);
			}
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject("\u0000\u0054\u0054\u7528\u6237\u53cb\u60c5\u6d4b\u8bd5\u65e5\u5fd7");
			message.setSentDate(new Date());
			message.setText(TextUtils.isEmpty(text) ? "empty text" : text);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			result = "邮件发送失败：" + e.toString() + "\n详细原因:" + e.getMessage();
		}
		return result;
	}
}
