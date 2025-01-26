package com.blood.bank.Blood.bank.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.DonorService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DonorService donorService;

    public AdminController(DonorService donorService) {
        this.donorService = donorService;
    }

    // View all donors
    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        List<Donor> donors = donorService.getAllDonors();
        model.addAttribute("donors", donors);
        return "admin_dashboard"; // Points to `adminDashboard.html` or `adminDashboard.jsp`
    }

    // Block a donor
    @PostMapping("/donors/{id}/block")
    public String blockDonor(@PathVariable Long id, Model model) {
        donorService.blockDonor(id);
        return RedirectionAndReturn.ADMIN_DASHBOARD_RED;
    }

    // Unblock a donor
    @PostMapping("/donors/{id}/unblock")
    public String unblockDonor(@PathVariable Long id, Model model) {
        donorService.unblockDonor(id);
        return RedirectionAndReturn.ADMIN_DASHBOARD_RED;
        
    }

    // Delete a donor
    @PostMapping("/donors/{id}/delete")
    public String deleteDonor(@PathVariable Long id, Model model) {
        donorService.deleteDonor(id);
        return RedirectionAndReturn.ADMIN_DASHBOARD_RED;
    }

    @GetMapping("/donors/{id}/edit")
    public String editDonor(@PathVariable Long id, Model model) {
        Donor donor = donorService.getDonorById(id);
        model.addAttribute("donor", donor);
        return "editdonor"; 
    }

    // Update donor details - Submit changes
    @PostMapping("/donors/{id}/edit")
    public String updateDonor(@PathVariable Long id, @ModelAttribute Donor updatedDonor, RedirectAttributes redirectAttributes) {
        try{
            donorService.updateDonor(id, updatedDonor);
            redirectAttributes.addFlashAttribute("sMsg","Donor Updated Successfully");
        return RedirectionAndReturn.ADMIN_DASHBOARD_RED;
            

        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("eMsg","Donor Updation Failed");
            return RedirectionAndReturn.ADMIN_DASHBOARD_RED;
            
        }
    }
}

