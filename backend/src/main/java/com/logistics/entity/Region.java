package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 区域
 */
@Data
public class Region {
    private Long id;
    private String name;
    private LocalDateTime createTime;
}
