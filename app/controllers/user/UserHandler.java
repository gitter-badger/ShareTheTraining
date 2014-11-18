package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

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
			String password, UserRole userRole) {
		if (getUserByEmail(userEmail) != null)
			return null;
		switch (userRole) {
		case ADMIN:
			return Admin.create(userEmail, userName, password, em);
		case CUSTOMER:
			return Customer.create(userEmail, userName, password, em);
		case TRAINER:
			return Trainer.create(userEmail, userName, password, em);
		default:
			return null;
		}
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
		if(u==null)
			return false;
		u.setUserStatus(UserStatus.INACTIVE);
		return true;
	}



	@Override
	public Collection<User> getUserByCustomeRule(FilterBuilder cb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize) {
		TypedQuery<Tuple> tq = em.createQuery(cb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, ascending));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		Collection<User> result = new ArrayList<User>();
		System.out.println(result.size());
		for (Tuple t : tq.getResultList()) {
			result.add((User) t.get(0));
		}
		return result;
	}
	
	@Override
	public boolean addAvailableDate(Date date, Trainer trainer){
		if(trainer == null)
			return false;
		return trainer.addAvailableDate(date);
	}
	
	@Override
	public boolean removeAvailableDate(Date date, Trainer trainer){
		if(trainer == null)
			return false;
		return trainer.removeAvailableDate(date);
	}

}
