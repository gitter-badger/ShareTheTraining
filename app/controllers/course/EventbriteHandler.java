package controllers.course;

import java.net.URISyntaxException;

import models.courses.ConcreteCourse;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

import com.fasterxml.jackson.databind.JsonNode;
import common.Utility;

public class EventbriteHandler {
	public static Promise<JSONObject> createEventbriteEvent(
			ConcreteCourse concreteCourse) {

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(
				"www.eventbriteapi.com/v3/");
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

	public static Promise<JSONObject> createEventbriteTicket(
			ConcreteCourse concreteCourse) {

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(
				"www.eventbriteapi.com/v3/");
		builder.setPath("events/" + concreteCourse.getEventbriteId()
				+ "/ticket_classes");
		builder.setParameter("token", Play.application().configuration()
				.getString("token.eventbrite.oauth"));
		JsonNode json = Json
				.newObject()
				.put("", "");
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
}
