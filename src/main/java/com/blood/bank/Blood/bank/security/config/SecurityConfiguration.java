package com.blood.bank.Blood.bank.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.blood.bank.Blood.bank.security.handler.SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        
        httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authorize -> {
                        authorize
                                .requestMatchers("/", "/auth/**", "/search/**","/blood/**", "/css/**","/images/**")
                                .permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/donor/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/login","/logout").permitAll()
                                .anyRequest()
                                .authenticated();
                    })
                    .formLogin(
                        formLogin -> formLogin.loginPage("/login")
                                               .successHandler(new SuccessHandler())
                                               .permitAll()
                    );
                   
                    
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://backend.com","https://localhost:8080"
        ));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    

}

