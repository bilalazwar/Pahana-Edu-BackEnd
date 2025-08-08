package mapper;

import dtos.UserDto;
import models.parent.User;
import models.person.Admin;
import models.person.Manager;
import models.person.Staff;
import models.person.UserType;
import models.rolePrivilege.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    // Domain -> DTO
    public static UserDto toDTO(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFull_name(user.getFull_name());
        dto.setRole_id(user.getRole_id());
        dto.setActive(user.isActive());

        return dto;
    }

    public static List<UserDto> toDTO(List<User> users) {
        if (users == null) return null;

        List<UserDto> dtoList = new ArrayList<>();

        for (User user : users) {
            dtoList.add(toDTO(user));
        }
        return dtoList;
    }




    // DTO -> Domain (assumes UserType is known)
    public static User toDomain(UserDto userDto, UserType userType) {
        if (userDto == null) return null;

        User user;

        switch (userType) {
            case ADMIN:
                user = new Admin();
                break;
            case MANAGER:
                user = new Manager();
                break;
            case STAFF:
                user = new Staff();
                break;
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(""); // password not available in DTO
        user.setRole_id(userDto.getRole_id());
        user.setFull_name(userDto.getFull_name());
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setLastLogin(null);
        user.setActive(userDto.isActive());

        return user;
    }
}
