package com.ecommerce.admin.config;

import com.ecommerce.library.model.Admin;
import com.ecommerce.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import javax.print.DocFlavor;
import java.util.stream.Collectors;

public class AdminDetailService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findAdminByUsername(username);
        if(ObjectUtils.isEmpty(admin)){
            throw new UsernameNotFoundException("User not found !!!");
        }
        return new User(
                admin.getUsername(),
                admin.getPassword(),
                admin.getRoles().stream().
                        map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }




}
