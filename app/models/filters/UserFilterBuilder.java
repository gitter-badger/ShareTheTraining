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
import models.users.UserStatus;
//TODO more filter
public class UserFilterBuilder implements FilterBuilder {
	UserRole userRole;
	boolean isVeteran = false;
	Location location;
	String email;
	String keyword;
	int userStatus = -1;
	String username;
	boolean education = false;
	boolean experience = false;
	int registerCourseCount = -1;

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		if (userRole == null)
			return buildUserQuery(cb, orderByColumn, ascending);
		else {
			switch (userRole) {
			case ADMIN:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending,
						Admin.class);
			case TRAINER:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending,
						Trainer.class);
			case CUSTOMER:
				return buildQueryBySpecificRole(cb, orderByColumn, ascending,
						Trainer.class);
			}
		}
		return null;
	}

	private CriteriaQuery<Tuple> buildUserQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<User> entityRoot = criteria.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (keyword != null) {
			keyword = keyword.replaceAll("\\s+", "%");
			Predicate keyWordConditions = cb.disjunction();
			keyWordConditions.getExpressions().add(
					cb.like(entityRoot.<String> get("name"), "%" + keyword
							+ "%"));
			predicates.add(keyWordConditions);
		}
		if (userStatus != -1) {
			predicates.add(cb.equal(entityRoot.<UserStatus> get("userStatus"),
					UserStatus.fromInteger(userStatus)));
		}
		if (email != null) {
			predicates.add(cb.equal(entityRoot.<String> get("email"), email));
		}
		if (username != null) {
			predicates.add(cb.equal(entityRoot.<String> get("username"),
					username));
		}
		if (orderByColumn != null) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		}
		return null;
	}

	private CriteriaQuery<Tuple> buildQueryBySpecificRole(CriteriaBuilder cb,
			String orderByColumn, boolean ascending, Class userClass) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root entityRoot = criteria.from(userClass);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (userStatus != -1) {
			predicates.add(cb.equal(entityRoot.<UserStatus> get("userStatus"),
					UserStatus.fromInteger(userStatus)));
		}
		if (email != null) {
			predicates.add(cb.equal(entityRoot.<String> get("email"), email));
		}
		if (username != null) {
			predicates.add(cb.equal(entityRoot.<String> get("username"),
					username));
		}
		if (keyword != null) {
			keyword = keyword.replaceAll("\\s+", "%");
			Predicate keyWordConditions = cb.disjunction();
			keyWordConditions.getExpressions().add(
					cb.like(entityRoot.<String> get("name"), "%" + keyword
							+ "%"));
			if (education) {
				keyWordConditions.getExpressions().add(
						cb.like(entityRoot.<String> get("education"), "%"
								+ keyword + "%"));
			}
			if (experience) {
				keyWordConditions.getExpressions().add(
						cb.like(entityRoot.<String> get("experience"), "%"
								+ keyword + "%"));
			}
			predicates.add(keyWordConditions);
		}
		if (orderByColumn != null) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		}
		return null;
	}

}
