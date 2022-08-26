package com.tangue.cpw.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Schema(defaultValue = "医嘱存储实体PO")
public class CpwPatDradvice {
    private String regNo;
    private String cpwNo;
    private Integer peNo;
    private String rowFlag;
    private String ioadFlag;
    private String adviceCode;
    private String aeNo;
    private String phaseNo;
    private String dradNo;
    private String nowDay;
    private Integer nowNo;
    private Integer groupNo;
    private Integer rowNo;
    private String itemCode;
    private Double dosage;
    private String dosageUnitCode;
    private Double qty;
    private String qtyUnitCode;
    private Double price;
    private Double total;
    private String freqCode;
    private String direCode;
    private String adviceFlag;
    private String adviceClass;
    private String adviceType;
    private String stopWay;
    private String confFlag;
    private Integer execDays;
    private String begDate;
    private String begTime;
    private String proEndDay;
    private String checkDate;
    private String execDate;
    private String endDate;
    private String endTime;
    private String revFlag;
    private String revOper;
    private String revTime;
    private String hisDradNo;
    private String hisGroupNo;
    private String hisRowNo;
    private String hisTime;
    private String regDept;
    private String regOper;
    private String confDept;
    private String confOper;
    private String execDept;
    private String execOper;
    private String variaFlag;
    private String deptCode;
    private String operCode;
    private String stopDr;
    private String stopOper;
}
