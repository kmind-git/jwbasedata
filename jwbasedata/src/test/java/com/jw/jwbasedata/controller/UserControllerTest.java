package com.jw.jwbasedata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jw.jwbasedata.common.page.PageResult;
import com.jw.jwbasedata.common.result.Result;
import com.jw.jwbasedata.dto.UserQueryDTO;
import com.jw.jwbasedata.service.UserService;
import com.jw.jwbasedata.vo.UserVO;
import com.jw.jwbasedata.config.ApiKeyConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试
 *
 * @author jw
 */
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@TestPropertySource(properties = "app.api-key.enabled=false")
@Import(UserControllerTest.TestConfig.class)
class UserControllerTest {

    @TestConfiguration
    @EnableConfigurationProperties(ApiKeyConfig.class)
    static class TestConfig {
        @Bean
        @Primary
        public ApiKeyConfig apiKeyConfig() {
            ApiKeyConfig config = new ApiKeyConfig();
            config.setEnabled(false);  // 测试环境禁用API密钥验证
            config.setKey("test-api-key-123456");
            config.setHeaderName("X-API-KEY");
            return config;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserVO testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserVO();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getUserPage_shouldReturnSuccess() throws Exception {
        // 准备测试数据
        PageResult<UserVO> pageResult = new PageResult<>(
                Arrays.asList(testUser),
                1L, 1L, 10L
        );

        // 模拟服务层调用
        when(userService.getUserPage(any(UserQueryDTO.class))).thenReturn(pageResult);

        // 执行请求并验证
        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.records[0].id").value(1))
                .andExpect(jsonPath("$.data.records[0].username").value("testuser"))
                .andExpect(jsonPath("$.data.total").value(1));
    }

    @Test
    void getUserPage_withInvalidPage_shouldReturnValidationError() throws Exception {
        // 测试页码小于1的情况
        mockMvc.perform(get("/api/users")
                        .param("page", "0")  // 无效页码
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void getUserPage_withInvalidSize_shouldReturnValidationError() throws Exception {
        // 测试每页大小超过限制的情况
        mockMvc.perform(get("/api/users")
                        .param("page", "1")
                        .param("size", "200")  // 超过最大限制100
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void getUserById_shouldReturnSuccess() throws Exception {
        // 模拟服务层调用
        when(userService.getUserById(1L)).thenReturn(testUser);

        // 执行请求并验证
        mockMvc.perform(get("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void getUserById_notFound_shouldReturnError() throws Exception {
        // 模拟用户不存在的情况
        when(userService.getUserById(999L)).thenThrow(
                new com.jw.jwbasedata.common.exception.BusinessException("用户不存在")
        );

        // 执行请求并验证
        mockMvc.perform(get("/api/users/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 业务异常返回200，错误码在响应体中
                .andExpect(jsonPath("$.code").value(-1))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }
}