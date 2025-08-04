package mapper;

import dtos.PrivilegeDTO;
import models.rolePrivilege.Privilege;

public class PrivilegeMapper {

    public static PrivilegeDTO toDTO(Privilege privilege) {
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setId(privilege.getId());
        privilegeDTO.setName(privilege.getName());
        return privilegeDTO;
    }

    public static Privilege toModel(PrivilegeDTO dto) {
        Privilege privilege = new Privilege();
        privilege.setId(dto.getId());
        privilege.setName(dto.getName());
        return privilege;
    }
}