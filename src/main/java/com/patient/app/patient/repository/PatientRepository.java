package com.patient.app.patient.repository;

import com.patient.app.patient.model.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Patient entity.
 * */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") @Size(max = 100, message = "Email must not exceed 100 characters") String email, Integer id);
}
