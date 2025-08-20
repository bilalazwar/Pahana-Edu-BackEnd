package services.customerService;

import dao.interfaces.CustomerDAO;
import models.customer.Customer;

import java.util.List;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void addCustomer(Customer customer) throws Exception {

        if (customer == null) {
            throw new IllegalArgumentException("Customer details are empty");
        }

        // Validate name
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        // Validate email (optional, depending on your logic)
        if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!customer.getEmail().matches(emailRegex)) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }

        // Validate phone number
        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }

        // Validate address
        if (customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer address is required");
        }

        // If all validations pass, proceed to DAO
        customerDAO.addCustomer(customer);
    }

    public Customer getCustomerById(int id) throws Exception {

        if(customerExists(id)){
            return customerDAO.getCustomerById(id);
        }
        else{
            throw new IllegalArgumentException("Invalid Customer Id.");
        }
    }
    public Customer getCustomerByMobileNumber(String mobileNumber) throws Exception {

//        System.out.println("Searching for customer by mobile number : " + mobileNumber);
        if (mobileNumber == null || !mobileNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid mobile number. It must be exactly 10 digits.");
        }

        return customerDAO.getCustomerByMobileNumber(mobileNumber);
    }

    public List<Customer> getAllCustomer() throws Exception {

        return customerDAO.getAllCustomers();
    }

    public void updateCustomer(Customer customer,int id) throws Exception {
        customerDAO.updateCustomer(customer, id);
    }

    public void deleteCustomer(int id) throws Exception {

        customerDAO.deleteCustomer(id);
    }


    public boolean customerExists(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Product Id.");
        }
        return customerDAO.customerExists(id);
    }

    public int getCustomerCount() throws Exception {
        return customerDAO.getCustomerCount();
    }
}
