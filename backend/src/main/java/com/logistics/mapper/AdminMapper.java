package com.logistics.mapper;

import com.logistics.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AdminMapper {

    @Select("<script>SELECT * FROM admin" +
            "<where><if test='username != null and username != \"\"'>AND username LIKE CONCAT('%', #{username}, '%')</if></where>" +
            " ORDER BY id DESC</script>")
    List<Admin> selectAll(@Param("username") String username);

    @Select("SELECT * FROM admin WHERE id = #{id}")
    Admin selectById(Long id);

    @Select("SELECT * FROM admin WHERE username = #{username}")
    Admin selectByUsername(String username);

    @Insert("INSERT INTO admin(username, password, name, avatar, phone, email) " +
            "VALUES(#{username}, #{password}, #{name}, #{avatar}, #{phone}, #{email})")
    void insert(Admin admin);

    @Update("UPDATE admin SET name = #{name}, avatar = #{avatar}, phone = #{phone}, email = #{email} WHERE id = #{id}")
    void updateProfile(Admin admin);

    @Update("UPDATE admin SET password = #{password} WHERE id = #{id}")
    void updatePassword(Long id, String password);

    @Delete("DELETE FROM admin WHERE id = #{id}")
    void deleteById(Long id);
}
