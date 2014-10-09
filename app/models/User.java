package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;


import play.data.format.Formats;
import play.data.validation.Constraints;
import common.BaseModelObject;

@Entity
public class User extends BaseModelObject{
	
	//
	@Constraints.Required
	@Formats.NonEmpty
	@Column(unique = true)
	public String email;
	
	@Constraints.Required
	@Formats.NonEmpty
	public String username;
	
	@Constraints.Required
	@Formats.NonEmpty
	public String password;
	
//	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
//	public Date birthday; 
	
	//
	public static User create( String email, String username, String password, EntityManager em){
		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		user.setPassword(password);
		em.persist(user);
		return user;
		
	}




	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
	
	
	
}
