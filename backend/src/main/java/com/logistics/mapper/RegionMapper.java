package com.logistics.mapper;

import com.logistics.entity.Region;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RegionMapper {

    @Select("<script>SELECT * FROM region" +
            "<where><if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if></where>" +
            " ORDER BY id</script>")
    List<Region> selectAll(@Param("name") String name);

    @Select("SELECT * FROM region WHERE id = #{id}")
    Region selectById(Long id);

    @Insert("INSERT INTO region(name) VALUES(#{name})")
    void insert(Region region);

    @Update("UPDATE region SET name = #{name} WHERE id = #{id}")
    void update(Region region);

    @Delete("DELETE FROM region WHERE id = #{id}")
    void deleteById(Long id);
}
