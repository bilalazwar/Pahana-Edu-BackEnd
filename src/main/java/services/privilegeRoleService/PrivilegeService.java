package services.privilegeRoleService;

import dao.implementations.PrivilegeDAOImpl;
import dao.interfaces.PrivilegeDAO;
import dtos.PrivilegeDTO;
import mapper.PrivilegeMapper;
import models.rolePrivilege.Privilege;

import java.util.ArrayList;
import java.util.List;

public class PrivilegeService {

    private final PrivilegeDAO privilegeDAO;

    // Constructor injection (Manual Dependency Injection)
    public PrivilegeService(PrivilegeDAO privilegeDAO) {
        this.privilegeDAO = privilegeDAO;
    }

    public String addPrivilege(PrivilegeDTO privilegeDTO) throws Exception {

        String name = privilegeDTO.getName();

        if (name != null && name.trim().length() > 3) {

            Privilege  privilege = PrivilegeMapper.toModel(privilegeDTO);
            privilegeDAO.addPrivilege(privilege);
            return "Successfully added a privilege";
        }
        else {
            return "unable to add privilege";
        }

    }

    public List<PrivilegeDTO> getAllPrivileges() throws Exception {

        List<Privilege> privileges = privilegeDAO.getAllPrivileges();
        List<PrivilegeDTO> privilegeDTOs = new ArrayList<>();

        for (Privilege privilege : privileges) {
            privilegeDTOs.add(PrivilegeMapper.toDTO(privilege));
        }

        return privilegeDTOs;
    }

    public String updatePrivilege(PrivilegeDTO privilegeDTO) throws Exception {

        String name = privilegeDTO.getName();

        if (name != null && name.trim().length() > 3) {

            privilegeDAO.updatePrivilege(privilegeDTO);
            return "Successfully updated a privilege";
        }
        else {
            return "unable to add privilege";
        }

    }

    public String deletePrivilegeById(int id) throws Exception {

        int returnValue = privilegeDAO.deletePrivilegeByID(id);

        if (returnValue == 1) {
            return "Successfully deleted";
        }
        else  {
            return "unable to delete privilege";
        }

    }

    public Privilege getPrivilegeById(int id) throws Exception {

        System.out.println("Value ====== "+ privilegeDAO.getPrivilegeById(id));
        return privilegeDAO.getPrivilegeById(id);
    }
}
