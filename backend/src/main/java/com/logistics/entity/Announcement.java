package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统公告（内容为富文本）
 */
@Data
public class Announcement {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createTime;
}
