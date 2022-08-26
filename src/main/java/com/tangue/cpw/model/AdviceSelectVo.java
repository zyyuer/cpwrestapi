package com.tangue.cpw.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@Schema
public class AdviceSelectVo {
    @Schema(name = "pid", description = "患者编号/病案号")
    @NotBlank(message = "患者编号不能为空")
    private String pid;

    @Schema(name = "regNo", description = "住院流水号")
    @NotBlank(message = "住院流水号不能为空")
    private String regNo;


    @Schema(name = "position", description = "调用位置", example = "01")
    @NotBlank(message = "调用位置不能为空（01）")
    private String position;

    @Schema(name = "operCode", description = "医生", example = "01")
    @NotBlank(message = "请求医生不能为空")
    private String operCode;
}
