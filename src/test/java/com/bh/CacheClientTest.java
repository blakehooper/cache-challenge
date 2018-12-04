package com.bh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CacheClientTest {

    private CacheClient cacheClient;

    @BeforeEach
    void setUp() {
        cacheClient = CacheClientBuilder.buildCacheClient();
    }

    @DisplayName("Smoke spec")
    @Test
    void basicFunctionality() {
        cacheClient.cache("test", "value");

        assert cacheClient.retrieve("test").equals("value");

        cacheClient.invalidate("test");

        assert cacheClient.retrieve("test") == null;
    }


    @DisplayName("Test cache operations")
    @ParameterizedTest
    @ValueSource(strings = {
            "test",
            "other",
            "key-z"
    })
    void testCacheOperationsWithMultipleIterations(String key) {
        cacheClient.cache(key, key);

        assert cacheClient.retrieve(key).equals(key);

        cacheClient.invalidate(key);

        assert cacheClient.retrieve(key) == null;
    }


}
