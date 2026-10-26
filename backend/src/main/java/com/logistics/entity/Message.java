package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息提醒（订单状态变化时自动通知用户/配送员）
 */
@Data
public class Message {
    private Long id;
    private Long userId;      // 接收人ID
    private String role;      // 接收人角色: USER / COURIER
    private String title;
    private String content;
    private Long orderId;     // 关联订单
    private Boolean isRead;
    private LocalDateTime createTime;
}
