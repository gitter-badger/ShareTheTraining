package controllers.user;

import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.OrderStatus;
import models.users.Customer;
import models.users.User;
import models.users.UserRole;

public interface IUserHandler {
	public User getUserByEmail(String userEmail);
	
	public User createNewUser(String userEmail, String userName, String password,
			UserRole userRole);
	
	public boolean writeReview();
	
	public boolean updateProfile();
	
	public boolean deactiveUser(String userEmail);


	public boolean registerCourse(Customer customer, ConcreteCourse concreteCourse,
			String orderId, Date orderDate, OrderStatus orderStatus);

	public Customer getCusByEmail(String userEmail);

	public User getUserById(int userId);

}
