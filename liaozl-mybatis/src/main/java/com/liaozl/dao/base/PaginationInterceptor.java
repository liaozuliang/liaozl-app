package com.liaozl.dao.base;

import com.liaozl.dao.base.dialect.Dialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 分页拦截器, 支持多种数据库
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor extends AbstractInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(PaginationInterceptor.class);

    private static int MAPPED_STATEMENT_INDEX = 0;
    private static int PARAMETER_INDEX = 1;
    private static int ROWBOUNDS_INDEX = 2;

    private Dialect dialect;

    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    private void processIntercept(final Object[] queryArgs) {
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];

        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        // 分页
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();

            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }

            limit = RowBounds.NO_ROW_LIMIT;

            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql), false);

            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;

            if (logger.isDebugEnabled()) {
                logger.debug("pageSql: " + sql);
            }
        } else if (parameter instanceof CountParameter) {// 获取总数
            CountParameter countParameter = (CountParameter) parameter;
            String countSql = "";
            BoundSql boundSql = null;

            if (countParameter.isHaveCountSql()) { //有统计sql，约定为sqlId + "_Count"
                boundSql = ms.getConfiguration()
                        .getMappedStatement(ms.getId() + "_Count")
                        .getBoundSql(countParameter.getParameter());
                countSql = boundSql.getSql().trim();
            } else { //
                boundSql = ms.getBoundSql(countParameter.getParameter());
                countSql = boundSql.getSql().trim();
                countSql = "select count(1) from (" + countSql + ") countTmp";
            }

            countSql = countSql.replaceAll("	", " ");

            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql), true);

            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
            queryArgs[PARAMETER_INDEX] = countParameter.getParameter();

            if (logger.isDebugEnabled()) {
                logger.debug("countSql: " + countSql);
            }
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String dialectClass = properties.getProperty("dialectClass");
        try {
            dialect = (Dialect) Class.forName(dialectClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
    }

    public static class CountParameter {
        private boolean haveCountSql;
        private Object parameter;

        public CountParameter(boolean haveCountSql, Object parameter) {
            this.haveCountSql = haveCountSql;
            this.parameter = parameter;
        }

        public Object getParameter() {
            return parameter;
        }

        public boolean isHaveCountSql() {
            return haveCountSql;
        }
    }

}
