package com.blood.bank.Blood.bank.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.blood.bank.Blood.bank.Repository.DonorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonorDetailsService implements UserDetailsService{


    private final DonorRepository repository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        return repository.findByEmail(email)
                         .orElseThrow(()-> new UsernameNotFoundException("User not found "+ email));
    }

}
