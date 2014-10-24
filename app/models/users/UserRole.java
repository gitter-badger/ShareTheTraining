package models.users;

import be.objectify.deadbolt.core.models.Role;

public enum UserRole implements Role
{
    ADMIN,
    CUSTOMER,
    TRAINER;

    @Override
    public String getName()
    {
        return name();
    }
}