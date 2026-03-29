package com.jw.jwbasedata.controller;

import com.jw.jwbasedata.common.page.PageResult;
import com.jw.jwbasedata.common.result.Result;
import com.jw.jwbasedata.dto.UserQueryDTO;
import com.jw.jwbasedata.service.UserService;
import com.jw.jwbasedata.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author jw
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @GetMapping
    public Result<PageResult<UserVO>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "邮箱") @RequestParam(required = false) String email) {

        UserQueryDTO queryDTO = new UserQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setUsername(username);
        queryDTO.setEmail(email);

        PageResult<UserVO> result = userService.getUserPage(queryDTO);
        return Result.success(result);
    }

    /**
     * 根据ID查询用户
     */
    @Operation(summary = "根据ID查询用户", description = "根据ID查询用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        return Result.success(user);
    }
}
