package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;


    @Autowired
    private OrderService orderService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        double tax = 0.0d;
        if(customer.getPhoneNumber() == null && customer.getAddress() == null & customer.getCity() == null && customer.getCountry() == null){
            model.addAttribute("customer", customer);
            return "account";
        }


        ShoppingCart shoppingCart = customer.getShoppingCart();

        if(shoppingCart.getCartItem().size() == 0){
            model.addAttribute("tax", "0$");
        }else {
            tax =  shoppingCart.getTotalPrice() * 10 / 100;
            model.addAttribute("tax", tax);
        }

        model.addAttribute("customer", customer);
        model.addAttribute("shoppingCart", shoppingCart);
        model.addAttribute("grandTotal", shoppingCart.getTotalPrice() + tax);
        return "checkout";
    }


    @GetMapping("/order")
    public String order(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        List<Order> orders = customer.getOrders();

        model.addAttribute("orders", orders);

        return "order";
    }


    @GetMapping("/save-order")
    public String saveOrder(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        ShoppingCart shoppingCart = customer.getShoppingCart();
        orderService.saveOrder(shoppingCart);

        return "redirect:/order";
    }

    @GetMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable("id") Long id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.cancelOrder(id);
        return "redirect:/order";
    }
}
