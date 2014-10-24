package controllers.user;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import models.courses.Course;
import models.users.User;

public class UserHandler implements IUserHandler {

	private EntityManager em;
	
	public UserHandler(EntityManager em){
		this.em = em;
	}
	
	@Override
	public User getUserByEmail(String email) {
		String hql = "from User u where u.email= :email";
		Query query = em.createQuery(hql).setParameter("email", email);
		Collection result = query.getResultList();
		if (result.size() > 0){
			return (User) result.iterator().next();
		}
		return null;
	}

}
