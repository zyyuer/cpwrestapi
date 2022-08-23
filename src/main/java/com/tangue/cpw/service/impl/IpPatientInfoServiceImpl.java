package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tangue.cpw.model.IpPatientInfo;
import com.tangue.cpw.repository.IpPatientInfoMapper;
import com.tangue.cpw.service.IpPatientInfoService;
import org.springframework.stereotype.Service;

@Service
public class IpPatientInfoServiceImpl
        extends ServiceImpl<IpPatientInfoMapper, IpPatientInfo>
        implements IpPatientInfoService {
}
