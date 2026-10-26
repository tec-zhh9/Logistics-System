package com.logistics.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 寄件订单
 * 状态流转：待接单 -> 已接单 -> 已送达 -> 已签收
 */
@Data
public class ExpressOrder {
    private Long id;
    private String orderNo;
    private Long userId;             // 寄件用户
    private Long courierId;          // 接单配送员
    private Long categoryId;
    private Long networkPointId;     // 寄件网点
    private BigDecimal weight;
    private BigDecimal amount;
    private String status;
    private String senderName;
    private String senderPhone;
    private String senderAddress;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime acceptTime;
    private LocalDateTime signTime;
}
