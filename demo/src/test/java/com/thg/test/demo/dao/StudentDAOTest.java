package com.thg.test.demo.dao;

import com.thg.test.demo.DemoApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Test;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/12 19:32
 **/
@SpringBootTest(classes = DemoApplication.class)
public class StudentDAOTest {

    @Autowired
    StudentDAO studentDAO;


    @Test
    public void test() {
        studentDAO.insertOne(12L,"tanhuigen",18);
    }
}