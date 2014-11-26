package models.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import models.courses.ConcreteCourse;
import models.locations.Location;
import models.users.Admin;
import models.users.Customer;
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
	private static  Set<String> orderBySet = new HashSet<String>(Arrays.asList("userRole", "userStatus"));
	private static  Set<String> trainerOrderBySet = new HashSet<String>(Arrays.asList("rating", "veteranRole"));
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
						Customer.class);
			}
		}
		return null;
	}

	private CriteriaQuery<Tuple> buildUserQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root<User> entityRoot = criteria.from(User.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
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
		if (orderByColumn != null && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		}
		return criteria;
	}

	private CriteriaQuery<Tuple> buildQueryBySpecificRole(CriteriaBuilder cb,
			String orderByColumn, boolean ascending, Class userClass) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root entityRoot = criteria.from(userClass);
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
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
		if (orderByColumn != null && orderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		} else if (orderByColumn != null && trainerOrderBySet.contains(orderByColumn)) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public boolean isVeteran() {
		return isVeteran;
	}

	public void setVeteran(boolean isVeteran) {
		this.isVeteran = isVeteran;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEducation() {
		return education;
	}

	public void setEducation(boolean education) {
		this.education = education;
	}

	public boolean isExperience() {
		return experience;
	}

	public void setExperience(boolean experience) {
		this.experience = experience;
	}

	public int getRegisterCourseCount() {
		return registerCourseCount;
	}

	public void setRegisterCourseCount(int registerCourseCount) {
		this.registerCourseCount = registerCourseCount;
	}

}
