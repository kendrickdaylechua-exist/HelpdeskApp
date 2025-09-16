//package com.exist.HelpdeskApp.dto.employee;
//
//import com.exist.HelpdeskApp.model.EmploymentStatus;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class EmployeeResponseTest {
//
//    @Test
//    void testGetterAndSetter() {
//        EmployeeResponse response = new EmployeeResponse();
//        response.setId(1);
//        response.setName("Test Name");
//        response.setAge(30);
//        response.setAddress("Test Address");
//        response.setContactNumber(("09~~~~~~~~~~"));
//        response.setEmploymentStatus(EmploymentStatus.FULL_TIME);
//        response.setRoleId(1);
//        response.setRoleName("Test Role");
//
//        assertEquals(1, response.getId());
//        assertEquals("Test Name", response.getName());
//        assertEquals(30, response.getAge());
//        assertEquals("Test Address", response.getAddress());
//        assertEquals("09~~~~~~~~~~", response.getContactNumber());
//        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
//        assertEquals(1, response.getRoleId());
//        assertEquals("Test Role", response.getRoleName());
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        EmployeeResponse response = new EmployeeResponse(
//                1,
//                "Test Name",
//                30,
//                "Test Address",
//                "09~~~~~~~~~~",
//                EmploymentStatus.FULL_TIME,
//                1,
//                "Test Role"
//        );
//
//        assertEquals(1, response.getId());
//        assertEquals("Test Name", response.getName());
//        assertEquals(30, response.getAge());
//        assertEquals("Test Address", response.getAddress());
//        assertEquals("09~~~~~~~~~~", response.getContactNumber());
//        assertEquals(EmploymentStatus.FULL_TIME, response.getEmploymentStatus());
//        assertEquals(1, response.getRoleId());
//        assertEquals("Test Role", response.getRoleName());
//    }
//}
