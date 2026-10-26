package com.logistics.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 配送员认证（普通用户申请，管理员审核 —— 通用预约审核模型）
 */
@Data
public class CourierCert {
    private Long id;
    private Long userId;        // 申请人
    private String name;        // 真实姓名
    private String phone;
    private String idCard;      // 身份证号
    private Long regionId;      // 申请负责区域
    private String status;      // 待审核 / 已通过 / 已驳回
    private String reason;      // 审核意见
    private LocalDateTime createTime;
    private LocalDateTime auditTime;
}
