package models.filters;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import models.locations.Location;

public class TrainerFilter implements FilterBuilder {
	boolean isVeteran = false;
	Location location;
	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		// TODO Auto-generated method stub
		return null;
	}

}
