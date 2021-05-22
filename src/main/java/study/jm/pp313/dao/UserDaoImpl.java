package study.jm.pp313.dao;

import org.springframework.stereotype.Repository;
import study.jm.pp313.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void add(User user) {
        em.persist(user);
    }

    @Override
    public User get(long id) {
        User usr = em.find(User.class, id);
        return usr;
    }

    @Override
    public User findUserByLogin(String email) {
        TypedQuery<User> query = em.createQuery("SELECT user FROM User user WHERE user.email=:l", User.class);
        query.setParameter("l", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
//            System.out.println(e.getMessage() + " in " + this.getClass().getName());
            return null;
        }
    }

    @Override
    public void update(User updatedUser) {
        em.merge(updatedUser);
    }

    @Override
    public void delete(long id) {
        User usr = em.find(User.class, id);
        em.remove(usr);
    }

    @Override
    public List<User> listUsers() {
        TypedQuery<User> query = em.createQuery("SELECT user from User user", User.class);
        return query.getResultList();
    }
}
