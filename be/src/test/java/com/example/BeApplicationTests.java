package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "tappay.partner-key=test-partner-key",
        "tappay.merchant-id=test-merchant-id"
})
class BeApplicationTests {

    @Test
    void contextLoads() {
    }

}
