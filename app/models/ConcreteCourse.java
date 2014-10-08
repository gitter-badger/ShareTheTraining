package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Transactional;
import common.BaseModelObject;



@Entity
public class ConcreteCourse extends BaseModelObject{
	@Transactional
	public static ConcreteCourse create(Course courseInfo) {
		ConcreteCourse concreteCourse = new ConcreteCourse();
		concreteCourse.setCourseInfo(courseInfo);
		//pm.save(concreteCourse);
		return concreteCourse;
	}
	@ManyToOne
	private Course courseInfo;
	
	public Course getCourseInfo() {
		return courseInfo;
	}
	public void setCourseInfo(Course courseInfo) {
		this.courseInfo = courseInfo;
	}

}
