package dao.implementations;

import dao.interfaces.UserDAO;
import models.parent.User;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) throws Exception {

    }

    @Override
    public User getUserById(int id) throws Exception {
        return null;
    }

    @Override
    public List<User> getAllUsers() throws Exception {
        return List.of();
    }

    @Override
    public void updateUser(User user) throws Exception {

    }

    @Override
    public void deleteUser(int id) throws Exception {

    }

    @Override
    public void addRoleToUser(int userId, int roleId) throws Exception {

    }

    @Override
    public void removeRoleFromUser(int userId, int roleId) throws Exception {

    }

//    @Override
//    public void save(User user) {
//        // insert SQL logic here
//    }
//
//    @Override
//    public User findById(int id) {
//        // Write select SQL logic here
//        return null;
//    }
//
//    @Override
//    public List<User> findAll() {
//        // Write logic to fetch all users
//        return new ArrayList<>();
//    }
}
