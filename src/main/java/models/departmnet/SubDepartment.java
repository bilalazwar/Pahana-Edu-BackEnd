package models.departmnet;

public class SubDepartment {
    private int id;
    private String name;
    private Department department;

    public SubDepartment(int id, String name, Department department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}