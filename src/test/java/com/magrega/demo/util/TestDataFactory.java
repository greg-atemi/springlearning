package com.magrega.demo.util;

import com.magrega.demo.model.User;
import com.magrega.demo.model.enums.Role;

import java.util.ArrayList;
import java.util.UUID;

public class TestDataFactory {

    public static User buildUser() {
        return User.builder()
                .firstName("Greg")
                .lastName("Atemi")
                .email("greg@systechafrica.com")
                .password("encodedpassword123")
                .role(Role.USER)
                .addressList(new ArrayList<>())
                .build();
    }

    public static User buildUser(String email) {
        return User.builder()
                .firstName("Greg")
                .lastName("Atemi")
                .email(email)
                .password("encodedpassword123")
                .role(Role.USER)
                .addressList(new ArrayList<>())
                .build();
    }

    public static User buildUserWithId() {
        return User.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .firstName("Greg")
                .lastName("Atemi")
                .email("greg@systechafrica.com")
                .password("encodedpassword123")
                .role(Role.USER)
                .addressList(new ArrayList<>())
                .build();
    }
}