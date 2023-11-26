package com.thg.mysql;

import com.thg.ComponentService;
import com.thg.mysql.config.MysqlConfigFactory;
import com.thg.mysql.dao.BasicDBServiceImpl;
import com.thg.mysql.model.BaseMysqlData;
import com.thg.mysql.model.CheckMysqlData;
import com.thg.mysql.model.InitMysqlData;
import com.thg.mysql.model.MysqlData;
import com.thg.utils.ComponentUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.testng.Assert;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/12 20:54
 **/


@Service
public class MysqlComponentService implements ComponentService<MysqlData> {


    final BasicDBServiceImpl basicDBService;
    final Map<String, BaseMysqlData> templateDataMap;

    public MysqlComponentService(BasicDBServiceImpl basicDBService,
        MysqlConfigFactory mysqlConfigFactory) {
        this.basicDBService = basicDBService;
        this.templateDataMap= mysqlConfigFactory.getTemplateData();
    }

    @Override
    public String getName() {
        return "mysql";
    }

    @Override
    public MysqlData postProcessAfterDeserialize(MysqlData data) {
        if (data == null || CollectionUtils.isEmpty(data.getInitDataSet())) {
            return data;
        }
        List<InitMysqlData> initDataSet = data.getInitDataSet();
        data.setInitDataSet(
            initDataSet.stream().map(this::replaceTemplateToData).collect(Collectors.toList()));
        return data;
    }

    @Override
    public void clearDataBeforeTest(MysqlData data) {
        clearMysqlData(data);
    }

    @Override
    public void initDataBeforeTest(MysqlData data) {
        List<InitMysqlData> initDataSet = data.getInitDataSet();
        if (CollectionUtils.isEmpty(initDataSet)) {
            return;
        }
        for (InitMysqlData initMysqlData : initDataSet) {
            basicDBService.insertWithInitData(initMysqlData);
        }
    }

    @Override
    public void checkDataAfterTest(MysqlData data) {
        List<CheckMysqlData> checkDataSet = data.getCheckDataSet();
        if (CollectionUtils.isEmpty(checkDataSet)) {
            return;
        }
        for (CheckMysqlData checkMysqlData : checkDataSet) {
            Map<String, Object> actualRes = basicDBService.selectByCheckData(checkMysqlData);
            Map<String, Object> expectRes = checkMysqlData.getFields();
            checkResult(actualRes,expectRes);
        }
    }

    @Override
    public void clearDataAfterTest(MysqlData data) {
        clearMysqlData(data);
    }

    void clearMysqlData(MysqlData data){
        List<InitMysqlData> initDataSet = data.getInitDataSet();
        if (CollectionUtils.isEmpty(initDataSet)) {
            return;
        }
        for (InitMysqlData initMysqlData : initDataSet) {
            basicDBService.deleteByInitData(initMysqlData);
        }
    }

    void checkResult(Map<String, Object> actualRes,Map<String, Object> expectRes){
        if(CollectionUtils.isEmpty(actualRes)) {
            Assert.assertTrue(CollectionUtils.isEmpty(expectRes));
        }
        for (Entry<String, Object> entry : expectRes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!actualRes.containsKey(key)){
                Assert.fail("The actual res do not have the field: "+key);
            }
            Assert.assertEquals(String.valueOf(actualRes.get(key)),String.valueOf(value));
        }
    }

    InitMysqlData replaceTemplateToData(InitMysqlData initMysqlData) {
        Map<String, Object> template = getTemplateFields(initMysqlData);
        //如果mode不使用模版或者initFields为空
        if (!initMysqlData.getUseModel() ||
            CollectionUtils.isEmpty(initMysqlData.getFields()) ||
            CollectionUtils.isEmpty(template)) {
            return initMysqlData;
        }
        for (Entry<String, Object> entry : template.entrySet()) {
            String key = entry.getKey();
            if (!initMysqlData.getFields().containsKey(key)) {
                initMysqlData.getFields().put(key, entry.getValue());
            }
        }
        return initMysqlData;
    }

    Map<String, Object> getTemplateFields(InitMysqlData initMysqlData) {
        String strictKey = ComponentUtils.buildMysqlStrictTemplateName(initMysqlData);
        if (templateDataMap.containsKey(strictKey)) {
            return templateDataMap.get(strictKey).getFields();
        }
        if (templateDataMap.containsKey(initMysqlData.getTable())) {
            return templateDataMap.get(initMysqlData.getTable()).getFields();
        }
        return null;
    }

}
