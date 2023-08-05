package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Order;
import com.ecommerce.library.service.MailService;
import com.ecommerce.library.service.OrderService;
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
    private  MailService mailService;

    @Autowired
    private OrderService orderService;

    public OrderController() {
    }

    @GetMapping("/orders")
    public String index(Model model){
        List<Order> orderList = orderService.getAll();

        model.addAttribute("orders", orderList);

        return "orders";
    }

    @GetMapping("/reject-order/{id}")
    public String cancelOrder(@PathVariable("id") Long id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        orderService.rejectOrder(id);
        return "redirect:/orders";
    }

    @GetMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable("id") Long id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        try{
            mailService.sendMail(id);
            orderService.acceptOrder(id);
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/orders";
        }
        return "redirect:/orders";
    }

}
