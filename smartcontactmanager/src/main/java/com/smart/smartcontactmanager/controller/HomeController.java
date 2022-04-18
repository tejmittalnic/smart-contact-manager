package com.smart.smartcontactmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/home")
    public String handler(Model model){
        model.addAttribute("title","Smart Contact Manager");
        return "home";
    }
}
