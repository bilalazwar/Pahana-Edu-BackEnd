package models.product;

import models.departmnet.SubDepartment;

public class Product {
    private int id;
    private String name;
    private double price;
    private String type;
    private int quantity;
    private SubDepartment subDepartment;

    public Product(int id, String name, String type,int quantity, double price, SubDepartment subDepartment) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.subDepartment = subDepartment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SubDepartment getSubDepartment() {
        return subDepartment;
    }

    public void setSubDepartment(SubDepartment subDepartment) {
        this.subDepartment = subDepartment;
    }
}
