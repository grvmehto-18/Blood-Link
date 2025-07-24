package com.blood.bank.Blood.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;

    @Column(nullable = false)
    private LocalDate donationDate;

    @Column(nullable = false)
    private int quantityMl;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String location;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedDate;

}