package com.za.crud_mvc_hibernate.dao;

import com.za.crud_mvc_hibernate.model.Role;
import com.za.crud_mvc_hibernate.model.User;
import java.util.List;

public interface UserDao {

    public List<User> getAllUsers();

    public List<Role> getAllRoles();

    public void saveUser(User user);

    public void editUser(User user);

    public User getUser(int id);

    public void deleteUser(int id);

    public User getUserByName(String username);

    Role getRoleByName(String name);
}

