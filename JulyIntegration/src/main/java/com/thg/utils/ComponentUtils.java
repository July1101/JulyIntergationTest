package com.thg.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.mysql.model.BaseMysqlData;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.util.CollectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 19:50
 **/
public class ComponentUtils {

    /**
     * 获取类中泛型的类型
     * @param clazz 类名
     * @return 类中泛型的类型
     */
    public static Class<?> getInterfaceClass(Class<?> clazz){
        Type[] types = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments();
        return (Class<?>)types[0];
    }

    /**
     * 比较两个map是否相似，expect有的actual都有
     * @param expect 预期map
     * @param actual 实际map
     * @return 是否相似
     * @param <T> 泛型
     */
    public static <T> boolean compareMap(Map<String,T> expect,Map<String,T> actual){
        if(CollectionUtils.isEmpty(expect)){
            return CollectionUtils.isEmpty(actual);
        }
        for (Entry<String, T> entry : expect.entrySet()) {
            if(!actual.containsKey(entry.getKey())){
                return false;
            }
            if(!actual.get(entry.getKey()).equals(entry.getValue())){
                return false;
            }
        }
        return true;
    }

    public static void setDeclaredField(Object t,String key, Object value){
        try{
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if(name.equals(key)){
                    Object deserializeValue = JsonUtils.deserialize(value, field.getType());
                    field.set(t,deserializeValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void setDeclaredField(Object t,String key, Object value, ObjectMapper objectMapper){
        try{
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                if(name.equals(key)){
                    Object deserializeValue = JsonUtils.deserialize(value, field.getType(),objectMapper);
                    field.set(t,deserializeValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String buildMysqlStrictTemplateName(BaseMysqlData baseMysqlData) {
        return baseMysqlData.getDsName() + "-" + baseMysqlData.getTable();
    }

}
