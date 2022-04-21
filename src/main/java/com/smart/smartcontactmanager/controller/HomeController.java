package com.smart.smartcontactmanager.controller;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.Contact;
import com.smart.smartcontactmanager.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private String userEmail;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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

    @GetMapping("/logout")
    public String logout(){
        userEmail="";
        return "signin";
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
        if(user.getRole().equals("ROLE_ADMIN")){
            if(user.getPassword().equals(u.getPassword()))
                return "admin/admin_dashboard";
        }
        if(user.getRole().equals("ROLE_USER")){
            if(user.getPassword().equals(u.getPassword())){
                userEmail = user.getEmail();
                return "normal/user_dashboard";
            }
            //return "redirect:/user/index";
            
        }
        return "signin";
    }

    @PostMapping("/do_signup")
    public String registerUser(@ModelAttribute("user") User user, Model model, HttpSession session){
        user.setRole("ROLE_USER");
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

    @GetMapping("/addContact")
    public String addContact(Model model){
        model.addAttribute("title","Add Contact");
        model.addAttribute("contact",new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact){
        User u = userRepository.getUserByUserName(userEmail);
        contact.setUser(u);
        u.getContacts().add(contact);
        userRepository.save(u);
        return "normal/add_contact_form";
    } 

    @GetMapping("/view-contact")
    public String viewContacts(Model model){

        //find contact list by


        User u = userRepository.getUserByUserName(userEmail);
        List<Contact> list = u.getContacts();
        model.addAttribute("contact_list", list);
        return "normal/view_contacts";
    }

    @GetMapping("/delete_contact/{cid}")
    public String deleteContact(@PathVariable("cid") int cid){
        this.contactRepository.deleteById(cid);
        return "redirect:/view-contact";
    }

    @GetMapping("/update_contact/{cid}")
    public String updateContact(@PathVariable("cid") int cid, Model model){
        Optional<Contact> optional = this.contactRepository.findById(cid);
        model.addAttribute("contact", optional.get());
        return "normal/update_contact_form";
    }

    @PostMapping("/do-update-contact/{cid}")
    public String doUpdate(@ModelAttribute("contact") Contact contact, @PathVariable("cid") int cid,Model model){
        System.out.println("-=-=-=-=-============= "+contact.getName());
        this.contactRepository.deleteById(cid);
        contact.setCid(cid);
        this.contactRepository.save(contact);
        return "redirect:/view-contact";
    }
}
