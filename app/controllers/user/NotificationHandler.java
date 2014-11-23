package controllers.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.filters.NotificationFilterBuilder;
import models.users.Customer;
import models.users.Trainer;
import common.BaseModelObject;

public class NotificationHandler implements INotificationHandler {
	private EntityManager em;

	public NotificationHandler() {
		this.em = JPA.em();
	}

	private Collection getNewItems(Class objectClass,
			Date lastTime, int pageNumber, int pageSize) {
		NotificationFilterBuilder nfb = new NotificationFilterBuilder();
		nfb.setCreation(lastTime);
		nfb.setObjectClass(objectClass);
		TypedQuery<Tuple> tq = em.createQuery(nfb.buildeQuery(
				em.getCriteriaBuilder(), "created", true));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		Collection<BaseModelObject> result = new ArrayList<BaseModelObject>();
		for (Tuple t : tq.getResultList()) {
			result.add((BaseModelObject) t.get(0));
		}
		return result;
	}
	
	@Override
	public Collection<Trainer> getNewTrainers(Date currentTime){
		return getNewItems(Trainer.class, currentTime, -1, -1);
	}
	
	@Override
	public Collection<Customer> getNewCustomers(Date currentTime){
		return getNewItems(Customer.class, currentTime, -1, -1);
	}
	
	@Override
	public Collection<Course> getNewCourses(Date currentTime){
		return getNewItems(Course.class, currentTime, -1, -1);
	}
	
	@Override
	public Collection<ConcreteCourse> getNewConcreteCourse(Date currentTime){
		return getNewItems(ConcreteCourse.class, currentTime, -1, -1);
	}
	
	@Override
	public Collection<CourseOrder> getNewCourseOrder(Date currentTime){
		return getNewItems(CourseOrder.class, currentTime, -1, -1);
	}
	
}
