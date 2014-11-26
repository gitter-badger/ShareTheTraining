package models.users;

import be.objectify.deadbolt.core.models.Role;

public enum UserRole implements Role
{
    ADMIN,
    CUSTOMER,
    TRAINER,
    GUEST;

    @Override
    public String getName()
    {
        return name();
    }
    
    public static UserRole fromInteger(int x) {
		switch (x) {
		case 0:
			return ADMIN;
		case 1:
			return CUSTOMER;
		case 2:
			return TRAINER;
		}
		return null;
	}
}