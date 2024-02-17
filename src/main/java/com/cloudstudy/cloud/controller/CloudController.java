package com.cloudstudy.cloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cloudStudy")
public class CloudController {



    @GetMapping("/v1")
    public String cloudStudy(){

        return "main";
    }
}
