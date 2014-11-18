package controllers.course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import controllers.user.UserHandler;
import play.Logger;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.courses.OrderStatus;
import models.filters.CourseFilterBuilder;
import models.filters.FilterBuilder;
import models.forms.ConcreteCourseForm;
import models.forms.CourseForm;
import models.users.Customer;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

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
			int pageSize, String orderByColumn, boolean ascending) {
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		cfb.setCategory(category);
		return this.getCourseByCustomRule(cfb, orderByColumn, ascending,
				pageNumber, pageSize);
	}

	@Override
	public Collection<Course> getCourseByTrainer(String trainerEmail,
			int pageNumber, int pageSize, String orderByColumn,
			boolean ascending) {
		CourseFilterBuilder cfb = new CourseFilterBuilder();
		cfb.setTrainerEmail(trainerEmail);
		return this.getCourseByCustomRule(cfb, orderByColumn, ascending,
				pageNumber, pageSize);
	}

	private static Collection<Course> getCourseByQuery(Query query,
			int pageNumber, int pageSize) {
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		Collection<Course> result = query.getResultList();
		return result;
	}

	@Override
	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize) {
		TypedQuery<Tuple> tq = em.createQuery(cb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, ascending));
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
			if (maximum < 1 || c.getSelectedCustomers().size() > maximum
					|| c.getMinimum() != -1 && c.getMinimum() > maximum) {
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
		try {
			ConcreteCourse c = this
					.getCourseByConcreteCourseId(concreteCourseId);
			if (minimum < 0 || c == null
					|| (c.getMaximum() != -1 && c.getMaximum() < minimum))
				return false;
			c.setMaximum(minimum);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public Course addNewCourse(String trainerEmail, CourseForm courseForm) {
		Trainer trainer = new UserHandler().getTrainerByEmail(trainerEmail);
		if (trainer == null)
			return null;
		Course course = trainer.createNewCourse(courseForm.getCourseName(), em);
		courseForm.bindCourse(course);
		return course;
	}

	@Override
	public boolean updateCourseInfo(String trainerEmail, CourseForm courseForm) {
		Course course = this.getCourseById(courseForm.getCourseId());
		if (course != null && course.getTrainer() != null
				&& course.getTrainer().getEmail().equals(trainerEmail)) {
			courseForm.bindCourse(course);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateConcreteCourse(String trainerEmail,
			ConcreteCourseForm courseForm) {
		ConcreteCourse concreteCourse = this
				.getCourseByConcreteCourseId(courseForm.getConcreteCourseId());
		Course course = this.getCourseById(courseForm.getCourseInfoId());
		if (course != null && course.getTrainer() != null
				&& course.getTrainer().getEmail().equals(trainerEmail)) {
			courseForm.bindConcreteCourse(concreteCourse);
			return true;
		}
		return false;
	}

	@Override
	public ConcreteCourse addNewConcreteCourse(String trainerEmail,
			ConcreteCourseForm courseForm) {
		Course course = this.getCourseById(courseForm.getCourseInfoId());
		ConcreteCourse concreteCourse = ConcreteCourse.create(course, em);
		if (course != null && course.getTrainer() != null
				&& course.getTrainer().getEmail().equals(trainerEmail)) {
			course.addConcreteCourse(concreteCourse);
			return concreteCourse;
		}
		return null;
	}

	@Override
	public CourseOrder registerCourse(Customer customer,
			ConcreteCourse concreteCourse, String orderId) {
		try {
			if(concreteCourse == null || customer == null)
				return null;
			if (concreteCourse.getStatus() == ConcreteCourseStatus.VERIFYING
					|| concreteCourse.getStatus() == ConcreteCourseStatus.FINISHED
					|| concreteCourse.getSelectedCustomers().size() == concreteCourse
							.getMaximum())
				return null;
			if (concreteCourse.getSelectedCustomers().contains(customer))
				return null;
			if (customer.registerCourse(concreteCourse)) {
				return CourseOrder.create(orderId, concreteCourse, customer,
						new Date(), OrderStatus.CONFIRMED, em);
			}
			return null;
		} catch (Exception e) {
			Logger.error(e.toString());
			return null;
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

	// TODO How to handle orders
	private boolean _deleteConcreteCourse(ConcreteCourse concreteCourse) {
		for (Customer customer : concreteCourse.getSelectedCustomers()) {
			this.dropCourse(customer, concreteCourse);
		}
		concreteCourse.getCourseInfo().removeConcreteCourse(concreteCourse);
		concreteCourse.getCourseInfo().updateDate();
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

	@Override
	public boolean activateCourse(int courseId) {
		try {
			Course course = this.getCourseById(courseId);
			if (course.getStatus() != CourseStatus.VERIFYING)
				return false;
			course.setStatus(CourseStatus.APPROVED);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean confirmConcreteCourse(ConcreteCourse concreteCourse) {
		if (concreteCourse.getStatus() != ConcreteCourseStatus.VERIFYING)
			return false;
		concreteCourse.setStatus(ConcreteCourseStatus.APPROVED);
		Course course = concreteCourse.getCourseInfo();
		if (course.getStatus() == CourseStatus.APPROVED)
			course.setStatus(CourseStatus.OPEN);
		if (concreteCourse.getCourseDate() != null) {
			if (course.getEarliestDate() == null
					|| course.getEarliestDate().after(
							concreteCourse.getCourseDate()))
				course.setEarliestDate(concreteCourse.getCourseDate());
			if (course.getLatestDate() == null
					|| course.getLatestDate().before(
							concreteCourse.getCourseDate()))
				course.setLatestDate(concreteCourse.getCourseDate());
		}
		return true;
	}

	@Override
	public boolean activateConcreteCourse(ConcreteCourse concreteCourse) {
		if (concreteCourse.getStatus() != ConcreteCourseStatus.APPROVED)
			return false;
		concreteCourse.setStatus(ConcreteCourseStatus.STARTED);
		// TODO MAYBE ORDER STATUS SHOULD BE CHANGED HERE
		return true;
	}

	@Override
	public boolean deactivateCourse(Course course) {
		// TODO How to handle order
		for (ConcreteCourse concreteCourse : course.getCourses()) {
			deactivateConcreteCourse(concreteCourse);
		}
		course.setStatus(CourseStatus.VERIFYING);
		return false;
	}

	@Override
	public boolean deactivateConcreteCourse(ConcreteCourse concreteCourse) {
		// TODO How to handle order
		if (concreteCourse.getStatus() == ConcreteCourseStatus.VERIFYING)
			return false;
		concreteCourse.setStatus(ConcreteCourseStatus.VERIFYING);
		concreteCourse.getCourseInfo().updateDate();
		return true;
	}
}
