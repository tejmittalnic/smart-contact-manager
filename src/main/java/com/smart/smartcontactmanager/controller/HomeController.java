package com.smart.smartcontactmanager.controller;

import javax.servlet.http.HttpSession;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/signin")
    public String signin(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("title","Smart Contact Manager");
        return "signin";
    }

    @PostMapping("/do_login")
    public String signin(@ModelAttribute("user") User user, Model model){
        User u = userRepository.getUserByUserName(user.getEmail());
        model.addAttribute("user", u);
        if(user.getRole().equals("ADMIN_ROLE")){
            if(user.getPassword().equals(u.getPassword()))
                return "admin/admin_dashboard";
        }
        if(user.getRole().equals("USER_ROLE")){
            if(user.getPassword().equals(u.getPassword()))
                return "normal/user_dashboard";
        }
        return "signin";
    }

    @PostMapping("/do_signup")
    public String registerUser(@ModelAttribute("user") User user, Model model, HttpSession session){
        user.setRole("USER_ROLE");
        user.setEnabled(true);
        userRepository.save(user);
        model.addAttribute("user", new User());
        session.setAttribute("Registered Successfully.", "alert-success");
        return "signin";
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
