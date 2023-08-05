package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.RoleRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.utils.ImageUtils;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setUsername(customerDto.getUsername());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPassword(customerDto.getPassword());
        customer.setRoles(Arrays.asList(roleRepository.findRoleByName("USER")));


        customerRepository.save(customer);
        return mapperDto(customer);
    }

    @Override
    public Customer save(MultipartFile imageCustomer ,Customer customer) {

         try{
            Customer customer1 = customerRepository.findCustomerByUsername(customer.getUsername());
            customer1.setAddress(customer.getAddress());
            customer1.setCity(customer.getCity());
            customer1.setCountry(customer.getCountry());
            customer1.setPhoneNumber(customer.getPhoneNumber());
            customer1.setImage(Base64.getEncoder().encodeToString(imageCustomer.getBytes()));
            customerRepository.save(customer1);
            return customer1;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Customer findByUserName(String username) {
        return customerRepository.findCustomerByUsername(username);
    }

    private CustomerDto mapperDto(Customer customer){
        return  new CustomerDto(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getUsername(),
                customer.getPassword(),
                customer.getPassword()
        );

    }
}
