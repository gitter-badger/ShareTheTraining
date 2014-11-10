package models;

import static org.fest.assertions.Assertions.assertThat;
import models.users.*;

import org.junit.Test;

import common.BaseTest;

public class UserTest extends BaseTest{
	@Test
	public void testTrainerCreation(){
		Trainer trainer = Trainer.create("hehehe", "xixixix", "lala", this.getmEm());
		assertThat(trainer.getId()).isNotNull();
		assertThat(trainer.getUserRole()).isEqualTo(UserRole.TRAINER);
		this.getmEm().remove(trainer);
	}
	
	@Test
	public void testCustomerCreation(){
		Customer customer = Customer.create("lalalala", "xixixi", "lala", this.getmEm());
		assertThat(customer.getId()).isNotNull();
		assertThat(customer.getUserRole()).isEqualTo(UserRole.CUSTOMER);
		this.getmEm().remove(customer);
	}
	
	@Test
	public void testAdminCreation(){
		Admin admin = Admin.create("lalalala", "xixixi", "lala", this.getmEm());
		assertThat(admin.getId()).isNotNull();
		assertThat(admin.getUserRole()).isEqualTo(UserRole.ADMIN);
		this.getmEm().remove(admin);
	}
}
