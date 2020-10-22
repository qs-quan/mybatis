package com.wayzim.interceptor;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.io.StringReader;
import java.sql.Connection;
import java.util.Properties;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-10-20 15:44
 */
@Intercepts({
        @Signature(
                type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class
        })
})
public class MySqlInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //先拦截到RoutingStatementHandler，里面有个StatementHandler类型的delegate变量，其实现类是BaseStatementHandler，然后就到BaseStatementHandler的成员变量mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        //id为执行的mapper方法的全路径名，如com.uv.dao.UserMapper.insertUser
        String id = mappedStatement.getId();
        //sql语句类型 select、delete、insert、update
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if(sqlCommandType == SqlCommandType.SELECT){
            BoundSql boundSql = statementHandler.getBoundSql();
            //获取到原始sql语句
            String sql = boundSql.getSql();
            //获取对应拦截实体类
            Class<?> classType = Class.forName(mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
            //获取对应拦截方法名
            String mName = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1, mappedStatement.getId().length());

            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            StringBuffer whereSql = new StringBuffer();
            Select select = (Select) parserManager.parse(new StringReader(sql));
            PlainSelect plain = (PlainSelect) select.getSelectBody();
            //增加sql语句的逻辑部分处理
            if (!sql.toLowerCase().contains("del_flag")) {
                whereSql.append("del_flag = 0");
            }
            // 获取当前查询条件
            Expression where = plain.getWhere();
            if (where == null) {
                if (whereSql.length() > 0) {
                    Expression expression = CCJSqlParserUtil
                            .parseCondExpression(whereSql.toString());
                    Expression whereExpression = (Expression) expression;
                    plain.setWhere(whereExpression);
                }
            } else {
                if (whereSql.length() > 0) {
                    //where条件之前存在，需要重新进行拼接
                    whereSql.append(" and ( " + where.toString() + " )");
                } else {
                    //新增片段不存在，使用之前的sql
                    whereSql.append(where.toString());
                }
                Expression expression = CCJSqlParserUtil
                        .parseCondExpression(whereSql.toString());
                plain.setWhere(expression);
            }
            metaObject.setValue("delegate.boundSql.sql", select.toString());
        }


        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}