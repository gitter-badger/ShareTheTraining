package models.filters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import play.data.Form;
import models.forms.CourseFilterForm;


public class DateFilterHandler {
	
	public Form<CourseFilterForm> transferChoiceToRange(int datec, Form<CourseFilterForm> filterForm){
		
		Calendar cal = Calendar.getInstance();
		Date current = new Date();
		cal.setTime(current);
		if(datec==1){
			cal.add(Calendar.MONTH, 1);
			
			filterForm.get().getCfb().setStartDate(current);
			filterForm.get().getCfb().setEndDate(cal.getTime());
		}
		if(datec==2){
			cal.add(Calendar.MONTH, 2);
			filterForm.get().getCfb().setStartDate(cal.getTime());
			cal.add(Calendar.MONTH, 2);
			filterForm.get().getCfb().setEndDate(cal.getTime());
		}
		if(datec==3){
			cal.add(Calendar.MONTH, 4);
			filterForm.get().getCfb().setStartDate(cal.getTime());
		}
		return filterForm;
	}
		
}
