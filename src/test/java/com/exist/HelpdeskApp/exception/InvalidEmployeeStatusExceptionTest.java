//package com.exist.HelpdeskApp.exception;
//
//import com.exist.HelpdeskApp.exception.businessexceptions.InvalidEmployeeStatusException;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class InvalidEmployeeStatusExceptionTest {
//    @Test
//    void testMessageIsStored() {
//        String message = "Unauthorized Access!";
//        InvalidEmployeeStatusException ex = new InvalidEmployeeStatusException(message);
//
//        assertEquals(message, ex.getMessage());
//    }
//
//    @Test
//    void testIsBusinessException() {
//        InvalidEmployeeStatusException ex = new InvalidEmployeeStatusException("error");
//        assertTrue(ex instanceof BusinessException);
//    }
//}
