package com.ecommerce.customer.controller;

import com.ecommerce.library.model.City;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CityService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CityService cityService;

    @GetMapping("/account")
    public String checkOut(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        Customer customer = customerService.findByUserName(principal.getName());
        List<City> cities = cityService.gelAllCity();

        model.addAttribute("cities", cities);
        model.addAttribute("customer", customer);

        return "account";
    }

    @RequestMapping(value = "/update-account", method = {RequestMethod.GET, RequestMethod.PUT,RequestMethod.POST})
    public String updateAccount(Model model, Principal principal, RedirectAttributes redirectAttributes,
                                @ModelAttribute("customer") Customer customer,
                                @RequestParam(value = "imageCustomer", required = false) MultipartFile imageProduct){
        if(principal == null){
            return "redirect:/login";
        }
        try{
            Customer customerSave =  customerService.save(imageProduct,customer);
            redirectAttributes.addFlashAttribute("customer", customerSave);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/account";
        }




        return "redirect:/account";
    }
}
