package com.blood.bank.Blood.bank.security.config;

import java.util.List;
import com.blood.bank.Blood.bank.security.config.handler.FailureHandler;
import com.blood.bank.Blood.bank.security.config.handler.SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final SuccessHandler successHandler;
    private final FailureHandler failureHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        
        httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> {
                        authorize
                                .requestMatchers("/", "/auth/**", "/search/**","/blood/**", "/images/**","/css/**","/js/**","/auth/forgot-password", "/auth/reset-password")
                                .permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/donor/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/login","/logout").permitAll()
                                .anyRequest()
                                .authenticated();
                    })
                    .formLogin(
                        formLogin -> formLogin.loginPage("/login")
                                               .successHandler(successHandler)
                                               .failureHandler(failureHandler)
                    );
                   
                    
        return httpSecurity.build();
    }


    @Bean
    public CorsConfigurationSource configurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "https://backend.com","https://localhost:8080/"
        ));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
    

}

