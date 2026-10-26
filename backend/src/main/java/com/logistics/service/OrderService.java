package com.logistics.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.logistics.common.BizException;
import com.logistics.common.UserContext;
import com.logistics.entity.ExpressOrder;
import com.logistics.entity.FreightRule;
import com.logistics.entity.User;
import com.logistics.mapper.ExpressOrderMapper;
import com.logistics.mapper.FreightRuleMapper;
import com.logistics.mapper.UserMapper;
import com.logistics.mapper.CourierMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private ExpressOrderMapper orderMapper;
    @Resource
    private FreightRuleMapper freightRuleMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourierMapper courierMapper;
    @Resource
    private MessageService messageService;

    public List<ExpressOrder> listAll(String orderNo, String status) {
        return orderMapper.selectAll(orderNo, status);
    }

    public List<ExpressOrder> listByUser(Long userId) {
        return orderMapper.selectByUserId(userId);
    }

    public List<ExpressOrder> listByCourier(Long courierId) {
        return orderMapper.selectByCourierId(courierId);
    }

    public List<ExpressOrder> listPending() {
        return orderMapper.selectPending();
    }

    public ExpressOrder getById(Long id) {
        return orderMapper.selectById(id);
    }

    /** 根据运费规则计算运费：首重 + 续重（不足1kg按1kg向上取整） */
    public BigDecimal calcFreight(BigDecimal weight, FreightRule rule) {
        BigDecimal amount = rule.getFirstPrice();
        if (weight.compareTo(rule.getFirstWeight()) > 0) {
            BigDecimal extraKg = weight.subtract(rule.getFirstWeight())
                    .divide(BigDecimal.ONE, 0, RoundingMode.CEILING);
            amount = amount.add(extraKg.multiply(rule.getContinuedPrice()));
        }
        return amount;
    }

    /** 用户下单：计算运费 -> 校验并扣除余额 -> 生成订单 -> 发送消息提醒 */
    @Transactional
    public void add(ExpressOrder order) {
        Long userId = UserContext.getUserId();
        User user = userMapper.selectById(userId);

        FreightRule rule = freightRuleMapper.selectByCategoryId(order.getCategoryId());
        if (rule == null) {
            throw new BizException("该寄件类别未配置运费规则");
        }
        BigDecimal amount = calcFreight(order.getWeight(), rule);
        if (user.getBalance().compareTo(amount) < 0) {
            throw new BizException("账户余额不足，请先充值");
        }

        userMapper.updateBalance(userId, user.getBalance().subtract(amount));

        order.setUserId(userId);
        order.setOrderNo("EX" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(4));
        order.setAmount(amount);
        order.setStatus("待接单");
        orderMapper.insert(order);

        messageService.send(userId, "USER", "寄件下单成功",
                "您的订单 " + order.getOrderNo() + " 已创建，支付金额 " + amount + " 元，等待配送员接单。", order.getId());
    }

    /** 配送员接单 */
    @Transactional
    public void accept(Long orderId) {
        Long courierId = UserContext.getUserId();
        ExpressOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (!"待接单".equals(order.getStatus())) {
            throw new BizException("该订单已被接单");
        }
        orderMapper.accept(orderId, courierId);
        messageService.send(order.getUserId(), "USER", "订单已接单",
                "您的订单 " + order.getOrderNo() + " 已由配送员接单，请耐心等待配送。", orderId);
    }

    /** 配送员送达 */
    @Transactional
    public void deliver(Long orderId) {
        ExpressOrder order = orderMapper.selectById(orderId);
        if (order == null || !"已接单".equals(order.getStatus())) {
            throw new BizException("订单当前状态不可送达");
        }
        orderMapper.deliver(orderId);
        messageService.send(order.getUserId(), "USER", "订单已送达",
                "您的订单 " + order.getOrderNo() + " 已送达，请及时签收。", orderId);
    }

    /** 用户签收：结算配送员收入 */
    @Transactional
    public void sign(Long orderId) {
        ExpressOrder order = orderMapper.selectById(orderId);
        if (order == null || !"已送达".equals(order.getStatus())) {
            throw new BizException("订单当前状态不可签收");
        }
        orderMapper.sign(orderId);
        if (order.getCourierId() != null) {
            // 配送员收入 = 订单金额，累计总收入、余额、接单数
            courierMapper.addIncome(order.getCourierId(), order.getAmount());
        }
        messageService.send(order.getCourierId(), "COURIER", "订单已签收",
                "订单 " + order.getOrderNo() + " 已被用户签收，收入 " + order.getAmount() + " 元已入账。", orderId);
    }

    public void delete(Long id) {
        orderMapper.deleteById(id);
    }
}
