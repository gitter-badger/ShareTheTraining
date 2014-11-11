package controllers.user;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.courses.Review;
import models.users.Admin;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

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
	public boolean writeReview() {
		return false;
	}

	@Override
	public boolean updateProfile() {
		// TODO Auto-generated method stub
		return false;
	}

	//// TODO How to handle selected courses
	@Override
	public boolean deactiveUser(String userEmail) {
		User u =  this.getUserByEmail(userEmail);
		
		em.remove(u);
		return false;
	}


	

}
