package controllers.course;

import java.net.URISyntaxException;

import models.courses.ConcreteCourse;
import models.courses.CourseOrder;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;

import common.Utility;
import controllers.routes;

public class EventbriteHandler {
	public static Promise<JSONObject> createEventbriteEventV3(
			ConcreteCourse concreteCourse) {

		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v3"));
		builder.setPath("events/");
		builder.setParameter("token", Play.application().configuration()
				.getString("token.eventbrite.oauth"));
		JsonNode json = Json
				.newObject()
				.put("event.name.html",
						concreteCourse.getCourseInfo().getCourseName() + "-"
								+ concreteCourse.getLocation().getCity() + "-"
								+ concreteCourse.getLocation().getRegion())
				.put("event.start.timezone", "America/Los_Angeles")
				.put("event.end.timezone", "America/Los_Angeles")
				.put("event.start.utc",
						Utility.getEventbriteDateFormat(concreteCourse
								.getCourseStartDate()))
				.put("event.end.utc",
						Utility.getEventbriteDateFormat(concreteCourse
								.getCourseStartDate()))
				.put("event.description.html",
						concreteCourse.getCourseInfo().getCourseDesc())
				.put("event.currency", "USD")
				.put("event.online_event", "true")
				.put("event.organizer_id",
						Play.application().configuration()
								.getString("token.eventbrite.organizer_id"));
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString())
					.setContentType("application/x-www-form-urlencoded")
					.post(json).map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject courseDetails = new JSONObject(response
									.getBody());
							return courseDetails;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}

	}

	public static Promise<JSONObject> createEventbriteTicketV3(
			ConcreteCourse concreteCourse) {

		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v3"));
		builder.setPath("events/" + concreteCourse.getEventbriteId()
				+ "/ticket_classes");
		builder.setParameter("token", Play.application().configuration()
				.getString("token.eventbrite.oauth"));
		JsonNode json = Json
				.newObject()
				.put("ticket_class.name",
						concreteCourse.getCourseInfo().getCourseName() + "-"
								+ concreteCourse.getLocation().getCity() + "-"
								+ concreteCourse.getLocation().getRegion())
				.put("ticket_class.qantity_total", concreteCourse.getMaximum())
				.put("ticket_class.cost.currency", "USC")
				.put("ticket_class.cost.value",
						concreteCourse.getCourseInfo().getPrice() * 100);
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString())
					.setContentType("application/x-www-form-urlencoded")
					.post(json).map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject courseDetails = new JSONObject(response
									.getBody());
							return courseDetails;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}
	}

	public static Promise<JSONObject> createEventbriteEvent(
			ConcreteCourse concreteCourse) {

		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v1"));
		builder.setPath("event_new");
		builder.setParameter("app_key", Play.application().configuration()
				.getString("token.eventbrite.app"));
		builder.setParameter("user_key", Play.application().configuration()
				.getString("token.eventbrite.user"));
		builder.setParameter("title", concreteCourse.getCourseInfo()
				.getCourseName()
				+ "-"
				+ concreteCourse.getLocation().getCity()
				+ "-" + concreteCourse.getLocation().getRegion());
		builder.setParameter("start_date", concreteCourse.getCourseStartDate()
				.toString());
		builder.setParameter("end_date", concreteCourse.getCourseEndDate()
				.toString());
		builder.setParameter("description", concreteCourse.getCourseInfo()
				.getCourseDesc());
		builder.setParameter(
				"confirmation_page",
				Play.application().configuration().getString("url.base")
						+ Play.application().configuration()
								.getString("ws.eventbrite.confirmation"));
		builder.setParameter("organizer_id", Play.application().configuration()
				.getString("token.eventbrite.organizer_id"));
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString()).get()
					.map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject courseDetails = new JSONObject(response
									.getBody());
							return courseDetails;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}

	}

	public static Promise<JSONObject> createEventbriteTicket(
			ConcreteCourse concreteCourse, String eventbriteId) {

		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v1"));
		builder.setPath("ticket_new");
		builder.setParameter("app_key", Play.application().configuration()
				.getString("token.eventbrite.app"));
		builder.setParameter("user_key", Play.application().configuration()
				.getString("token.eventbrite.user"));
		builder.setParameter("event_id", eventbriteId);
		builder.setParameter("name", concreteCourse.getCourseInfo()
				.getCourseName()
				+ "-"
				+ concreteCourse.getLocation().getCity()
				+ "-" + concreteCourse.getLocation().getRegion());
		builder.setParameter("price",
				Double.toString(concreteCourse.getCourseInfo().getPrice()));
		builder.setParameter("quantity_available",
				Integer.toString(concreteCourse.getCourseInfo().getMaximum()));
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString()).get()
					.map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject result = new JSONObject(response
									.getBody());
							return result;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}
	}

	public static Promise<JSONObject> updatePaymentMethod(
			String eventbriteId) {

		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v1"));
		builder.setPath("ticket_new");
		builder.setParameter("app_key", Play.application().configuration()
				.getString("token.eventbrite.app"));
		builder.setParameter("user_key", Play.application().configuration()
				.getString("token.eventbrite.user"));
		builder.setParameter("event_id", eventbriteId);
		builder.setParameter("accept_paypal", "1");
		builder.setParameter("paypal_email", Play.application().configuration()
				.getString("ws.eventbrite.paypal"));
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString()).get()
					.map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject result = new JSONObject(response
									.getBody());
							return result;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}
	}

	public static Promise<JSONObject> publishEvent(String eventbriteId) {
		URIBuilder builder = new URIBuilder().setScheme(
				Play.application().configuration()
						.getString("ws.eventbrite.scheme")).setHost(
				Play.application().configuration()
						.getString("ws.eventbrite.v1"));
		builder.setPath("event_update");
		builder.setParameter("app_key", Play.application().configuration()
				.getString("token.eventbrite.app"));
		builder.setParameter("user_key", Play.application().configuration()
				.getString("token.eventbrite.user"));
		builder.setParameter("event_id", eventbriteId);
		builder.setParameter("status", "live");
		try {
			final Promise<JSONObject> resultPromise = WS
					.url(builder.build().toString()).get()
					.map(new Function<WSResponse, JSONObject>() {
						public JSONObject apply(WSResponse response) {
							JSONObject result = new JSONObject(response
									.getBody());
							return result;
						}
					});
			return resultPromise;
		} catch (URISyntaxException e) {
			Logger.error(e.toString());
			return null;
		}
	}
	
	public static Promise<Boolean> EventbriteProcess(final ConcreteCourse concreteCourse){
		/*
		Promise<JSONObject> courseDetails = createEventbriteEvent(concreteCourse);
		if(courseDetails!=null){
			return courseDetails.map(new Function<JSONObject, Boolean>() {
				@Override
				public Boolean apply(JSONObject courseDetails) throws Throwable {
					final String eventbriteId = getEventbriteIdAfterCreation(courseDetails);
					if(eventbriteId!=null){
						Promise<JSONObject> newTicket = createEventbriteTicket(concreteCourse,eventbriteId);
						newTicket.map(new Function<JSONObject, Boolean>() {
							@Override
							public Boolean apply(JSONObject result) throws Throwable {
								String status = getTicketResult(result);
								if("ok".equals(status)){
									concreteCourse.setEventbriteId(eventbriteId);
									return true;
								}
								return false;
							}
							
						});
					}
					return false;
				}
			});
		}
		*/
		return Promise.promise(new Function0<Boolean>() {
			public Boolean apply() {
				return false;
			}
		});
	}

	public static String getEventbriteIdAfterCreation(JSONObject courseDetails) {
		try {
			return courseDetails.getJSONObject("process").getString("id");
		} catch (Exception e) {
			return null;
		}
	}

	public static String getTicketResult(JSONObject result) {
		try {
			return result.getJSONObject("process").getString("status");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getPaymentResult(JSONObject result) {
		try {
			return result.getJSONObject("process").getString("status");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getEventUpdateResult(JSONObject result) {
		try {
			return result.getJSONObject("event").getString("status");
		} catch (Exception e) {
			return null;
		}
	}
}
