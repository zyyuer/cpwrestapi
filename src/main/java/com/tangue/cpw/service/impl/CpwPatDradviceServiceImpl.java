package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tangue.cpw.model.CpwPatDradvice;
import com.tangue.cpw.repository.CpwPatDradviceMapper;
import com.tangue.cpw.service.CpwPatDradviceService;
import org.springframework.stereotype.Service;

@Service
public class CpwPatDradviceServiceImpl
        extends ServiceImpl<CpwPatDradviceMapper, CpwPatDradvice>
        implements CpwPatDradviceService {
}
