package com.liaozl.dao.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BaseDao extends SqlSessionDaoSupport {

    //mybatis-spring.jar 版本�?.x.x用这这种方式注入即可
    //@Resource
    //protected SqlSessionFactory sqlSessionFactory;

    @Resource
    protected JdbcTemplate jdbcTemplate;

    //mybatis-spring.jar 版本�?.2.x须用这这种方式注�?
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public int insert(String statement, Object parameter) {
        return getSqlSession().insert(statement, parameter);
    }

    public int update(String statement, Object parameter) {
        return getSqlSession().update(statement, parameter);
    }

    public int delete(String statement, Object parameter) {
        return getSqlSession().delete(statement, parameter);
    }

    public Object selectOne(String statement, Object parameter) {
        return getSqlSession().selectOne(statement, parameter);
    }

    public int selectCount(String statement, Object parameter) {
        return (int)selectOne(statement, parameter);
    }

    public List<?> selectList(String statement) {
        return getSqlSession().selectList(statement);
    }

    public List<?> selectList(String statement, Object parameter) {
        return getSqlSession().selectList(statement, parameter);
    }

    public <T> List<T> selectList(String statement, Object parameter, int pageSize, int pageIndex) {
        return getSqlSession().selectList(statement, parameter, new RowBounds((pageIndex - 1) * pageSize, pageSize));
    }

    public <T> Page<T> selectPage(String statement, Object parameter, Page<T> page) {
        return selectPage(statement, parameter, page, null);
    }

    /**
     * 分页查询，并统计总数
     * @param statement 查询sql
     * @param parameter 参数
     * @param page 分页对象
     * @param countStatement 统计sql
     * @param <T>
     * @return
     */
    public <T> Page<T> selectPage(String statement, Object parameter, Page<T> page, String countStatement) {
        if (StringUtils.isNoneBlank(countStatement)) {
            int count = selectCount(countStatement, parameter);
            page.setTotalRecord(count);
            if (count <= 0) {
                page.setData(new ArrayList<T>());
                return page;
            }
        } else if (page.isQueryRecordCount()) {
            int count = selectCount(statement, page.isHaveCountSql(), parameter);
            page.setTotalRecord(count);
            if (count <= 0) {
                page.setData(new ArrayList<T>());
                return page;
            }
        }

        List<T> dataList = selectList(statement, parameter, page.getRowNumber(), page.getPage());
        page.setData(dataList);

        return page;
    }

    public int selectCount(String statement, boolean haveCountSql, Object parameter) {
        return (Integer) getSqlSession().selectOne(statement, new PaginationInterceptor.CountParameter(haveCountSql, parameter));
    }

    public void executeSql(String sql) {
        jdbcTemplate.execute(sql);
    }
}
