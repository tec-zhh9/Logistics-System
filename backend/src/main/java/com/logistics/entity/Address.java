package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 我的地址
 */
@Data
public class Address {
    private Long id;
    private Long userId;
    private String name;       // 联系人
    private String phone;
    private String address;
    private Boolean isDefault; // 是否默认地址
    private LocalDateTime createTime;
}
