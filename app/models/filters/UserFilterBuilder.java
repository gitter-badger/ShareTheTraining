package models.filters;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import models.courses.ConcreteCourse;
import models.locations.Location;
import models.users.Admin;
import models.users.Trainer;
import models.users.User;
import models.users.UserRole;

public class UserFilterBuilder implements FilterBuilder {
	UserRole userRole;
	boolean isVeteran = false;
	Location location;
	String email;
	String keyword;
	int userStatus;

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		if(userRole == null)
			return buildUserQuery(cb, orderByColumn, ascending);
		else{
			switch(userRole){
			case ADMIN:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending, Admin.class);
			case TRAINER:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending, Trainer.class);
			case CUSTOMER:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending, Trainer.class);
			}
		}
		return null;
	}

	private CriteriaQuery<Tuple> buildUserQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<User> entityRoot = criteria.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (orderByColumn != null) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		return null;
	}

	private CriteriaQuery<Tuple> buildQueryBySpecificRole(CriteriaBuilder cb,
			String orderByColumn, boolean ascending, Class userClass) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root entityRoot = criteria.from(userClass);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (orderByColumn != null) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb
					.desc(entityRoot.get(orderByColumn));
			criteria.orderBy(order);
		}
		return null;
	}

}
