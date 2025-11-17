package com.patient.app.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class representing a Patient in the system.
 */
@Entity
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(name = "uk_patient_email", columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    /**
     * Primary key identifier for the patient.
     * Uses database identity generation for auto-increment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Patient's first name.
     */
    @NotBlank(message = "First name is required.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Patient's last name.
     */
    @NotBlank(message = "Last name is required.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Patient's residential address.
     */
    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    /**
     * Patient's city of residence.
     */
    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    /**
     * Patient's state of residence (2-letter code).
     */
    @NotBlank(message = "State is required")
    @Column(nullable = false)
    private String state;

    /**
     * Patient's ZIP code.
     * Validation: Required, must match US ZIP code format
     */
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP code must be in format 12345 or 12345-6789")
    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    /**
     * Patient's contact phone number.
     * Validation: Required, must match standard phone format
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?1?\\s*\\(?[0-9]{3}\\)?[-.\\s]?[0-9]{3}[-.\\s]?[0-9]{4}$",
            message = "Phone number must be valid (e.g., 123-456-7890)")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    /**
     * Patient's email address.
     * Validation: Required, valid email format, unique in system
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    /**
     * Timestamp when the patient record was created.
     * Automatically set by Hibernate on initial persist.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the patient record was last updated.
     * Automatically updated by Hibernate on each update.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return email != null && email.equals(patient.email);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
