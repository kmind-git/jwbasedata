package com.jw.jwbasedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jw.jwbasedata.common.exception.BusinessException;
import com.jw.jwbasedata.common.page.PageResult;
import com.jw.jwbasedata.dto.UserQueryDTO;
import com.jw.jwbasedata.entity.User;
import com.jw.jwbasedata.mapper.UserMapper;
import com.jw.jwbasedata.service.UserService;
import com.jw.jwbasedata.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author jw
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public PageResult<UserVO> getUserPage(UserQueryDTO queryDTO) {
        log.info("分页查询用户列表，参数：{}", queryDTO);

        // 构建分页条件
        Page<User> page = queryDTO.toPage();

        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                .like(StringUtils.hasText(queryDTO.getEmail()), User::getEmail, queryDTO.getEmail())
                .orderByDesc(User::getCreatedAt);

        // 执行查询
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);

        // 转换结果
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, userPage.getTotal(), userPage.getCurrent(), userPage.getSize());
    }

    @Override
    public UserVO getUserById(Long id) {
        log.info("根据ID查询用户，ID：{}", id);

        if (id == null) {
            throw new BusinessException("用户ID不能为空");
        }

        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        return convertToVO(user);
    }

    /**
     * 实体转VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
