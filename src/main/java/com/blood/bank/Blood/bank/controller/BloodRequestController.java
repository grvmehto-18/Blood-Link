package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.model.BloodRequest;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.BloodRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    @GetMapping("/new")
    public String showRequestForm(Model model) {
        model.addAttribute("bloodRequest", new BloodRequest());
        return "bloodRequestForm";
    }

    @PostMapping("/new")
    public String createBloodRequest(@ModelAttribute BloodRequest bloodRequest, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            Donor requester = (Donor) authentication.getPrincipal();
            bloodRequest.setRequester(requester);
            bloodRequestService.createBloodRequest(bloodRequest);
            redirectAttributes.addFlashAttribute("sMsg", "Blood request submitted successfully!");
            return RedirectionAndReturn.DONOR_DASHBOARD_RED;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to submit blood request: " + e.getMessage());
            return "redirect:/requests/new";
        }
    }

    @GetMapping("/my-requests")
    public String viewMyRequests(Model model, Authentication authentication) {
        Donor currentUser = (Donor) authentication.getPrincipal();
        List<BloodRequest> myRequests = bloodRequestService.getBloodRequestsByRequester(currentUser.getId());
        model.addAttribute("myRequests", myRequests);
        return "myBloodRequests";
    }

    // Admin/Donor view of all pending requests (can be filtered later)
    @GetMapping("/all")
    public String viewAllRequests(Model model) {
        List<BloodRequest> allRequests = bloodRequestService.getAllBloodRequests();
        model.addAttribute("allRequests", allRequests);
        return "allBloodRequests";
    }

    @PostMapping("/{id}/fulfill")
    public String fulfillRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bloodRequestService.updateBloodRequestStatus(id, "Fulfilled");
            redirectAttributes.addFlashAttribute("sMsg", "Blood request fulfilled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to fulfill blood request: " + e.getMessage());
        }
        return "redirect:/requests/all";
    }

    @PostMapping("/{id}/cancel")
    public String cancelRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bloodRequestService.updateBloodRequestStatus(id, "Cancelled");
            redirectAttributes.addFlashAttribute("sMsg", "Blood request cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("eMsg", "Failed to cancel blood request: " + e.getMessage());
        }
        return "redirect:/requests/all";
    }
}