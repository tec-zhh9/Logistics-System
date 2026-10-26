package com.logistics.mapper;

import com.logistics.entity.WithdrawRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface WithdrawRecordMapper {

    @Select("SELECT * FROM withdraw_record ORDER BY id DESC")
    List<WithdrawRecord> selectAll();

    @Select("SELECT * FROM withdraw_record WHERE id = #{id}")
    WithdrawRecord selectById(Long id);

    @Select("SELECT * FROM withdraw_record WHERE courier_id = #{courierId} ORDER BY id DESC")
    List<WithdrawRecord> selectByCourierId(Long courierId);

    @Insert("INSERT INTO withdraw_record(courier_id, amount) VALUES(#{courierId}, #{amount})")
    void insert(Long courierId, double amount);

    @Update("UPDATE withdraw_record SET status = #{status}, reason = #{reason}, audit_time = NOW() WHERE id = #{id}")
    void audit(Long id, String status, String reason);

    @Delete("DELETE FROM withdraw_record WHERE id = #{id}")
    void deleteById(Long id);
}
