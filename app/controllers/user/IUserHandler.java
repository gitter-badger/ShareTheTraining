package controllers.user;

import java.util.Collection;
import java.util.Date;

import models.courses.ConcreteCourse;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;
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
	
	public boolean updateProfile(String userEmail, UserForm form);
	
	public boolean deactiveUser(String userEmail);


	public Customer getCustomerByEmail(String userEmail);
	
	public Trainer getTrainerByEmail(String userEmail);
	
	public Admin getAdminByEmail(String userEmail);

	public User getUserById(int userId);

	public Collection<User> getUserByCustomeRule(FilterBuilder cb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize);

	public boolean addAvailableDate(Date date, Trainer trainer);

	public boolean removeAvailableDate(Date date, Trainer trainer);

}
