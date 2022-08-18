package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.DemoPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemoMapper extends BaseMapper<DemoPo> {
    //新增方法查找所有父类SQL注入方式
    public List<DemoPo> selectAllParent();
}