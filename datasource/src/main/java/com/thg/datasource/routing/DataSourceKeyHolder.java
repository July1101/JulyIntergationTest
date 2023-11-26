package com.thg.datasource.routing;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */
public class DataSourceKeyHolder {

    private static final ThreadLocal<String> DS_NAME = new ThreadLocal<>();

    public static String get(){
        return DS_NAME.get();
    }

    public static void set(String dataSourceName){
        if(dataSourceName==null){
            DS_NAME.remove();
        }else{
            DS_NAME.set(dataSourceName);
        }
    }

    public DataSourceKeyHolder() {
    }
}
