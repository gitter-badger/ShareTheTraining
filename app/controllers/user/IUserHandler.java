package controllers.user;

import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.OrderStatus;
import models.forms.UserForm;
import models.users.Admin;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

public interface IUserHandler {
	public User getUserByEmail(String userEmail);
	
	public User createNewUser(String userEmail, String userName, String password,
			UserRole userRole);
	
	public boolean writeReview();
	
	public boolean updateProfile(String userEmail, UserForm form);
	
	public boolean deactiveUser(String userEmail);


	public boolean registerCourse(Customer customer, ConcreteCourse concreteCourse,
			String orderId, Date orderDate, OrderStatus orderStatus);

	public Customer getCustomerByEmail(String userEmail);
	
	public Trainer getTrainerByEmail(String userEmail);
	
	public Admin getAdminByEmail(String userEmail);

	public User getUserById(int userId);

}
