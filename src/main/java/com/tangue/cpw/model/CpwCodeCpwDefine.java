package com.tangue.cpw.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cpw_code_cpwdefine")
public class CpwCodeCpwDefine {
    private Integer cpwVer;
    private String cpwCode;
    private String cpwName;
}
