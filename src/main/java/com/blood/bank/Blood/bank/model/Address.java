package com.blood.bank.Blood.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String state;
    private String district;
    private String city;
    private String streetAddress;
    private String zipCode;
    private String landmark;

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Donor donor;
}
