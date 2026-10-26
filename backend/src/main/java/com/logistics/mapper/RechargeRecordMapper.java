package com.logistics.mapper;

import com.logistics.entity.RechargeRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RechargeRecordMapper {

    @Select("SELECT * FROM recharge_record ORDER BY id DESC")
    List<RechargeRecord> selectAll();

    @Select("SELECT * FROM recharge_record WHERE user_id = #{userId} ORDER BY id DESC")
    List<RechargeRecord> selectByUserId(Long userId);

    @Insert("INSERT INTO recharge_record(user_id, amount) VALUES(#{userId}, #{amount})")
    void insert(Long userId, double amount);

    @Delete("DELETE FROM recharge_record WHERE id = #{id}")
    void deleteById(Long id);
}
