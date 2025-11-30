// src/test/java/tn/esprit/studentmanagement/department/DepartmentValidationTest.java
package tn.esprit.studentmanagement.departements;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.DepartmentService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentValidationTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    void testDepartmentWithNullName() {
        Department department = new Department();
        department.setName(null);
        department.setLocation("Bâtiment A");
        department.setPhone("+216 12 345 678");
        department.setHead("Dr. Test");

        // Le nom ne devrait pas être null
        assertThrows(Exception.class, () -> {
            departmentService.saveDepartment(department);
        });
    }

    @Test
    void testDepartmentWithEmptyName() {
        Department department = new Department();
        department.setName("");
        department.setLocation("Bâtiment A");
        department.setPhone("+216 12 345 678");
        department.setHead("Dr. Test");

        // Le nom ne devrait pas être vide
        assertThrows(Exception.class, () -> {
            departmentService.saveDepartment(department);
        });
    }

    @Test
    void testDepartmentWithVeryLongName() {
        Department department = new Department();
        department.setName("Département de Génie Informatique et Technologies Avancées de Tunis");
        department.setLocation("Bâtiment A");
        department.setPhone("+216 12 345 678");
        department.setHead("Dr. Test");

        // Devrait fonctionner même avec un nom long
        assertDoesNotThrow(() -> {
            Department saved = departmentService.saveDepartment(department);
            assertNotNull(saved);
        });
    }

    @Test
    void testDepartmentPhoneFormat() {
        Department department = new Department();
        department.setName("Test Department");
        department.setLocation("Bâtiment A");
        department.setPhone("123456"); // Format invalide
        department.setHead("Dr. Test");

        // Devrait accepter différents formats de téléphone
        assertDoesNotThrow(() -> {
            Department saved = departmentService.saveDepartment(department);
            assertNotNull(saved);
        });
    }
}