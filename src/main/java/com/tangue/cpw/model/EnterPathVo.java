package com.tangue.cpw.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@Schema
public class EnterPathVo {
    @Schema(name = "pid", description = "患者编号/病案号")
    @NotBlank(message = "患者编号不能为空")
    private String pid;

    @Schema(name = "regNo", description = "住院流水号")
    @NotBlank(message = "住院流水号不能为空")
    private String regNo;

    @Schema(name = "patName", description = "患者姓名")
    @NotBlank(message = "患者姓名不能为空")
    private String patName;

    @Pattern(regexp = "^[0,1,2]$", message = "性别只能是0、1、2")
    @Schema(name = "patSex", description = "患者性别", example = "0：未知1：男2：女")
    private String patSex;

    @NotBlank(message = "出生日期不能为空")
    @Length(min = 10, max = 10)
    @Schema(name = "birthday", description = "患者生日字符串", example = "1977.11.22")
    private String birthday;

    @NotNull(message = "住院次数不能为空")
    @Min(1)
    @Max(999)
    @Schema(name = "cnt", description = "患者住院次数")
    private Integer cnt;

    @NotNull(message = "患者身高不能为空")
    @Range(min=0,max=300,message="身高不正确")
    @Schema(name = "patHeight", description = "患者身高")
    private Double patHeight;

    @NotNull(message = "患者体重不能为空")
    @Range(min=0,max=200,message="体重不正确")
    @Schema(name = "patWeight", description = "患者体重")
    private Double patWeight;

    @NotBlank(message = "患者费别编码不能为空")
    @Schema(name = "feeCode", description = "患者费别编码")
    private String feeCode;

    @Pattern(regexp = "^[0,1,2]$", message = "婚姻状况只能是0、1、2")
    @Schema(name = "patMarry", description = "婚姻状况", example = "0：未知1：未婚2：已婚")
    private String patMarry;

    @NotBlank(message = "病区科室不能为空")
    @Schema(name = "inDept", description = "所在病区科室")
    private String inDept;

    @NotBlank(message = "诊断编码不能为空")
    @Schema(name = "icdCode", description = "诊断编码")
    private String icdCode;

    @NotBlank(message = "入院时间不能为空")
    @Length(min = 19, max = 19)
    @Schema(name = "inTime", description = "入院时间", example = "2022.08.22 22:54:33")
    private String inTime;

    @NotBlank(message = "住院医生编号不能为空")
    @Schema(name = "drOper", description = "住院医生编号")
    private String drOper;

    @NotBlank(message = "责任护士编号不能为空")
    @Schema(name = "nuOper", description = "责任护士编号")
    private String nuOper;

    @NotBlank(message = "护理等级不能为空")
    @Schema(name = "nuOper", description = "护理等级")
    private String nurseCls;

    @NotBlank(message = "床位号不能为空")
    @Schema(name = "bedCode", description = "床位号")
    private String bedCode;

    @NotBlank(message = "入径医生不能为空")
    @Schema(name = "inOper", description = "入径医生")
    private String inOper;

}
