package study.jm.pp313.dao;

import study.jm.pp313.model.Role;

import java.util.Collection;

public interface RoleDao {

    Role findByRoleName(String role);

    Collection<Role> getAllRoles();
}
