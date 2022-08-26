package com.tangue.cpw.service;

import com.tangue.cpw.model.AdviceCommitDto;
import com.tangue.cpw.model.AdviceReturnDto;
import com.tangue.cpw.model.AdviceSelectVo;
import com.tangue.cpw.utils.ValidableList;

import java.util.List;

public interface AdviceCommitService {
    public String adviceCommit(ValidableList<AdviceCommitDto> adviceCommitDto);
}
