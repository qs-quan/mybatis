package com.wayzim.arguementparse;

import com.wayzim.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-05 22:48
 */
interface RoleMapper {

    @Select("select * from t_role where id=#{id} and name=#{name}")
    List<User> selectById(String id, String name);
}

public class Application {

    public static void main(String[] args) {
        RoleMapper roleMapper = (RoleMapper) Proxy.newProxyInstance(
                RoleMapper.class.getClassLoader(),
                new Class[]{RoleMapper.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Map<String, Object> argNameMap = buildMethodArgNameMap(method, args);
                        Select annotation = method.getAnnotation(Select.class);
                        if (annotation != null) {
                            String sql = annotation.value()[0];
                            String sqlstr = parseSql(sql, argNameMap);
                            System.out.println(sql);
                            System.out.println(sqlstr);
                        }
                        return null;
                    }
                });

        roleMapper.selectById("1", "test");
    }

    public static String parseSql(String sql, Map<String, Object> argsNameMap) {
        StringBuilder sqlSb = new StringBuilder();
        char[] chars = sql.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '#') {
                int nextIndex = i + 1;
                if (chars[nextIndex] != '{') {
                    throw new RuntimeException(String.format("这里应该为#{\nsql:%s\nindex:%d", sql, nextIndex));
                }
                // 截取变量名
                StringBuilder agrNameSb = new StringBuilder();
                i = parseSqlAgr(agrNameSb, chars, i + 1);
                Object agrvalue = argsNameMap.get(agrNameSb.toString());
                if (agrvalue == null) {
                    throw new RuntimeException(String.format("找不到参数值argName:%s", agrNameSb.toString()));
                }
                sqlSb.append(agrvalue.toString());
                continue;
            }
            sqlSb.append(c);
        }
        return sqlSb.toString();
    }


    public static int parseSqlAgr(StringBuilder agrSb, char[] sql, int index) {
        index++;
        for (; index < sql.length; index++) {
            char c = sql[index];
            if (c != '}') {
                agrSb.append(c);
                continue;
            }
            if (c == '}') {
                return index;
            }
        }
        throw new RuntimeException(String.format("缺少右括号}\nsql:/%s\nindex%d", sql.toString(), index));

    }

    public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) {
        Map<String, Object> argsNameMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        int[] index = new int[]{0};
        Arrays.stream(parameters).forEach(parameter -> {
            argsNameMap.put(parameter.getName(), args[index[0]++]);
        });
        return argsNameMap;
    }
}
