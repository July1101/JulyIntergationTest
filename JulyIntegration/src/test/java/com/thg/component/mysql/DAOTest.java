package com.thg.component.mysql;

import com.thg.IntegrationApplication;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.thg.mysql.dao.BasicDAO;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/24 23:19
 **/


@SpringBootTest(classes = IntegrationApplication.class)
public class DAOTest {

    @Autowired
    BasicDAO basicDAO;


    @Test
    public void test() {
        Map<String,Object> map = new HashMap<>();
        map.put("student_id",123);
        basicDAO.deleteDBWithConditionMap("student",map);

    }
}
