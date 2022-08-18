package com.tangue.cpw.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoVo {
    @Schema(name = "deptCode", description = "科室编号主键", example = "0102")
    private String deptCode;
    @Schema(name = "deptName", description = "科室名称", example = "耳鼻喉科")
    private String deptName;
    @Schema(name = "deptParent", description = "父级科室编号", example = "0102")
    private String deptParent;
    private Integer deptGrade;
    private String leafFlag;
    private String deptType;
    @Schema(hidden = true)//隐藏这个属性，文档中看不到
    private String innerFlag;
    private String useFlag;
}
