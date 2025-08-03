package dao;

import models.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDAO {
    public void save(User user);

    public User findById(int id);

    public List<User> findAll();
}
