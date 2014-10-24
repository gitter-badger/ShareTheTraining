package controllers.user;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.users.User;

public interface IUserHandler {
	public User getUserByEmail(String email);
}
