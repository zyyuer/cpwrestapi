package com.tangue.cpw.model;

import lombok.Data;

@Data
public class CpwRegister {
    private String regNo;
    private String pid;
    private String cpwNo;
    private String icdCode;
    private Integer cpwVer;
    private String cpwCode;
    private String drOper;
    private String bedCode;
    private String nurseCls;
    private String nuOper;
    private String inDept;
    private String begDate;
    private String inOper;
    private String inDate;
    private String inTime;
    private String outDept;
    private String endDate;
    private String outOper;
    private String outDate;
    private String outTime;
    private String cpwregFlag;
}
