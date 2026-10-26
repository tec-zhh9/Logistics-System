package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 寄件类别
 */
@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createTime;
}
