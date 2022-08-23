package com.tangue.cpw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangue.cpw.model.CpwRegister;
import com.tangue.cpw.repository.CpwRegisterMapper;
import com.tangue.cpw.repository.DualMapper;
import com.tangue.cpw.utils.SequenceGenerate;
import com.tangue.cpw.utils.ValidUtils;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class CpwApplicationTests {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private DualMapper dualMapper;
    @Autowired
    private CpwRegisterMapper cpwRegisterMapper;

    @Resource
    private SequenceGenerate sequenceGenerate;
    @Test
    void contextLoads() {
    }

    @Test
    public void encode() {
        System.out.println(passwordEncoder.encode("00"));
        System.out.println(passwordEncoder.matches("00", "$2a$10$icsB1lGnlvi4oIkGaRF3XeberNuLOLJPZI0NqItslQDm7j.N6VaQe"));
    }

    @Test
    public void isValidDateTest() {
        boolean validDate = ValidUtils.isValidDate("1977-11-22");
        Assert.isTrue(validDate == true);
    }

    @Test
    public void isValidDateTimeTest() {
//        boolean validDatetime = ValidUtils.isValidDatetime("2022-08-22 22:54:33");
//        Date sysDate = dualMapper.getSysDate().getSysDate();
//        System.out.println(sysDate);
//        Assert.isTrue(validDatetime == true);

//        String strDate = "2022-08-22";
//        Date date = ValidUtils.strToDate(strDate, "yyyy-MM-dd");
//        System.out.println(date);

        Date date = new Date();
        String s = ValidUtils.DateToStr(date, "yyyy-MM-dd");
        System.out.println(s);
    }

    @Test
    public void countTest() {
        //检查患者路径状态
        QueryWrapper<CpwRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reg_no", "250933")
                .ne("cpwreg_flag", "91");
        Long cnt = cpwRegisterMapper.selectCount(queryWrapper);
        Assert.isTrue(cnt == 1);
    }

    @Test
    public void getSequenceNo() {
//        String sequenceNo = dualMapper.getSequenceNo("SEQ_CPW_REGISTER");
//        Assert.isTrue(sequenceNo != null);

        String seqNo = sequenceGenerate.getSequenceNo("SEQ_CPW_REGISTER");
        System.out.println(seqNo);
    }
}
