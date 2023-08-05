package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.websocket.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;


    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }


    @GetMapping("/index")
    public String home(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("adminDtos", new AdminDto());
        return "register";
    }




    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDtos") AdminDto adminDto,
                              BindingResult bindingResult,
                              Model model

                                ){
       try{
           if(bindingResult.hasErrors()){
               model.addAttribute("adminDto", new AdminDto());
               System.out.println(bindingResult.toString());
               return "register";
           }
           String userName = adminDto.getUsername();
           Admin admin = adminService.findAdminByUserName(userName);
           if(!ObjectUtils.isEmpty(admin)){
               model.addAttribute("adminDto", new AdminDto());
               model.addAttribute("emailError", "Your email has been registered !!!");
               System.out.println("Admin not null !!!");
               return "register";
           }

           if (adminDto.getPassword().equalsIgnoreCase(adminDto.getRepeatPassword())) {
               System.out.println("Register successfully !!!");
               adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
               adminService.save(adminDto);
               model.addAttribute("adminDto", new AdminDto());
               model.addAttribute("success", "Register successfully !!!");
           } else {
               System.out.println("Password is not same !!!");
               model.addAttribute("adminDto", new AdminDto());
               model.addAttribute("passwordError", "Password is not same  !!!");
           }
           return "register";

       }catch (Exception e){
           e.printStackTrace();
           model.addAttribute("errors", "Can not register because error sever !!!");

       }
        return  "register";
    }


    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        return "forgot-password";
    }


}
