package com.blood.bank.Blood.bank.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Donor implements UserDetails,Principal{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Date dateOfBirth;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String occupation;

    @Column(length = 500)
    private String address;

    private Date lastDonateDate;


    private boolean hasDiseases;
    private boolean hasAllergies;
    private boolean hasCardiacConditions;
    private boolean hasBleedingDisorders;
    private boolean hasHIV;

    // private boolean accountLocked;
    private boolean enabled;
    
    private String verificationCode;

    private LocalDateTime time;



    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
        name = "joinTable", // Name of the join table
        joinColumns = @JoinColumn(name = "donor_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;




    //Overrided Methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                         .stream()
                         .map(r -> new SimpleGrantedAuthority(r.getName()))
                         .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }

    
   

  
    
   


   


}
