package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物流网点
 */
@Data
public class NetworkPoint {
    private Long id;
    private String name;
    private Long regionId;
    private String address;
    private String phone;
    private String image;
    private String description;
    private LocalDateTime createTime;
}
