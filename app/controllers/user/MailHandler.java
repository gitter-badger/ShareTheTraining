package controllers.user;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import play.Logger;
import play.Play;
import play.twirl.api.Html;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import controllers.routes;
import controllers.authentication.IAuthenticationHandler;
import models.users.UserAction;
import views.html.email.*;

public class MailHandler implements IMailHandler {
	@Override
	public boolean sendMailWithToken(String userName, String email,
			String token, UserAction action,
			IAuthenticationHandler authenticationHandler) {
		Html content = null;
		switch (action) {
		case REGISTER:
			content = reset_content.render(generateURL(token, action));
			sendEmail(email, "hehe", content);
			return true;
		case PASSWORDRESET:
			content = register_content.render(generateURL(token, action));
			sendEmail(email, "hehe", content);
			return true;
		default:
			return false;
		}
	}

	private String generateURL(String token, UserAction action) {
		try {
			URIBuilder builder = new URIBuilder().setScheme("http").setHost(
					Play.application().configuration().getString("url.base"));
			String url = builder.build().toString();
			switch (action) {
			case PASSWORDRESET:
				url += routes.Application.resetpsw(token).url();
				break;
			case REGISTER:
				url += routes.Application.activate(token).url();
				break;
			}
			return url;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
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

}
