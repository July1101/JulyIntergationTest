package com.thg.datasource;

import com.thg.datasource.routing.DataSourceKeyHolder;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */
public class DataSourceUtils {

    public static void setDataSourceByName(String name){
        DataSourceKeyHolder.set(name);
    }
}
