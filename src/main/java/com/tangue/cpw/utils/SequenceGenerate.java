package com.tangue.cpw.utils;

import com.tangue.cpw.repository.DualMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class SequenceGenerate {
    @Resource
    private DualMapper dualMapper;

    //生成样式序列号 20220823_00008
    public String getSequenceNo(String SequenceName) {
        String sequenceNo = dualMapper.getSequenceNo(SequenceName);
        String date = ValidUtils.DateToStr(new Date(), "yyyyMMdd");
        String sno = String.format("%05d", Integer.valueOf(sequenceNo));

        return date + "_" + sno;
    }
}
