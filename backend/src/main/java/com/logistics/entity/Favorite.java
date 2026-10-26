package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 我的收藏（收藏网点）
 */
@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long networkPointId;
    private LocalDateTime createTime;
}
