package common;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.courses.ConcreteCourse;

public class Utility {
	public static String getStyledDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
		String s = formatter.format(date);
		return s;
	}
}
