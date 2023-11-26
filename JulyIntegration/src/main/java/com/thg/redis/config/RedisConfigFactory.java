package com.thg.redis.config;

import static com.thg.exception.ExceptionConstants.MYSQL_PROPERTY_ERROR;
import static com.thg.exception.ExceptionConstants.REDIS_PROPERTY_ERROR;

import com.fasterxml.jackson.databind.JavaType;
import com.thg.config.properties.JulyRedisProperties;
import com.thg.exception.InvalidPropertyException;
import com.thg.redis.model.JulyRedisSerializer;
import com.thg.config.properties.JulyRedisProperties.ConvertProperty;
import com.thg.config.properties.JulyRedisProperties.RedisProperty;
import com.thg.redis.model.DefaultRedisSerializer;
import com.thg.utils.GenericClassUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/6/21 20:42
 **/

@Component
@Slf4j
@ConditionalOnProperty(prefix = "integration.redis", name = "enable", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({JulyRedisProperties.class})
public class RedisConfigFactory {

    private final Map<String,RedisTemplate<String,Object>> proxyRedisTemplateMap;

    @Getter
    private final Map<String,JavaType> jsonNodeConvertMap;
    
    @Getter
    private final Map<String, RedisSerializer> redisSerializerMap;

    public RedisConfigFactory(JulyRedisProperties julyRedisProperties,
        List<JulyRedisSerializer> beanSerializer) throws InvalidPropertyException {
        checkRedisProperties(julyRedisProperties);
        List<JulyRedisSerializer> propertySerializers = buildRedisSerializer(julyRedisProperties.getConvert());
        this.redisSerializerMap = buildRedisSerializerMap(propertySerializers, beanSerializer);
        this.jsonNodeConvertMap = buildInnerJsonNodeConvertMap(propertySerializers,beanSerializer);
        this.proxyRedisTemplateMap = buildProxyRedisTemplateMap(julyRedisProperties.getProperties());
        log.info("redisConfigFactory is build complete.");
    }


    public RedisTemplate<String, Object> getRedisTemplate(String redisName){
        return proxyRedisTemplateMap.get(redisName);
    }

    private Map<String,RedisTemplate<String, Object>> buildProxyRedisTemplateMap(
        List<RedisProperty> redisProperties) {
        return redisProperties.stream()
            .collect(Collectors.toMap(RedisProperty::getName, this::buildSingleRedisTemplate));
    }


    private RedisTemplate<String, Object> buildSingleRedisTemplate(RedisProperty property) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(property.getHost());
        config.setPort(property.getPort());
        config.setUsername(property.getUsername());
        config.setPassword(property.getPassword());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        jedisConnectionFactory.afterPropertiesSet();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return RedisTemplateCglibProxy.newInstance(redisTemplate);
    }


    private void checkRedisProperties(JulyRedisProperties julyRedisProperties)
        throws InvalidPropertyException {
        List<RedisProperty> properties = julyRedisProperties.getProperties();
        List<ConvertProperty> convert = julyRedisProperties.getConvert();
        if (CollectionUtils.isEmpty(properties)){
            throw new InvalidPropertyException(REDIS_PROPERTY_ERROR,"there is no redis property");
        }
        for (RedisProperty property : properties) {
            validateProperty(property.getName(),"no redis name");
            validateProperty(property.getHost(),"no host");
            validateProperty(property.getPort() == 0,"port is zero");
            validateProperty(property.getUsername(),"no username");
            validateProperty(property.getPassword(),"no password");
        }
        if (!CollectionUtils.isEmpty(convert)) {
            for (ConvertProperty convertProperty : convert) {
                validateProperty(convertProperty.getPath(),"no path in convert");
                validateProperty(convertProperty.getName(),"no name in convert");
            }
        }
    }

    private List<JulyRedisSerializer> buildRedisSerializer(
        List<ConvertProperty> convertProperties) {
        if (CollectionUtils.isEmpty(convertProperties)) {
            return null;
        }
        return convertProperties.stream().map(DefaultRedisSerializer::new)
            .collect(Collectors.toList());
    }


    private Map<String, RedisSerializer> buildRedisSerializerMap(
        List<JulyRedisSerializer> properties, List<JulyRedisSerializer> julyRedisSerializerList){
        Map<String, RedisSerializer> res = new HashMap<>();
        if(!CollectionUtils.isEmpty(properties)){
            res.putAll(
                properties.stream()
                    .collect(Collectors.toMap(JulyRedisSerializer::className, x -> x)));
        }
        //Serializer bean比default优先级更高
        if(!CollectionUtils.isEmpty(julyRedisSerializerList)){
            res.putAll(
                julyRedisSerializerList.stream()
                    .collect(Collectors.toMap(JulyRedisSerializer::className, x -> x)));
        }
        return res;
    }


    private Map<String, JavaType> buildInnerJsonNodeConvertMap(
        List<JulyRedisSerializer> properties, List<JulyRedisSerializer> beanSerializers) {
        Map<String, JavaType> convertMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(properties)){
            convertMap.putAll(
                properties.stream().collect(
                    Collectors.toMap(JulyRedisSerializer::className, this::getJavaTypeByByteConvert))
            );
        }
        if(!CollectionUtils.isEmpty(beanSerializers)){
            convertMap.putAll(
                beanSerializers.stream().collect(
                    Collectors.toMap(JulyRedisSerializer::className, this::getJavaTypeByByteConvert))
            );
        }
        return convertMap;
    }

    private JavaType getJavaTypeByByteConvert(JulyRedisSerializer serializer){
        return GenericClassUtils.getTypeOfInterfaceClass(serializer.getClass(),
            JulyRedisSerializer.class,0);
    }


    private void validateProperty(String toCheck, String errorMsg) throws InvalidPropertyException {
        validateProperty(!StringUtils.hasText(toCheck),errorMsg);
    }

    private void validateProperty(boolean exp, String errorMsg) throws InvalidPropertyException {
        if (exp) {
            throw new InvalidPropertyException(REDIS_PROPERTY_ERROR,
                "there has error property which has " + errorMsg);
        }
    }
}
