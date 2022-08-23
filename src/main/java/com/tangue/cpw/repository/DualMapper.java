package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.DualPo;
import org.springframework.stereotype.Repository;

@Repository
public interface DualMapper extends BaseMapper<DualPo> {
    public DualPo getSysDate();

    public String getSequenceNo(String sequenceName);
}
