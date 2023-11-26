package com.thg.integrationtesting.cases;

import com.thg.IntegrationApplication;
import com.thg.core.action.TestBase;
import com.thg.datasource.routing.DataSourceKeyHolder;
import com.thg.integrationtesting.beans.MyCase;
import com.thg.integrationtesting.mock.MyMock;
import com.thg.mock.model.JulyMockScan;
import com.thg.test.demo.DemoApplication;
import com.thg.test.demo.dao.NewDAO;
import com.thg.test.demo.dao.StudentDAO;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by tanhuigen
 * Date 2022-10-16
 * Description 
 */
@ActiveProfiles(value = {"dev_test"})
//@Listeners(JulyTestListener.class)
@JulyMockScan(basePackageClasses = MyMock.class)
@SpringBootTest(classes = {IntegrationApplication.class, DemoApplication.class})
public class MyTest extends TestBase<MyCase> {

    @Autowired
    StudentDAO studentDAO;

    @Autowired
    NewDAO newDAO;

    @Autowired
    MyMock myMock;

    @Override
    protected String filePath() {
        return "/data/case.json";
    }


    @Override
    protected void doAction(MyCase myCase) {
        Long studentId = myCase.getStudentId();
        String studentName = myCase.getStudentName();
        studentDAO.updateOne(studentId,studentName,18);

        DataSourceKeyHolder.set("db2");
        studentDAO.insertOne(123L,"123",18);
        System.out.println("newDAO.getName() = " + newDAO.getName());
        System.out.println("Mockito.mockingDetails(newDAO).getInvocations() = "
            + Mockito.mockingDetails(newDAO).getInvocations());

    }
}
