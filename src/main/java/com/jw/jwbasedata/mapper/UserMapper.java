package com.jw.jwbasedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jw.jwbasedata.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 *
 * @author jw
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
