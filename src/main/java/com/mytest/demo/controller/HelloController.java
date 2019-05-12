package com.mytest.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Gladle!";
    }
}