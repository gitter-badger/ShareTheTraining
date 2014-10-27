package models.courses;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.solr.common.SolrInputDocument;

import models.locations.Location;
import models.spellchecker.SolrDao;
import models.users.Customer;
import common.BaseModelObject;

@Entity
public class ConcreteCourse extends BaseModelObject {

	public static ConcreteCourse create(Course courseInfo, EntityManager em) {
		ConcreteCourse concreteCourse = new ConcreteCourse();
		concreteCourse.setCourseInfo(courseInfo);
		em.persist(concreteCourse);
		SolrInputDocument doc = concreteCourse.getSolrDoc();
		if(doc!=null)
			new SolrDao().putDoc(doc);
		return concreteCourse;
	}

	@ManyToOne
	private Course courseInfo;

	

	@ManyToMany(cascade = { CascadeType.ALL })
	private Collection<Customer> selectedCustomers = new ArrayList<Customer>();
	
	@OneToMany(mappedBy = "concreteCourse", cascade = { CascadeType.ALL })
	private Collection<WaitListRecord> waitListRecords = new ArrayList<WaitListRecord>();
	
	@OneToMany(mappedBy = "course", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();

	private Date courseDate;

	private Time length;

	private Location location = new Location(-1, -1, "", 0, 0);

	private int minimum;
	
	private int maximum;
	
	private String eventbriteId;
	
	private ConcreteCourseStatus status = ConcreteCourseStatus.unstarted;
	
	public Course getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(Course courseInfo) {
		this.courseInfo = courseInfo;
	}

	public Date getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}

	public Time getLength() {
		return length;
	}

	public void setLength(Time length) {
		this.length = length;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


	public Collection<Customer> getSelectedCustomers() {
		return selectedCustomers;
	}

	public void setSelectedCustomers(Collection<Customer> selectedCustomers) {
		this.selectedCustomers = selectedCustomers;
	}

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public Collection<WaitListRecord> getWaitListRecords() {
		return waitListRecords;
	}

	public void setWaitListRecords(Collection<WaitListRecord> waitListRecords) {
		this.waitListRecords = waitListRecords;
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public ConcreteCourseStatus getStatus() {
		return status;
	}

	public void setStatus(ConcreteCourseStatus status) {
		this.status = status;
	}

	public String getEventbriteId() {
		return eventbriteId;
	}

	public void setEventbriteId(String eventbriteId) {
		this.eventbriteId = eventbriteId;
	}

	public String getEventbriteUrl(){
		return "http://www.eventbrite.com/e/software-engineering-tickets-"+eventbriteId;
	}

}
