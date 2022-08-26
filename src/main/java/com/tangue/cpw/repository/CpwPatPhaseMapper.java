package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.CpwPatPhase;
import org.springframework.stereotype.Repository;

@Repository
public interface CpwPatPhaseMapper extends BaseMapper<CpwPatPhase> {
    public CpwPatPhase getMaxNowDay(String cpwNo);
}
