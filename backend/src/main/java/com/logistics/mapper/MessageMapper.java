package com.logistics.mapper;

import com.logistics.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MessageMapper {

    @Select("SELECT * FROM message WHERE user_id = #{userId} AND role = #{role} ORDER BY id DESC")
    List<Message> selectMy(Long userId, String role);

    @Select("SELECT * FROM message WHERE id = #{id}")
    Message selectById(Long id);

    @Insert("INSERT INTO message(user_id, role, title, content, order_id) " +
            "VALUES(#{userId}, #{role}, #{title}, #{content}, #{orderId})")
    void insert(Message message);

    @Update("UPDATE message SET is_read = 1 WHERE id = #{id}")
    void markRead(Long id);

    @Update("UPDATE message SET is_read = 1 WHERE user_id = #{userId} AND role = #{role}")
    void markAllRead(Long userId, String role);

    @Delete("DELETE FROM message WHERE id = #{id}")
    void deleteById(Long id);
}
