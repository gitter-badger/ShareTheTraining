package controllers.user;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;

import models.users.UserAction;
import views.html.login.*;

public class MailHandler implements IMailHandler{

	@Override
	public boolean sendMailWithToken(String userName, String email, String token, UserAction action) {
		switch(action){
		case REGISTER:
			return true;
		case PASSWORDRESET:
			return true;
		default :
			return false;
		}
	}
	
	private boolean sendEmail(String email, String title, String content){
		try{
			MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
			mail.setSubject("mailer");
			mail.setRecipient("Agent Smith",email);
			mail.setFrom("Thomas A. Anderson <noreply@email.com>");
			mail.sendHtml(content);
		} catch(Exception e){
			return false;
		}
		return true;
	}



}
