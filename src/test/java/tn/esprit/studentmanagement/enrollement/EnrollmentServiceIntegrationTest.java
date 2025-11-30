// src/test/java/tn/esprit/studentmanagement/enrollment/EnrollmentServiceIntegrationTest.java
package tn.esprit.studentmanagement.enrollement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.EnrollmentService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class EnrollmentServiceIntegrationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    private Enrollment testEnrollment;

    @BeforeEach
    void setUp() {
        testEnrollment = new Enrollment();
        testEnrollment.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        testEnrollment.setGrade(14.5);
        testEnrollment.setStatus(Status.ACTIVE);
    }

    @Test
    void testSaveEnrollment() {
        // Given
        Enrollment enrollment = testEnrollment;

        // When
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);

        // Then
        assertNotNull(savedEnrollment);
        assertNotNull(savedEnrollment.getIdEnrollment());
        assertEquals(LocalDate.of(2024, 9, 1), savedEnrollment.getEnrollmentDate());
        assertEquals(14.5, savedEnrollment.getGrade());
        assertEquals(Status.ACTIVE, savedEnrollment.getStatus());
    }

    @Test
    void testGetEnrollmentById() {
        // Given
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(testEnrollment);
        Long enrollmentId = savedEnrollment.getIdEnrollment();

        // When
        Enrollment foundEnrollment = enrollmentService.getEnrollmentById(enrollmentId);

        // Then
        assertNotNull(foundEnrollment);
        assertEquals(enrollmentId, foundEnrollment.getIdEnrollment());
        assertEquals(14.5, foundEnrollment.getGrade());
        assertEquals(Status.ACTIVE, foundEnrollment.getStatus());
    }

    @Test
    void testGetAllEnrollments() {
        // Given
        enrollmentService.saveEnrollment(new Enrollment(null, LocalDate.of(2024, 9, 1), 15.0, Status.ACTIVE, null, null));
        enrollmentService.saveEnrollment(new Enrollment(null, LocalDate.of(2024, 9, 2), 16.5, Status.PENDING, null, null));
        enrollmentService.saveEnrollment(new Enrollment(null, LocalDate.of(2024, 9, 3), 18.0, Status.COMPLETED, null, null));

        // When
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        // Then
        assertNotNull(enrollments);
        assertTrue(enrollments.size() >= 3);

        // Vérifier la présence des différents statuts
        boolean hasActive = enrollments.stream()
                .anyMatch(enroll -> Status.ACTIVE.equals(enroll.getStatus()));
        boolean hasPending = enrollments.stream()
                .anyMatch(enroll -> Status.PENDING.equals(enroll.getStatus()));
        boolean hasCompleted = enrollments.stream()
                .anyMatch(enroll -> Status.COMPLETED.equals(enroll.getStatus()));

        assertTrue(hasActive);
        assertTrue(hasPending);
        assertTrue(hasCompleted);
    }

    @Test
    void testDeleteEnrollment() {
        // Given
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(testEnrollment);
        Long enrollmentId = savedEnrollment.getIdEnrollment();

        // When & Then - La suppression ne doit pas lancer d'exception
        assertDoesNotThrow(() -> enrollmentService.deleteEnrollment(enrollmentId));
    }

    @Test
    void testUpdateEnrollment() {
        // Given
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(testEnrollment);
        Long enrollmentId = savedEnrollment.getIdEnrollment();

        // When - Modifier l'inscription
        savedEnrollment.setGrade(19.5);
        savedEnrollment.setStatus(Status.COMPLETED);
        savedEnrollment.setEnrollmentDate(LocalDate.of(2024, 12, 15));

        Enrollment updatedEnrollment = enrollmentService.saveEnrollment(savedEnrollment);

        // Then
        assertNotNull(updatedEnrollment);
        assertEquals(enrollmentId, updatedEnrollment.getIdEnrollment());
        assertEquals(19.5, updatedEnrollment.getGrade());
        assertEquals(Status.COMPLETED, updatedEnrollment.getStatus());
        assertEquals(LocalDate.of(2024, 12, 15), updatedEnrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentWithoutGrade() {
        // Given - Une inscription sans note (cours en cours)
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(null); // Pas de note encore
        enrollment.setStatus(Status.ACTIVE);

        // When & Then
        assertDoesNotThrow(() -> {
            Enrollment saved = enrollmentService.saveEnrollment(enrollment);
            assertNotNull(saved);
            assertNull(saved.getGrade());
        });
    }
}