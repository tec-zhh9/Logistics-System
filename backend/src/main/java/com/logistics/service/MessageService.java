package com.logistics.service;

import com.logistics.entity.Message;
import com.logistics.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Resource
    private MessageMapper messageMapper;

    /** 供其他业务调用的消息发送（订单状态变化时通知） */
    public void send(Long userId, String role, String title, String content, Long orderId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setRole(role);
        message.setTitle(title);
        message.setContent(content);
        message.setOrderId(orderId);
        messageMapper.insert(message);
    }

    public List<Message> listMy(Long userId, String role) {
        return messageMapper.selectMy(userId, role);
    }

    public void markRead(Long id) {
        messageMapper.markRead(id);
    }

    public void markAllRead(Long userId, String role) {
        messageMapper.markAllRead(userId, role);
    }

    public void delete(Long id) {
        messageMapper.deleteById(id);
    }
}
