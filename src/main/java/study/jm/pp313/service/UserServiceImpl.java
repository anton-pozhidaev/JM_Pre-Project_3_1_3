package study.jm.pp313.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jm.pp313.dao.RoleDao;
import study.jm.pp313.dao.UserDao;
import study.jm.pp313.model.AuthenticationProvider;
import study.jm.pp313.model.Role;
import study.jm.pp313.model.User;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    private final String DEFAULT_PASSWORD = "1234";

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void addInitUsersToDB() {

        List<User> initUsersList = new ArrayList<>();
        roleDao.addRole(new Role("USER"));
        roleDao.addRole(new Role("ADMIN"));
        roleDao.addRole(new Role("VIEWER"));

        Set<Role> userRole = Collections.singleton(roleDao.findRoleByName("USER"));
        Set<Role> adminRole = Collections.singleton(roleDao.findRoleByName("ADMIN"));
        Set<Role> viewerRole = Collections.singleton(roleDao.findRoleByName("VIEWER"));

        initUsersList.add(new User("Vladimir", "Putin", "putya@mainbunker.ru"
                , "1999-12-19", "USSR, Popamira 123"
                , "1234", AuthenticationProvider.LOCAL, adminRole));

        initUsersList.add(new User("Dmitriy", "Peskov", "pesok@kremlin.ru",
                "1999-12-20", "USSR, Popamira 456"
                , "1234", AuthenticationProvider.LOCAL, userRole));

        initUsersList.add(new User("Uasya", "Vatnikov", "vata@mail.ru",
                "1999-12-21", "USSR, Popamira 789"
                , "1234", AuthenticationProvider.LOCAL, viewerRole));

        initUsersList.add(new User("Alexey", "Navalniy", "leha@better-future.ru",
                "1999-12-22", "USSR, Moscow"
                , "1234", AuthenticationProvider.LOCAL, adminRole));

        initUsersList.add(new User("Vladimir", "Solovyov", "pomet@trash.ru",
                "1999-12-23", "USSR, Popamira 666"
                , "1234", AuthenticationProvider.LOCAL, userRole));

        for (User user : initUsersList) {
            this.add(user);
        }
    }

    @Transactional
    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider(AuthenticationProvider.LOCAL);
        userDao.add(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User get(long id) {
        return userDao.get(id);
    }

    @Transactional
    @Override
    public void update(User updatedUser) {
        User currentUser = userDao.get(updatedUser.getId());
        String newPassword = updatedUser.getPassword();
        String oldPassword = currentUser.getPassword();

        if (newPassword.equals("") || newPassword.equals(oldPassword)) {
            updatedUser.setPassword(oldPassword);
        } else {
            updatedUser.setPassword(passwordEncoder.encode(newPassword));
        }
        updatedUser.setAuthProvider(currentUser.getAuthProvider());
        userDao.update(updatedUser);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional
    @Override
    public User findUserByUsername(String login) {
        return userDao.findUserByLogin(login);
    }

    @Transactional
    @Override
    public void processOAuthPostLogin(String username, Map<String, Object> attributes) {
        System.out.println(attributes);
        User newUser = new User();
        newUser.setEmail(username);
        if (username.contains("@gmail.com")) {
            newUser.setAuthProvider(AuthenticationProvider.GOOGLE);
            newUser.setFirstName((String) attributes.get("given_name"));
            newUser.setLastName((String) attributes.get("family_name"));
        }
        if (!username.contains("@")) {
            newUser.setAuthProvider(AuthenticationProvider.GITHUB);
            newUser.setFirstName(username.split("-")[0]);
            newUser.setLastName(username.split("-")[1]);
        }
        newUser.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        newUser.setRoles(Collections.singleton(roleDao.findRoleByName("USER")));
        userDao.add(newUser);
    }
}
