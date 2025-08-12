package dao.interfaces;

import models.customer.Customer;
import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer) throws Exception;
    Customer getCustomerById(int id) throws Exception;
    Customer getCustomerByMobileNumber(String mobileNumber) throws Exception;
    List<Customer> getAllCustomers() throws Exception;
    void updateCustomer(Customer customer, int id) throws Exception;
    void deleteCustomer(int id) throws Exception;
    boolean customerExists(int id) throws Exception;
    int getCustomerCount() throws Exception;
}
