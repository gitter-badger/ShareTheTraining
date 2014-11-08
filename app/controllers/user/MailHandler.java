package controllers.user;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import models.users.UserAction;
import views.html.email.*;

public class MailHandler implements IMailHandler {
	@Override
	public boolean sendMailWithToken(String userName, String email,
			String token, UserAction action) {
		String content=null;
		switch (action) {
		case REGISTER:
			//content = ;
			sendEmail(email, "hehe", content);
			return true;
		case PASSWORDRESET:
			content = null;
			sendEmail(email, "hehe", content);
			return true;
		default:
			return false;
		}
	}

	private boolean sendEmail(String email, String title, String content) {
		try {
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class)
					.email();
			mail.setSubject(title);
			mail.setRecipient("Agent Smith", email);
			mail.setFrom("Thomas A. Anderson <noreply@email.com>");
			mail.sendHtml(content);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
