package study.jm.pp313.dao;

import study.jm.pp313.model.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    User get(long id);

    User findUserByLogin(String login);

    void update(User updatedUser);

    void delete(long id);

    List<User> listUsers();

}
