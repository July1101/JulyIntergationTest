package com.thg.deserialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.thg.core.model.BaseCase;
import java.util.List;

/**
 * @Project: common
 * @author: tanhuigen@bytedance.com
 * @date: 2023/3/21 15:06
 **/
public interface DeserializeService{

    /**
     * 从文件中读取数据序列化
     * @param filePath 文件路径: 文件名or文件夹名
     * @param clazz 序列化类型
     * @return 返回case结构体
     */
    <T extends BaseCase> List<T> deserializeFromFile(String filePath,Class<T> clazz);

}
