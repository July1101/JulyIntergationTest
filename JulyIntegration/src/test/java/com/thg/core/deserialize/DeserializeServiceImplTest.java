package com.thg.core.deserialize;

import com.thg.IntegrationApplication;
import com.thg.core.model.BaseCase;
import com.thg.deserialize.DeserializeService;
import java.util.List;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;


/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/22 19:04
 **/

@SpringBootTest(classes = IntegrationApplication.class)
public class DeserializeServiceImplTest {

    @Autowired
    DeserializeService deserializeService;


    private final String filePath = "new/test_deserialize.json";

    @Data
    public static class MyCase extends BaseCase{
        String info;
        Long studentId;
    }

    @Test
    public void testDeserializeService() {
        List<MyCase> myCases = deserializeService.deserializeFromFile(filePath, MyCase.class);
        MyCase myCase = myCases.get(0);
        System.out.println("myCase.getName() = " + myCase.getName());
        System.out.println("myCase.getInfo() = " + myCase.getInfo());
        System.out.println("myCase.getStudentId() = " + myCase.getStudentId());
        System.out.println("myCases = " + myCases);
    }

}