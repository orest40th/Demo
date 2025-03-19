package com.mate.academy.demo;

import com.mate.academy.demo.config.CustomMySqlContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @BeforeAll
    static void setup() {
        CustomMySqlContainer.getInstance().start();
    }

    @Test
    void contextLoads() {
    }
}
