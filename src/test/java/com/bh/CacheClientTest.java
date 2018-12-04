package com.bh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CacheClientTest {

    private CacheClient cacheClient;

    @BeforeEach
    void setUp() {
        cacheClient = CacheClientBuilder.buildCacheClient();
    }


    @DisplayName("Test cache operations")
    @Test
    void testDateGeneratorRolls() {
        cacheClient.cache("test", "value");

        assert cacheClient.retrieve("test").equals("value");

        cacheClient.invalidate("test");

        assert cacheClient.retrieve("test") == null;
    }


}
