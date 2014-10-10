package models;

import javax.persistence.Entity;

@Entity
public class Customer extends User{

	protected Customer(String email, String username, String password) {
		super(email, username, password);
	}

}
