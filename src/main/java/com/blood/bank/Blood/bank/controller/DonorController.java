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
        DonorRegistrationDto donorDto = new DonorRegistrationDto();
        donorDto.setId(authenticatedUser.getId());
        donorDto.setFullName(authenticatedUser.getFullName());
        donorDto.setEmail(authenticatedUser.getEmail());
        donorDto.setPhoneNumber(authenticatedUser.getPhoneNumber());
        donorDto.setBloodGroup(authenticatedUser.getBloodGroup());
        donorDto.setOccupation(authenticatedUser.getOccupation());
        donorDto.setDateOfBirth(authenticatedUser.getDateOfBirth());
        donorDto.setLastDonateDate(authenticatedUser.getLastDonateDate());
        donorDto.setGender(authenticatedUser.getGender());
        donorDto.setHasAllergies(authenticatedUser.isHasAllergies());
        donorDto.setHasBleedingDisorders(authenticatedUser.isHasBleedingDisorders());
        donorDto.setHasCardiacConditions(authenticatedUser.isHasCardiacConditions());
        donorDto.setHasDiseases(authenticatedUser.isHasDiseases());
        donorDto.setHasHIV(authenticatedUser.isHasHIV());

        if (!authenticatedUser.getAddresses().isEmpty()) {
            Address address = authenticatedUser.getAddresses().iterator().next();
            donorDto.setCountry(address.getCountry());
            donorDto.setState(address.getState());
            donorDto.setDistrict(address.getDistrict());
            donorDto.setCity(address.getCity());
            donorDto.setStreetAddress(address.getStreetAddress());
            donorDto.setZipCode(address.getZipCode());
            donorDto.setLandmark(address.getLandmark());
        }

        model.addAttribute("donorDto", donorDto);

        return "donor_dashboard";
    }


    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Donor donor = donorService.getDonorById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        DonorRegistrationDto donorDto = new DonorRegistrationDto();
        donorDto.setId(donor.getId());
        donorDto.setFullName(donor.getFullName());
        donorDto.setEmail(donor.getEmail());
        donorDto.setPhoneNumber(donor.getPhoneNumber());
        donorDto.setBloodGroup(donor.getBloodGroup());
        donorDto.setOccupation(donor.getOccupation());
        donorDto.setDateOfBirth(donor.getDateOfBirth());
        donorDto.setLastDonateDate(donor.getLastDonateDate());
        donorDto.setGender(donor.getGender());
        donorDto.setHasAllergies(donor.isHasAllergies());
        donorDto.setHasBleedingDisorders(donor.isHasBleedingDisorders());
        donorDto.setHasCardiacConditions(donor.isHasCardiacConditions());
        donorDto.setHasDiseases(donor.isHasDiseases());
        donorDto.setHasHIV(donor.isHasHIV());
        donorDto.setUsername(donor.getUsername());
        donorDto.setPassword(donor.getPassword());

        if (!donor.getAddresses().isEmpty()) {
            Address address = donor.getAddresses().iterator().next();
            donorDto.setCountry(address.getCountry());
            donorDto.setState(address.getState());
            donorDto.setDistrict(address.getDistrict());
            donorDto.setCity(address.getCity());
            donorDto.setStreetAddress(address.getStreetAddress());
            donorDto.setZipCode(address.getZipCode());
            donorDto.setLandmark(address.getLandmark());
        }

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

        Donor existingDonor = donorService.getDonorById(id)
                .orElseThrow(() -> new DonorNotFoundException("Donor not found with id: " + id));

        existingDonor.setFullName(donorDto.getFullName());
        existingDonor.setGender(donorDto.getGender());
        existingDonor.setDateOfBirth(donorDto.getDateOfBirth());
        existingDonor.setBloodGroup(donorDto.getBloodGroup());
        existingDonor.setPhoneNumber(donorDto.getPhoneNumber());
        existingDonor.setEmail(donorDto.getEmail());
        existingDonor.setOccupation(donorDto.getOccupation());
        existingDonor.setLastDonateDate(donorDto.getLastDonateDate());
        existingDonor.setHasDiseases(donorDto.isHasDiseases());
        existingDonor.setHasAllergies(donorDto.isHasAllergies());
        existingDonor.setHasCardiacConditions(donorDto.isHasCardiacConditions());
        existingDonor.setHasBleedingDisorders(donorDto.isHasBleedingDisorders());
        existingDonor.setHasHIV(donorDto.isHasHIV());

        if (existingDonor.getAddresses().isEmpty()) {
            existingDonor.getAddresses().add(new Address());
        }
        Address address = existingDonor.getAddresses().iterator().next();
        address.setCountry(donorDto.getCountry());
        address.setState(donorDto.getState());
        address.setDistrict(donorDto.getDistrict());
        address.setCity(donorDto.getCity());
        address.setStreetAddress(donorDto.getStreetAddress());
        address.setZipCode(donorDto.getZipCode());
        address.setLandmark(donorDto.getLandmark());
        address.setDonor(existingDonor);


        donorService.updateDonor(id, existingDonor);
        redirectAttributes.addFlashAttribute("sMsg", "Donor Updated Successfully");
        return "redirect:/donor/dashboard";
    }

}

