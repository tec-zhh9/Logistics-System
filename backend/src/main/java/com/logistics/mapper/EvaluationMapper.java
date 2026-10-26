package com.logistics.mapper;

import com.logistics.entity.Evaluation;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface EvaluationMapper {

    @Select("SELECT * FROM evaluation ORDER BY id DESC")
    List<Evaluation> selectAll();

    @Select("SELECT * FROM evaluation WHERE id = #{id}")
    Evaluation selectById(Long id);

    @Select("SELECT * FROM evaluation WHERE user_id = #{userId} ORDER BY id DESC")
    List<Evaluation> selectByUserId(Long userId);

    @Insert("INSERT INTO evaluation(user_id, order_id, courier_id, score, content) " +
            "VALUES(#{userId}, #{orderId}, #{courierId}, #{score}, #{content})")
    void insert(Evaluation evaluation);

    @Delete("DELETE FROM evaluation WHERE id = #{id}")
    void deleteById(Long id);
}
