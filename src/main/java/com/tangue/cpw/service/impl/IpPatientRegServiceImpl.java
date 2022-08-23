package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tangue.cpw.model.IpPatientReg;
import com.tangue.cpw.repository.IpPatientRegMapper;
import com.tangue.cpw.service.IpPatientRegService;
import org.springframework.stereotype.Service;

@Service
public class IpPatientRegServiceImpl
        extends ServiceImpl<IpPatientRegMapper, IpPatientReg>
        implements IpPatientRegService {
}
