package com.tangue.cpw.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Schema
public class AdviceCommitDto {
    @Schema(name = "pid", description = "患者编号/病案号")
    @NotBlank(message = "患者编号不能为空")
    private String pid;

    @Schema(name = "regNo", description = "住院流水号")
    @NotBlank(message = "住院流水号不能为空")
    private String regNo;

    @Schema(name = "adviceCode", description = "路径模板中的医嘱号", example = "-")
    @NotBlank(message = "路径模板医嘱号不能为空，路径外医嘱传'-'")
    private String adviceCode;

    @Schema(name = "adviceGroupno", description = "路径模板中的医嘱组号", example = "0")
    @NotNull(message = "路径模板医嘱组号不能为空，路径外医嘱传0")
    private Integer adviceGroupno;

    @Schema(name = "rowNo", description = "路径模板中的医嘱组中的行号", example = "0")
    @NotNull(message = "路径模板医嘱行号不能为空，路径外医嘱传0")
    private Integer rowNo;

    @Schema(name = "hisDradNo", description = "his中同一患者唯一的his医嘱号")
    @NotBlank(message = "患者在his中的医嘱唯一编号")
    private String hisDradNo;

    @Schema(name = "hisGroupNo", description = "his中患者医嘱的组号")
    @NotNull(message = "患者在his中的医嘱的组号")
    private Integer hisGroupNo;

    @Schema(name = "hisRowNo", description = "his中患者医嘱的行号")
    @NotNull(message = "患者在his中的医嘱的行号")
    private Integer hisRowNo;

    @Schema(name = "rowFlag", description = "1路径内，2his下达，3")
    @Pattern(regexp = "^[1,2,3]$", message = "医嘱标示只能是1、2、3")
    private String rowFlag;

    @Schema(name = "itemCode", description = "医嘱项目唯一编码")
    @NotBlank(message = "医嘱项目编码不能为空")
    private String itemCode;

    @Schema(name = "itemName", description = "医嘱项目名称")
    @NotBlank(message = "医嘱项目名称不能为空")
    private String itemName;

    @Schema(name = "dosage", description = "医嘱单次用量")
    @NotNull(message = "医嘱单次用量不能为空")
    private Double dosage;

    @Schema(name = "dosageUnitCode", description = "医嘱单次用量单位")
    @NotBlank(message = "医嘱单次用量单位不能为空")
    private String dosageUnitCode;

    @Schema(name = "qty", description = "医嘱单次合计数量")
    @NotNull(message = "医嘱单次合计数量不能为空")
    private Double qty;

    @Schema(name = "qtyUnitCode", description = "医嘱单次合计数量单位")
    @NotBlank(message = "医嘱单次合计数量单位不能为空")
    private String qtyUnitCode;

    @Schema(name = "price", description = "医嘱单次数量单价")
    @NotNull(message = "医嘱单次数量单价不能为空")
    private Double price;

    @Schema(name = "total", description = "医嘱单次价格合计")
    @NotNull(message = "医嘱单次数量单价不能为空")
    private Double total;

    @Schema(name = "freqCode", description = "医嘱频次")
    @NotBlank(message = "医嘱频次不能为空")
    private String freqCode;

    @Schema(name = "direCode", description = "医嘱用法")
    @NotBlank(message = "医嘱用法不能为空")
    private String direCode;

    @Schema(name = "adviceFlag", description = "1长，2临，3出院带药")
    @Pattern(regexp = "^[1,2,3]$", message = "医嘱效期类型只能是1、2、3")
    private String adviceFlag;

    @Schema(name = "adviceClass", description = "医嘱类型，01普通，02护理，03手术，11检查，12检验...", example = "01")
    @NotBlank(message = "医嘱类型不能为空")
    private String adviceClass;

    @Schema(name = "adviceFlag", description = "1医嘱，2护嘱", example = "1")
    @Pattern(regexp = "^[1,2]$", message = "医嘱效期类型只能是1、2")
    private String adviceType;

    @Schema(name = "begTime", description = "长嘱医嘱开始时间")
    @NotBlank(message = "长期医嘱开始时间不能为空")
    private String begTime;

    @Schema(name = "stopWay", description = "停嘱方式，2手工停", example = "2")
    @Pattern(regexp = "^[0,1,2,3,9]$", message = "停嘱方式只能是0、1、2、3、9")
    private String stopWay;

    @Schema(name = "endTime", description = "长嘱停嘱时间")
    private String endTime;

    @Schema(name = "regDept", description = "开嘱科室编号")
    @NotBlank(message = "开嘱科室编号不能为空")
    private String regDept;

    @Schema(name = "regOper", description = "开嘱医生编号")
    @NotBlank(message = "开嘱医生编号不能为空")
    private String regOper;

    @Schema(name = "operCode", description = "提交医嘱医生编号")
    @NotBlank(message = "提交医嘱医生编号不能为空")
    private String operCode;

    @Schema(name = "hisTime", description = "提交医嘱时间")
    @NotBlank(message = "提交医嘱时间不能为空")
    private String hisTime;
}
