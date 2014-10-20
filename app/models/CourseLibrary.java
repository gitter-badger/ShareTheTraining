package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;




public class CourseLibrary implements ICourseLibrary{
	
	public CourseLibrary(EntityManager em){
		this.em = em;
	}

	private EntityManager em;
	
	@Override
	public Course getCourseById(String courseId) {
		String hql="from Course c where c.courseId= :courseId";
		Query query = em.createQuery(hql).setParameter("courseId", courseId);
		Collection result = query.getResultList();
		if(result.size()<1)
			return null;
		return (Course) result.iterator().next();
	}

	@Override
	public Course getCourseByCategory(int category, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Course> getCourseByTrainer(String trainerId, int pageNumber, int pageSize) {
		String hql="from Course c where c.courseId= :courseId";
		Query query = em.createQuery(hql).setParameter("trainerId", trainerId);
		Collection result = query.getResultList();
		if(result.size()<1)
			return null;
		return result;
	}

	@Override
	public List<Course> getCourseByCustomRule(FilterBuilder cb) {
		TypedQuery<Tuple> tq=  em.createQuery(cb.buildeQuery(em.getCriteriaBuilder(), "price", true));
		List<Course> result = new ArrayList<Course>();
		for (Tuple t : tq.getResultList()) {
			result.add((Course) t.get(0));
			}
		return result;
	}

}
