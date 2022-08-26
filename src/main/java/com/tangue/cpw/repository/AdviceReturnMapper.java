package com.tangue.cpw.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangue.cpw.model.AdviceReturnDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceReturnMapper extends BaseMapper<AdviceReturnDto> {
    public List<AdviceReturnDto> getAdviceReturn(
            @Param("regNo") String regNo,
            @Param("pid") String pid,
            @Param("peNo") Integer peNo
            );
}
