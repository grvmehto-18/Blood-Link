package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.model.Donation;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.DonationService;
import com.blood.bank.Blood.bank.service.DonorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService donationService;
    private final DonorService donorService;

    @GetMapping("/record")
    @PreAuthorize("hasRole('ADMIN')")
    public String showRecordForm(Model model) {
        model.addAttribute("donation", new Donation());
        model.addAttribute("donors", donorService.getAllDonors()); // For selecting donor
        return "recordDonationForm";
    }

    @PostMapping("/record")
    @PreAuthorize("hasRole('ADMIN')")
    public String recordDonation(@ModelAttribute Donation donation, RedirectAttributes redirectAttributes) {
        try {
            donationService.recordDonation(donation);
            redirectAttributes.addFlashAttribute("sMsg", "Donation recorded successfully!");
            return "redirect:/donations/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to record donation: " + e.getMessage());
            return "redirect:/donations/record";
        }
    }

//    @GetMapping("/list")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String listAllDonations(Model model) {
////        List<Donation> donations = donationService.findAll();
//        model.addAttribute("donations", donations);
//        return "donationList";
//    }

    @GetMapping("/history/{donorId}")
    @PreAuthorize("hasRole('ADMIN') or (#donorId == authentication.principal.id)")
    public String viewDonorDonationHistory(@PathVariable Long donorId, Model model) {
        Donor donor = donorService.getDonorById(donorId).orElse(null);
        List<Donation> donations = donationService.getDonationsByDonor(donorId);
        boolean eligible = donationService.isDonorEligible(donorId);

        model.addAttribute("donor", donor);
        model.addAttribute("donations", donations);
        model.addAttribute("eligible", eligible);
        return "donorDonationHistory";
    }
}