package com.logistics.mapper;

import com.logistics.entity.Courier;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface CourierMapper {

    @Select("<script>SELECT * FROM courier" +
            "<where>" +
            "<if test='username != null and username != \"\"'>AND username LIKE CONCAT('%', #{username}, '%')</if>" +
            "<if test='name != null and name != \"\"'>AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "</where> ORDER BY id DESC</script>")
    List<Courier> selectAll(@Param("username") String username, @Param("name") String name);

    @Select("SELECT * FROM courier WHERE id = #{id}")
    Courier selectById(Long id);

    @Select("SELECT * FROM courier WHERE username = #{username}")
    Courier selectByUsername(String username);

    @Insert("INSERT INTO courier(username, password, name, gender, phone, region_id) " +
            "VALUES(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{regionId})")
    void insert(Courier courier);

    @Update("UPDATE courier SET name = #{name}, gender = #{gender}, avatar = #{avatar}, phone = #{phone} WHERE id = #{id}")
    void updateProfile(Courier courier);

    @Update("UPDATE courier SET balance = balance + #{amount}, total_income = total_income + #{amount}, " +
            "order_count = order_count + 1 WHERE id = #{id}")
    void addIncome(Long id, BigDecimal amount);

    @Update("UPDATE courier SET balance = balance + #{amount} WHERE id = #{id}")
    void addBalance(Long id, BigDecimal amount);

    @Update("UPDATE courier SET balance = balance - #{amount} WHERE id = #{id}")
    void deductBalance(Long id, BigDecimal amount);

    @Update("UPDATE courier SET password = #{password} WHERE id = #{id}")
    void updatePassword(Long id, String password);

    @Update("UPDATE courier SET status = #{status} WHERE id = #{id}")
    void updateStatus(Long id, String status);

    @Delete("DELETE FROM courier WHERE id = #{id}")
    void deleteById(Long id);
}
