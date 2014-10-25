package models.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import org.apache.solr.common.SolrInputDocument;

import be.objectify.deadbolt.core.models.Role;
import models.courses.Course;
import models.spellchecker.SolrDao;


@Entity
public class Trainer extends User{

	public static Trainer create(String email, String username, String password, EntityManager em) {
		Trainer trainer = new Trainer(email, username, password);
		em.persist(trainer);
		SolrInputDocument doc = trainer.getSolrDoc();
		if(doc!=null)
			new SolrDao().putDoc(doc);
		return trainer;

	}
	
	protected Trainer(String email, String username, String password) {
		super(email, username, password);
		this.setUserRole(UserRole.TRAINER);
	}
	
	public Trainer(){}

	@Override
	public List<? extends Role> getRoles() {
		List<UserRole> list = new ArrayList<UserRole>();
		list.add(this.getUserRole());
		return list;
	}
	
	@OneToMany(mappedBy = "trainer", cascade = { CascadeType.ALL })
	private Collection<Course> courses = new ArrayList<Course>();

	
	@ElementCollection
	private Set<Date> availableDates = new HashSet<Date>();


	public Collection<Course> getCourses() {
		return courses;
	}

	public void setCourses(Collection<Course> courses) {
		this.courses = courses;
	}

	public Set<Date> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(Set<Date> availableDates) {
		this.availableDates = availableDates;
	}
}
