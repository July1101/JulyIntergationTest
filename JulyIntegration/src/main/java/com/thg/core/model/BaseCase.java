package com.thg.core.model;

import java.util.Map;
import lombok.Data;


/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/21 15:07
 **/

@Data
public class BaseCase {
    Long caseId;
    String name;
    Map<String,Object> random;
    Map<String,Object> componentMap;
}
