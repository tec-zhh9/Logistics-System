package com.logistics.service;

import com.logistics.entity.FreightRule;
import com.logistics.mapper.FreightRuleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FreightRuleService {

    @Resource
    private FreightRuleMapper freightRuleMapper;

    public List<FreightRule> list() {
        return freightRuleMapper.selectAll();
    }

    public FreightRule getByCategoryId(Long categoryId) {
        return freightRuleMapper.selectByCategoryId(categoryId);
    }

    public void add(FreightRule rule) {
        freightRuleMapper.insert(rule);
    }

    public void update(FreightRule rule) {
        freightRuleMapper.update(rule);
    }

    public void delete(Long id) {
        freightRuleMapper.deleteById(id);
    }
}
