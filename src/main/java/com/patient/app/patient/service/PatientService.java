package com.patient.app.patient.service;

import com.patient.app.patient.model.Patient;

import java.util.List;

public interface PatientService {

    /**
     * Get all patients as a list
     * @return A list of all Patients
     */
    List<Patient> getPatientList();

    /**
     * Get patient by ID
     * @param id the patient ID
     * @return Patient entity
     * @throws java.util.NoSuchElementException if patient is not found
     */
    Patient getPatientById(int id);

    /**
     * Create new patient
     * @param patient Patient entity to create
     * @return Created patient entity
     * @throws IllegalArgumentException if patient already exists or data integrity violation occurs
     */
    Patient createPatient(Patient patient);

    /**
     * Update patient by ID
     * @param id the patient ID to update
     * @param patientDetails Patient entity with updated details
     * @return Updated patient entity
     * @throws java.util.NoSuchElementException if patient is not found
     * @throws IllegalArgumentException if email already exists or data integrity violation occurs
     */
    Patient updatePatient(Integer id, Patient patientDetails);

    /**
     * Delete patient by ID
     * @param id the patient ID to delete
     * @throws java.util.NoSuchElementException if patient is not found
     */
    void deletePatient(Integer id);
}
