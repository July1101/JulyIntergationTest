package com.thg.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/22 20:19
 **/

@Slf4j
public class AssertUtils {

    public static <K,V> void assertEquals(Map<K,V> expect,
        Map<K,V> actual, boolean strictMode){
        if (strictMode && expect.size() != actual.size()) {
            log.error("Map size do not matches. expect is:{} but actual is:{}", expect.size(), actual.size());
            Assert.fail();
        }
        for (Entry<K, V> entry : expect.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if(!actual.containsKey(key)){
                log.error("Actual map has not the key:{}",key);
                Assert.fail();
            }
            Assert.assertEquals("map value is not equal",value,actual.get(key));
        }
    }

    public static <T> void assertEquals(Collection<T> expectList,Collection<T> actualList,boolean strictMode){
        if (expectList == null || actualList == null) {
            Assert.assertEquals(expectList, actualList);
        }
        List<T> expect = expectList.stream().distinct().collect(Collectors.toList());
        List<T> actual = actualList.stream().distinct().collect(Collectors.toList());
        if (strictMode && expect.size() != actual.size()) {
            log.error("List size do not matches. expect is:{} but actual is:{}", expect.size(), actual.size());
            Assert.fail();
        }
        expect.forEach(t-> Assert.assertTrue(actual.contains(t)));
    }
}
