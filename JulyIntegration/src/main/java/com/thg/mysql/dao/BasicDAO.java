package com.thg.mysql.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Created by tanhuigen
 * Date 2022-10-29
 * Description 
 */

public interface BasicDAO {


    // ignore_security_alert
    @Insert(
        "<script>\n" +
            "insert into ${table}\n"
            + "     <foreach collection=\"fields\" item=\"items\" open=\"(\" separator=\",\" close=\")\">\n"
            + "         ${items}\n"
            + "     </foreach>\n"
            + "     values\n"
            + "     <foreach collection=\"values\" item=\"val\" open=\"(\" separator=\",\" close=\")\">\n"
            + "          #{val}\n"
            + "     </foreach>\n"
            + "</script>"
    )
    void insertDBByTableAndFieldValueList(@Param("table") String table,
        @Param("fields") List<String> fields,
        @Param("values") List<Object> values);

    // ignore_security_alert
    @Insert(" <script>\n"
        + "     insert into ${table}\n"
        + "     <foreach collection=\"map.entrySet()\" index=\"key\" item=\"val\" open=\"(\" separator=\",\" close=\")\">\n"
        + "         ${key}\n"
        + "     </foreach>\n"
        + "     values\n"
        + "     <foreach collection=\"map.entrySet()\" item=\"val\" open=\"(\" separator=\",\" close=\")\">\n"
        + "          #{val}\n"
        + "     </foreach>\n"
        + "</script>  ")
    void insertDBByTableAndKeyValueMap(@Param("table") String table, @Param("map") Map<String,Object> map);

    // ignore_security_alert
    @Delete(" <script>\n"
        + "     delete from ${table}\n"
        + "     where\n"
        + "     <foreach collection=\"map.entrySet()\" index=\"key\" item=\"val\" open=\"(\" separator=\"and\" close=\")\">\n"
        + "         ${key} = #{val}\n"
        + "     </foreach>\n"
        + "</script>  ")
    int deleteDBWithConditionMap(@Param("table") String table, @Param("map") Map<String,Object> map);

    // ignore_security_alert
    @Select(" <script>\n"
        + "     select * from ${table}\n"
        + "     where\n"
        + "     <foreach collection=\"map.entrySet()\" index=\"key\" item=\"val\" open=\"(\" separator=\"and\" close=\")\">\n"
        + "         ${key} = #{val}\n"
        + "     </foreach>\n"
        + "</script>  ")
    List<Map<String,Object>> selectDB(@Param("table") String table, @Param("map") Map<String,Object> map);


}
