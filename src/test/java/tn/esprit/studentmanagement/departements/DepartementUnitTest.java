// src/test/java/tn/esprit/studentmanagement/department/DepartmentUnitTest.java
package tn.esprit.studentmanagement.departements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tn.esprit.studentmanagement.entities.Department;

class DepartmentUnitTest {

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setIdDepartment(1L);
        department.setName("Informatique");
        department.setLocation("Bâtiment A");
        department.setPhone("+216 12 345 678");
        department.setHead("Dr. Mohamed Ali");
    }

    @Test
    void testDepartmentCreation() {
        assertNotNull(department);
        assertEquals("Informatique", department.getName());
        assertEquals("Bâtiment A", department.getLocation());
        assertEquals("+216 12 345 678", department.getPhone());
        assertEquals("Dr. Mohamed Ali", department.getHead());
    }

    @Test
    void testDepartmentWithValidData() {
        assertDoesNotThrow(() -> {
            Department validDept = new Department(1L, "Mathématiques", "Bâtiment B",
                    "+216 98 765 432", "Dr. Sami Ben", null);
            assertNotNull(validDept);
        });
    }

    @Test
    void testDepartmentToString() {
        String toString = department.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Informatique"));
    }

    @Test
    void testDepartmentEquality() {
        Department dept1 = new Department(1L, "Informatique", "Bâtiment A",
                "+216 12 345 678", "Dr. Mohamed Ali", null);
        Department dept2 = new Department(1L, "Informatique", "Bâtiment A",
                "+216 12 345 678", "Dr. Mohamed Ali", null);

        assertEquals(dept1.getName(), dept2.getName());
        assertEquals(dept1.getLocation(), dept2.getLocation());
    }
}