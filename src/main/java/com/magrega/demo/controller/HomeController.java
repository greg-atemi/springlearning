package com.magrega.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
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
