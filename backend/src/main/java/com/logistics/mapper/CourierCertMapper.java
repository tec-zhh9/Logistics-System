package com.logistics.mapper;

import com.logistics.entity.CourierCert;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CourierCertMapper {

    @Select("SELECT * FROM courier_cert ORDER BY id DESC")
    List<CourierCert> selectAll();

    @Select("SELECT * FROM courier_cert WHERE id = #{id}")
    CourierCert selectById(Long id);

    @Select("SELECT * FROM courier_cert WHERE user_id = #{userId} ORDER BY id DESC")
    List<CourierCert> selectByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM courier_cert WHERE user_id = #{userId} AND status = '待审核'")
    Long countPending(Long userId);

    @Insert("INSERT INTO courier_cert(user_id, name, phone, id_card, region_id) " +
            "VALUES(#{userId}, #{name}, #{phone}, #{idCard}, #{regionId})")
    void insert(CourierCert cert);

    @Update("UPDATE courier_cert SET status = #{status}, reason = #{reason}, audit_time = NOW() WHERE id = #{id}")
    void audit(Long id, String status, String reason);

    @Delete("DELETE FROM courier_cert WHERE id = #{id}")
    void deleteById(Long id);
}
