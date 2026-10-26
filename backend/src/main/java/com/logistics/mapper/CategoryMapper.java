package com.logistics.mapper;

import com.logistics.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CategoryMapper {

    @Select("<script>SELECT * FROM category" +
            "<where><if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if></where>" +
            " ORDER BY id</script>")
    List<Category> selectAll(@Param("name") String name);

    @Select("SELECT * FROM category WHERE id = #{id}")
    Category selectById(Long id);

    @Insert("INSERT INTO category(name, description) VALUES(#{name}, #{description})")
    void insert(Category category);

    @Update("UPDATE category SET name = #{name}, description = #{description} WHERE id = #{id}")
    void update(Category category);

    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteById(Long id);
}
