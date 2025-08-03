package dao.interfaces;

import models.departmnet.SubDepartment;

import java.util.List;

public interface SubDepartmentDAO {

    void addSubDepartment(SubDepartment subDepartment) throws Exception;
    SubDepartment getSubDepartmentById(int id) throws Exception;
    List<SubDepartment> getAllSubDepartments() throws Exception;
    void updateSubDepartment(SubDepartment subDepartment) throws Exception;
    void deleteSubDepartment(int id) throws Exception;
}
