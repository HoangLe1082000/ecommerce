package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("customerDto", new CustomerDto());
        return "sign-in";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("customerDto", new CustomerDto());
        return "signup";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "signup";
        }
        try{
            Customer customer = customerService.findByUserName(customerDto.getUsername());
            if(customer == null){
                if(customerDto.getPassword().equals(customerDto.getRepeatPassword())){
                    customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                    customerService.save(customerDto);
                    model.addAttribute("success", "Register Successfully !!!");
                    model.addAttribute("customerDto", new CustomerDto());
                    return "signup";
                }
                model.addAttribute("customerDto", new CustomerDto());
                model.addAttribute("password", "Password is not same !!!");
            }else {
                model.addAttribute("customerDto", new CustomerDto());
                model.addAttribute("username", "User have been registered !!!");
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("error", "Server has error !!!");
            model.addAttribute("customerDto", new CustomerDto());
        }

        return "signup";
    }


}
