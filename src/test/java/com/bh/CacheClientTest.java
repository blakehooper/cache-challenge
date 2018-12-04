package com.bh;

import com.bh.cacheclient.CacheClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CacheClientTest {

    private static DistributedCacheBuilder distributedCacheBuilder;

    private CacheClient cacheClient;

    @BeforeAll
    static void setUpOnce() {
        distributedCacheBuilder = DistributedCacheBuilder.getInstance();
    }

    @BeforeEach
    void setUp() {
        cacheClient = distributedCacheBuilder.getCacheClient();
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
