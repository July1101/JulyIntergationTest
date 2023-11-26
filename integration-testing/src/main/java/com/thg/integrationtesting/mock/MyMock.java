package com.thg.integrationtesting.mock;

import com.thg.mock.model.JulyMock;
import com.thg.mock.model.MockMethod;
import com.thg.mock.model.UnMockMethod;
import com.thg.test.demo.dao.NewDAO;
import java.util.List;
import java.util.Map;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 16:47
 **/

@JulyMock
public class MyMock {

    @MockBean
    private NewDAO newDAO;

    @MockMethod(id="newDAO")
    public void mockNewDAO(String hello,
        List<String> world, Map<Long,String> map){
        String res = hello + world.toString() + map.toString();
        Mockito.doReturn(res).when(newDAO).getName();
    }

    @UnMockMethod(id = "newDAO")
    public void unMockNewDAO(){
        Mockito.reset(newDAO);
    }
}
