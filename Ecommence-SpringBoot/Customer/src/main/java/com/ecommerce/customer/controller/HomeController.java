package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.*;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.OrderService;
import com.ecommerce.library.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;


    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String index(Model model, Principal principal , HttpSession session){
        List<Category> categories = categoryService.findAll();
        List<ProductDto> productDtos = productService.findAll();

        if(principal != null){
            String image = customerService.findByUserName(principal.getName()).getImage();
            session.setAttribute("image", image);
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUserName(principal.getName());
            ShoppingCart shoppingCart = customer.getShoppingCart();


            if(shoppingCart != null){
                session.setAttribute("shoppingCart", shoppingCart);
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }else {
                session.setAttribute("shoppingCart", new ShoppingCart());
                session.setAttribute("totalItems", "0");
            }
        }else {
            session.setAttribute("image", null);
            session.setAttribute("shoppingCart", new ShoppingCart());
        }

        List<Order> orders = orderService.getAll();

        List<OrderDetails> orderDetails = new ArrayList<>();
        List<Product> productsBestSell = new ArrayList<>();


        for(OrderDetails orderDetail : orderDetails){
            productsBestSell.add(orderDetail.getProduct());
        }

        model.addAttribute("productsBestSell",productsBestSell);

        model.addAttribute("categories", categories);
        model.addAttribute("products", productDtos);
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal , HttpSession session){
        List<Category> categories = categoryService.findAll();
        List<ProductDto> productDtos = productService.findAll();

        if(principal != null){
            session.setAttribute("username", principal.getName());
            Customer customer = customerService.findByUserName(principal.getName());
            ShoppingCart shoppingCart = customer.getShoppingCart();
            session.setAttribute("totalItems", shoppingCart.getTotalItems());


        }

        productDtos.stream().map(ProductDto::getName).forEach(System.out::println);


        model.addAttribute("categories", categories);
        model.addAttribute("products", productDtos);
        return "home";
    }
}
