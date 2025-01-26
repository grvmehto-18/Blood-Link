package com.blood.bank.Blood.bank.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.DonorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/donor")
public class DonorController {


    private final DonorService donorService;

    @GetMapping("/dashboard")
    public String donorDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login"; 
        }
        
        Donor authenticatedUser = (Donor) authentication.getPrincipal();
        model.addAttribute("authenticatedUser", authenticatedUser);
        
        return "donor_dashboard"; 
    }
    

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        
        Donor donor = donorService.getDonorById(id);
        model.addAttribute("donor", donor);
        return "update"; 
    }

    @PostMapping("/{id}/update")
    public String updateDonorDetails(@ModelAttribute Donor updatedDonor,@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try{
            donorService.updateDonor(id, updatedDonor);
            redirectAttributes.addFlashAttribute("sMsg","Donor Updated Successfully");
            return "redirect:/admin/dashboard";

        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("eMsg","Donor Updation Failed");
            return "redirect:/admin/dashboard";
        }
    }

}

