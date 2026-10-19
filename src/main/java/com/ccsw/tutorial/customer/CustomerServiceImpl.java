package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Joanne Zamorano
 *
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer get(Long id) {

        return this.customerRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> findAll() {

        return (List<Customer>) this.customerRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, CustomerDto dto) throws Exception {

        Customer customer;

        String customerName = customerNameNormalizado(dto.getName());

        List<Customer> allCustomers = (List<Customer>) this.customerRepository.findAll();

        for(Customer c: allCustomers){
            String nameExist = customerNameNormalizado(c.getName());

            if(nameExist.equals(customerName)){
                if (id == null || !c.getId().equals(id)){
                    throw new Exception("Name exists");
                }
            }
        }

        if (id == null) {
            customer = new Customer();
        } else {
            customer = this.get(id);
        }

        customer.setName(dto.getName());

        this.customerRepository.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if(this.get(id) == null){
            throw new Exception("Not exists");
        }

        this.customerRepository.deleteById(id);
    }


    private String customerNameNormalizado(String name){
        if (name == null){
            return "";
        }
        String customerName = name.toLowerCase().trim();
        customerName = customerName.replace("á","a")
                .replace("é","e")
                .replace("í","i")
                .replace("ó","o")
                .replace("ú","u");
        return customerName;
    }
}
