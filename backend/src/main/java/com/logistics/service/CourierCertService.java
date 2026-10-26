package com.logistics.service;

import com.logistics.common.BizException;
import com.logistics.entity.Courier;
import com.logistics.entity.CourierCert;
import com.logistics.entity.User;
import com.logistics.mapper.CourierCertMapper;
import com.logistics.mapper.CourierMapper;
import com.logistics.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourierCertService {

    @Resource
    private CourierCertMapper certMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CourierMapper courierMapper;
    @Resource
    private MessageService messageService;

    public List<CourierCert> listAll() {
        return certMapper.selectAll();
    }

    public List<CourierCert> listByUser(Long userId) {
        return certMapper.selectByUserId(userId);
    }

    /** 普通用户申请认证为配送员 */
    @Transactional
    public void apply(CourierCert cert) {
        Long userId = cert.getUserId();
        if (certMapper.countPending(userId) > 0) {
            throw new BizException("您已有一笔认证申请正在审核中，请耐心等待");
        }
        certMapper.insert(cert);
        messageService.send(userId, "USER", "认证申请已提交",
                "您的配送员认证申请已提交，请等待管理员审核。", null);
    }

    /** 管理员审核：通过后自动将用户升级为配送员账号 */
    @Transactional
    public void audit(Long id, String status, String reason) {
        CourierCert cert = certMapper.selectById(id);
        if (cert == null) {
            throw new BizException("认证申请不存在");
        }
        if (!"待审核".equals(cert.getStatus())) {
            throw new BizException("该申请已审核");
        }
        certMapper.audit(id, status, reason);

        if ("已通过".equals(status)) {
            User user = userMapper.selectById(cert.getUserId());
            if (user != null && courierMapper.selectByUsername(user.getUsername()) == null) {
                Courier courier = new Courier();
                courier.setUsername(user.getUsername());
                courier.setPassword(user.getPassword());   // 沿用原密码
                courier.setName(cert.getName());
                courier.setGender(user.getGender());
                courier.setPhone(cert.getPhone());
                courier.setRegionId(cert.getRegionId());
                courierMapper.insert(courier);
            }
        }
        messageService.send(cert.getUserId(), "USER",
                "认证审核" + ("已通过".equals(status) ? "通过" : "未通过"),
                "您的配送员认证申请审核结果：" + status + (reason == null || reason.isBlank() ? "" : "，审核意见：" + reason), null);
    }

    public void delete(Long id) {
        certMapper.deleteById(id);
    }
}
