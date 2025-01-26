package com.blood.bank.Blood.bank.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class BloodCamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    public BloodCamp() {
    }

    public BloodCamp(int id, String location, LocalDate date, String description) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.description = description;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

