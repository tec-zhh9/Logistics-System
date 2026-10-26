package com.logistics.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 普通用户
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String avatar;
    private String phone;
    private BigDecimal balance;      // 账户余额
    private String status;           // 正常 / 禁用
    private LocalDateTime createTime;
}
