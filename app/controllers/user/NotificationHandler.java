package controllers.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import play.Logger;
import play.db.DB;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.Course;
import models.courses.CourseOrder;
import models.filters.NotificationFilterBuilder;
import models.locations.Location;
import models.users.Customer;
import models.users.NotificationItem;
import models.users.Trainer;
import common.BaseModelObject;

public class NotificationHandler implements INotificationHandler {
	private EntityManager em;

	public NotificationHandler() {
		this.em = JPA.em();
	}

	private Collection getNewItems(Class objectClass, Date lastTime,
			int pageNumber, int pageSize) {
		NotificationFilterBuilder nfb = new NotificationFilterBuilder();
		nfb.setCreation(lastTime);
		nfb.setObjectClass(objectClass);
		TypedQuery<Tuple> tq = em.createQuery(nfb.buildeQuery(
				em.getCriteriaBuilder(), "created", true));
		if (pageNumber != -1 && pageSize != -1) {
			tq.setMaxResults(pageSize);
			tq.setFirstResult(pageSize * (pageNumber - 1));
		}
		Collection<BaseModelObject> result = new ArrayList<BaseModelObject>();
		for (Tuple t : tq.getResultList()) {
			result.add((BaseModelObject) t.get(0));
		}
		return result;
	}

	private long getNewItemsCount(Class objectClass, Date lastTime) {
		try {
			NotificationFilterBuilder nfb = new NotificationFilterBuilder();
			nfb.setCreation(lastTime);
			nfb.setObjectClass(objectClass);
			nfb.setCount(true);
			TypedQuery<Tuple> tq = em.createQuery(nfb.buildeQuery(
					em.getCriteriaBuilder(), "created", true));
			return (long) tq.getSingleResult().get(0);
		} catch (Exception e) {
			Logger.error(e.toString());
			return 0;
		}
	}

	@Override
	public Collection<Trainer> getNewTrainers(Date lastTime) {
		return getNewItems(Trainer.class, lastTime, -1, -1);
	}
	
	@Override
	public long getNewTrainerCount(Date lastTime){
		return getNewItemsCount(Trainer.class, lastTime);
	}

	@Override
	public Collection<Customer> getNewCustomers(Date lastTime) {
		return getNewItems(Customer.class, lastTime, -1, -1);
	}
	
	@Override
	public long getNewCustomerCount(Date lastTime) {
		return getNewItemsCount(Customer.class, lastTime);
	}

	@Override
	public Collection<Course> getNewCourses(Date lastTime) {
		return getNewItems(Course.class, lastTime, -1, -1);
	}

	@Override
	public long getNewCourseCount(Date lastTime) {
		return getNewItemsCount(Course.class, lastTime);
	}
	
	@Override
	public Collection<ConcreteCourse> getNewConcreteCourse(Date lastTime) {
		return getNewItems(ConcreteCourse.class, lastTime, -1, -1);
	}
	
	@Override
	public long getNewConcreteCourseCount(Date lastTime) {
		return getNewItemsCount(ConcreteCourse.class, lastTime);
	}

	@Override
	public Collection<CourseOrder> getNewCourseOrder(Date lastTime) {
		return getNewItems(CourseOrder.class, lastTime, -1, -1);
	}
	
	@Override
	public long getNewCourseOrderCount(Date lastTime) {
		return getNewItemsCount(CourseOrder.class, lastTime);
	}

	@Override
	public void updateNotifiedDate(NotificationItem notificationItem,
			Date current) {
		Connection connection = DB.getConnection();
		String updateSQL = "UPDATE notification SET action_timestamp = ? WHERE action = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(updateSQL);
			preparedStatement.setTimestamp(1, new Timestamp(current.getTime()));
			preparedStatement.setInt(2, notificationItem.ordinal());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
	}

	@Override
	public Date getLastNotifiedDate(NotificationItem notificationItem) {
		Connection connection = DB.getConnection();
		String selectSQL = "SELECT * FROM notification WHERE action = ?";
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(selectSQL);
			preparedStatement.setInt(1, notificationItem.ordinal());
			ResultSet rs = preparedStatement.executeQuery(selectSQL);
			if (rs.next()) {
				return new Date(rs.getTimestamp("action_timestamp").getTime());
			}
		} catch (SQLException e) {
			Logger.info(e.toString());
		}
		return new Date();
	}

}
