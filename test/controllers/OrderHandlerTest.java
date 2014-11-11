package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import models.courses.CourseOrder;
import models.courses.OrderStatus;
import models.filters.OrderFilterBuilder;

import org.junit.Test;

import common.BaseTest;
import controllers.course.OrderHandler;

public class OrderHandlerTest extends BaseTest {

	@Test
	public void testFilterCourseByMultiOption() {
		CourseOrder order = CourseOrder.create("123",
				this.initData.concreteCourse1, this.initData.customer1,
				new Date(), OrderStatus.ORDERED, this.getmEm());
		OrderHandler orderHandler = new OrderHandler();
		OrderFilterBuilder fb = new OrderFilterBuilder();
		fb.setUserEmail(this.initData.customer1.getEmail());
		Collection<CourseOrder> result = orderHandler
				.getCourseOrderByCustomRule(fb, null, -1,
						-1);
		assertThat(result.size()).isEqualTo(1);
		this.getmEm().remove(order);
	}
}
