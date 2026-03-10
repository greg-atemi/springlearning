package com.magrega.demo.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Integer addressId;
}
