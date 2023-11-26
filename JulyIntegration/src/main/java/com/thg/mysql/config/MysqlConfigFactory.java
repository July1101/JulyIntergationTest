package com.thg.mysql.config;

import static com.thg.exception.ExceptionConstants.MYSQL_PROPERTY_ERROR;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thg.config.properties.JulyMysqlProperties;
import com.thg.config.properties.JulyMysqlProperties.JulyDataSourceProperty;
import com.thg.exception.InvalidPropertyException;
import com.thg.mysql.dao.BasicDAO;
import com.thg.mysql.model.BaseMysqlData;
import com.thg.utils.ComponentUtils;
import com.thg.utils.FileUtils;
import com.thg.utils.JsonUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/7/8 15:27
 **/




@Component
@ConditionalOnProperty(prefix = "integration.datasource", name = "enable", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({JulyMysqlProperties.class})
public class MysqlConfigFactory {

    @Getter
    private final Map<String, BaseMysqlData> templateData;

    @Getter
    private final BasicDAO proxyBasicDAO;

    private final Map<String, SqlSessionTemplate> sqlSessionTemplateMap;

    public MysqlConfigFactory(JulyMysqlProperties julyMysqlProperties)
        throws NoSuchFileException, InvalidPropertyException {
        validateMysqlProperties(julyMysqlProperties);
        this.templateData = buildTemplateMapByFilePath(julyMysqlProperties.getTemplatePath());
        this.sqlSessionTemplateMap = buildSqlSessionTemplateMap(
            julyMysqlProperties.getProperties());
        this.proxyBasicDAO = (BasicDAO) Proxy.newProxyInstance(this.getClass().getClassLoader(),
            new Class[]{BasicDAO.class},
            new MapperInterceptor(sqlSessionTemplateMap, BasicDAO.class));
    }

    private void validateMysqlProperties(JulyMysqlProperties mysqlProperties)
        throws NoSuchFileException, InvalidPropertyException {
        String templatePath = mysqlProperties.getTemplatePath();
        if (StringUtils.hasText(templatePath) && !FileUtils.existsFile(templatePath)) {
            throw new NoSuchFileException(templatePath);
        }
        Map<String, JulyDataSourceProperty> properties = mysqlProperties.getProperties();
        if(CollectionUtils.isEmpty(properties)){
            throw new InvalidPropertyException(MYSQL_PROPERTY_ERROR,"no datasource properties");
        }
        for (Entry<String, JulyDataSourceProperty> entry : properties.entrySet()) {
            String dsName = entry.getKey();
            JulyDataSourceProperty property = entry.getValue();
            validateProperty(dsName, "no dsName");
            validateProperty(property.getUrl(), "no url");
            validateProperty(property.getUsername(), "no userName");
            validateProperty(property.getPassword(), "no password");
        }
    }


    private Map<String, BaseMysqlData> buildTemplateMapByFilePath(String mysqlTemplatePath){
        if (!StringUtils.hasText(mysqlTemplatePath)) {
            return new HashMap<>();
        }
        TypeReference<List<BaseMysqlData>> typeReference = new TypeReference<List<BaseMysqlData>>() {};
        List<BaseMysqlData> mysqlTemplates = JsonUtils.deserializeWithFilePath(mysqlTemplatePath,
            typeReference, new ObjectMapper());
        Map<String, BaseMysqlData> strictMap = mysqlTemplates.stream().collect(
            Collectors.toMap(ComponentUtils::buildMysqlStrictTemplateName, mysqlTemplate -> mysqlTemplate));
        Map<String, BaseMysqlData> basicMap = mysqlTemplates.stream().collect(
            Collectors.toMap(BaseMysqlData::getTable, mysqlTemplate -> mysqlTemplate));
        strictMap.putAll(basicMap);
        return strictMap;
    }

    private Map<String, SqlSessionTemplate> buildSqlSessionTemplateMap(
        Map<String, JulyDataSourceProperty> propertyMap) {
        return propertyMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey,
            entry -> createSqlSessionTemplate(entry.getValue())));
    }

    private SqlSessionTemplate createSqlSessionTemplate(JulyDataSourceProperty property) {
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        DataSource dataSource = createDataSource(property);
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setLogImpl(MyLog.class);
        configuration.addInterceptor(new MyBatisInterceptor());
        configuration.addMapper(BasicDAO.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private DataSource createDataSource(JulyDataSourceProperty property){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(property.getUrl());
        config.setUsername(property.getUsername());
        config.setPassword(property.getPassword());
        config.setDriverClassName(property.getDriverClassName());
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }

    private void validateProperty(String toCheck, String errorMsg) throws InvalidPropertyException {
        if (StringUtils.hasText(toCheck)) {
            return;
        }
        throw new InvalidPropertyException(MYSQL_PROPERTY_ERROR,
            "there has error property which has " + errorMsg);
    }

    @AllArgsConstructor
    private class MapperInterceptor implements InvocationHandler {

        private Map<String, SqlSessionTemplate> sessionTemplateMap;
        private Class<?> interfaceClass;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String currentDataSource = DataSourceSwitcher.currentDataSource();
            SqlSessionTemplate sqlSessionTemplate = sessionTemplateMap.get(currentDataSource);
            Object mapper = sqlSessionTemplate.getMapper(interfaceClass);
            return method.invoke(mapper, args);
        }
    }
}
