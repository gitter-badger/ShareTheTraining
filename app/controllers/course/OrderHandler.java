package controllers.course;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import play.db.jpa.JPA;
import models.courses.Course;
import models.courses.CourseOrder;

public class OrderHandler implements IOrderHandler {
	private EntityManager em;

	public OrderHandler() {
		this.em = JPA.em();
	}

	@Override
	public Collection<CourseOrder> getCourseOrderByCustomer(String userEmail) {
		String hql = "from CourseOrder o where o.customer.email= :userEmail";
		Query query = em.createQuery(hql).setParameter("userEmail", userEmail);
		Collection result = query.getResultList();
		return result;
	}
	
	@Override
	public Collection<CourseOrder> getCourseOrderByCourse(Integer id) {
		String hql = "from CourseOrder o where o.concreteCourse.courseInfo.id= :id";
		Query query = em.createQuery(hql).setParameter("id", id);
		Collection result = query.getResultList();
		return result; 
	}
	
	@Override
	public Collection<CourseOrder> getCourseOrderByConcreteCourse(String concreteCourseId) {
		String hql = "from CourseOrder o where o.concreteCourse.concreteCourseId= :concreteCourseId";
		Query query = em.createQuery(hql).setParameter("concreteCourseId", concreteCourseId);
		Collection result = query.getResultList();
		return result; 
	}
	

}
