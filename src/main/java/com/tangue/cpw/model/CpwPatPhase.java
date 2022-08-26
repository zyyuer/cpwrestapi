package com.tangue.cpw.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CpwPatPhase {
    @TableId
    private String phaseNo;
    private String regNo;
    private String cpwNo;
    private Integer peNo;
    private Integer cpDateNo;
    private String nowDay;
    private Integer nowNo;
    private String conFlag;
    private String variaFlag;
    private String operCode;
    private String operDate;
    private String operTime;
    private String conOper;
    private String conDate;
    private String conTime;

}
