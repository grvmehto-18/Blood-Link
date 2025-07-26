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
import com.blood.bank.Blood.bank.model.Address;
import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import com.blood.bank.Blood.bank.mapper.DonorMapper;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.DonorService;
import com.blood.bank.Blood.bank.mapper.DonorMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/donor")
public class DonorController {


    private final DonorService donorService;
    private final DonorMapper donorMapper;
    private static final Logger logger = LoggerFactory.getLogger(DonorController.class);

    @GetMapping("/dashboard")
    public String donorDashboard(Model model) {
        logger.info("Donor Dashboard");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Donor authenticatedUser = (Donor) authentication.getPrincipal();
        DonorRegistrationDto donorDto = donorMapper.toDto(authenticatedUser);

        model.addAttribute("donorDto", donorDto);

        return "donor_dashboard";
    }


    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        DonorRegistrationDto donorDto = donorService.getDonorDtoById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        model.addAttribute("donorDto", donorDto);
        return "update";
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PostMapping("/{id}/update")
    public String updateDonorDetails(@PathVariable Long id, @Valid @ModelAttribute("donorDto") DonorRegistrationDto donorDto,
                                     BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Error: " + bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("donorDto", donorDto);
            return "update";
        }

        donorService.updateDonor(id, donorDto);
        redirectAttributes.addFlashAttribute("sMsg", "Donor Updated Successfully");
        return "redirect:/donor/dashboard";
    }

}

