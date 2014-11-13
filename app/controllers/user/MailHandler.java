package controllers.user;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import play.Logger;
import play.Play;
import play.twirl.api.Html;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import models.users.UserAction;
import views.html.email.*;

public class MailHandler implements IMailHandler {
	@Override
	public boolean sendMailWithToken(String userName, String email,
			String token, UserAction action) {
		Html content = null;
		switch (action) {
		case REGISTER:
			content = reset_content.render("hehe", generateURL(token, action));
			sendEmail(email, "hehe", content);
			return true;
		case PASSWORDRESET:
			content = register_content.render("hehe", generateURL(token, action));
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
			Logger.error(e.toString());
			return false;
		}
		return true;
	}

	private String generateURL(String token, UserAction action) {
		URIBuilder builder = new URIBuilder().setScheme("http").setHost(
				Play.application().configuration().getString("url.base"));
		switch(action){
		case PASSWORDRESET:
			builder.setPath("resetpsw");
			break;
		case REGISTER:
			builder.setPath("activate");
			break;
		}
		builder.setParameter("token", token);
		URI uri;
		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}
		return uri.toString();
	}
}
