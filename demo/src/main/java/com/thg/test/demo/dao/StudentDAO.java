package com.thg.test.demo.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by tanhuigen
 * Date 2022-10-16
 * Description 
 */
@Mapper
@Repository
public interface StudentDAO {


    @Insert("insert into student (student_id,name,age) values (#{studentId},#{name},#{age})")
    void insertOne(@Param("studentId")Long studentId,@Param("name") String name,@Param("age") int age);


    @Update("update student set name=#{name},age=#{age} where student_id=#{studentId}")
    void updateOne(@Param("studentId")Long studentId,@Param("name") String name,@Param("age") int age);

    @Select("select * from student where student_id=#{studentId} limit 1")
    Map<String,Object> selectOne(@Param("studentId")Long studentId);

}
