package com.pm.patientservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @NotNull
  private String name;

  @NotNull
  @Email
  @Column(unique = true)
  private String email;

  private static final String INDIAN_PHONE_REGEX = "^[6-9]\\d{9}$";
  @NotNull
  @Column(unique = true)
  @Pattern(regexp = INDIAN_PHONE_REGEX, message = "Invalid Indian phone number. Must be 10 digits and start with 6, 7, 8, or 9.")
  private String phoneNo;

  @NotNull
  private LocalDate dateOfBirth;

  @NotNull
  private LocalDate registeredDate;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public @NotNull String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public @NotNull @Email String getEmail() {
    return email;
  }

  public void setEmail(@NotNull @Email String email) {
    this.email = email;
  }

  public @NotNull String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(@NotNull String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public @NotNull LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public @NotNull LocalDate getRegisteredDate() {
    return registeredDate;
  }

  public void setRegisteredDate(@NotNull LocalDate registeredDate) {
    this.registeredDate = registeredDate;
  }

}
