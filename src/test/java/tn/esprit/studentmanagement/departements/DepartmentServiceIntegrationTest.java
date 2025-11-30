// src/test/java/tn/esprit/studentmanagement/department/DepartmentServiceIntegrationTest.java
package tn.esprit.studentmanagement.departements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.DepartmentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class DepartmentServiceIntegrationTest {

    @Autowired
    private DepartmentService departmentService;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setName("Génie Civil");
        testDepartment.setLocation("Bâtiment C");
        testDepartment.setPhone("+216 55 123 456");
        testDepartment.setHead("Dr. Ahmed Ben");
    }

    @Test
    void testSaveDepartment() {
        // Given
        Department department = testDepartment;

        // When
        Department savedDepartment = departmentService.saveDepartment(department);

        // Then
        assertNotNull(savedDepartment);
        assertNotNull(savedDepartment.getIdDepartment());
        assertEquals("Génie Civil", savedDepartment.getName());
        assertEquals("Dr. Ahmed Ben", savedDepartment.getHead());
    }

    @Test
    void testGetDepartmentById() {
        // Given
        Department savedDepartment = departmentService.saveDepartment(testDepartment);
        Long departmentId = savedDepartment.getIdDepartment();

        // When
        Department foundDepartment = departmentService.getDepartmentById(departmentId);

        // Then
        assertNotNull(foundDepartment);
        assertEquals(departmentId, foundDepartment.getIdDepartment());
        assertEquals("Génie Civil", foundDepartment.getName());
    }

    @Test
    void testGetAllDepartments() {
        // Given
        departmentService.saveDepartment(new Department(null, "Informatique", "Bâtiment A",
                "+216 11 111 111", "Dr. Ali", null));
        departmentService.saveDepartment(new Department(null, "Mathématiques", "Bâtiment B",
                "+216 22 222 222", "Dr. Sami", null));

        // When
        List<Department> departments = departmentService.getAllDepartments();

        // Then
        assertNotNull(departments);
        assertTrue(departments.size() >= 2);

        // Vérifier que les départements existent
        boolean hasInformatique = departments.stream()
                .anyMatch(dept -> "Informatique".equals(dept.getName()));
        boolean hasMathematiques = departments.stream()
                .anyMatch(dept -> "Mathématiques".equals(dept.getName()));

        assertTrue(hasInformatique);
        assertTrue(hasMathematiques);
    }

    @Test
    void testDeleteDepartment() {
        // Given
        Department savedDepartment = departmentService.saveDepartment(testDepartment);
        Long departmentId = savedDepartment.getIdDepartment();

        // When
        departmentService.deleteDepartment(departmentId);

        // Then - Vérifier que la suppression ne lance pas d'exception
        assertDoesNotThrow(() -> departmentService.deleteDepartment(departmentId));
    }

    @Test
    void testUpdateDepartment() {
        // Given
        Department savedDepartment = departmentService.saveDepartment(testDepartment);
        Long departmentId = savedDepartment.getIdDepartment();

        // When - Modifier le département
        savedDepartment.setName("Génie Civil Modifié");
        savedDepartment.setHead("Dr. Nouveau Chef");
        Department updatedDepartment = departmentService.saveDepartment(savedDepartment);

        // Then
        assertNotNull(updatedDepartment);
        assertEquals(departmentId, updatedDepartment.getIdDepartment());
        assertEquals("Génie Civil Modifié", updatedDepartment.getName());
        assertEquals("Dr. Nouveau Chef", updatedDepartment.getHead());
    }
}