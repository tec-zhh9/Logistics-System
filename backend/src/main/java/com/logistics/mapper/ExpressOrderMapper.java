package com.logistics.mapper;

import com.logistics.entity.ExpressOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ExpressOrderMapper {

    @Select("<script>SELECT * FROM express_order" +
            "<where>" +
            "<if test='orderNo != null and orderNo != \"\"'>AND order_no LIKE CONCAT('%', #{orderNo}, '%')</if>" +
            "<if test='status != null and status != \"\"'>AND status = #{status}</if>" +
            "</where> ORDER BY id DESC</script>")
    List<ExpressOrder> selectAll(@Param("orderNo") String orderNo, @Param("status") String status);

    @Select("SELECT * FROM express_order WHERE user_id = #{userId} ORDER BY id DESC")
    List<ExpressOrder> selectByUserId(Long userId);

    @Select("SELECT * FROM express_order WHERE courier_id = #{courierId} ORDER BY id DESC")
    List<ExpressOrder> selectByCourierId(Long courierId);

    @Select("SELECT * FROM express_order WHERE status = '待接单' ORDER BY id DESC")
    List<ExpressOrder> selectPending();

    @Select("SELECT * FROM express_order WHERE id = #{id}")
    ExpressOrder selectById(Long id);

    @Insert("INSERT INTO express_order(order_no, user_id, category_id, network_point_id, weight, amount, status, " +
            "sender_name, sender_phone, sender_address, receiver_name, receiver_phone, receiver_address, remark) " +
            "VALUES(#{orderNo}, #{userId}, #{categoryId}, #{networkPointId}, #{weight}, #{amount}, #{status}, " +
            "#{senderName}, #{senderPhone}, #{senderAddress}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, #{remark})")
    void insert(ExpressOrder order);

    @Update("UPDATE express_order SET status = '已接单', courier_id = #{courierId}, accept_time = NOW() WHERE id = #{id}")
    void accept(Long id, Long courierId);

    @Update("UPDATE express_order SET status = '已送达' WHERE id = #{id}")
    void deliver(Long id);

    @Update("UPDATE express_order SET status = '已签收', sign_time = NOW() WHERE id = #{id}")
    void sign(Long id);

    @Delete("DELETE FROM express_order WHERE id = #{id}")
    void deleteById(Long id);
}
