package com.logistics.service;

import com.logistics.common.BizException;
import com.logistics.entity.Courier;
import com.logistics.entity.WithdrawRecord;
import com.logistics.mapper.CourierMapper;
import com.logistics.mapper.WithdrawRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WithdrawService {

    @Resource
    private WithdrawRecordMapper withdrawMapper;
    @Resource
    private CourierMapper courierMapper;
    @Resource
    private MessageService messageService;

    public List<WithdrawRecord> listAll() {
        return withdrawMapper.selectAll();
    }

    public List<WithdrawRecord> listByCourier(Long courierId) {
        return withdrawMapper.selectByCourierId(courierId);
    }

    /** 配送员发起提现：先冻结扣减余额，驳回时退回 */
    @Transactional
    public void add(Long courierId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException("提现金额必须大于0");
        }
        Courier courier = courierMapper.selectById(courierId);
        if (courier.getBalance().compareTo(amount) < 0) {
            throw new BizException("可提现余额不足");
        }
        courierMapper.deductBalance(courierId, amount);
        withdrawMapper.insert(courierId, amount.doubleValue());
        messageService.send(courierId, "COURIER", "提现申请已提交",
                "您的提现申请（" + amount + " 元）已提交，请等待管理员审核。", null);
    }

    /** 管理员审核提现：驳回时退回余额 */
    @Transactional
    public void audit(Long id, String status, String reason) {
        WithdrawRecord record = withdrawMapper.selectById(id);
        if (record == null) {
            throw new BizException("提现记录不存在");
        }
        if (!"待审核".equals(record.getStatus())) {
            throw new BizException("该提现申请已审核");
        }
        withdrawMapper.audit(id, status, reason);
        if ("已驳回".equals(status)) {
            courierMapper.addBalance(record.getCourierId(), record.getAmount());
        }
        messageService.send(record.getCourierId(), "COURIER",
                "提现审核" + ("已通过".equals(status) ? "通过" : "未通过"),
                "您的提现申请（" + record.getAmount() + " 元）审核结果：" + status
                        + (reason == null || reason.isBlank() ? "" : "，审核意见：" + reason), null);
    }

    public void delete(Long id) {
        withdrawMapper.deleteById(id);
    }
}
