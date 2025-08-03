package dao;

import models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    
    @Override
    public void save(User user) {
        // insert SQL logic here
    }

    @Override
    public User findById(int id) {
        // Write select SQL logic here
        return null;
    }

    @Override
    public List<User> findAll() {
        // Write logic to fetch all users
        return new ArrayList<>();
    }
}
