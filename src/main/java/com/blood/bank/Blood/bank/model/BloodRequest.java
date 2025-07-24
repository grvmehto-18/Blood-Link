package com.blood.bank.Blood.bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Donor requester;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String status; // e.g., "Pending", "Fulfilled", "Cancelled"

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime requestDate;

    private LocalDateTime fulfilledDate;

}