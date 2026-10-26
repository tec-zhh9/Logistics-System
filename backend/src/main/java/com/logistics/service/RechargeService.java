package com.logistics.service;

import com.logistics.common.BizException;
import com.logistics.mapper.RechargeRecordMapper;
import com.logistics.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RechargeService {

    @Resource
    private RechargeRecordMapper rechargeMapper;
    @Resource
    private UserMapper userMapper;

    public List<com.logistics.entity.RechargeRecord> listAll() {
        return rechargeMapper.selectAll();
    }

    public List<com.logistics.entity.RechargeRecord> listByUser(Long userId) {
        return rechargeMapper.selectByUserId(userId);
    }

    /** 用户充值（模拟支付成功），余额同步增加 */
    @Transactional
    public void add(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("充值金额必须大于0");
        }
        rechargeMapper.insert(userId, amount.doubleValue());
        userMapper.addBalance(userId, amount);
    }

    public void delete(Long id) {
        rechargeMapper.deleteById(id);
    }
}
