package study.jm.pp313.dao;

import study.jm.pp313.model.Role;

import java.util.Collection;

public interface RoleDao {

    void addRole(Role role);

    Role findRoleByName(String role);

    Collection<Role> getAllRoles();
}
