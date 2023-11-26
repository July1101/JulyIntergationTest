package com.thg.mock.config;

import com.thg.TestContext;
import com.thg.config.postprocessor.PostProcessService;
import com.thg.mock.model.MockMethod;
import com.thg.mock.model.UnMockMethod;
import com.thg.mock.model.TestMock;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.util.ReflectionUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/4 15:54
 **/
public class MockMethodPostProcessServiceImpl implements PostProcessService {

    @Override
    public boolean hit(Object bean, String beanName) {
        return TestContext.mockBeanMap.containsKey(beanName);
        //return AnnotationUtils.isAnnotationDeclaredLocally(JulyMock.class,bean.getClass());
    }

    @Override
    public Object beanProcess(Object bean, String beanName) {
        injectMockFieldToBean(bean,TestContext.mockBeanMap.get(beanName));
        registryTestMock(bean);
        return bean;
    }

    private void injectMockFieldToBean(Object bean,Map<String,Object> mockMap){
        Arrays.stream(bean.getClass().getDeclaredFields())
            .filter(x->mockMap.containsKey(x.getName()))
            .forEach(field->{
                field.setAccessible(true);
                Object mockValue = mockMap.get(field.getName());
                ReflectionUtils.setField(field,bean,mockValue);
            });
    }

    private void registryTestMock(Object bean) {
        Method[] methods = bean.getClass().getDeclaredMethods();
        Map<String, Method> mockMethodMap = filterMockMethod(methods);
        Map<String, Method> unMockMethodMap = filterUnMockMethod(methods);
        buildTestMockListAndPutContext(mockMethodMap, unMockMethodMap, bean);
    }

    Map<String, Method> filterMockMethod(Method[] methods) {
        return Arrays.stream(methods).filter((v) -> v.getAnnotation(MockMethod.class) != null)
            .collect(Collectors.toMap(
                method -> method.getAnnotation(MockMethod.class).id(),
                method -> method));
    }

    Map<String, Method> filterUnMockMethod(Method[] methods) {
        return Arrays.stream(methods).filter(v -> v.getAnnotation(UnMockMethod.class) != null)
            .collect(Collectors.toMap(
                method -> method.getAnnotation(UnMockMethod.class).id(),
                method -> method));
    }

    /**
     * mockMethod不为null,unMockMethod有可能为null
     */
    void buildTestMockListAndPutContext(Map<String, Method> mocks,
        Map<String, Method> unMocks, Object bean) {
        List<TestMock> result = new ArrayList<>();
        for (Entry<String, Method> entry : mocks.entrySet()) {
            String mockId = entry.getKey();
            Method mockMethod = entry.getValue();
            Method unMockmethod = unMocks.get(mockId);
            result.add(new TestMock(mockId, bean, mockMethod, unMockmethod));
        }
        result.forEach(
            testMock -> TestContext.testMockMap.putIfAbsent(testMock.getMockId(), testMock));
    }

}
