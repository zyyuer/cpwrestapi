package com.tangue.cpw.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class IpPatientInfo {
    @TableId
    private String pid;
    private String cardNo;
    private String patName;
    private String patSex;
    private String birthday;
}
