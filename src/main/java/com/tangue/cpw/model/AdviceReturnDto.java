package com.tangue.cpw.model;

import lombok.Data;

@Data
public class AdviceReturnDto {
    private String pid;
    private String regNo;
    private Integer adviceGroupno;
    private String adviceCode;
    private Integer adviceRowno;
    private String itemCode;
    private String itemName;
    private Double dosage;
    private String dosageUnitCode;
    private Double smallQty;
    private Double bigQty;
    private Integer unitFlag;
    private String freqCode;
    private String direCode;
    private String adviceFlag;
    private String adviceClass;
    private String adviceType;
}
