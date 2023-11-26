package com.thg.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thg.TestContext;
import com.thg.deserialize.objectdeserializer.IntegerDeserializer;
import com.thg.deserialize.objectdeserializer.LongDeserializer;
import com.thg.deserialize.objectdeserializer.ObjectDeserializer;
import com.thg.deserialize.objectdeserializer.StringDeserializer;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import org.springframework.util.CollectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/21 11:10
 **/
public class JsonUtils {

    /**
     * 将通用Object序列化成指定类型
     * @param object object
     * @param clazz  序列化类型
     * @return  序列化后的数据
     * @param <T> 序列化后的类型
     */
    public static <T> T deserialize(Object object,Class<T> clazz) {
        return deserialize(object,clazz,new ObjectMapper());
    }

    public static Object deserializeBytes(byte[] bytes,JavaType javaType){
        try {
            return (new ObjectMapper()).readValue(bytes,javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(Object object,Class<T> clazz,ObjectMapper objectMapper) {
        try{
            return objectMapper.readValue(JSON.toJSONBytes(object),clazz);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    public static Object deserialize(Object object,JavaType javaType){
        ObjectMapper objectMapper = TestContext.cacheObjectMapper;
        return deserialize(object,javaType,objectMapper);
    }

    public static Object deserialize(Object object, JavaType javaType,ObjectMapper objectMapper){
        try{
            return objectMapper.readValue(JSON.toJSONBytes(object),javaType);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public static <T> List<T> deserializeToList(Object value, Class<T> clazz, ObjectMapper objectMapper) {
        CollectionType arrayType = objectMapper.getTypeFactory()
            .constructCollectionType(List.class, clazz);
        try {
            return objectMapper.readValue(JSON.toJSONBytes(value), arrayType);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 将通用Object序列化成指定类型
     */
    public static <T> T deserialize(Object object,TypeReference<T> typeReference) {
        return deserialize(object,typeReference,new ObjectMapper());
    }

    /**
     * 将通用Object序列化成指定类型
     */
    public static <T> T deserialize(Object object,TypeReference<T> typeReference,ObjectMapper objectMapper) {
        try{
            return objectMapper.readValue(JSON.toJSONBytes(object),typeReference);
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    public static <T> List<T> deserializeWithFilePath(String dataFilePath,
        TypeReference<List<T>> typeReference, ObjectMapper objectMapper){
        List<T> res = new ArrayList<>();
        deserializeWithWalkFileTree(JsonUtils::deserializeConsumerWithEachFile,dataFilePath,
            new Object[]{objectMapper,typeReference,res});
        return res;
    }

    static <T> void deserializeConsumerWithEachFile(File file, Object[] args) {
        try {
            ObjectMapper objectMapper = (ObjectMapper) args[0];
            TypeReference<List<T>> typeReference = (TypeReference<List<T>>) args[1];
            List<T> resList = (List<T>) args[2];
            List<T> partList = objectMapper.readValue(file, typeReference);
            if (!CollectionUtils.isEmpty(partList)) {
                resList.addAll(partList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 树形遍历目录下的所有文件/文件夹，对每个单独文件单独执行函数
     * @param function 执行的函数
     * @param filePath 遍历的文件路径
     * @param args     执行函数的其他参数
     */
    public static void deserializeWithWalkFileTree(BiConsumer<File,Object[]> function,String filePath,Object[] args){
        try {
            Path path = FileUtils.getFilePath(filePath);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                    File toFile = file.toFile();
                    function.accept(toFile,args);
                    return super.visitFile(file, attrs);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据randomMap生成objectMapper
     * @param randomMap 随机数Map
     * @return objectMapper
     */
    public static ObjectMapper generaterObjectMapper(@Nullable Map<String, Object> randomMap) {
        if (randomMap == null) {
            randomMap = new HashMap<>();
        }
        SimpleModule simpleModule = new SimpleModule();
        ObjectMapper objectMapper = new ObjectMapper();
        LongDeserializer longDeserializer = new LongDeserializer(randomMap);
        StringDeserializer stringDeserializer = new StringDeserializer(randomMap);
        IntegerDeserializer integerDeserializer = new IntegerDeserializer(randomMap);
        simpleModule.addDeserializer(Long.class, longDeserializer)
            .addDeserializer(long.class, longDeserializer);
        simpleModule.addDeserializer(Integer.class, integerDeserializer)
            .addDeserializer(int.class, integerDeserializer);
        simpleModule.addDeserializer(String.class, stringDeserializer);
        objectMapper.registerModules(simpleModule, new JavaTimeModule());
        return objectMapper;
    }

}
