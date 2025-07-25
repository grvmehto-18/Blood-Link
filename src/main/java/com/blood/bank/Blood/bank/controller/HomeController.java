package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blood.bank.Blood.bank.model.Donor;

import com.blood.bank.Blood.bank.model.Address;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/blood")
@Controller
public class HomeController {

    @GetMapping("/admin")
    public String adminPage(){
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
        model.addAttribute("donorRegistrationDto", new DonorRegistrationDto());
        return "donorRegistration";
    }

    @GetMapping("/verify")
    public String getVerificationPage() {
        return "verify"; // Show the verification form
    }

    @GetMapping("/camp")
    public String campPage() {
        return "blood_camp_list";
    }


}
