package com.liaozl.dao.mapper;

import com.liaozl.dao.base.Page;
import com.liaozl.dao.module.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author liaozuliang
 * @date 2016-09-27
 */
public interface UserMapper {

    /**
     * 查询公共库的表数据
     * @param id
     * @param name
     * @return
     */
    public User getUserByIdAndName(@Param("id") int id, @Param("name") String name);

}
