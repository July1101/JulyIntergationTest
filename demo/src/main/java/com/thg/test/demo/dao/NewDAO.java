package com.thg.test.demo.dao;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 16:52
 **/

@Data
@Component
public class NewDAO {
    @Value("123")
    private String name;

    public void say(){
        System.out.println("this.name = " + this.name);
    }

    public int getNum(){
        return 1;
    }

    public String doRun(int a){
        return "mm";
    }

}
