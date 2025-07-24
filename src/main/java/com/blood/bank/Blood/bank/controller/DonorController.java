package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.exception.DonorNotFoundException;
import com.blood.bank.Blood.bank.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private static final Logger logger = LoggerFactory.getLogger(DonorController.class);

    @GetMapping("/dashboard")
    public String donorDashboard(Model model) {
        logger.info("Donor Dashboard");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Donor authenticatedUser = (Donor) authentication.getPrincipal();
        model.addAttribute("authenticatedUser", authenticatedUser);

        return "donor_dashboard";
    }


    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Donor donor = donorService.getDonorById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));
        model.addAttribute("donor", donor);
        return "update";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PostMapping("/{id}/update")
    public String updateDonorDetails(@PathVariable Long id, @Valid @ModelAttribute("donor") Donor updatedDonor,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "update";
        }

        donorService.updateDonor(id, updatedDonor);
        redirectAttributes.addFlashAttribute("sMsg", "Donor Updated Successfully");
        return RedirectionAndReturn.DONOR_DASHBOARD_RED;
    }

}

