package models.forms;

import java.util.Calendar;
import java.util.Date;

import play.data.Form;
import models.filters.CourseFilterBuilder;

public class CourseFilterForm extends CourseFilterBuilder{


		private int dataChoice = -1;
		
		

		
		public int getDataChoice() {
			return dataChoice;
		}

		public void setDataChoice(int dataChoice) {
			this.dataChoice = dataChoice;
		}

		
		public  void transferChoiceToRange(){
			
			Calendar cal = Calendar.getInstance();
			Date current = new Date();
			cal.setTime(current);
			if(dataChoice==1){
				cal.add(Calendar.MONTH, 1);
				
				this.setStartDate(current);
				this.setEndDate(cal.getTime());
			}
			if(dataChoice==2){
				cal.add(Calendar.MONTH, 1);
				this.setStartDate(cal.getTime());
				cal.add(Calendar.MONTH, 2);
				this.setEndDate(cal.getTime());
			}
			if(dataChoice==3){
				this.setStartDate(current);
			}
		}
		
}
