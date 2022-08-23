package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.CpwCodeCpwDefine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpwCodeCpwDefineMapper extends BaseMapper<CpwCodeCpwDefine> {
    public CpwCodeCpwDefine getCpwDefine(@Param("cpwInDept") String cpwInDept, @Param("diagCode") String diagCode);
}
