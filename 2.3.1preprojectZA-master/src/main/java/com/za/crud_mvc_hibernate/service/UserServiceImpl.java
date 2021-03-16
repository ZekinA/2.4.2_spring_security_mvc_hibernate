package com.za.crud_mvc_hibernate.service;

import com.za.crud_mvc_hibernate.dao.UserDao;
import com.za.crud_mvc_hibernate.model.Role;
import com.za.crud_mvc_hibernate.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<Role> getAllRoles() {
        return userDao.getAllRoles();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void saveUser(User user) {
        setUserRoles(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDao.saveUser(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void editUser(User user) {
        setUserRoles(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDao.editUser(user);
    }

    @Override
    public User getUser(int id) {
        return userDao.getUser(id);
    }

    @Override
    public User getUserByName(String username) {
        return userDao.getUserByName(username);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Transactional
    public void setUserRoles(User user) {
        user.setRoles(user
                .getRoles()
                .stream()
                .map(role -> userDao.getRoleByName(role.getName()))
                .collect(Collectors.toSet()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(), user.getPassword(), user.getRoles());

        return userDao.getUserByName(username);
    }
}