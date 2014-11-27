package models.courses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import org.apache.solr.common.SolrInputDocument;

import play.Logger;
import models.locations.Location;
import models.spellchecker.SolrDao;
import models.users.Trainer;
import common.BaseModelObject;

@Entity
public class Course extends BaseModelObject {

	public static Course create(String courseName, Trainer trainer,
			EntityManager em) {
		Course course = new Course();
		course.setCourseName(courseName);
		course.setTrainer(trainer);
		em.persist(course);
		course.putSolrDoc();
		return course;
	}

	private String courseName;

	private int concreteCourseCount = 0;

	@ManyToOne
	private Trainer trainer;

	private int courseCategory;

	// TODO should this be in ConcreteCourse?
	private double price;

	private String fromCompany;

	@Lob
	private String courseDesc;

	private CourseStatus status = CourseStatus.VERIFYING;

	private String methods;

	private String keyPoints;

	private int popularity = 0;

	private double rating;

	private Date earliestDate;

	private Date latestDate;

	private boolean displayRating;

	private int popular = 0;

	private int minimum = -1;

	private int maximum = -1;
	
	private String image;

	@OneToMany
	private Collection<Review> reviews = new ArrayList<Review>();

	@OneToMany(mappedBy = "courseInfo", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();

	public void updateRating(double rating) {
		this.rating = (this.rating * this.reviews.size() + rating)
				/ (this.reviews.size() + 1);
	}

	public void addReview(Review review) {
		this.updateRating(review.getCourseRating());
		this.reviews.add(review);
	}

	public void addConcreteCourse(ConcreteCourse concreteCourse) {
		this.courses.add(concreteCourse);
		/*
		 * if (this.earliestDate == null ||
		 * this.earliestDate.after(concreteCourse.getCourseDate()))
		 * this.setEarliestDate(concreteCourse.getCourseDate()); if
		 * (this.latestDate == null ||
		 * this.latestDate.before(concreteCourse.getCourseDate()))
		 * this.setLatestDate(concreteCourse.getCourseDate());
		 * if(this.courses.size() == 1 && this.status == CourseStatus.APPROVED)
		 * this.status = CourseStatus.OPEN;
		 */
	}

	public static List<Selection> getSelections(Path path) {
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(path.get("price"));
		selections.add(path.get("courseCategory"));
		selections.add(path.get("popularity"));
		selections.add(path.get("rating"));
		selections.add(path.get("status"));
		return selections;
	}

	@Override
	public SolrInputDocument getSolrDoc() {
		try{
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", this.getId());
		doc.addField("name", this.getCourseName());
		doc.addField("description", this.getKeyPoints());
		return doc;
		}catch(Exception e){
			Logger.info(e.toString());
			return null;
		}
	}

	public boolean removeConcreteCourse(ConcreteCourse c) {
		return this.courses.remove(c);
		/*
		 * if (this.courses.remove(c)) { if (this.courses.size() == 0 &&
		 * this.status == CourseStatus.OPEN)
		 * this.setStatus(CourseStatus.APPROVED); return true; } return false;
		 */
	}

	public void updateDate() {
		Date earliestDate = null, latestDate = null;
		for (ConcreteCourse concreteCourse : this.courses) {
			if (concreteCourse.getStatus() != ConcreteCourseStatus.VERIFYING
					&& concreteCourse.getCourseStartDate() != null) {
				if (earliestDate == null
						|| earliestDate.after(concreteCourse.getCourseStartDate())) {
					earliestDate = concreteCourse.getCourseStartDate();
				}

				if (latestDate == null
						|| latestDate.before(concreteCourse.getCourseStartDate())) {
					latestDate = concreteCourse.getCourseStartDate();
				}
			}
		}
		this.setEarliestDate(earliestDate);
		this.setLatestDate(latestDate);
	}

	public List<String> getKeyPointsAsList() {
		List<String> keyPointsList = new ArrayList<String>();
		if (keyPoints != null)
			keyPointsList = Arrays.asList(keyPoints.split(","));
		return keyPointsList;
	}

	public List<String[]> getLocationList() {
		List<String[]> result = new ArrayList<String[]>();
		HashMap<String, Set<String>> locationSet = new HashMap<String, Set<String>>();
		for (ConcreteCourse c : courses) {
			Location location = c.getLocation();
			if (!locationSet.containsKey(location.getRegion()))
				;
			locationSet.put(location.getRegion(), new HashSet<String>());
			if (!locationSet.get(location.getRegion()).contains(
					location.getCity())) {
				String[] locationArray = new String[] { location.getRegion(),
						location.getCity() };
				result.add(locationArray);
			}
		}
		return result;
	}

	public Collection<ConcreteCourse> getFilterCourse() {
		return null;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}

	public Collection<ConcreteCourse> getCourses() {
		return courses;
	}

	public void setCourses(Collection<ConcreteCourse> courses) {
		this.courses = courses;
	}

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	public String getFromCompany() {
		return fromCompany;
	}

	public void setFromCompany(String fromCompany) {
		this.fromCompany = fromCompany;
	}

	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getConcreteCourseCount() {
		return concreteCourseCount;
	}

	public void setConcreteCourseCount(int concreteCourseCount) {
		this.concreteCourseCount = concreteCourseCount;
	}

	public Date getEarliestDate() {
		return earliestDate;
	}

	public void setEarliestDate(Date earliestDate) {
		this.earliestDate = earliestDate;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}

	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public String getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
	}

	public boolean isDisplayRating() {
		return displayRating;
	}

	public void setDisplayRating(boolean displayRating) {
		this.displayRating = displayRating;
	}

	public int getPopular() {
		return popular;
	}

	public void setPopular(int popular) {
		this.popular = popular;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
