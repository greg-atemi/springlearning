package com.magrega.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController
{
    @RequestMapping("/login")
    public String greet()
    {
        return "Login Page!";
    }

}
