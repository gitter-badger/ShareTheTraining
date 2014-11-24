package common;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import models.filters.FilterBuilder;

public class Utility {
	public static  List<Tuple> findBaseModelObject(FilterBuilder fb,
			String orderByColumn, boolean ascending, int pageNumber,
			int pageSize, EntityManager em){
		TypedQuery<Tuple> tq = em.createQuery(fb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, ascending));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		return tq.getResultList();
	}
}
