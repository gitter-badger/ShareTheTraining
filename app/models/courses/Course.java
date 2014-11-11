package models.courses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Selection;

import org.apache.solr.common.SolrInputDocument;

import models.spellchecker.SolrDao;
import models.users.Trainer;
import common.BaseModelObject;

@Entity
public class Course extends BaseModelObject {

	public static Course create(String courseName, int courseCategory,
			String courseDesc, EntityManager em) {
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseCategory(courseCategory);
		course.setCourseDesc(courseDesc);
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

	private int popularity;

	private double rating;

	
	@OneToMany(mappedBy = "course", cascade = { CascadeType.ALL })
	private Collection<Review> reviews = new ArrayList<Review>();


	@OneToMany(mappedBy = "courseInfo", cascade = { CascadeType.ALL })
	private Collection<ConcreteCourse> courses = new ArrayList<ConcreteCourse>();

	public void updateRating(double rating) {
		this.rating = (this.rating * this.reviews.size()+rating) / (this.reviews.size()+1);
	}
	
	public void addReview(Review review){
		this.updateRating(review.getCourseRating());
		this.reviews.add(review);
	}
	
	public Collection<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Collection<Review> reviews) {
		this.reviews = reviews;
	}

	public static List<Selection> getSelections(Path path) {
		List<Selection> selections = new ArrayList<Selection>();
		selections.add(path.get("price"));
		selections.add(path.get("courseCategory"));
		selections.add(path.get("courseDesc"));
		selections.add(path.get("popularity"));
		return selections;
	}

	@Override
	public SolrInputDocument getSolrDoc() {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", this.getId());
		doc.addField("name", this.getCourseName());
		doc.addField("description", this.getCourseDesc());
		return doc;
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

	public String getKeyPoints() {
		return keyPoints;
	}

	public void setKeyPoints(String keyPoints) {
		this.keyPoints = keyPoints;
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
}
