package com.magrega.demo.dto.user;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String phoneNumber;
}