package dao.interfaces;

import models.parent.User;

import java.util.List;

public interface UserDAO {

    void addUser(User user) throws Exception;
    User getUserById(int id) throws Exception;
    List<User> getAllUsers() throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(int id) throws Exception;

    // Many-to-many handling
    void addRoleToUser(int userId, int roleId) throws Exception;
    void removeRoleFromUser(int userId, int roleId) throws Exception;
}
