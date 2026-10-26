package com.logistics.mapper;

import com.logistics.entity.FreightRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface FreightRuleMapper {

    @Select("SELECT * FROM freight_rule ORDER BY id DESC")
    List<FreightRule> selectAll();

    @Select("SELECT * FROM freight_rule WHERE id = #{id}")
    FreightRule selectById(Long id);

    @Select("SELECT * FROM freight_rule WHERE category_id = #{categoryId}")
    FreightRule selectByCategoryId(Long categoryId);

    @Insert("INSERT INTO freight_rule(category_id, first_weight, first_price, continued_price) " +
            "VALUES(#{categoryId}, #{firstWeight}, #{firstPrice}, #{continuedPrice})")
    void insert(FreightRule rule);

    @Update("UPDATE freight_rule SET category_id = #{categoryId}, first_weight = #{firstWeight}, " +
            "first_price = #{firstPrice}, continued_price = #{continuedPrice} WHERE id = #{id}")
    void update(FreightRule rule);

    @Delete("DELETE FROM freight_rule WHERE id = #{id}")
    void deleteById(Long id);
}
