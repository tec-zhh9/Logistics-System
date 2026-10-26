package com.logistics.mapper;

import com.logistics.entity.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NewsMapper {

    @Select("<script>SELECT * FROM news" +
            "<where><if test='title != null and title != \"\"'>AND title LIKE CONCAT('%', #{title}, '%')</if></where>" +
            " ORDER BY id DESC</script>")
    List<News> selectAll(@Param("title") String title);

    @Select("SELECT * FROM news WHERE id = #{id}")
    News selectById(Long id);

    @Insert("INSERT INTO news(title, cover, content) VALUES(#{title}, #{cover}, #{content})")
    void insert(News news);

    @Update("UPDATE news SET title = #{title}, cover = #{cover}, content = #{content} WHERE id = #{id}")
    void update(News news);

    @Update("UPDATE news SET view_count = view_count + 1 WHERE id = #{id}")
    void addViewCount(Long id);

    @Delete("DELETE FROM news WHERE id = #{id}")
    void deleteById(Long id);
}
