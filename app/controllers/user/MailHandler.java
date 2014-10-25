package controllers.user;

import models.users.UserAction;

public class MailHandler implements IMailHandler{

	@Override
	public boolean sendMailWithToken(String email, String token, UserAction action) {
		switch(action){
		case REGISTER:
			
			return true;
		case PASSWORDRESET:
			return true;
		default :
			return false;
		}
	}



}
