package controllers;

import static org.fest.assertions.Assertions.assertThat;
import models.users.ActionToken;
import models.users.User;
import models.users.UserAction;
import models.users.UserRole;
import models.users.UserStatus;

import org.junit.Test;

import play.mvc.Http.Context;
import common.BaseTest;
import controllers.authentication.AuthenticationHandler;
import controllers.user.MailHandler;
import controllers.user.UserHandler;

public class AuthenticationHandlerTest extends BaseTest {
	@Test
	public void testRegister() {
		AuthenticationHandler authenticationHandler = new AuthenticationHandler();
		UserHandler userHandler = new UserHandler();
		boolean reigsterResult = authenticationHandler.doRegister(
				"wjf3121@gmail.com", "hehe", "123", UserRole.CUSTOMER,
				userHandler, new MailHandler());
		assertThat(reigsterResult).isEqualTo(true);
		User u = userHandler.getUserByEmail("wjf3121@gmail.com");
		assertThat(u.getUserRole()).isEqualTo(UserRole.CUSTOMER);
		assertThat(u.getUserStatus()).isEqualTo(UserStatus.INACTIVE);
		ActionToken at = authenticationHandler.findToken("wjf3121@gmail.com",
				UserAction.REGISTER);
		String confirmToken = authenticationHandler.generateConfirmToken(at);
		authenticationHandler.activateUser(confirmToken, userHandler);
		u = userHandler.getUserByEmail("wjf3121@gmail.com");
		assertThat(u.getUserStatus()).isEqualTo(UserStatus.ACTIVE);
		this.getmEm().remove(at);
		this.getmEm().remove(u);
	}

	@Test
	public void testLogin() {
		Context mockContext = this.getMockContext();
		AuthenticationHandler authenticationHandler = new AuthenticationHandler();
		UserHandler userHandler = new UserHandler();
		User u = userHandler.createNewUser("wjf3121@gmail.com", "hehe", "123",
				UserRole.CUSTOMER);
		u.setUserStatus(UserStatus.ACTIVE);
		authenticationHandler.doLogin("wjf3121@gmail.com", "123",
				mockContext, userHandler);
		assertThat(mockContext.session().get("connected")).isEqualTo("wjf3121@gmail.com");
		this.getmEm().remove(u);
	}
	
	@Test
	public void testLogout() {
		Context mockContext = this.getMockContext();
		AuthenticationHandler authenticationHandler = new AuthenticationHandler();
		UserHandler userHandler = new UserHandler();
		User u = userHandler.createNewUser("wjf3121@gmail.com", "hehe", "123",
				UserRole.CUSTOMER);
		u.setUserStatus(UserStatus.ACTIVE);
		authenticationHandler.doLogin("wjf3121@gmail.com", "123",
				mockContext, userHandler);
		assertThat(mockContext.session().get("connected")).isEqualTo("wjf3121@gmail.com");
		authenticationHandler.doLogout(mockContext);
		assertThat(mockContext.session().get("connected")).isEqualTo(null);
		this.getmEm().remove(u);
	}
}
