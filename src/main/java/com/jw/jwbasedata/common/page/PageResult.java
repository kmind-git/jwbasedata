package com.jw.jwbasedata.common.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author jw
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 每页大小
     */
    private Long size;

    public PageResult() {
    }

    public PageResult(List<T> records, Long total, Long page, Long size) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    /**
     * 从MyBatis-Plus的Page对象转换
     */
    public static <T> PageResult<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }
}
