package controllers.user;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.users.User;
import models.users.UserRole;

public interface IUserHandler {
	public User getUserByEmail(String userEmail);
	
	public User createNewUser(String userEmail, String userName, String password,
			UserRole userRole);
	
	public boolean writeReview();
	
	public boolean registerCourse();
}
