package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物流资讯（内容为富文本）
 */
@Data
public class News {
    private Long id;
    private String title;
    private String cover;
    private String content;
    private Integer viewCount;
    private LocalDateTime createTime;
}
