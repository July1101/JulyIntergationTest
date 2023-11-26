package com.thg.deserialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thg.ComponentService;
import com.thg.deserialize.randomdeserializer.RandomDeserializer;
import com.thg.core.model.BaseCase;
import com.thg.utils.ComponentUtils;
import com.thg.utils.FileUtils;
import com.thg.utils.GenericClassUtils;
import com.thg.utils.JsonUtils;
import com.thg.utils.RandomUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/21 15:11
 **/
@Slf4j
@Service
public class DeserializeServiceImpl implements DeserializeService{


    private final static String NAME = "name";
    private final static String RANDOM = "random";
    private final static String MOCK = "mock";
    private final static String MOCK_KEY = "mockId";

    private final ObjectMapper randomMapper;


    private final Map<String,ComponentService> componentServiceMap;
    public DeserializeServiceImpl(List<ComponentService> componentServiceList) {
        this.componentServiceMap = componentServiceList.stream().collect(
            Collectors.toMap(ComponentService::getName,componentService->componentService));
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Object.class, new RandomDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(simpleModule);
        this.randomMapper = mapper;
    }

    @Override
    public <T extends BaseCase> List<T> deserializeFromFile(String file, Class<T> clazz) {

        //step1：先生成CID并写回case文件
        JsonUtils.deserializeWithWalkFileTree(this::generateCaseIdAndWriteToSingleFile,file,null);
        TypeReference<List<Map<String,Object>>> baseTypeReference = new TypeReference<List<Map<String, Object>>>() {};
        //step2: 将文件路径下case初步序列化
        List<Map<String,Object>> originCases =
            JsonUtils.deserializeWithFilePath(file,baseTypeReference,new ObjectMapper());
        //对每一个originCase进行深度序列化
        return originCases.stream().map(oneCase->
            deepDeserialize(oneCase,clazz)
        ).collect(Collectors.toList());
    }

    private <T extends BaseCase> T deepDeserialize(Map<String,Object> originCase,Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            //先反序列化通用部分，name、randomMap
            deserializeCommonPart(t,originCase);
            // randomMap生成objectMapper序列化器，然后再序列化其他字段
            ObjectMapper objectMapper = JsonUtils.generaterObjectMapper(t.getRandom());
            //先反序列化容器
            Map<String,Object> componentMap = new HashMap<>();
            for (Entry<String, Object> entry : originCase.entrySet()) {
                String key = entry.getKey();
                if(componentServiceMap.containsKey(key)){
                    //先获取服务
                    ComponentService componentService = componentServiceMap.get(key);
                    //再获取component类型
                    JavaType javaType = GenericClassUtils.getJavaTypeByComponentService(componentService.getClass());
                    //序列化value
                    Object componentValue;
                    componentValue = JsonUtils.deserialize(entry.getValue(), javaType, objectMapper);
                    //前置处理
                    Object afterInject = componentService.postProcessAfterDeserialize(componentValue);
                    //最后put到componentMap
                    componentMap.put(key,afterInject);
                }
            }
            t.setComponentMap(componentMap);
            //移除掉originCase中已经序列化key
            componentMap.forEach(originCase::remove);

            //最后反序列化extends实体类私有字段
            for (Entry<String, Object> entry : originCase.entrySet()) {
                String key = entry.getKey();
                ComponentUtils.setDeclaredField(t,key,entry.getValue(),objectMapper);
            }
            return t;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private <T extends BaseCase> void deserializeCommonPart(T t, Map<String, Object> originCase) {
        if (!originCase.containsKey(NAME)) {
            log.error("The case do not have case name");
            throw new RuntimeException("The case do not have case name");
        }
        t.setName(JsonUtils.deserialize(originCase.get(NAME), String.class));
        extractAndSetCaseId(t);
        if (originCase.containsKey(RANDOM)) {
            t.setRandom(JsonUtils.deserialize(originCase.get(RANDOM),
                new TypeReference<Map<String, Object>>() {
                }, randomMapper));
        }
    }

    private <T extends BaseCase> void extractAndSetCaseId(T t){
        String name = t.getName();
        String regEx = "(\\d+)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            Long caseId = Long.parseLong(matcher.group());
            t.setCaseId(caseId);
        }else{
            throw new RuntimeException("caseId parse error");
        }
    }


    private void generateCaseIdAndWriteToSingleFile(File toFile,Object[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            DefaultPrettyPrinter dpp = new DefaultPrettyPrinter();
            dpp.indentArraysWith(new DefaultIndenter("  ", "\n"));
            objectMapper.setDefaultPrettyPrinter(dpp);
            JsonNode root = objectMapper.readValue(toFile, JsonNode.class);
            boolean needWriteBackToFile = false;

            //如果caseName不是[CID:1234]格式，则加入该格式
            for (int i = 0; i < root.size(); i++) {
                JsonNode child = root.get(i);
                if (child.has("name")) {
                    String caseName = child.get("name").asText();
                    if (!caseName.matches("\\[CID:\\d+\\](.*)")) {
                        ((ObjectNode) child).put("name", generateNewCaseName(caseName));
                        needWriteBackToFile = true;
                    }
                }
            }
            //如果case文件有所改动，理应写回case文件
            if (needWriteBackToFile) {
                String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
                FileUtils.writeJsonToFile(str, toFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String generateNewCaseName(String oldCaseName) {
        return "[CID:" + RandomUtils.generateNewId() + "]" + oldCaseName;
    }
}
