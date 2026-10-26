package com.logistics.service;

import com.logistics.common.BizException;
import com.logistics.mapper.FavoriteMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    public List<Map<String, Object>> listByUser(Long userId) {
        return favoriteMapper.selectMyFavorites(userId);
    }

    public void add(Long userId, Long networkPointId) {
        if (favoriteMapper.countByUserAndPoint(userId, networkPointId) > 0) {
            throw new BizException("该网点已在收藏中");
        }
        favoriteMapper.insert(userId, networkPointId);
    }

    public void remove(Long userId, Long networkPointId) {
        favoriteMapper.deleteByUserAndPoint(userId, networkPointId);
    }
}
