package dao.interfaces;

import models.departmnet.Department;

import java.util.List;

public interface DepartmentDAO {

    void addDepartment(Department department) throws Exception;
    Department getDepartmentById(int id) throws Exception;
    List<Department> getAllDepartments() throws Exception;
    void updateDepartment(Department department) throws Exception;
    void deleteDepartment(int id) throws Exception;
}
