package mapper;

import dtos.UserDto;
import models.parent.User;
import models.person.Admin;
import models.person.Manager;
import models.person.Staff;
import models.person.UserType;
import models.rolePrivilege.Role;

import java.time.LocalDateTime;

public class UserMapper {

    // Domain -> DTO
    public static UserDto toDTO(User user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFull_name(),
                user.getRole_id(),
                user.isActive()
        );
    }
    // DTO -> Domain (assumes UserType is known)
    public static User toDomain(UserDto userDto, UserType userType) {
        if (userDto == null) return null;

        int id = userDto.getId();
        String username = userDto.getUsername();
        int role_id = userDto.getRole_id();
        String email = userDto.getEmail();
        String fullname = userDto.getFull_name();
        boolean isActive = userDto.isActive();
        LocalDateTime now = LocalDateTime.now();

        switch (userType) {
            case ADMIN:
                return new Admin(
                        id,
                        username,
                        "", // password not available in DTO
                        role_id,
                        fullname,
                        email,
                        now, // createdAt
                        now, // updatedAt
                        null, // lastLogin
                        isActive
                );

            case MANAGER:
                return new Manager(
                        id,
                        username,
                        "",
                        role_id,
                        fullname,
                        email,
                        now,
                        now,
                        null,
                        isActive
                );

            case STAFF:
                return new Staff(
                        id,
                        username,
                        "",
                        role_id,
                        fullname,
                        email,
                        now,
                        now,
                        null,
                        isActive
                );

            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
