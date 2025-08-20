package dao.interfaces;

import models.parent.User;

import java.util.List;

public interface UserDAO {

    void addUser(User user) throws Exception;
    User getUserById(int id) throws Exception;
    List<User> getAllUsers() throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(int id) throws Exception;
    boolean userExist(int id) throws Exception;
    int verifyUserPassword(String username, String enteredPassword, int roleId) throws Exception;
    void updatePassword(int userId, String newPassword) throws Exception;
}


