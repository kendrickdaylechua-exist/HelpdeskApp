package com.exist.HelpdeskApp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.liquibase.enabled=false"
})
class HelpdeskAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
