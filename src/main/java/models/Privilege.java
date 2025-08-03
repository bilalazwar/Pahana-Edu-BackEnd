package models;

public class Privilege {
    private Long id;
    private String name;

    public Privilege(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
}
