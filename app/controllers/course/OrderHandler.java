package controllers.course;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import common.Utility;

import play.Logger;
import play.Play;
import play.db.jpa.JPA;
import models.courses.ConcreteCourse;
import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.FilterBuilder;
import models.users.Customer;

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
	public Collection<CourseOrder> getCourseOrderByCustomRule(FilterBuilder fb,
			String orderByColumn, int pageNumber, int pageSize) {
		List<Tuple> tupleList = Utility.findBaseModelObject(fb, orderByColumn,
				true, pageNumber, pageSize, em);
		Collection<CourseOrder> result = new ArrayList<CourseOrder>();
		for (Tuple t : tupleList) {
			result.add((CourseOrder) t.get(0));
		}
		return result;
	}

	public boolean updateOrderStatus(String orderId, OrderStatus s) {
		CourseOrder order = this.getCourseOrderByOrderId(orderId);
		order.setOrderStatus(s);
		return true;
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	public static JSONObject getOrderDetails(String orderId) {
		try {
			URIBuilder builder = new URIBuilder().setScheme("https").setHost(
					"www.eventbriteapi.com/v3/orders/");
			builder.setPath(orderId);
			builder.setParameter("token", Play.application().configuration()
					.getString("token.eventbrite.oauth"));
			HttpGet httpget = new HttpGet(builder.build().toString());
			HttpResponse response = new DefaultHttpClient().execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				JSONObject orderDetails = new JSONObject(result);
				return orderDetails;
			}
			return null;
		} catch (Exception e) {
			Logger.error(e.toString());
			return null;
		}
	}

	public static double getGrossFee(JSONObject orderDetails) throws JSONException {
		if (orderDetails != null)
			return orderDetails.getJSONArray("attendees").getJSONObject(0)
					.getJSONObject("costs").getJSONObject("gross")
					.getDouble("value");
		return -1;
	}

	public static String getEventbriteUserId(JSONObject orderDetails) throws JSONException {
		if (orderDetails != null)
			return orderDetails.getJSONArray("attendees").getJSONObject(0)
					.getString("id");
		return null;
	}

	@Override
	public CourseOrder newCourseOrder(String orderId,
			ConcreteCourse concreteCourse, Customer customer) throws JSONException {
		CourseOrder courseOrder = CourseOrder.create(orderId, concreteCourse,
				customer, new Date(), OrderStatus.CONFIRMED, em);
		JSONObject orderDetails = getOrderDetails(orderId);
		courseOrder.setGross(getGrossFee(orderDetails));
		courseOrder.setEventbriteUserId(getEventbriteUserId(orderDetails));
		return courseOrder;
	}

}
