package controllers.authentication;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.codec.binary.Base64;

import common.Password;
import controllers.user.IMailHandler;
import controllers.user.IUserHandler;
import controllers.user.MailHandler;
import controllers.user.UserHandler;
import models.users.ActionToken;
import models.users.User;
import models.users.UserAction;
import models.users.UserRole;
import models.users.UserStatus;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http.Context;

public class AuthenticationHandler implements IAuthenticationHandler {

	EntityManager em;

	public AuthenticationHandler() {
		this.em = JPA.em();
	}

	@Override
	public User doLogin(String userEmail, String password, UserRole userRole,
			Context context, IUserHandler userHandler) {
		User u = userHandler.getUserByEmail(userEmail);
		if (u != null && Password.check(password, u.getPassword())
				&& u.getUserRole() == userRole) {
			context.session().put("connected", u.getEmail());
			return u;
		}
		return null;
	}

	@Override
	public boolean doRegister(String userEmail, String userName,
			String password, UserRole userRole, IUserHandler userHandler,
			Context context, IMailHandler mailHandler) {
		try {
			if (userHandler.createNewUser(userEmail, userName, password,
					userRole) != null) {
				ActionToken actionToken = assignNewToke(userEmail,
						UserAction.REGISTER);
				String confirmToken = generateConfirmToken(actionToken);
				mailHandler.sendMailWithToken(userEmail, confirmToken,
						UserAction.REGISTER);
				return true;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean activeUser(String token, IUserHandler userHandler) {
		String[] tokenAndEmail = token.split("\\#");
		if (tokenAndEmail.length == 2) {
			String userEmail = new String(Base64.decodeBase64(tokenAndEmail[1]));
			ActionToken actionToken = findToken(userEmail, UserAction.REGISTER);
			if (vaildateToken(actionToken)) {
				User user = userHandler.getUserByEmail(userEmail);
				if (user != null) {
					user.setUserStatus(UserStatus.ACTIVE);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean doLogout(Context context) {
		if (context.session().get("connected") != null) {
			context.session().remove("connected");
			return true;
		}
		return false;
	}

	@Override
	public boolean authorizeResetPassword(String userEmail,
			IMailHandler mailHandler) {
		try {
			ActionToken actionToken = assignNewToke(userEmail,
					UserAction.PASSWORDRESET);
			String confirmToken = generateConfirmToken(actionToken);
			mailHandler.sendMailWithToken(userEmail, confirmToken,
					UserAction.PASSWORDRESET);
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean doResetPassword(String token, String newPassword,
			IUserHandler userHandler) {
		String[] tokenAndEmail = token.split("\\#");
		if (tokenAndEmail.length == 2) {
			String userEmail = new String(Base64.decodeBase64(tokenAndEmail[1]));
			ActionToken actionToken = findToken(userEmail,
					UserAction.PASSWORDRESET);
			if (vaildateToken(actionToken)) {
				User user = userHandler.getUserByEmail(userEmail);
				if (user != null) {
					user.setPassword(newPassword);
					return true;
				}
			}
		}
		return false;
	}

	private boolean vaildateToken(ActionToken actionToken) {
		return actionToken != null
				&& actionToken.getExpireDate().after(new Date());
	}

	private String generateConfirmToken(ActionToken actionToken) {
		return actionToken.getToken()
				+ "#"
				+ Base64.encodeBase64URLSafeString(actionToken.getUserEmail()
						.getBytes());
	}

	private ActionToken assignNewToke(String userEmail, UserAction userAction) {
		ActionToken oldToken = findToken(userEmail, userAction);
		if (oldToken != null)
			em.remove(oldToken);
		Date expireDate = getExpireDate(1);
		return ActionToken.create(userEmail, userAction, UUID.randomUUID()
				.toString(), expireDate);
	}

	private Date getExpireDate(int days) {
		Date now = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	private ActionToken findToken(String userEmail, UserAction userAction) {
		String hql = "from ActionToken t where t.userEmail = :userEmail and t.userAction = :userAction";
		Query query = em.createQuery(hql).setParameter("userEmail", userEmail)
				.setParameter("userAction", userAction);
		Collection result = query.getResultList();
		if (result.size() > 0) {
			return (ActionToken) result.iterator().next();
		}
		return null;
	}
}
