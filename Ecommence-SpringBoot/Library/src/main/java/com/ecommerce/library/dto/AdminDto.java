package com.ecommerce.library.dto;

import com.ecommerce.library.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    @Size(min = 3, max = 10, message = "Invalid First Name(3-10 characters)")
    private String firstName;

    @Size(min = 3, max = 10, message = "Invalid Last Name(3-10 characters)")
    private String lastName;

    private String username;

    @Size(min = 5, max = 15, message = "Invalid Password(5-15 characters)")
    private String password;

    private String repeatPassword;

    private String image;


}
