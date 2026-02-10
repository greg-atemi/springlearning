package com.magrega.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController
{
    @RequestMapping("/")
    public String greet()
    {
        return "Welcome to Magrega!";
    }

    @RequestMapping("/about-us")
    public String about()
    {
        return "We are a PLC";
    }
}
