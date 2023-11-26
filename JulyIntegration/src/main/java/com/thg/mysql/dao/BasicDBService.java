package com.thg.mysql.dao;

import com.thg.mysql.model.CheckMysqlData;
import com.thg.mysql.model.InitMysqlData;
import java.util.Map;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 15:47
 **/
public interface BasicDBService {

    void insertWithInitData(InitMysqlData initMysqlData);

    Integer deleteByInitData(InitMysqlData initMysqlData);


    Map<String, Object> selectByCheckData(CheckMysqlData checkMysqlData);

}
