package com.thg.datasource.property;

import lombok.Data;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */

@Data
public class DataSourceProperty {

    private String name;
    private boolean primary = false;

    private String url;
    private String username;
    private String password;
    private Integer maximumPoolSize = 30;
    private Integer minimumIdle;
}
