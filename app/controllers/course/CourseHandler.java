package controllers.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import common.Utility;
import controllers.user.IUserHandler;
import controllers.user.UserHandler;
import play.Logger;
import play.db.DB;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.ConcreteCourseStatus;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.CourseStatus;
import models.courses.OrderStatus;
import models.filters.ConcreteCourseFilterBuilder;
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

	
	public Collection<ConcreteCourse> getAllConcreteCourse(){
		ConcreteCourseFilterBuilder fb =new ConcreteCourseFilterBuilder();
		fb.setCfb(new CourseFilterBuilder());
		List<Tuple> tupleList = Utility.findBaseModelObject(fb, null,
				true, -1, -1, em);
		Collection<ConcreteCourse> result = new ArrayList<ConcreteCourse>();
		for (Tuple t : tupleList) {
			result.add((ConcreteCourse) t.get(0));
		}
		return result;
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
	public Collection<Course> getCourseByCustomRule(FilterBuilder fb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize) {
		List<Tuple> tupleList = Utility.findBaseModelObject(fb, orderByColumn,
				ascending, pageNumber, pageSize, em);
		Collection<Course> result = new ArrayList<Course>();
		for (Tuple t : tupleList) {
			result.add((Course) t.get(0));
		}
		return result;
	}

	@Override
	public Map<Integer, List<ConcreteCourse>> getConcreteCourseMap(
			FilterBuilder fb, String orderByColumn, boolean ascending,
			int pageNumber, int pageSize) {
		List<Tuple> tupleList = Utility.findBaseModelObject(fb, orderByColumn,
				ascending, pageNumber, pageSize, em);
		Map<Integer, List<ConcreteCourse>> result = new HashMap<Integer, List<ConcreteCourse>>();
		for (Tuple t : tupleList) {
			ConcreteCourse concreteCourse = (ConcreteCourse) t.get(0);
			if (!result.containsKey(concreteCourse.getCourseInfo().getId()))
				result.put(concreteCourse.getCourseInfo().getId(),
						new ArrayList<ConcreteCourse>());
			result.get(concreteCourse.getCourseInfo().getId()).add(
					concreteCourse);
		}
		return result;
	}

	@Override
	public boolean modifyMaximum(int courseId, int maximum) {
		try {
			Course course = this.getCourseById(courseId);
			if (course == null
					|| maximum < 1
					|| (course.getMinimum() != -1 && course.getMinimum() > maximum))
				return false;
			for (ConcreteCourse c : course.getCourses()) {
				if (c.getSelectedCustomers().size() > maximum)
					return false;
			}
			course.setMaximum(maximum);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public boolean modifyMinimum(int courseId, int minimum) {
		try {
			Course course = this.getCourseById(courseId);
			if (course == null
					|| minimum < 0
					|| (course.getMaximum() != -1 && course.getMaximum() < minimum))
				return false;
			course.setMaximum(minimum);
			return true;
		} catch (Exception e) {
			Logger.error(e.toString());
			return false;
		}
	}

	@Override
	public Course addNewCourse(String trainerEmail, CourseForm courseForm,
			IUserHandler userHandler) {
		Trainer trainer = userHandler.getTrainerByEmail(trainerEmail);
		if (trainer == null)
			return null;
		Course course = trainer.createNewCourse(courseForm.getCourseName(), em);
		courseForm.setCourseId(course.getId());
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
			ConcreteCourse concreteCourse, String orderId,
			IOrderHandler orderHandler) {
		try {
			if (concreteCourse == null || customer == null)
				return null;
			if (concreteCourse.getStatus() == ConcreteCourseStatus.VERIFYING
					|| concreteCourse.getStatus() == ConcreteCourseStatus.FINISHED
					|| concreteCourse.getSelectedCustomers().size() == concreteCourse
							.getCourseInfo().getMaximum())
				return null;
			if (concreteCourse.getSelectedCustomers().contains(customer))
				return null;
			if (customer.registerCourse(concreteCourse)) {
				return orderHandler.newCourseOrder(orderId, concreteCourse,
						customer);
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
			course.getTrainer().getCourses().remove(course);
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

	@Override
	public Collection<ConcreteCourse> getDisplayedConcreteCourse(
			CourseFilterBuilder cfb, int courseId) {
		ConcreteCourseFilterBuilder ccfb = new ConcreteCourseFilterBuilder();
		ccfb.setCfb(cfb);
		ccfb.setCourseId(courseId);
		Map<Integer, List<ConcreteCourse>> courseMap = this
				.getConcreteCourseMap(ccfb, null, true, -1, -1);
		if (courseMap.containsKey(courseId)) {
			return courseMap.get(courseId);
		}
		return new ArrayList<ConcreteCourse>();
	}

	public static Map<Integer, String> getCategoryMap() {
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT * FROM category";
		Map<Integer, String> categoryMap = new HashMap<Integer, String>();
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			while (rs.next()) {
				int category = rs.getInt("state");
				String categoryText = rs.getString("city");
				categoryMap.put(category, categoryText);

			}
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
		return categoryMap;
	}
	
	public static boolean isCategoryExist(String category){
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT * FORM category WHERE name = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			preparedStatement.setString(1, category);
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			if (rs.next()) 
				return true;
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
		return false;
	}
	
	public static boolean addCategory(String category){
		if (isCategoryExist(category))
			return false;
		Connection connection = DB.getConnection();
		String updateSQL = "INSERT INTO category (name) ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(updateSQL);
			preparedStatement.setString(1, category);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			Logger.info(e.toString());
			return false;
		}
	}
}
