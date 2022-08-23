package com.tangue.cpw.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
public class IpPatientReg {
    @TableId
    private String regNo;
    private String pid;
    private Integer cnt;
    private Double patHeight;
    private Double patWeight;
    private String feeCode;
    private String patMarry;
    private String inDept;
    private String inDate;
    private String inTime;
    private String outDept;
    private String outCase;
    private String outDate;
    private String outTime;
    private String regOper;
    private String regDate;
    private String regTime;
    private String chgFlag;

}
