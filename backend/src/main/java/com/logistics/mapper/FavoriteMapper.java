package com.logistics.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface FavoriteMapper {

    /** 查询用户收藏的网点详情（联表） */
    @Select("SELECT f.id, f.network_point_id AS networkPointId, np.name, np.region_id AS regionId, " +
            "np.address, np.phone, np.image, np.description, f.create_time AS createTime " +
            "FROM favorite f JOIN network_point np ON f.network_point_id = np.id " +
            "WHERE f.user_id = #{userId} ORDER BY f.id DESC")
    List<Map<String, Object>> selectMyFavorites(Long userId);

    @Select("SELECT COUNT(*) FROM favorite WHERE user_id = #{userId} AND network_point_id = #{networkPointId}")
    Long countByUserAndPoint(Long userId, Long networkPointId);

    @Insert("INSERT INTO favorite(user_id, network_point_id) VALUES(#{userId}, #{networkPointId})")
    void insert(Long userId, Long networkPointId);

    @Delete("DELETE FROM favorite WHERE user_id = #{userId} AND network_point_id = #{networkPointId}")
    void deleteByUserAndPoint(Long userId, Long networkPointId);
}
