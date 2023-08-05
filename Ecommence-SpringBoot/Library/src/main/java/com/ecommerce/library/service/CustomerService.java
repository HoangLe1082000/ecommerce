package com.ecommerce.library.service;


import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomerService {

    CustomerDto save(CustomerDto customerDto);

    Customer save(MultipartFile multipartFile,Customer customer) throws IOException;

    Customer findByUserName(String username);
}
