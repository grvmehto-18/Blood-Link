package com.blood.bank.Blood.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.blood.bank.Blood.bank.model.Donor;

@Controller
public class IndexController {

     @GetMapping("/")
    public String indexPage(){
        return "index";
    }

    @GetMapping("/regiPage")
    public String openRegister(Model model){
        model.addAttribute("donor",new Donor());
        return "donorRegistration";
    }

    @GetMapping("/login")
    public String openLoginPage(){
        return "login"; 
    }
}
