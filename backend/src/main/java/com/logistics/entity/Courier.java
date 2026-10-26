package com.logistics.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配送员
 */
@Data
public class Courier {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String avatar;
    private String phone;
    private Long regionId;             // 负责区域
    private BigDecimal balance;        // 可提现余额
    private BigDecimal totalIncome;    // 累计总收入
    private Integer orderCount;        // 累计接单数量
    private String status;             // 正常 / 禁用
    private LocalDateTime createTime;
}
