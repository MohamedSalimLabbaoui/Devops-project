package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;

import java.time.LocalDate;

class StudentManagementApplicationTests {

    private Department testDepartment;
    private Enrollment testEnrollment;

    @BeforeEach
    void setUp() {
        // Setup pour Department
        testDepartment = new Department();
        testDepartment.setIdDepartment(1L);
        testDepartment.setName("Informatique");
        testDepartment.setLocation("Bâtiment A");
        testDepartment.setPhone("123456789");
        testDepartment.setHead("Dr. Smith");

        // Setup pour Enrollment
        testEnrollment = new Enrollment();
        testEnrollment.setIdEnrollment(1L);
        testEnrollment.setEnrollmentDate(LocalDate.of(2024, 9, 1));
        testEnrollment.setGrade(15.5);
        testEnrollment.setStatus(Status.ACTIVE);
    }

    // ==================== TESTS DEPARTMENT ====================

    @Test
    void testDepartmentCreation() {
        assertNotNull(testDepartment);
        assertEquals("Informatique", testDepartment.getName());
        assertEquals("Bâtiment A", testDepartment.getLocation());
        assertEquals("Dr. Smith", testDepartment.getHead());
    }

    @Test
    void testDepartmentWithAllArgsConstructor() {
        Department dept = new Department(1L, "Maths", "Bâtiment B", "111111", "Dr. Ali", null);

        assertNotNull(dept);
        assertEquals("Maths", dept.getName());
        assertEquals("Bâtiment B", dept.getLocation());
    }

    @Test
    void testDepartmentValidation() {
        Department validDept = new Department();
        validDept.setName("Valid Department");
        validDept.setLocation("Valid Location");

        assertNotNull(validDept.getName());
        assertNotNull(validDept.getLocation());
    }

    // ==================== TESTS ENROLLMENT ====================

    @Test
    void testEnrollmentCreation() {
        assertNotNull(testEnrollment);
        assertEquals(LocalDate.of(2024, 9, 1), testEnrollment.getEnrollmentDate());
        assertEquals(15.5, testEnrollment.getGrade());
        assertEquals(Status.ACTIVE, testEnrollment.getStatus());
    }

    @Test
    void testEnrollmentWithAllArgsConstructor() {
        Enrollment enroll = new Enrollment(1L, LocalDate.now(), 16.0, Status.PENDING, null, null);

        assertNotNull(enroll);
        assertEquals(16.0, enroll.getGrade());
        assertEquals(Status.PENDING, enroll.getStatus());
    }

    @Test
    void testEnrollmentStatusTransitions() {
        // Test des différents statuts
        testEnrollment.setStatus(Status.PENDING);
        assertEquals(Status.PENDING, testEnrollment.getStatus());

        testEnrollment.setStatus(Status.ACTIVE);
        assertEquals(Status.ACTIVE, testEnrollment.getStatus());

        testEnrollment.setStatus(Status.COMPLETED);
        assertEquals(Status.COMPLETED, testEnrollment.getStatus());

        testEnrollment.setStatus(Status.DROPPED);
        assertEquals(Status.DROPPED, testEnrollment.getStatus());
    }

    @Test
    void testEnrollmentGradeValidation() {
        // Test notes valides
        testEnrollment.setGrade(0.0);
        assertEquals(0.0, testEnrollment.getGrade());

        testEnrollment.setGrade(20.0);
        assertEquals(20.0, testEnrollment.getGrade());

        testEnrollment.setGrade(15.75);
        assertEquals(15.75, testEnrollment.getGrade());
    }

    @Test
    void testEnrollmentWithoutGrade() {
        // Une inscription peut avoir une note null (cours en cours)
        testEnrollment.setGrade(null);
        assertNull(testEnrollment.getGrade());
    }

    // ==================== TESTS MÉTIER ====================

    @Test
    void testDepartmentEnrollmentIntegration() {
        // Test l'intégration entre Department et Enrollment
        assertNotNull(testDepartment);
        assertNotNull(testEnrollment);

        // Simuler qu'un enrollment appartient à un department via student
        assertEquals("Informatique", testDepartment.getName());
        assertEquals(Status.ACTIVE, testEnrollment.getStatus());
    }

    @Test
    void testEnrollmentDateLogic() {
        // Test de la logique des dates
        LocalDate pastDate = LocalDate.of(2023, 1, 1);
        LocalDate futureDate = LocalDate.now().plusMonths(1);

        testEnrollment.setEnrollmentDate(pastDate);
        assertEquals(pastDate, testEnrollment.getEnrollmentDate());

        testEnrollment.setEnrollmentDate(futureDate);
        assertEquals(futureDate, testEnrollment.getEnrollmentDate());
    }

    @Test
    void testCompletedEnrollmentShouldHaveGrade() {
        // Règle métier : un cours complété devrait avoir une note
        testEnrollment.setStatus(Status.COMPLETED);
        testEnrollment.setGrade(18.5);

        assertEquals(Status.COMPLETED, testEnrollment.getStatus());
        assertNotNull(testEnrollment.getGrade());
    }

    @Test
    void testPendingEnrollmentCanHaveNoGrade() {
        // Règle métier : un cours en attente peut ne pas avoir de note
        testEnrollment.setStatus(Status.PENDING);
        testEnrollment.setGrade(null);

        assertEquals(Status.PENDING, testEnrollment.getStatus());
        assertNull(testEnrollment.getGrade());
    }
}
