package controllers.course;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;

public class OrderHandler implements IOrderHandler {
	private EntityManager em;

	public OrderHandler() {
		this.em = JPA.em();
	}

	@Override
	public CourseOrder getCourseOrderByOrderId(String orderId) {
		String hql = "from CourseOrder o where o.orderId= :orderId";
		Query query = em.createQuery(hql).setParameter("orderId", orderId);
		Collection result = query.getResultList();
		if (result.size() > 0)
			return (CourseOrder) result.iterator().next();
		return null;
	}

	@Override
	public Collection<CourseOrder> getCourseOrderByCustomer(String userEmail) {
		String hql = "from CourseOrder o where o.customer.email= :userEmail";
		Query query = em.createQuery(hql).setParameter("userEmail", userEmail);
		Collection result = query.getResultList();
		return result;
	}

	@Override
	public Collection<CourseOrder> getCourseOrderByCourse(Integer id) {
		String hql = "from CourseOrder o where o.concreteCourse.courseInfo.id= :id";
		Query query = em.createQuery(hql).setParameter("id", id);
		Collection result = query.getResultList();
		return result;
	}

	@Override
	public Collection<CourseOrder> getCourseOrderByConcreteCourse(
			String concreteCourseId) {
		String hql = "from CourseOrder o where o.concreteCourse.concreteCourseId= :concreteCourseId";
		Query query = em.createQuery(hql).setParameter("concreteCourseId",
				concreteCourseId);
		Collection result = query.getResultList();
		return result;
	}

	@Override
	public Collection<CourseOrder> getCourseOrderByCustomRule(FilterBuilder cb,
			String orderByColumn, int pageNumber, int pageSize) {
		TypedQuery<Tuple> tq = em.createQuery(cb.buildeQuery(
				em.getCriteriaBuilder(), orderByColumn, true));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		Collection<CourseOrder> result = new ArrayList<CourseOrder>();
		for (Tuple t : tq.getResultList()) {
			result.add((CourseOrder) t.get(0));
		}
		return result;
	}

	public boolean updateOrderStatus(String orderId, OrderStatus s) {
		CourseOrder order = this.getCourseOrderByOrderId(orderId);
		order.setOrderStatus(s);
		return true;
	}

}
