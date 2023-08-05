package com.ecommerce.customer.controller;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CartController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart = customer.getShoppingCart();
        double tax = 0.0d;
        if(shoppingCart == null){
            model.addAttribute("check", "No items in your cart !!!");
            model.addAttribute("tax", "0$");
        }else {
            tax =  shoppingCart.getTotalPrice() * 10 / 100;
            model.addAttribute("tax", tax);
        }

        model.addAttribute("grandTotal", shoppingCart.getTotalPrice() + tax);
        model.addAttribute("shoppingCart", shoppingCart);

        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            Principal principal,
            Model model,
            HttpServletRequest request){

            if(principal == null ){
                return "redirect:/login";
            }
        Product product = productService.getProductById(productId);
        Customer customer = customerService.findByUserName(principal.getName());
        ShoppingCart shoppingCart =  shoppingCartService.addItemToCart(product, quantity, customer);
       System.out.println("Request : " + request.getHeader("Referer"));
        return "redirect:" +  request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", params = "action=update", method = RequestMethod.POST)
    public  String updateCart(@RequestParam("quantity") int quantity,
                              @RequestParam("id") Long productId,
                              RedirectAttributes model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        Product product = productService.getProductById(productId);
        ShoppingCart shoppingCart = shoppingCartService.updateItemToCart(product, quantity, customer);

        model.addFlashAttribute("shoppingCart", shoppingCart);

        return "redirect:/cart";
    }

    @RequestMapping(value = "/update-cart", params = "action=delete", method = RequestMethod.POST)
    public  String deleteCart(@RequestParam("quantity") int quantity,
                              @RequestParam("id") Long productId,
                              RedirectAttributes model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }

        String username = principal.getName();
        Customer customer = customerService.findByUserName(username);
        Product product = productService.getProductById(productId);
        ShoppingCart shoppingCart = shoppingCartService.deleteItemToCart(product, quantity, customer);

        model.addFlashAttribute("shoppingCart", shoppingCart);

        return "redirect:/cart";
    }
}
