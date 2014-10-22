package models.users;

import javax.persistence.Entity;
import javax.persistence.EntityManager;

@Entity
public class Admin extends User {

	protected Admin(String email, String username, String password) {
		super(email, username, password);
		// TODO Auto-generated constructor stub
	}


}
