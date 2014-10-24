package models.users;

import be.objectify.deadbolt.core.models.Role;

public enum UserRoles implements Role
{
    admin,
    customer,
    trainer;

    @Override
    public String getName()
    {
        return name();
    }
}