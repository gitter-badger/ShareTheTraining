package models.forms;

import java.util.Calendar;
import java.util.Date;

import play.data.Form;
import models.filters.CourseFilterBuilder;

public class CourseFilterForm {

		private CourseFilterBuilder cfb = new CourseFilterBuilder();
		
		private int pageNumber;
		
		private int pageSize;

		public CourseFilterBuilder getCfb() {
			return cfb;
		}

		public void setCfb(CourseFilterBuilder cfb) {
			this.cfb = cfb;
		}

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		
		public static CourseFilterForm transferChoiceToRange(int datec, CourseFilterForm filterForm){
			
			Calendar cal = Calendar.getInstance();
			Date current = new Date();
			cal.setTime(current);
			if(datec==1){
				cal.add(Calendar.MONTH, 1);
				
				filterForm.getCfb().setStartDate(current);
				filterForm.getCfb().setEndDate(cal.getTime());
			}
			if(datec==2){
				cal.add(Calendar.MONTH, 1);
				filterForm.getCfb().setStartDate(cal.getTime());
				cal.add(Calendar.MONTH, 2);
				filterForm.getCfb().setEndDate(cal.getTime());
			}
			if(datec==3){
				filterForm.getCfb().setStartDate(current);
			}
			return filterForm;
		}
		
}
