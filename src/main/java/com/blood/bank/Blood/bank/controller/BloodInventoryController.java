package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.model.BloodInventory;
import com.blood.bank.Blood.bank.service.BloodInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BloodInventoryController {

    private final BloodInventoryService bloodInventoryService;

    @GetMapping("/list")
    public String listInventory(Model model) {
        List<BloodInventory> inventoryList = bloodInventoryService.getAllInventory();
        model.addAttribute("inventoryList", inventoryList);
        return "bloodInventoryList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bloodInventory", new BloodInventory());
        return "bloodInventoryForm";
    }

    @PostMapping("/add")
    public String addInventory(@ModelAttribute BloodInventory bloodInventory, RedirectAttributes redirectAttributes) {
        try {
            bloodInventoryService.addOrUpdateInventory(bloodInventory);
            redirectAttributes.addFlashAttribute("sMsg", "Blood inventory added/updated successfully!");
            return "redirect:/inventory/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to add/update blood inventory: " + e.getMessage());
            return "redirect:/inventory/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BloodInventory bloodInventory = bloodInventoryService.getInventoryById(id)
                .orElseThrow(() -> new RuntimeException("Blood Inventory not found"));
        model.addAttribute("bloodInventory", bloodInventory);
        return "bloodInventoryForm";
    }

    @PostMapping("/edit/{id}")
    public String updateInventory(@PathVariable Long id, @ModelAttribute BloodInventory bloodInventory, RedirectAttributes redirectAttributes) {
        try {
            bloodInventory.setId(id);
            bloodInventoryService.addOrUpdateInventory(bloodInventory);
            redirectAttributes.addFlashAttribute("sMsg", "Blood inventory updated successfully!");
            return "redirect:/inventory/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to update blood inventory: " + e.getMessage());
            return "redirect:/inventory/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bloodInventoryService.deleteInventory(id);
            redirectAttributes.addFlashAttribute("sMsg", "Blood inventory deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to delete blood inventory: " + e.getMessage());
        }
        return "redirect:/inventory/list";
    }
}