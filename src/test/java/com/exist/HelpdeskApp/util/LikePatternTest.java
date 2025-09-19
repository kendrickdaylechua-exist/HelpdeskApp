package com.exist.HelpdeskApp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LikePatternTest {

    @Test
    void testLikePattern() {
        String test = "test";

        String result = StringConverters.likePattern(test);

        assertEquals("%test%", result);
    }

}
