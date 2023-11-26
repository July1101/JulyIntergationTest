package com.thg.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.thg.ComponentService;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.core.DefaultParameterNameDiscoverer;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/20 21:11
 **/
public class GenericClassUtils {

    static final DefaultParameterNameDiscoverer dis = new DefaultParameterNameDiscoverer();

    public static String[] getParameterNames(Method method){
        return dis.getParameterNames(method);
    }

    public static JavaType getJavaTypeByComponentService(Class<?> clazz){
        return getTypeOfInterfaceClass(clazz, ComponentService.class,0);
    }

    public static Type getTypeOfSuperClass(Class<?> clazz, int index) {
        Type superclass = clazz.getGenericSuperclass();
        if (!(superclass instanceof ParameterizedType)) {
            throw new RuntimeException("Your class does not have generic type");
        }
        Type[] typeArguments = ((ParameterizedType) superclass).getActualTypeArguments();
        if (index >= typeArguments.length) {
            throw new RuntimeException("The index is more than typeArguments length");
        }
        return typeArguments[index];
    }

    public static JavaType getTypeOfInterfaceClass(Class<?> clazz, Class<?> interfaceClass,
        int index) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        Type[] typeArguments = null;
        for (Type genericInterface : genericInterfaces) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
            if (parameterizedType.getRawType().equals(interfaceClass)) {
                typeArguments = parameterizedType.getActualTypeArguments();
                break;
            }
        }
        if (typeArguments == null) {
            throw new RuntimeException("There no interfaces:" + interfaceClass.getName()
                + " in class:" + clazz.getName());
        }
        if (index >= typeArguments.length) {
            throw new RuntimeException("The index is more than typeArguments length");
        }
        return TypeFactory.defaultInstance().constructType(typeArguments[index]);
    }

}
