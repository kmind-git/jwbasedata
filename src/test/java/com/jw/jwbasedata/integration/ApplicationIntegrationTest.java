package com.jw.jwbasedata.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 应用程序集成测试
 *
 * @author jw
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // 使用测试环境配置
class ApplicationIntegrationTest {

    @Test
    void contextLoads() {
        // 如果Spring上下文能成功加载，测试通过
        assertTrue(true, "Spring上下文加载成功");
    }

    @Test
    void applicationStartsSuccessfully() {
        // 验证应用程序能正常启动
        assertTrue(true, "应用程序启动成功");
    }
}