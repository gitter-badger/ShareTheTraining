package models.filters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import common.BaseModelObject;
import models.courses.CourseStatus;
import models.users.UserStatus;

public class NotificationFilterBuilder implements FilterBuilder {

	Date creation = null;

	Class objectClass = null;

	@Override
	public CriteriaQuery<Tuple> buildeQuery(CriteriaBuilder cb,
			String orderByColumn, boolean ascending) {
		if (objectClass == null) {
			return buildQueryBySpecificRole(cb, orderByColumn, ascending,
					BaseModelObject.class);
		}
		return buildQueryBySpecificRole(cb, orderByColumn, ascending,
				objectClass);
	}

	private CriteriaQuery<Tuple> buildQueryBySpecificRole(CriteriaBuilder cb,
			String orderByColumn, boolean ascending, Class objectClass) {
		CriteriaQuery<Tuple> criteria = cb.createTupleQuery();
		Root entityRoot = criteria.from(objectClass);
		List<Predicate> predicates = new ArrayList<Predicate>();
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(0, entityRoot);
		criteria.multiselect(selections.toArray(new Selection[0])).distinct(
				true);
		predicates.add(cb.greaterThanOrEqualTo(
				entityRoot.<Date> get("created"), creation));
		if (orderByColumn != null) {
			javax.persistence.criteria.Order order = ascending ? cb
					.asc(entityRoot.get(orderByColumn)) : cb.desc(entityRoot
					.get(orderByColumn));
			criteria.orderBy(order);
		}
		criteria.where(predicates.toArray(new Predicate[] {}));
		return criteria;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Class getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(Class objectClass) {
		this.objectClass = objectClass;
	}
}