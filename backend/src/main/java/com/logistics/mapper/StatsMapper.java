package com.logistics.mapper;

import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 数据统计（首页看板 ECharts 图表 + 首页数据）
 */
public interface StatsMapper {

    /** 近一个月每天寄件订单金额（折线图） */
    @Select("SELECT DATE(create_time) AS name, IFNULL(SUM(amount), 0) AS value " +
            "FROM express_order WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(create_time) ORDER BY name")
    List<Map<String, Object>> orderAmountLast30Days();

    /** 不同网点寄件订单数量（柱状图） */
    @Select("SELECT np.name AS name, COUNT(o.id) AS value FROM network_point np " +
            "LEFT JOIN express_order o ON o.network_point_id = np.id " +
            "GROUP BY np.id, np.name ORDER BY value DESC")
    List<Map<String, Object>> orderCountByNetwork();

    /** 不同类别寄件数量占比（饼图） */
    @Select("SELECT c.name AS name, COUNT(o.id) AS value FROM category c " +
            "LEFT JOIN express_order o ON o.category_id = c.id " +
            "GROUP BY c.id, c.name ORDER BY value DESC")
    List<Map<String, Object>> orderCountByCategory();

    @Select("SELECT COUNT(*) FROM express_order WHERE courier_id = #{courierId} AND accept_time >= CURDATE()")
    Long todayOrderCount(Long courierId);

    @Select("SELECT IFNULL(SUM(amount), 0) FROM express_order " +
            "WHERE courier_id = #{courierId} AND status = '已签收' AND sign_time >= CURDATE()")
    BigDecimal todayIncome(Long courierId);

    /** 热门寄件网点（按订单量） */
    @Select("SELECT np.id, np.name, np.address, np.phone, COUNT(o.id) AS orderCount " +
            "FROM network_point np JOIN express_order o ON o.network_point_id = np.id " +
            "GROUP BY np.id, np.name, np.address, np.phone ORDER BY orderCount DESC LIMIT 5")
    List<Map<String, Object>> hotNetworkPoints();

    /** 热门物流资讯（按浏览量） */
    @Select("SELECT id, title, view_count AS viewCount, create_time AS createTime " +
            "FROM news ORDER BY view_count DESC LIMIT 5")
    List<Map<String, Object>> hotNews();
}
