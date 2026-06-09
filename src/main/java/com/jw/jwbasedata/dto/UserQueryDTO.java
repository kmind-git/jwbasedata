package com.jw.jwbasedata.dto;

import com.jw.jwbasedata.common.page.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询请求DTO
 *
 * @author jw
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询请求")
public class UserQueryDTO extends PageQuery {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名（模糊查询）")
    private String username;

    @Schema(description = "邮箱（模糊查询）")
    private String email;
}
