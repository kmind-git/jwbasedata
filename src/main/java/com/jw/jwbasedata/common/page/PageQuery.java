package com.jw.jwbasedata.common.page;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author jw
 */
@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（从1开始）
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer size = 10;

    /**
     * 获取MyBatis-Plus分页对象
     */
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(this.page, this.size);
    }
}
