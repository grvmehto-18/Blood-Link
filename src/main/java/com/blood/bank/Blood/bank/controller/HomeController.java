package com.blood.bank.Blood.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blood.bank.Blood.bank.model.Donor;

@RequestMapping("/blood")
@Controller
public class HomeController {

    @GetMapping("/admin")
    public String loginPage(){
        return "admin";
    }


    @GetMapping("/about")
    public String aboutPage(){
        return "About_us";
    }

    @GetMapping("/contactUs")
    public String contactUsPage(){
        return "contact_us";
    }

    
    @GetMapping("/regiPage")
    public String openRegister(Model model){
        model.addAttribute("donor",new Donor());
        return "donorRegistration";
    }

    @GetMapping("/verify")
    public String getVerificationPage(Model model) {
        String email = (String)model.getAttribute("email");
        if(email == null){
            model.addAttribute("eMsg","Email is missing. Please enter your email then submit the verification code");
            return "verify";
        }
        return "verify"; // Show the verification form
    }


}
