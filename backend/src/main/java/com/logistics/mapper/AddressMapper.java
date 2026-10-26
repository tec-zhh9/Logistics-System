package com.logistics.mapper;

import com.logistics.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface AddressMapper {

    @Select("SELECT * FROM address WHERE user_id = #{userId} ORDER BY is_default DESC, id DESC")
    List<Address> selectByUserId(Long userId);

    @Select("SELECT * FROM address WHERE id = #{id}")
    Address selectById(Long id);

    @Insert("INSERT INTO address(user_id, name, phone, address, is_default) " +
            "VALUES(#{userId}, #{name}, #{phone}, #{address}, #{isDefault})")
    void insert(Address address);

    @Update("UPDATE address SET name = #{name}, phone = #{phone}, address = #{address}, is_default = #{isDefault} WHERE id = #{id}")
    void update(Address address);

    @Update("UPDATE address SET is_default = 0 WHERE user_id = #{userId}")
    void resetDefault(Long userId);

    @Delete("DELETE FROM address WHERE id = #{id}")
    void deleteById(Long id);
}
