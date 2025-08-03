package dao.interfaces;

import models.rolePrivilege.Privilege;

import java.util.List;

public interface PrivilegeDAO {

    void addPrivilege(Privilege privilege) throws Exception;
    Privilege getPrivilegeById(int id) throws Exception;
    List<Privilege> getAllPrivileges() throws Exception;
    void updatePrivilege(Privilege privilege) throws Exception;
    void deletePrivilege(int id) throws Exception;
}
