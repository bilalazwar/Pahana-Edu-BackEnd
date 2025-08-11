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

        if(customer == null){
            throw new Exception("Product is null");
        }
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
