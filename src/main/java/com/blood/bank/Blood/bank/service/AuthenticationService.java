package com.blood.bank.Blood.bank.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.Repository.RoleRepository;
import com.blood.bank.Blood.bank.dto.VerifyUserDto;
import com.blood.bank.Blood.bank.model.Donor;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {

    private final DonorRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    

    public AuthenticationService(DonorRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public Donor signUP(Donor input){
     
        var userRole = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new IllegalStateException("ROLE USER NOT FOUND"));
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        input.setVerificationCode(generateVerificationCode());
        input.setTime(LocalDateTime.now().plusMinutes(15));
        input.setEnabled(false);
        input.setRoles(Set.of(userRole));
        Donor unVerifiedDonor = userRepository.save(input);
        sendVerificationEmail(input);

        return unVerifiedDonor;

    }


    public void verifyUser(VerifyUserDto verifyUserDto){
        Optional <Donor> optionalUser = userRepository.findByEmail(verifyUserDto.getEmail());

        if(optionalUser.isPresent()){
            Donor user = optionalUser.get();

            if(user.getVerificationCode().equals(verifyUserDto.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setTime(null);

                userRepository.save(user);
            }else{
                throw new RuntimeException("Invalid verification code");
            }
        }else{
            throw new RuntimeException("User Not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<Donor> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()){
            Donor user = optionalUser.get();
            if(user.isEnabled()){
                throw new RuntimeException("User is verified");
            }

            user.setVerificationCode(generateVerificationCode());
            user.setTime(LocalDateTime.now().plusHours(1));

            sendVerificationEmail(user);
            userRepository.save(user);
        }else{
            throw new RuntimeException("User Not Found");
        }
    }

    public void sendVerificationEmail(Donor user){
        String subject = "Account Verification Mail";
        String verificationCode = user.getVerificationCode();

        String htmlMessage = "<html>"+"<body>"
                             +"<h1>This is accout Verification Code: "+verificationCode+"</h1>"
                             +"</body>"+"</html>";

        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        }catch(MessagingException e){
            e.printStackTrace();
        }


    }

    private String generateVerificationCode(){
        Random random = new Random();

        int code = random.nextInt(900000)+100000;

        return String.valueOf(code);
    }
    
}

