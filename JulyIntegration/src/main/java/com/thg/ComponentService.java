package com.thg;


/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/12 20:41
 *
 *
 **/
public interface ComponentService<T> {


    String getName();

    default T postProcessAfterDeserialize(T data){
        return data;
    };

    default void clearDataBeforeTest(T data){};

    default void initDataBeforeTest(T data){};

    void checkDataAfterTest(T data);

    default void clearDataAfterTest(T data){}

}
