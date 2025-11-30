// src/test/java/tn/esprit/studentmanagement/enrollment/EnrollmentUnitTest.java
package tn.esprit.studentmanagement.enrollement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import java.time.LocalDate;

class EnrollmentUnitTest {

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        enrollment = new Enrollment();
        enrollment.setIdEnrollment(1L);
        enrollment.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        enrollment.setGrade(15.5);
        enrollment.setStatus(Status.ACTIVE);
    }

    @Test
    void testEnrollmentCreation() {
        assertNotNull(enrollment);
        assertEquals(LocalDate.of(2024, 9, 1), enrollment.getEnrollmentDate());
        assertEquals(15.5, enrollment.getGrade());
        assertEquals(Status.ACTIVE, enrollment.getStatus());
    }

    @Test
    void testEnrollmentWithAllArgsConstructor() {
        Enrollment enrollment = new Enrollment(1L, LocalDate.now(), 16.0, Status.ACTIVE, null, null);

        assertNotNull(enrollment);
        assertEquals(16.0, enrollment.getGrade());
        assertEquals(Status.ACTIVE, enrollment.getStatus());
    }

    @Test
    void testEnrollmentGradeValidation() {
        // Note valide
        enrollment.setGrade(18.5);
        assertEquals(18.5, enrollment.getGrade());

        // Note à 0
        enrollment.setGrade(0.0);
        assertEquals(0.0, enrollment.getGrade());

        // Note à 20
        enrollment.setGrade(20.0);
        assertEquals(20.0, enrollment.getGrade());
    }

    @Test
    void testEnrollmentStatusTransitions() {
        enrollment.setStatus(Status.PENDING);
        assertEquals(Status.PENDING, enrollment.getStatus());

        enrollment.setStatus(Status.ACTIVE);
        assertEquals(Status.ACTIVE, enrollment.getStatus());

        enrollment.setStatus(Status.COMPLETED);
        assertEquals(Status.COMPLETED, enrollment.getStatus());

        enrollment.setStatus(Status.DROPPED);
        assertEquals(Status.DROPPED, enrollment.getStatus());
    }

    @Test
    void testEnrollmentDateInPast() {
        LocalDate pastDate = LocalDate.of(2023, 1, 15);
        enrollment.setEnrollmentDate(pastDate);
        assertEquals(pastDate, enrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentDateInFuture() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        enrollment.setEnrollmentDate(futureDate);
        assertEquals(futureDate, enrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentToString() {
        String toString = enrollment.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ACTIVE"));
    }
}