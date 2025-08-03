package dao.implementations;

import dao.interfaces.DepartmentDAO;
import models.departmnet.Department;

import java.util.List;

public class PrivilegeDAOImpl implements DepartmentDAO {
    @Override
    public void addDepartment(Department department) throws Exception {

    }

    @Override
    public Department getDepartmentById(int id) throws Exception {
        return null;
    }

    @Override
    public List<Department> getAllDepartments() throws Exception {
        return List.of();
    }

    @Override
    public void updateDepartment(Department department) throws Exception {

    }

    @Override
    public void deleteDepartment(int id) throws Exception {

    }
}
