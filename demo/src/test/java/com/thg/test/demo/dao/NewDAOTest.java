package com.thg.test.demo.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockSettings;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/25 19:59
 **/

@RunWith(MockitoJUnitRunner.class)
public class NewDAOTest {

    NewDAO newDAO;


    @Test
    public void name() {

        InvocationListener invocationListener = new InvocationListener() {
            @Override
            public void reportInvocation(MethodInvocationReport methodInvocationReport) {

                System.out.println("methodInvocationReport.threwException() = "
                    + methodInvocationReport.threwException());
                System.out.println("methodInvocationReport.getInvocation() = "
                    + methodInvocationReport.getInvocation());
                System.out.println("methodInvocationReport.getLocationOfStubbing() = "
                    + methodInvocationReport.getLocationOfStubbing());
                System.out.println("methodInvocationReport.getThrowable() = "
                    + methodInvocationReport.getThrowable());
                System.out.println("methodInvocationReport.getReturnedValue() = "
                    + methodInvocationReport.getReturnedValue());
            }
        };
        MockSettings settings = Mockito.withSettings().invocationListeners(invocationListener);
        newDAO = Mockito.mock(NewDAO.class,settings);

        System.out.println("NewDAO.class.getFields().length = " + NewDAO.class.getDeclaredFields().length);

        Mockito.doReturn("hello").when(newDAO).getName();
//        Mockito.doReturn("123").when(newDAO).doRun(1);
        //Mockito.doReturn(10086).when(newDAO).getNum();
        Mockito.doAnswer(x->"123").when(newDAO).doRun(Mockito.anyInt());

        System.out.println("newDAO.getName() = " + newDAO.getName());
        System.out.println("newDAO.getName() = " + newDAO.getName());
        System.out.println("newDAO.getNum() = " + newDAO.getNum());
        System.out.println("newDAO.doRun(1) = " + newDAO.doRun(1));
        System.out.println("newDAO.doRun(2) = " + newDAO.doRun(2));

        MockingDetails mockingDetails = Mockito.mockingDetails(newDAO);
        System.out.println(
            "mockingDetails.printInvocations() = " + mockingDetails.printInvocations());

        for (Invocation invocation : mockingDetails.getInvocations()) {
            System.out.println(
                "invocation.getArgumentsAsMatchers() = " + invocation.getArgumentsAsMatchers());
            System.out.println("invocation.getLocation() = " + invocation.getLocation());
        }

        System.out.println("mockingDetails.getInvocations() = " + mockingDetails.getInvocations());
        System.out.println("mockingDetails.getStubbings() = " + mockingDetails.getStubbings());


    }
}