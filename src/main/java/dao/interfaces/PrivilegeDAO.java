package dao.interfaces;

import dtos.PrivilegeDTO;
import models.rolePrivilege.Privilege;

import java.util.List;

public interface PrivilegeDAO {

    void addPrivilege(Privilege privilege) throws Exception;
    Privilege getPrivilegeById(int id) throws Exception;
    List<Privilege> getAllPrivileges() throws Exception;
    int updatePrivilege(PrivilegeDTO privilegeDTO) throws Exception;
    int deletePrivilegeByID(int id) throws Exception;
}
