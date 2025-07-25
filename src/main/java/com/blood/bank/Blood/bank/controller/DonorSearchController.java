package com.blood.bank.Blood.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        public String searchPage(Model model){
            model.addAttribute("countries", donorService.findDistinctCountries());
            return "search";
        }

        @GetMapping("/states")
        @ResponseBody
        public List<String> getStatesByCountry(@RequestParam String country) {
            return donorService.findDistinctStatesByCountry(country);
        }

        @GetMapping("/districts")
        @ResponseBody
        public List<String> getDistrictsByCountryAndState(@RequestParam String country, @RequestParam String state) {
            return donorService.findDistinctDistrictsByCountryAndState(country, state);
        }

        @GetMapping("/cities")
        @ResponseBody
        public List<String> getCitiesByCountryAndStateAndDistrict(@RequestParam String country, @RequestParam String state, @RequestParam String district) {
            return donorService.findDistinctCitiesByCountryAndStateAndDistrict(country, state, district);
        }

        @GetMapping("/search")
        public String searchDonors(@RequestParam String bloodGroup,
                                   @RequestParam(value = "country", required = false) String country,
                                   @RequestParam(value = "state", required = false) String state,
                                   @RequestParam(value = "district", required = false) String district,
                                   @RequestParam(value = "city", required = false) String city,
                                   @RequestParam(value = "lastDonateDate", required = false) String lastDonateDate,
                                   @RequestParam(value = "availability", required = false) String availability,
                                   @RequestParam(value = "gender", required = false) String gender,
                                   @RequestParam(value = "minAge", required = false) Integer minAge,
                                   @RequestParam(value = "maxAge", required = false) Integer maxAge,
                                   Model model) {
            List<Donor> donors = donorService.searchDonors(bloodGroup, country, state, district, city, lastDonateDate, availability, gender, minAge, maxAge);
            model.addAttribute("donors", donors);
            return "search_result";
        }
    }
