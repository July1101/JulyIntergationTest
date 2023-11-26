package com.thg.mock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/5/21 21:33
 **/

@Data
public class MockData {
    private String mockId;
    private Map<String,Object> methodParams;
}
