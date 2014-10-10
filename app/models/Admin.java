package models;

import javax.persistence.Entity;

@Entity
public class Admin extends User {

	protected Admin(String email, String username, String password) {
		super(email, username, password);
	}

}
