package com.blood.bank.Blood.bank.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @Column(nullable = false)
    @NotBlank(message = "Gender is required")
    private String gender;

    @Column(nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Column(nullable = false)
    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    private String username;
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private String occupation;

    @Column(length = 500)
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address cannot be more than 500 characters")
    private String address;

    @Past(message = "Last donate date must be in the past")
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

    private String resetToken;
    private LocalDateTime resetTokenExpiryDate;



    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(
            name = "join_table",
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
