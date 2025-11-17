package com.patient.app.patient.service;

import com.patient.app.patient.model.Patient;
import com.patient.app.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class PatientTestService {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patientLahiru;

    @BeforeEach
    void setUp() {
        patientLahiru = Patient.builder()
                .id(1)
                .firstName("Lahiru")
                .lastName("Wanigasinghe")
                .address("No 158")
                .city("Malabe")
                .state("Colombo")
                .zipCode("41090")
                .phoneNumber("+947112345678")
                .email("lahiru.wa@aeturnum.com")
                .build();
    }

    @Test
    @DisplayName("Create valid patient with valid data")
    void createPatientWithValidData() {
        // Given
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patientLahiru);

        // When
        Patient savedPatient = patientService.createPatient(patientLahiru);

        // Then
        assertNotNull(savedPatient);
        assertEquals(patientLahiru.getId(), savedPatient.getId());
        assertEquals(patientLahiru.getFirstName(), savedPatient.getFirstName());
        verify(patientRepository, times(1)).existsByEmail(patientLahiru.getEmail());
        verify(patientRepository, times(1)).save(patientLahiru);
    }

    @Test
    @DisplayName("Throw exception when patient with email already exists")
    void throwExceptionWhenPatientWithEmailAlreadyExists() {
        // Given
        when(patientRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatient(patientLahiru));

        assertEquals("Patient already exists", exception.getMessage());
        verify(patientRepository, times(1)).existsByEmail(patientLahiru.getEmail());
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Throw exception when data integrity violation occurs")
    void createPatientWithDataIntegrityViolation() {
        // Given
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenThrow(DataIntegrityViolationException.class);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.createPatient(patientLahiru));

        assertEquals("Invalid patient data provided", exception.getMessage());
        verify(patientRepository, times(1)).existsByEmail(patientLahiru.getEmail());
        verify(patientRepository, times(1)).save(patientLahiru);
    }

    @Test
    @DisplayName("Delete patient with valid ID")
    void deletePatientWithValidId() {
        // Given
        int patientId = 1;
        when(patientRepository.existsById(patientId)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(patientId);

        // When
        patientService.deletePatient(patientId);

        // Then
        verify(patientRepository, times(1)).existsById(patientId);
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    @DisplayName("Delete patient with Invalid ID")
    void deletePatientWithInvalidId() {
        // Given
        int patientId = 99;
        when(patientRepository.existsById(patientId)).thenReturn(false);

        // When & Then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> patientService.deletePatient(patientId));

        assertEquals("Patient not found", exception.getMessage());
        verify(patientRepository, times(1)).existsById(patientId);
        verify(patientRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Update patient with valid data")
    void updatePatientWithValidData() {
        // Given
        int patientId = 1;
        Patient updatedPatientData = Patient.builder()
                .firstName("Lahiru")
                .lastName("Wanigasinghe")
                .email("updated.email@aeturnum.com")
                .phoneNumber("+947176543210")
                .address("New Address")
                .city("New City")
                .state("New State")
                .zipCode("12345")
                .build();

        when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.of(patientLahiru));
        when(patientRepository.existsByEmailAndIdNot(anyString(), anyInt())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patientLahiru);

        // When
        Patient updatedPatient = patientService.updatePatient(patientId, updatedPatientData);

        // Then
        assertNotNull(updatedPatient);
        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).existsByEmailAndIdNot(updatedPatientData.getEmail(), patientId);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}
