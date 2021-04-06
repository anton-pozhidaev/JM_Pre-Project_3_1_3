package study.jm.pp313.service;


import study.jm.pp313.model.User;

import java.util.List;

public interface UserService {

    void add(User user);

    User get(long id);

    void update(User updatedUser);

    void delete(long id);

    List<User> listUsers();

    User findUserByUsername(String login);

    void addInitUsersToDB();
}

