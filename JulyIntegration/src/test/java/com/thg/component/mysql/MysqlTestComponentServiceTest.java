package com.thg.component.mysql;


import com.thg.IntegrationApplication;
import com.thg.component.mysql.MysqlTestComponentServiceTest.MyCase;
import com.thg.mysql.dao.BasicDAO;
import com.thg.core.action.TestBase;
import com.thg.core.model.BaseCase;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 19:19
 **/
@SpringBootTest(classes = IntegrationApplication.class)
public class MysqlTestComponentServiceTest extends TestBase<MyCase> {

    @Autowired
    BasicDAO basicDAO;


    @Override
    protected String filePath() {
        return "/data/case.json";
    }

    @Override
    protected void doAction(MyCase myCase) {
        Map<String,Object> map = new HashMap<>();
        map.put("student_id",123);
        basicDAO.deleteDBWithConditionMap("student",map);
    }

    @Data
    public static class MyCase extends BaseCase{
        String info;
        Long studentId;
    }

}