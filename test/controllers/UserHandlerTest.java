package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import models.filters.UserFilterBuilder;
import models.users.User;
import models.users.UserRole;

import org.junit.Test;

import common.BaseTest;
import controllers.user.UserHandler;


public class UserHandlerTest extends BaseTest {
	@Test
	public void testGetUserByCustomerRule(){
		UserFilterBuilder ub = new UserFilterBuilder();
		ub.setEmail("sda");
		ub.setUserRole(UserRole.TRAINER);
		UserHandler uh = new UserHandler();
		Collection<User> result = uh.getUserByCustomeRule(ub, null, true, -1, -1);
		User user = result.iterator().next();
		assertThat(result.size()).isEqualTo(1);
		assertThat(user.getEmail()).isEqualTo("sda");
	}
}
