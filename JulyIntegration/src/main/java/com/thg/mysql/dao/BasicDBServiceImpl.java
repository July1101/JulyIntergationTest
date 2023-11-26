package com.thg.mysql.dao;

import com.thg.mysql.config.MysqlConfigFactory;
import com.thg.mysql.model.CheckMysqlData;
import com.thg.mysql.model.InitMysqlData;
import com.thg.mysql.config.DataSourceSwitcher;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.Assert;


/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/18 15:47
 **/

@Slf4j
@Service
public class BasicDBServiceImpl implements BasicDBService {

    private final BasicDAO basicDAO;

    public BasicDBServiceImpl(MysqlConfigFactory mysqlConfigFactory) {
        this.basicDAO = mysqlConfigFactory.getProxyBasicDAO();
    }

    @Override
    public void insertWithInitData(InitMysqlData initMysqlData) {
        Map<String, Object> fields = initMysqlData.getFields();
        //如果fields为空则不插入返回
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        DataSourceSwitcher.setDatasource(initMysqlData.getDsName());
        basicDAO.insertDBByTableAndKeyValueMap(initMysqlData.getTable(), fields);
    }

    @Override
    public Integer deleteByInitData(InitMysqlData initMysqlData) {
        Map<String, Object> deleteKey = initMysqlData.getDeleteKey();
        if (CollectionUtils.isEmpty(deleteKey)) {
            return 0;
        }
        DataSourceSwitcher.setDatasource(initMysqlData.getDsName());
        return basicDAO.deleteDBWithConditionMap(initMysqlData.getTable(), deleteKey);
    }

    @Override
    public Map<String, Object> selectByCheckData(CheckMysqlData checkMysqlData) {
        Map<String, Object> selectKey = checkMysqlData.getSelectKey();
        if (CollectionUtils.isEmpty(selectKey)) {
            throw new RuntimeException(
                "The selectKeys do not exists in table:" + checkMysqlData.getTable());
        }
        DataSourceSwitcher.setDatasource(checkMysqlData.getDsName());
        List<Map<String, Object>> resList = basicDAO.selectDB(checkMysqlData.getTable(), selectKey);
        if (CollectionUtils.isEmpty(resList)) {
            return new HashMap<>();
        } else if (resList.size() > 1) {
            Assert.fail(
                "There is multi result of query and the number is " + resList.size()
                    + ", which table is " + checkMysqlData.getTable()
                    + " and the selectKey is" + checkMysqlData.getSelectKey());
        }
        return resList.get(0);
    }
}
