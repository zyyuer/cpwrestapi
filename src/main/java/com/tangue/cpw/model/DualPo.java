package com.tangue.cpw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DualPo {
    private Date sysDate;
    private String dummy;
}
