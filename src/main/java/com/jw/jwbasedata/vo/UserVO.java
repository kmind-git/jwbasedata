package com.jw.jwbasedata.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户返回VO
 *
 * @author jw
 */
@Data
@Schema(description = "用户信息")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}
