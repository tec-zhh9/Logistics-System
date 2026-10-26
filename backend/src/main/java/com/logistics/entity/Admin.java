package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员
 */
@Data
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String phone;
    private String email;
    private LocalDateTime createTime;
}
