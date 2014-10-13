package models;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;




public class CourseLibrary implements ICourseLibrary{
	
	public CourseLibrary(EntityManager em){
		this.em = em;
	}

	private EntityManager em;
	
	@Override
	public Course getCourseById(String courseID) {
		System.out.println(courseID);
		String hql="from Course c where c.courseID= :courseID";
		Query query = em.createQuery(hql).setParameter("courseID", courseID);
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
	public Course getCourseByTrainer(int location, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Course getCourseByCustomRule() {
		// TODO Auto-generated method stub
		return null;
	}

}
