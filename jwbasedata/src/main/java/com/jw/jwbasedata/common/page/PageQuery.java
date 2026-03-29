package com.jw.jwbasedata.common.page;

import lombok.Data;

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
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 获取MyBatis-Plus分页对象
     */
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(this.page, this.size);
    }
}
