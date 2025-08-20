package services.userService;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.interfaces.RolePrivilegeDAO;
import dao.interfaces.UserDAO;
import dtos.UserRegistrationDto;
import factory.UserFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.parent.User;
import models.person.UserType;
import models.rolePrivilege.RolePrivilege;
import java.time.LocalDateTime;
import java.util.List;

public class UserAuthenticationService {

    private final UserDAO userDAO;
    private final RolePrivilegeDAO rolePrivilegeDAO;
    private final ObjectMapper objectMapper;

    public UserAuthenticationService(UserDAO userDAO, RolePrivilegeDAO rolePrivilegeDAO, ObjectMapper objectMapper) {
        this.userDAO = userDAO;
        this.rolePrivilegeDAO = rolePrivilegeDAO;
        this.objectMapper = objectMapper;
    }


    public int login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = parseUserFromRequest(request);

        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");

        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");

        int verifiedId = userDAO.verifyUserPassword(user.getUsername(), user.getPassword(), user.getRole_id());

        if (verifiedId > 0) {
            List<RolePrivilege> privileges = rolePrivilegeDAO.getPrivilegesByRoleId(user.getRole_id());
            return verifiedId;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    private User parseUserFromRequest(HttpServletRequest request) throws Exception {
        UserRegistrationDto dto = objectMapper.readValue(request.getReader(), UserRegistrationDto.class);
        UserType userType = resolveUserType(dto.getRole_id());

        return UserFactory.createUser(userType,
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole_id(),
                dto.getFull_name(),
                dto.getEmail(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                true);
    }

    private UserType resolveUserType(int roleId) {
        UserType userType;

        switch (roleId) {
            case 1:
                userType = UserType.ADMIN;
                break;
            case 2:
                userType = UserType.MANAGER;
                break;
            case 3:
                userType = UserType.STAFF;
                break;
            default:
                throw new IllegalArgumentException("Invalid role_id:");
        };
        return userType;
    }
}
