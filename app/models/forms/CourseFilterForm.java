package models.forms;

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
		
		
}
