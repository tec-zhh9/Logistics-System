package com.logistics.mapper;

import com.logistics.entity.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface UserMapper {

    @Select("<script>SELECT * FROM user" +
            "<where>" +
            "<if test='username != null and username != \"\"'>AND username LIKE CONCAT('%', #{username}, '%')</if>" +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='phone != null and phone != \"\"'>AND phone LIKE CONCAT('%', #{phone}, '%')</if>" +
            "</where> ORDER BY id DESC</script>")
    List<User> selectAll(@Param("username") String username, @Param("name") String name, @Param("phone") String phone);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectById(Long id);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    @Insert("INSERT INTO user(username, password, name, phone) VALUES(#{username}, #{password}, #{name}, #{phone})")
    void insert(User user);

    @Update("UPDATE user SET name = #{name}, gender = #{gender}, avatar = #{avatar}, phone = #{phone} WHERE id = #{id}")
    void updateProfile(User user);

    @Update("UPDATE user SET balance = balance + #{amount} WHERE id = #{id}")
    void addBalance(Long id, BigDecimal amount);

    @Update("UPDATE user SET balance = #{balance} WHERE id = #{id}")
    void updateBalance(Long id, BigDecimal balance);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    void updatePassword(Long id, String password);

    @Update("UPDATE user SET status = #{status} WHERE id = #{id}")
    void updateStatus(Long id, String status);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteById(Long id);
}
