package com.liaozl.dao.base;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * 读、写分离，连接不同数据库，对于公共的表，采用直接加数据库名的方式做跨数据库链接
 */
@Intercepts(@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class}))
public class MultiDataSourceInterceptor extends AbstractInterceptor {

    private final static Logger logger = Logger.getLogger(MultiDataSourceInterceptor.class);

    private String commonDatabase;
    private static final List<String> commonTables = new ArrayList<String>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler statement = (RoutingStatementHandler) invocation.getTarget();
        BoundSql boundSql = statement.getBoundSql();
        String sql = boundSql.getSql();

        Connection con = (Connection) invocation.getArgs()[0];

        if ((sql.trim().substring(0, 6).contains("select")
                || sql.trim().substring(0, 6).contains("SELECT"))
                && !sql.contains("'queryMain'='queryMain'")
                && !sql.contains("\"queryMain\"=\"queryMain\"")) {
            con.setReadOnly(true);//读，连接从库
        }

        Object result = null;
        try {
            sql = appentDbNameForCommonTables(sql);
            FieldUtils.writeDeclaredField(boundSql, "sql", sql, true);

            if (logger.isDebugEnabled()) {
                logger.debug("sql = \n" + sql);
            }

            result = invocation.proceed();
        } finally {
            con.setReadOnly(false);//写，连接主库
        }

        return result;
    }

    /**
     * 把通用的表加上数据库名
     * @param sql
     * @return
     */
    private String appentDbNameForCommonTables(String sql) {
        if (commonDatabase == null || commonDatabase.trim().equals("") || commonTables.size() == 0) {
            return sql;
        }

        for (String table : commonTables) {
            char[] var = new char[1];
            var[0] = 10;
            sql = sql.replaceAll(new String(var), " ");
            sql = sql.replaceAll(table + " ", commonDatabase + "." + table + " ");
            sql = sql.replaceAll(table + "\\(", commonDatabase + "." + table + "(");
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 获取公共的表名
     */
    @Override
    public void setProperties(Properties properties) {
        String commonDataSource = properties.getProperty("commonDataSource");
        String commonDatabase = properties.getProperty("commonDatabase");
        String commonTables = properties.getProperty("commonTables");

        logger.info("Common database is : " + commonDatabase);
        logger.info("Common tables are : " + commonTables);

        if (commonTables == null || commonTables.trim().equals("")) {
            return;
        }

        this.commonDatabase = commonDatabase;
        for (String table : commonTables.split(",")) {
            MultiDataSourceInterceptor.commonTables.add(table);
        }
    }

    public static List<String> getCommonTables() {
        List<String> out = new ArrayList<String>();
        for (String table : commonTables) {
            out.add(table);
        }
        return out;
    }

}
