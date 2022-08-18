package com.tangue.cpw.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "code_department")
public class DemoPo {
    @TableId("dept_code")
    private String deptCode;
    private String deptName;
    private String deptParent;
    private Integer deptGrade;
    private String leafFlag;
    private String deptType;
    private String innerFlag;
    private String useFlag;
}
