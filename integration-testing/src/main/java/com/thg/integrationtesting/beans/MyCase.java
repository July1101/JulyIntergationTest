package com.thg.integrationtesting.beans;

import com.thg.core.model.BaseCase;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by tanhuigen
 * Date 2022-10-16
 * Description 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyCase extends BaseCase {
    Long studentId;
    String studentName;
}
