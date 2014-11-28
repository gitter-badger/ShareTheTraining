package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import common.Utility;

import play.Logger;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;
import models.forms.UserForm;
import models.users.Admin;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;
import models.users.UserStatus;

public class UserHandler implements IUserHandler {

	private EntityManager em;

	public UserHandler() {
		this.em = JPA.em();
	}

	@Override
	public User getUserById(int userId) {
		String hql = "from User u where u.id= :userId";
		Query query = em.createQuery(hql).setParameter("userId", userId);
		Collection result = query.getResultList();
		if (result.size() > 0) {
			return (User) result.iterator().next();
		}
		return null;
	}

	@Override
	public User getUserByEmail(String userEmail) {
		String hql = "from User u where u.email= :userEmail";
		Query query = em.createQuery(hql).setParameter("userEmail", userEmail);
		Collection result = query.getResultList();
		if (result.size() > 0) {
			return (User) result.iterator().next();
		}
		return null;
	}

	@Override
	public Customer getCustomerByEmail(String userEmail) {
		User user = getUserByEmail(userEmail);
		if (user != null && user.getUserRole() == UserRole.CUSTOMER)
			return (Customer) user;
		return null;
	}

	@Override
	public Trainer getTrainerByEmail(String userEmail) {
		User user = getUserByEmail(userEmail);
		if (user != null && user.getUserRole() == UserRole.TRAINER)
			return (Trainer) user;
		return null;
	}

	@Override
	public Admin getAdminByEmail(String userEmail) {
		User user = getUserByEmail(userEmail);
		if (user != null && user.getUserRole() == UserRole.ADMIN)
			return (Admin) user;
		return null;
	}

	@Override
	public User createNewUser(String userEmail, String userName,
			String password, UserRole userRole, UserForm userForm) {
		if (getUserByEmail(userEmail) != null)
			return null;
		User user = null;
		switch (userRole) {
		case ADMIN:
			user = Admin.create(userEmail, userName, password, em);
			break;
		case CUSTOMER:
			user =  Customer.create(userEmail, userName, password, em);
			break;
		case TRAINER:
			user = Trainer.create(userEmail, userName, password, em);
			break;
		default:
			return null;
		}
		userForm.bindUser(user);
		return user;
	}

	@Override
	public boolean updateProfile(String userEmail, UserForm form) {
		try {
			User user = this.getUserByEmail(form.getEmail());
			if (!userEmail.equals(form.getEmail()))
				return false;
			Logger.info(form.getEmail());
			Logger.info(userEmail);

			return form.bindUser(user);
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean deactiveUser(String userEmail) {
		User u = this.getUserByEmail(userEmail);
		if (u == null)
			return false;
		u.setUserStatus(UserStatus.INACTIVE);
		return true;
	}

	@Override
	public Collection<User> getUserByCustomeRule(FilterBuilder fb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize) {
		List<Tuple> tupleList = Utility.findBaseModelObject(fb, orderByColumn,
				ascending, pageNumber, pageSize, em);
		Collection<User> result = new ArrayList<User>();
		for (Tuple t : tupleList) {
			result.add((User) t.get(0));
		}
		return result;
	}

	@Override
	public boolean addAvailableDate(Date date, Trainer trainer) {
		if (trainer == null)
			return false;
		return trainer.addAvailableDate(date);
	}

	@Override
	public boolean removeAvailableDate(Date date, Trainer trainer) {
		if (trainer == null)
			return false;
		return trainer.removeAvailableDate(date);
	}

	@Override
	public Admin newAdmin(Admin superAdmin, String userEmail, String userName,
			String password, UserForm userForm) {
		if (superAdmin.isSuper()) {
			Admin newAdmin = (Admin) this.createNewUser(userEmail, userName,
					password, UserRole.ADMIN, userForm);
			return newAdmin;
		}
		return null;
	}

}
