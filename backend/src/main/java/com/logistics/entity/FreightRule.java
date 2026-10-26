package com.logistics.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运费规则（按寄件类别：首重 + 续重计费）
 */
@Data
public class FreightRule {
    private Long id;
    private Long categoryId;
    private BigDecimal firstWeight;      // 首重(kg)
    private BigDecimal firstPrice;       // 首重价格
    private BigDecimal continuedPrice;   // 续重单价(元/kg)
    private LocalDateTime createTime;
}
