package com.jw.jwbasedata.service;

import com.jw.jwbasedata.common.page.PageResult;
import com.jw.jwbasedata.dto.UserQueryDTO;
import com.jw.jwbasedata.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author jw
 */
public interface UserService {

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<UserVO> getUserPage(UserQueryDTO queryDTO);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long id);
}
