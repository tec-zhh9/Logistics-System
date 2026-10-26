package com.logistics.mapper;

import com.logistics.entity.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AnnouncementMapper {

    @Select("<script>SELECT * FROM announcement" +
            "<where><if test='title != null and title != \"\"'>AND title LIKE CONCAT('%', #{title}, '%')</if></where>" +
            " ORDER BY id DESC</script>")
    List<Announcement> selectAll(@Param("title") String title);

    @Select("SELECT * FROM announcement WHERE id = #{id}")
    Announcement selectById(Long id);

    @Insert("INSERT INTO announcement(title, content) VALUES(#{title}, #{content})")
    void insert(Announcement announcement);

    @Update("UPDATE announcement SET title = #{title}, content = #{content} WHERE id = #{id}")
    void update(Announcement announcement);

    @Delete("DELETE FROM announcement WHERE id = #{id}")
    void deleteById(Long id);
}
