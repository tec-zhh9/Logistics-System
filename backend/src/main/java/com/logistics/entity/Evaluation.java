package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评价
 */
@Data
public class Evaluation {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long courierId;
    private Integer score;      // 1-5 分
    private String content;
    private LocalDateTime createTime;
}
