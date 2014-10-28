package controllers.user;

import models.courses.ConcreteCourse;
import models.users.Customer;
import models.users.User;
import models.users.UserRole;

public interface IUserHandler {
	public User getUserByEmail(String userEmail);
	
	public User createNewUser(String userEmail, String userName, String password,
			UserRole userRole);
	
	public boolean writeReview();
	
	public boolean registerCourse(Customer customer, ConcreteCourse course, String orderId);
}
