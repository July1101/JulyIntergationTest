package com.thg.mysql.config;

/**
 * Created by tanhuigen
 * Date 2022-09-03
 * Description
 */
public class DataSourceSwitcher {

    private static final ThreadLocal<String> DS_NAME = new ThreadLocal<>();

    public static String currentDataSource(){
        return DS_NAME.get();
    }

    public static void setDatasource(String dataSourceName){
        if(dataSourceName==null){
            DS_NAME.remove();
        }else{
            DS_NAME.set(dataSourceName);
        }
    }

    public DataSourceSwitcher() {
    }
}
