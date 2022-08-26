package com.tangue.cpw.service;

import com.tangue.cpw.model.AdviceReturnDto;
import com.tangue.cpw.model.AdviceSelectVo;
import com.tangue.cpw.model.EnterPathVo;

import java.util.List;

public interface AdviceSelectService {
    public List<AdviceReturnDto> adviceSelect(AdviceSelectVo adviceSelectVo);
}
