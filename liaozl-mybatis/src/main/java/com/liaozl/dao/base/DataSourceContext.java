package com.liaozl.dao.base;

import com.alibaba.druid.util.StringUtils;

public class DataSourceContext {

    private static final ThreadLocal<String> dataSourceContext = new ThreadLocal<String>();

    public static void setDataSource(DataSourceEnum dataSource) {
        dataSourceContext.set(dataSource.toString());
    }

    public static String getDataSource() {
        String currentDB = dataSourceContext.get();
        if (StringUtils.isEmpty(currentDB)) {
            currentDB = DataSourceEnum.TEST1.toString();
        }
        return currentDB;
    }

    public static void clearDataSource() {
        dataSourceContext.remove();
    }

    public static void useTest1() {
        setDataSource(DataSourceEnum.TEST1);
    }

    public static void useTest2() {
        setDataSource(DataSourceEnum.TEST2);
    }

    private enum DataSourceEnum {
        TEST1,
        TEST2
    }
}
