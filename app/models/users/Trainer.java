package models.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;

import org.apache.solr.common.SolrInputDocument;

import be.objectify.deadbolt.core.models.Role;
import play.db.jpa.JPA;
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

}
