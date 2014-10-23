package models.courses;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import models.filters.FilterBuilder;

public class CourseLibrary implements ICourseLibrary {

	public CourseLibrary(EntityManager em) {
		this.em = em;
	}

	private EntityManager em;

	@Override
	public Course getCourseById(String courseId) {
		String hql = "from Course c where c.courseId= :courseId";
		Query query = em.createQuery(hql).setParameter("courseId", courseId);
		Collection result = query.getResultList();
		if (result.size() < 1)
			return null;
		return (Course) result.iterator().next();
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

	private Collection<Course> getCourseByQuery(Query query, int pageNumber,
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

}
