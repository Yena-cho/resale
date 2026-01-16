package com.finger.shinhandamoa.common;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


//@Intercepts({
//    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
//})
public class SqlLoggingPlugin implements Interceptor {

private boolean loggingSql = true;
private static final Logger logger = LoggerFactory.getLogger("sqlLog");

public void setLoggingSql(boolean loggingSql) {
    this.loggingSql = loggingSql;
}

@Override
public Object intercept(Invocation invocation) throws Throwable {

    Object[] args = invocation.getArgs();

    MappedStatement ms = (MappedStatement) args[0];
    if (loggingSql == true) {
        if (args[1] instanceof Map) {
            Map<String, Object> params = (Map<String, Object>) args[1];
            return loggingSql(ms, params, invocation);
        } else {
            String params = (String) args[1];
            return loggingSql(ms, params, invocation);
        }
    } else {
        return noLoggingSql(ms, invocation);
    }
}


private Object noLoggingSql(MappedStatement ms, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
    long start = System.currentTimeMillis();
    Object proceed = invocation.proceed();
    long finish = System.currentTimeMillis();
    logger.info("{} <== Elapsed: {}ms", ms.getId(), (finish - start));
    return proceed;
}
private Object loggingSql(MappedStatement ms, String params, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
    BoundSql boundSql = ms.getBoundSql(params);
    StringBuilder sql = new StringBuilder(boundSql.getSql());

    int index = sql.indexOf("?");
    sql.replace(index, index + 1, "'" + params + "'");

    long start = System.currentTimeMillis();
    Object proceed = invocation.proceed();
    long finish = System.currentTimeMillis();
    logger.info("{} ==>  Preparing: {}", ms.getId(), sql.toString());
    if (proceed != null && proceed instanceof List) {
        List<Object> list = (List<Object>) proceed;
        logger.info("{} <== Total: {} Elapsed: {}ms", ms.getId(), list.size(), (finish - start));
    } else {
        logger.info("{} <== Elapsed: {}ms", ms.getId(), (finish - start));
    }
    return proceed;
}


private Object loggingSql(MappedStatement ms, Map<String, Object> params, Invocation invocation) throws InvocationTargetException, IllegalAccessException {
    BoundSql boundSql = ms.getBoundSql(params);
    StringBuilder sql = new StringBuilder(boundSql.getSql());

    for (ParameterMapping param : boundSql.getParameterMappings()) {
        String property = param.getProperty();
        int index = sql.indexOf("?");
        sql.replace(index, index + 1, "'" + params.get(property) + "'");
    }

    long start = System.currentTimeMillis();
    Object proceed = invocation.proceed();
    long finish = System.currentTimeMillis();
    logger.info("{} ==>  Preparing: {}", ms.getId(), sql.toString());
    if (proceed != null && proceed instanceof List) {
        List<Object> list = (List<Object>) proceed;
        logger.info("{} <== Total: {} Elapsed: {}ms", ms.getId(), list.size(), (finish - start));
    } else {
        logger.info("{} <== Elapsed: {}ms", ms.getId(), (finish - start));
    }
    return proceed;
}
@Override
public Object plugin(Object target) {
    return Plugin.wrap(target, this);

}

@Override
public void setProperties(Properties properties) {
//    logger.info("properties => {}", properties);
//    String value = properties.getProperty("loggingSql");
	String value = "true";
    if (value.equals("true")) {
        loggingSql = true;
    } else {
        loggingSql = false;
    }

}

}
