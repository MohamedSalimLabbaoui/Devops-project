// src/test/java/tn/esprit/studentmanagement/enrollment/EnrollmentValidationTest.java
package tn.esprit.studentmanagement.enrollement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.EnrollmentService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EnrollmentValidationTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Test
    void testEnrollmentWithNullDate() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(null); // Date obligatoire
        enrollment.setGrade(15.0);
        enrollment.setStatus(Status.ACTIVE);

        assertThrows(Exception.class, () -> {
            enrollmentService.saveEnrollment(enrollment);
        });
    }

    @Test
    void testEnrollmentWithNullStatus() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(15.0);
        enrollment.setStatus(null); // Statut obligatoire

        assertThrows(Exception.class, () -> {
            enrollmentService.saveEnrollment(enrollment);
        });
    }

    @Test
    void testEnrollmentWithInvalidGradeTooHigh() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(25.0); // Note > 20
        enrollment.setStatus(Status.COMPLETED);

        // Devrait accepter mais c'est à la logique métier de valider
        assertDoesNotThrow(() -> {
            Enrollment saved = enrollmentService.saveEnrollment(enrollment);
            assertNotNull(saved);
            assertEquals(25.0, saved.getGrade());
        });
    }

    @Test
    void testEnrollmentWithInvalidGradeNegative() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(-5.0); // Note négative
        enrollment.setStatus(Status.COMPLETED);

        // Devrait accepter mais c'est à la logique métier de valider
        assertDoesNotThrow(() -> {
            Enrollment saved = enrollmentService.saveEnrollment(enrollment);
            assertNotNull(saved);
            assertEquals(-5.0, saved.getGrade());
        });
    }

    @Test
    void testCompletedEnrollmentWithoutGrade() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setGrade(null); // Pas de note
        enrollment.setStatus(Status.COMPLETED); // Mais statut COMPLETED

        // Un cours complété devrait avoir une note
        assertDoesNotThrow(() -> {
            Enrollment saved = enrollmentService.saveEnrollment(enrollment);
            assertNotNull(saved);
            assertNull(saved.getGrade()); // Mais la base de données l'accepte
        });
    }
}