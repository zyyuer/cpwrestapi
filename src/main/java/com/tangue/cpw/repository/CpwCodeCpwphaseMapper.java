package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.CpwCodeCpwphase;
import org.springframework.stereotype.Repository;

@Repository
public interface CpwCodeCpwphaseMapper extends BaseMapper<CpwCodeCpwphase> {
    public CpwCodeCpwphase getMaxNowNo(Integer cpwVer, String cpwCode);
}
