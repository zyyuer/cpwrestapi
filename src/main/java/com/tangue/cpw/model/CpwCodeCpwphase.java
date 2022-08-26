package com.tangue.cpw.model;

import lombok.Data;

@Data
public class CpwCodeCpwphase {
    private Integer cpwVer;
    private String cpwCode;
    private Integer peNo;
    private String phaName;
    private String useFlag;
    private Integer begDay;
    private Integer endDay;
    private String uniteFllag;
    private String modifyFlag;
    private String operCode;
    private String operTime;
}
