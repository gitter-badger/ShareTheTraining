package controllers;

import static org.fest.assertions.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import models.users.Trainer;

import org.junit.Test;

import common.BaseTest;
import controllers.user.NotificationHandler;
import play.Logger;

public class NotificationHandlerTest extends BaseTest{

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNewItem(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "06-04-1989";
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date2 = sdf.parse(dateInString);
		} catch (ParseException e) {
			Logger.error(e.toString());
		}
		NotificationHandler nfh = new NotificationHandler();
		date1.setSeconds(date1.getSeconds()+1);
		Collection<Trainer> list1 = nfh.getNewTrainers(date1);
		Collection<Trainer> list2 = nfh.getNewTrainers(date2);
		System.out.println(date1.toString());
		assertThat(list1.size()).isEqualTo(0);
		assertThat(list2.size()).isEqualTo(1);
		assertThat(list2.iterator().next().getEmail()).isEqualTo("sda");
	}
	
	@Test
	public void testGetNewItemCount(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		String dateInString = "06-04-1989";
		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date2 = sdf.parse(dateInString);
		} catch (ParseException e) {
			Logger.error(e.toString());
		}
		NotificationHandler nfh = new NotificationHandler();
		date1.setSeconds(date1.getSeconds()+1);
		assertThat(nfh.getNewTrainerCount(date1)).isEqualTo(0);
		assertThat(nfh.getNewTrainerCount(date2)).isEqualTo(1);
	}
}
