package controllers.user;

import play.twirl.api.Html;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import models.users.UserAction;
import views.html.email.*;

public class MailHandler implements IMailHandler {
	@Override
	public boolean sendMailWithToken(String userName, String email,
			String token, UserAction action) {
		Html content=null;
		switch (action) {
		case REGISTER:
			content = reset_content.render("hehe", "");
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

	private boolean sendEmail(String email, String title, Html content) {
		try {
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class)
					.email();
			mail.setSubject(title);
			mail.setRecipient("Agent Smith <noreply@email.com>", email);
			mail.setFrom("Thomas A. Anderson <noreply@email.com>");
			mail.sendHtml(content.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
