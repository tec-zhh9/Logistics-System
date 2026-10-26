package com.logistics.mapper;

import com.logistics.entity.NetworkPoint;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NetworkPointMapper {

    @Select("<script>SELECT * FROM network_point" +
            "<where>" +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='regionId != null'>AND region_id = #{regionId}</if>" +
            "</where> ORDER BY id DESC</script>")
    List<NetworkPoint> selectAll(@Param("name") String name, @Param("regionId") Long regionId);

    @Select("SELECT * FROM network_point WHERE id = #{id}")
    NetworkPoint selectById(Long id);

    @Insert("INSERT INTO network_point(name, region_id, address, phone, image, description) " +
            "VALUES(#{name}, #{regionId}, #{address}, #{phone}, #{image}, #{description})")
    void insert(NetworkPoint point);

    @Update("UPDATE network_point SET name = #{name}, region_id = #{regionId}, address = #{address}, " +
            "phone = #{phone}, image = #{image}, description = #{description} WHERE id = #{id}")
    void update(NetworkPoint point);

    @Delete("DELETE FROM network_point WHERE id = #{id}")
    void deleteById(Long id);
}
