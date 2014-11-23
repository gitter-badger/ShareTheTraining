package models.filters;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class ConcreteCourseFilter implements FilterBuilder{
	private CourseFilterBuilder cfb;

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		// TODO Auto-generated method stub
		return null;
	}
}
