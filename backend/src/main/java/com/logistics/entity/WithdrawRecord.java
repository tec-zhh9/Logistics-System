package com.logistics.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录（配送员发起，管理员审核）
 */
@Data
public class WithdrawRecord {
    private Long id;
    private Long courierId;
    private BigDecimal amount;
    private String status;      // 待审核 / 已通过 / 已驳回
    private String reason;      // 审核意见
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
}
