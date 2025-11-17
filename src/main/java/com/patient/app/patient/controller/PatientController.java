package com.patient.app.patient.controller;

import com.patient.app.patient.model.Patient;
import com.patient.app.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient Management App", description = "APIs for managing patient records. CRUD")
public class PatientController {

    private final PatientService patientService;

    /**
     * Get all patient as a list
     * @return list of all patient with 200 OK
     * */
    @Operation(summary = "Get all patients", description = "Retrieve list of all patients")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved patients")
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        log.info("Get all patients");

        List<Patient> patients = patientService.getPatientList();
        return ResponseEntity.ok(patients);
    }

    /**
     * Get patient by Id.
     * @param id patient Id
     * @return patient entity data with 200 OK, or 404 if not found
     */
    @Operation(summary = "Get patient by Id", description = "Retrieve a specific patient by their Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found",
                    content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @Parameter(description = "Id of the patient to be retrieved") @PathVariable int id) {
        log.info("Get a patient by Id", id);

        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    /**
     * Create a new patient.
     * @param patient patient data to create
     * @return created patient entity with 201 CREATED
     */
    @Operation(summary = "Create a new patient", description = "Create a new patient record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "Patient with email already exists", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Patient> createPatient( @Parameter(description = "Patient object to be created")
                                                    @Valid @RequestBody Patient patient) {
        log.info("Creating new patient with email: {}", patient.getEmail());

        Patient createdPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    /**
     * Update an existing patient.
     * @param id patient ID
     * @param patientDetails updated patient data
     * @return updated patient with 200 OK
     */
    @Operation(summary = "Update an existing patient", description = "Update a patient by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully", content = @Content(schema = @Schema(implementation = Patient.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email conflict", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @Parameter(description = "Id of the patient to be updated") @PathVariable int id, @Parameter(description = "Updated patient object")
            @Valid @RequestBody Patient patientDetails) {

        log.info("Updating patient - {}", id);
        Patient updatedPatient = patientService.updatePatient(id, patientDetails);
        return ResponseEntity.ok(updatedPatient);
    }

    /**
     * Delete a patient.
     * @param id patient ID
     * @return 204 NO CONTENT on success
     */
    @Operation(summary = "Delete a patient", description = "Delete a patient record by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "Id of the patient to be deleted", example = "1")
            @PathVariable int id) {
        log.info("Deleting patient - {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
