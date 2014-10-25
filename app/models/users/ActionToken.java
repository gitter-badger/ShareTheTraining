package models.users;

import java.util.Date;

import javax.persistence.Entity;

import common.BaseModelObject;
import common.Password;

@Entity
public class ActionToken extends BaseModelObject {
	private String userEmail;
	
	private UserAction userAction;
	
	private String token;
	
	private Date expireDate;
	
	
	public static ActionToken create(String userEmail, UserAction userAction, String guid, Date expireDate){
		ActionToken actionToken = new ActionToken(userEmail, userAction, guid, expireDate);
		return actionToken;
	}

	protected ActionToken(String userEmail, UserAction userAction, String guid, Date expireDate){
		this.userEmail = userEmail;
		this.userAction = userAction;
		this.expireDate = expireDate;
		try {
			this.token = Password.getSaltedHash(guid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public UserAction getUserAction() {
		return userAction;
	}

	public void setUserAction(UserAction userAction) {
		this.userAction = userAction;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
}
