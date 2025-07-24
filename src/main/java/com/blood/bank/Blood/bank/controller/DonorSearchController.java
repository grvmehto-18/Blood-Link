package com.blood.bank.Blood.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.DonorService;

import java.util.List;

    @RequestMapping("/search")
    @Controller
    public class DonorSearchController {

        private final DonorService donorService;

        public DonorSearchController(DonorService donorService) {
            this.donorService = donorService;
        }

        @GetMapping("/searchPage")
        public String searchPage(){
            return "search";
        }
        @GetMapping("/search")
        public String searchDonors(@RequestParam String bloodGroup,
                                   @RequestParam("address") String location, Model model) {
            List<Donor> donors = donorService.searchDonors(bloodGroup, location);
            model.addAttribute("donors", donors);
            return "search_result";
        }
    }
