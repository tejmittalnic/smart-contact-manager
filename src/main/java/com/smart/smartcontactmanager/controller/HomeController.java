package com.smart.smartcontactmanager.controller;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public String handler(Model model){
        model.addAttribute("title","Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("title","Smart Contact Manager");
        return "signup";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        Contact contact = new Contact();
        contact.setName("TPM");
        contactRepository.save(contact);
        return "success";
    }
}
