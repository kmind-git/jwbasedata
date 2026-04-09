package com.jw.jwbasedata.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw.jwbasedata.common.exception.BusinessException;
import com.jw.jwbasedata.common.page.PageResult;
import com.jw.jwbasedata.dto.UserQueryDTO;
import com.jw.jwbasedata.entity.User;
import com.jw.jwbasedata.mapper.UserMapper;
import com.jw.jwbasedata.service.impl.UserServiceImpl;
import com.jw.jwbasedata.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * UserServiceImpl 单元测试
 *
 * @author jw
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserVO testUserVO;
    private UserQueryDTO queryDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setCreatedAt(LocalDateTime.now());

        testUserVO = new UserVO();
        testUserVO.setId(1L);
        testUserVO.setUsername("testuser");
        testUserVO.setEmail("test@example.com");
        testUserVO.setCreatedAt(testUser.getCreatedAt());

        queryDTO = new UserQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(10);
    }

    @Test
    void getUserPage_withValidQuery_shouldReturnPageResult() {
        // 准备测试数据
        Page<User> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Collections.singletonList(testUser));
        mockPage.setTotal(1L);

        // 模拟Mapper调用
        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);

        // 执行测试
        PageResult<UserVO> result = userService.getUserPage(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals(1L, result.getTotal());
        assertEquals(1L, result.getPage());
        assertEquals(10L, result.getSize());

        UserVO vo = result.getRecords().get(0);
        assertEquals(1L, vo.getId());
        assertEquals("testuser", vo.getUsername());
        assertEquals("test@example.com", vo.getEmail());

        // 验证Mapper调用
        verify(userMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void getUserPage_withUsernameFilter_shouldApplyFilter() {
        // 设置查询条件
        queryDTO.setUsername("test");

        // 准备测试数据
        Page<User> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Collections.singletonList(testUser));
        mockPage.setTotal(1L);

        // 模拟Mapper调用
        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);

        // 执行测试
        PageResult<UserVO> result = userService.getUserPage(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());

        // 验证Mapper被调用时包含了正确的查询条件
        verify(userMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void getUserPage_withEmailFilter_shouldApplyFilter() {
        // 设置查询条件
        queryDTO.setEmail("example.com");

        // 准备测试数据
        Page<User> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Collections.singletonList(testUser));
        mockPage.setTotal(1L);

        // 模拟Mapper调用
        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);

        // 执行测试
        PageResult<UserVO> result = userService.getUserPage(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getRecords().size());

        // 验证Mapper被调用
        verify(userMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void getUserById_withValidId_shouldReturnUser() {
        // 模拟Mapper调用
        when(userMapper.selectById(1L)).thenReturn(testUser);

        // 执行测试
        UserVO result = userService.getUserById(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());

        // 验证Mapper调用
        verify(userMapper, times(1)).selectById(1L);
    }

    @Test
    void getUserById_withNullId_shouldThrowException() {
        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.getUserById(null));

        assertEquals("用户ID不能为空", exception.getMessage());

        // 验证Mapper未被调用
        verify(userMapper, never()).selectById(any());
    }

    @Test
    void getUserById_withNonExistingId_shouldThrowException() {
        // 模拟用户不存在
        when(userMapper.selectById(999L)).thenReturn(null);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.getUserById(999L));

        assertEquals("用户不存在", exception.getMessage());

        // 验证Mapper调用
        verify(userMapper, times(1)).selectById(999L);
    }

    @Test
    void getUserPage_withEmptyResult_shouldReturnEmptyPage() {
        // 准备空结果
        Page<User> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Collections.emptyList());
        mockPage.setTotal(0L);

        // 模拟Mapper调用
        when(userMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class)))
                .thenReturn(mockPage);

        // 执行测试
        PageResult<UserVO> result = userService.getUserPage(queryDTO);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0L, result.getTotal());
        assertEquals(1L, result.getPage());
        assertEquals(10L, result.getSize());

        // 验证Mapper调用
        verify(userMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }
}