package com.tangue.cpw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoVo {
    private String deptCode;
    private String deptName;
    private String deptParent;
    private Integer deptGrade;
    private String leafFlag;
    private String deptType;
    private String innerFlag;
    private String useFlag;
}
