//package com.exist.HelpdeskApp;
//
//import com.exist.HelpdeskApp.dto.employee.EmployeeResponse;
//import com.exist.HelpdeskApp.model.Employee;
//import com.exist.HelpdeskApp.model.EmploymentStatus;
//import com.exist.HelpdeskApp.model.Role;
//import com.exist.HelpdeskApp.model.embeddable.Address;
//import com.exist.HelpdeskApp.model.embeddable.Contacts;
//import com.exist.HelpdeskApp.model.embeddable.Name;
//
//public class TestDataFactory {
//    public static Employee admin() {
//        Name name = new Name("Admin", null, null);
//        return new Employee(
//                1,
//                name,
//                30,
//                new Address(),
//                new Contacts(),
//                EmploymentStatus.FULL_TIME,
//                adminRole(),
//                false,
//                1
//        );
//    }
//
//    public static Role adminRole() {
//        return new Role(
//                1,
//                "Admin",
//                false,
//                1
//        );
//    }
//
//    public static EmployeeResponse adminEmployeeRequest() {
//        Name name = new Name("Admin", null, null);
//        return new EmployeeResponse(
//                1,
//                name,
//                30,
//                new Address(),
//               new Contacts(),
//                EmploymentStatus.FULL_TIME,
//                1,
//                "Admin"
//        );
//    }
//}
