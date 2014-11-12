package controllers.course;

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
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;
import models.forms.CourseForm;
import models.users.Customer;

public class CourseHandler implements ICourseHandler {

	private EntityManager em;

	public CourseHandler() {
		this.em = JPA.em();
	}

	@Override
	public Course getCourseById(Integer courseId) {
		String hql = "from Course c where c.id= :courseId";
		Query query = em.createQuery(hql).setParameter("courseId", courseId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (Course) result.iterator().next();
		return null;
	}

	@Override
	public ConcreteCourse getCourseByEventbriteId(String eventbriteId) {
		String hql = "from ConcreteCourse c where c.eventbriteId= :eventbriteId";
		Query query = em.createQuery(hql).setParameter("eventbriteId",
				eventbriteId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (ConcreteCourse) result.iterator().next();
		return null;
	}

	@Override
	public ConcreteCourse getCourseByConcreteCourseId(String concreteCourseId) {
		String hql = "from ConcreteCourse c where c.concreteCourseId= :concreteCourseId";
		Query query = em.createQuery(hql).setParameter("concreteCourseId",
				concreteCourseId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (ConcreteCourse) result.iterator().next();
		return null;
	}

	@Override
	public Collection<Course> getCourseByCategory(int category, int pageNumber,
			int pageSize) {
		String hql = "from Course c where c.courseCategory= :category";
		Query query = em.createQuery(hql).setParameter("category", category);
		return getCourseByQuery(query, pageNumber, pageSize);
	}

	@Override
	public Collection<Course> getCourseByTrainer(String trainerEmail,
			int pageNumber, int pageSize) {
		String hql = "from Course c where c.trainer.email= :trainerEmail";
		Query query = em.createQuery(hql).setParameter("trainerEmail",
				trainerEmail);
		return getCourseByQuery(query, pageNumber, pageSize);
	}

	private static Collection<Course> getCourseByQuery(Query query,
			int pageNumber, int pageSize) {
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		Collection<Course> result = query.getResultList();
		return result;
	}

	@Override
<<<<<<< HEAD
	public Collection<Course> getCourseByCustomRule(FilterBuilder cb, String orderByColumn,
			int pageNumber, int pageSize) {
		Logger.info("category:  "+cb.getCategory());
=======
	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			String orderByColumn, int pageNumber, int pageSize) {
>>>>>>> 3104a62f855e7b082a32e74bb82bf4567e6280bd
		TypedQuery<Tuple> tq = em.createQuery(cb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, true));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		Collection<Course> result = new ArrayList<Course>();
		System.out.println(result.size());
		for (Tuple t : tq.getResultList()) {
			result.add((Course) t.get(0));
		}
		return result;
	}

	@Override
	public boolean modifyMaximum(String concreteCourseId, int maximum) {
		try {
			ConcreteCourse c = this
					.getCourseByConcreteCourseId(concreteCourseId);
			if (c.getSelectedCustomers().size() > maximum) {
				return false;
			}
			c.setMaximum(maximum);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean modifyMinimum(String concreteCourseId, int minimum) {
		ConcreteCourse c = this.getCourseByConcreteCourseId(concreteCourseId);
		c.setMaximum(minimum);
		return true;
	}

	@Override
	public boolean addNewCourse(String trainerEmail, CourseForm courseForm) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCourseInfo(int courseId, CourseForm courseForm) {
		return false;
	}

	@Override
	public boolean registerCourse(Customer customer,
			ConcreteCourse concreteCourse, String orderId) {
		try {
			if (concreteCourse.getStatus() != ConcreteCourseStatus.STARTED
					|| concreteCourse.getSelectedCustomers().size() == concreteCourse
							.getMaximum())
				return false;
			if (customer.registerCourse(concreteCourse)) {
				CourseOrder order = CourseOrder.create(orderId, concreteCourse,
						customer, new Date(), OrderStatus.CONFIRMED, em);
				return true;
			}
			return false;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean dropCourse(Customer customer, ConcreteCourse concreteCourse) {
		return customer.dropCourse(concreteCourse);
	}

	@Override
	public boolean deleteConcreteCourse(String concreteCourseId) {
		try {
			ConcreteCourse concreteCourse = this
					.getCourseByConcreteCourseId(concreteCourseId);
			if (concreteCourse == null)
				return false;
			return _deleteConcreteCourse(concreteCourse);
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	//TODO How to handle orders
	private boolean _deleteConcreteCourse(ConcreteCourse concreteCourse) {
		for (Customer customer : concreteCourse.getSelectedCustomers()) {
			this.dropCourse(customer, concreteCourse);
		}
		concreteCourse.getCourseInfo().removeConcreteCourse(concreteCourse);
		em.remove(concreteCourse);
		return true;
	}

	@Override
	public boolean deleteCourse(int courseId) {
		try {
			Course course = this.getCourseById(courseId);
			for (ConcreteCourse c : course.getCourses()) {
				this._deleteConcreteCourse(c);
			}
			em.remove(course);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}
}
