package com.patient.app.patient.service;

import com.patient.app.patient.model.Patient;
import com.patient.app.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service layer for patient business operations.
 * */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    /**
     * Get all patients as a list
     * @return A list of all Patients
     */
    public List<Patient> getPatientList(){
        return patientRepository.findAll();
    }

    /**
     * Get patient by ID
     * @param id the patient ID
     * @return Patient entity
     * @throws java.util.NoSuchElementException if patient is not found
     */
    public Patient getPatientById(int id){
        log.info("Request received for id: {}"+ id);

        return  patientRepository.findById(id).orElseThrow(() -> {log.info("Patient not found");
            return new NoSuchElementException("Patient not found");});
    }

    /**
     * Create new patient
     * @param patient Patient entity to create
     * @return Created patient entity
     * @throws IllegalArgumentException if patient already exists or data integrity violation occurs
     */
    @Transactional
    public Patient createPatient(Patient patient){
        log.info("Request received for patient: {}"+ patient);

        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Patient already exists");
        }

        try {
            Patient savedPatient = patientRepository.save(patient);
            log.info("Successfully created patient with id: {}", savedPatient.getId());
            return savedPatient;

        }catch (DataIntegrityViolationException e){
            log.error("Data integrity violation while creating patient: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid patient data provided");
        }
    }

    /**
     * Update patient by ID
     * @param id the patient ID to update
     * @param patientDetails Patient entity with updated details
     * @return Updated patient entity
     * @throws java.util.NoSuchElementException if patient is not found
     * @throws IllegalArgumentException if email already exists or data integrity violation occurs
     */
    @Transactional
    public Patient updatePatient(Integer id, Patient patientDetails){
        log.info("Request received for id: {}"+ id);

        Patient existingPatient = getPatientById(id);

        // Check if email is being changed and conflicts with another patient
        if (!existingPatient.getEmail().equals(patientDetails.getEmail()) &&
                patientRepository.existsByEmailAndIdNot(patientDetails.getEmail(), id)) {
            log.error("Email conflict during update for patient id: {}", id);
            throw new IllegalArgumentException("Email " + patientDetails.getEmail() + " already exists for another patient");
        }

        existingPatient.setFirstName(patientDetails.getFirstName());
        existingPatient.setLastName(patientDetails.getLastName());
        existingPatient.setAddress(patientDetails.getAddress());
        existingPatient.setCity(patientDetails.getCity());
        existingPatient.setState(patientDetails.getState());
        existingPatient.setZipCode(patientDetails.getZipCode());
        existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
        existingPatient.setEmail(patientDetails.getEmail());

        try {
            Patient updatedPatient = patientRepository.save(existingPatient);
            log.info("Successfully updated patient with id: {}", id);
            return updatedPatient;

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation while updating patient: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid patient data provided");
        }
    }

    /**
     * Delete patient by ID
     * @param id the patient ID to delete
     * @throws java.util.NoSuchElementException if patient is not found
     */
    @Transactional
    public void deletePatient(Integer id){
        log.info("Deleting patient with id: {}", id);

        // Check if patient exists before deleting
        if (!patientRepository.existsById(id)) {
            log.error("Patient with id {} not found for deletion", id);
            throw new NoSuchElementException("Patient not found");
        }

        patientRepository.deleteById(id);
        log.info("Successfully deleted patient with id: {}", id);
    }
}
