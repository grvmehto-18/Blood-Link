package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.model.BloodCamp;
import com.blood.bank.Blood.bank.service.BloodCampService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bloodcamps")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BloodCampController {

    private final BloodCampService bloodCampService;

    @GetMapping("/list")
    public String listBloodCamps(Model model) {
        List<BloodCamp> bloodCamps = bloodCampService.getAllBloodCamps();
        model.addAttribute("bloodCamps", bloodCamps);
        return "blood_camp_list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bloodCamp", new BloodCamp());
        return "bloodCampForm";
    }

    @PostMapping("/add")
    public String addBloodCamp(@ModelAttribute BloodCamp bloodCamp, RedirectAttributes redirectAttributes) {
        try {
            bloodCampService.saveBloodCamp(bloodCamp);
            redirectAttributes.addFlashAttribute("sMsg", "Blood camp added successfully!");
            return "redirect:/bloodcamps/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to add blood camp: " + e.getMessage());
            return "redirect:/bloodcamps/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BloodCamp bloodCamp = bloodCampService.getBloodCampById(id)
                .orElseThrow(() -> new RuntimeException("Blood Camp not found"));
        model.addAttribute("bloodCamp", bloodCamp);
        return "bloodCampForm";
    }

    @PostMapping("/edit/{id}")
    public String updateBloodCamp(@PathVariable Long id, @ModelAttribute BloodCamp bloodCamp, RedirectAttributes redirectAttributes) {
        try {
            bloodCamp.setId(id.intValue()); // Assuming ID is int in model
            bloodCampService.saveBloodCamp(bloodCamp);
            redirectAttributes.addFlashAttribute("sMsg", "Blood camp updated successfully!");
            return "redirect:/bloodcamps/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to update blood camp: " + e.getMessage());
            return "redirect:/bloodcamps/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBloodCamp(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bloodCampService.deleteBloodCamp(id);
            redirectAttributes.addFlashAttribute("sMsg", "Blood camp deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to delete blood camp: " + e.getMessage());
        }
        return "redirect:/bloodcamps/list";
    }
}