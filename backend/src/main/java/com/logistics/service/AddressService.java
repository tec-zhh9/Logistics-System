package com.logistics.service;

import com.logistics.common.BizException;
import com.logistics.entity.Address;
import com.logistics.mapper.AddressMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Resource
    private AddressMapper addressMapper;

    public List<Address> listByUser(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    @Transactional
    public void add(Address address) {
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.resetDefault(address.getUserId());
        }
        addressMapper.insert(address);
    }

    @Transactional
    public void update(Address address) {
        Address old = addressMapper.selectById(address.getId());
        if (old == null || !old.getUserId().equals(address.getUserId())) {
            throw new BizException("地址不存在");
        }
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            addressMapper.resetDefault(address.getUserId());
        }
        addressMapper.update(address);
    }

    public void delete(Long id, Long userId) {
        Address old = addressMapper.selectById(id);
        if (old == null || !old.getUserId().equals(userId)) {
            throw new BizException("地址不存在");
        }
        addressMapper.deleteById(id);
    }
}
