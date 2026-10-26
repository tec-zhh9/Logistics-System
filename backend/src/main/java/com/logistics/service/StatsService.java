package com.logistics.service;

import com.logistics.entity.Courier;
import com.logistics.mapper.CourierMapper;
import com.logistics.mapper.StatsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Resource
    private StatsMapper statsMapper;
    @Resource
    private CourierMapper courierMapper;

    /** 管理员看板：近一个月订单金额（折线图） */
    public List<Map<String, Object>> orderAmountLast30Days() {
        return statsMapper.orderAmountLast30Days();
    }

    /** 管理员看板：不同网点订单数量（柱状图） */
    public List<Map<String, Object>> orderCountByNetwork() {
        return statsMapper.orderCountByNetwork();
    }

    /** 管理员看板：不同类别订单数量占比（饼图） */
    public List<Map<String, Object>> orderCountByCategory() {
        return statsMapper.orderCountByCategory();
    }

    /** 配送员首页统计：今日接单数、今日收入、总接单数、总收入 */
    public Map<String, Object> courierHome(Long courierId) {
        Courier courier = courierMapper.selectById(courierId);
        Map<String, Object> data = new HashMap<>();
        data.put("todayOrderCount", statsMapper.todayOrderCount(courierId));
        data.put("todayIncome", statsMapper.todayIncome(courierId));
        data.put("totalOrderCount", courier == null ? 0 : courier.getOrderCount());
        data.put("totalIncome", courier == null ? BigDecimal.ZERO : courier.getTotalIncome());
        return data;
    }

    /** 用户首页：热门寄件网点 + 热门物流资讯 */
    public Map<String, Object> userHome() {
        Map<String, Object> data = new HashMap<>();
        data.put("hotNetworkPoints", statsMapper.hotNetworkPoints());
        data.put("hotNews", statsMapper.hotNews());
        return data;
    }
}
