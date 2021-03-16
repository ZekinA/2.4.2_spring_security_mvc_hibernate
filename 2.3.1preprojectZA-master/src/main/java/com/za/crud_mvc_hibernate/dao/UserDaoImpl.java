package com.za.crud_mvc_hibernate.dao;

import com.za.crud_mvc_hibernate.model.Role;
import com.za.crud_mvc_hibernate.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {

        List<User> users = entityManager.createQuery("from User", User.class).getResultList();

        return users;
    }

    @Override
    public List<Role> getAllRoles() {

        List<Role> roles = entityManager.createQuery("from Role", Role.class).getResultList();

        return roles;
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void editUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUser(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public User getUserByName(String username) {
        return entityManager.createQuery("from User where name = :name", User.class)
                .setParameter("name", username).getSingleResult();
    }

    @Override
    public Role getRoleByName(String name) {
        return entityManager.createQuery("from Role where name = :name", Role.class)
                .setParameter("name", name).getSingleResult();
    }
}