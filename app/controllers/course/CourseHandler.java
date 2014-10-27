package controllers.course;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import models.courses.Course;
import models.filters.FilterBuilder;
import models.forms.CourseForm;

public class CourseHandler implements ICourseHandler {
	
	private EntityManager em;
	
	public CourseHandler(){
		this.em = JPA.em();
	}

	@Override
	public Course getCourseById(String courseId) {
		String hql = "from Course c where c.courseId= :courseId";
		Query query = em.createQuery(hql).setParameter("courseId", courseId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (Course) result.iterator().next();
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

	private static Collection<Course> getCourseByQuery(Query query, int pageNumber,
			int pageSize) {
		query.setMaxResults(pageSize);
		query.setFirstResult(pageSize * (pageNumber - 1));
		Collection<Course> result = query.getResultList();
		return result;
	}
	
	@Override
	public Collection<Course> getCourseByCustomRule(FilterBuilder cb,
			int pageNumber, int pageSize) {
		TypedQuery<Tuple> tq = em.createQuery(cb.buildeQuery(
				em.getCriteriaBuilder(), "price", true));
		tq.setMaxResults(pageSize);
		tq.setFirstResult(pageSize * (pageNumber - 1));
		Collection<Course> result = new ArrayList<Course>();
		for (Tuple t : tq.getResultList()) {
			result.add((Course) t.get(0));
		}
		return result;
	}

	@Override
	public boolean modifyMaximum(String courseId, int maximum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyMinimum(String courseId, int minimum) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNewCourse(String trainerEmail, CourseForm courseForm) {
		// TODO Auto-generated method stub
		return false;
	}

}
