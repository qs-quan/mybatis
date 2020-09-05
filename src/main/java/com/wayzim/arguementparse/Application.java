package com.wayzim.arguementparse;

import com.wayzim.pojo.User;
import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-05 22:48
 */
interface RoleMapper {

    @Select("select * from t_role where id={#id}")
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
                        Map<String, Object> methodArgNameMap = buildMethodArgNameMap(method, args);
                        System.out.println(methodArgNameMap);
                        return null;
                    }
                });

        roleMapper.selectById("1","test");
    }

    public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) {
        Map<String, Object> map = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        int[] index = new int[]{0};
        Arrays.stream(parameters).forEach(parameter -> {
            map.put(parameter.getName(), args[index[0]++]);
        });
        return map;
    }
}
