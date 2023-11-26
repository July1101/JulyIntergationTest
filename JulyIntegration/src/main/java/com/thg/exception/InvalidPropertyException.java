package com.thg.exception;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/8 16:29
 **/
public class InvalidPropertyException extends Exception{

    private final String errorMessage;

    public InvalidPropertyException(String location,String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
