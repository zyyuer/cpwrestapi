package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.*;
import com.tangue.cpw.repository.*;
import com.tangue.cpw.service.EnterPathService;
import com.tangue.cpw.service.IpPatientInfoService;
import com.tangue.cpw.service.IpPatientRegService;
import com.tangue.cpw.utils.SequenceGenerate;
import com.tangue.cpw.utils.ValidUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class EnterPathServiceImpl implements EnterPathService {
    private DualMapper dualMapper;
    private CpwRegisterMapper cpwRegisterMapper;
    private DefSystemInfcCodeMapper defSystemInfcCodeMapper;
    private DefOperatorRoleMapper defOperatorRoleMapper;
    private CpwCodeCpwDefineMapper cpwCodeCpwDefineMapper;
    private IpPatientInfoMapper ipPatientInfoMapper;
    private IpPatientInfoService ipPatientInfoService;
    private IpPatientRegService ipPatientRegService;
    private SequenceGenerate sequenceGenerate;
    private CpwPatPhaseMapper cpwPatPhaseMapper;

    public EnterPathServiceImpl(DualMapper dualMapper,
                                CpwRegisterMapper cpwRegisterMapper,
                                DefSystemInfcCodeMapper defSystemInfcCodeMapper,
                                DefOperatorRoleMapper defOperatorRoleMapper,
                                CpwCodeCpwDefineMapper cpwCodeCpwDefineMapper,
                                IpPatientInfoMapper ipPatientInfoMapper,
                                IpPatientInfoService ipPatientInfoService,
                                IpPatientRegService ipPatientRegService,
                                SequenceGenerate sequenceGenerate,
                                CpwPatPhaseMapper cpwPatPhaseMapper) {
        this.dualMapper = dualMapper;
        this.cpwRegisterMapper = cpwRegisterMapper;
        this.defSystemInfcCodeMapper = defSystemInfcCodeMapper;
        this.defOperatorRoleMapper = defOperatorRoleMapper;
        this.cpwCodeCpwDefineMapper = cpwCodeCpwDefineMapper;
        this.ipPatientInfoMapper = ipPatientInfoMapper;
        this.ipPatientInfoService = ipPatientInfoService;
        this.ipPatientRegService = ipPatientRegService;
        this.sequenceGenerate = sequenceGenerate;
        this.cpwPatPhaseMapper = cpwPatPhaseMapper;
    }

    /**
     * ??????????????????
     *
     * @param enterPathVo
     */
    @Override
    @Transactional
    public String enterPath(EnterPathVo enterPathVo) {
        /**
         * ????????????
         */
        String birthday = enterPathVo.getBirthday();
        String inTime = enterPathVo.getInTime();
        //???????????????????????????????????????
        checkDateRange(birthday, inTime);
        //????????????????????????
        String pid = enterPathVo.getPid();
        String regNo = enterPathVo.getRegNo();
        checkRegPid(regNo, pid);
        //????????????????????????????????????????????????
        String inDept = enterPathVo.getInDept();
        String inOper = enterPathVo.getInOper();
        String drOper = enterPathVo.getDrOper();
        String nuOper = enterPathVo.getNuOper();
        String feeCode = enterPathVo.getFeeCode();
        String cpInDept = convertCode(inDept, "CODE_DEPARTMENT", "????????????");
        String cpInOper = convertCode(inOper, "CODE_OPERATOR", "????????????");
        String cpDrOper = convertCode(drOper, "CODE_OPERATOR", "????????????");
        String cpNuOper = convertCode(nuOper, "CODE_OPERATOR", "????????????");
        String cpFeeCode = convertCode(feeCode, "CODE_FEE_CLASS", "??????????????????");
        //??????????????????????????????
        String icdCode = enterPathVo.getIcdCode();
        CpwCodeCpwDefine cpwDefine = cpwCodeCpwDefineMapper.getCpwDefine(cpInDept, icdCode);
        if (cpwDefine == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "????????????????????????????????????" + cpInDept + "(" + icdCode + ")");
        }

        /**
         * ????????????
         * 1.ip_patient_info
         * 2.ip_patient_reg
         * 3.cpw_register
         * 4.cpw_pat_phase
         */
        //1.ip_patient_info
        IpPatientInfo ipPatientInfo = new IpPatientInfo();
        ipPatientInfo.setPid(enterPathVo.getPid());
        ipPatientInfo.setCardNo(enterPathVo.getPid());
        ipPatientInfo.setPatName(enterPathVo.getPatName());
        ipPatientInfo.setPatSex(enterPathVo.getPatSex());
        ipPatientInfo.setBirthday(enterPathVo.getBirthday());
        ipPatientInfoService.saveOrUpdate(ipPatientInfo);
        //2.ip_patient_reg
        Date sysDate = dualMapper.getSysDate().getSysDate();
        String sysDateShort = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd");
        String sysDateLong = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd HH:mm:ss");
        IpPatientReg ipPatientReg = new IpPatientReg();
        ipPatientReg.setRegNo(enterPathVo.getRegNo());
        ipPatientReg.setPid(enterPathVo.getPid());
        ipPatientReg.setCnt(enterPathVo.getCnt());
        ipPatientReg.setPatHeight(enterPathVo.getPatHeight());
        ipPatientReg.setPatWeight(enterPathVo.getPatWeight());
        ipPatientReg.setFeeCode(cpFeeCode);
        ipPatientReg.setPatMarry(enterPathVo.getPatMarry());
        ipPatientReg.setInDept(cpInDept);
        ipPatientReg.setInDate(enterPathVo.getInTime().substring(0, 10));
        ipPatientReg.setInTime(enterPathVo.getInTime());
        ipPatientReg.setRegDate(sysDateShort);
        ipPatientReg.setRegTime(sysDateLong);
        ipPatientReg.setRegOper(cpInOper);
        ipPatientReg.setChgFlag("01");
        ipPatientRegService.saveOrUpdate(ipPatientReg);
        //3.cpw_register
        CpwRegister cpwRegister = new CpwRegister();
        cpwRegister.setRegNo(enterPathVo.getRegNo());
        cpwRegister.setPid(enterPathVo.getPid());
        String cpwNo = sequenceGenerate.getSequenceNo("SEQ_CPW_REGISTER");
        cpwRegister.setCpwNo(cpwNo);
        cpwRegister.setIcdCode(enterPathVo.getIcdCode());
        cpwRegister.setCpwVer(cpwDefine.getCpwVer());
        cpwRegister.setCpwCode(cpwDefine.getCpwCode());
        cpwRegister.setDrOper(cpDrOper);
        cpwRegister.setBedCode(enterPathVo.getBedCode());
        cpwRegister.setNurseCls(enterPathVo.getNurseCls());
        cpwRegister.setNuOper(cpNuOper);
        cpwRegister.setInDept(cpInDept);
        cpwRegister.setBegDate(sysDateShort);
        cpwRegister.setInOper(cpInOper);
        cpwRegister.setInDate(sysDateShort);
        cpwRegister.setInTime(sysDateLong);
        cpwRegister.setCpwregFlag("00");
        cpwRegisterMapper.insert(cpwRegister);
        //4.cpw_pat_phase
        CpwPatPhase cpwPatPhase = new CpwPatPhase();
        cpwPatPhase.setPhaseNo(sequenceGenerate.getSequenceNo("SEQ_CPW_PAT_PHASE"));
        cpwPatPhase.setRegNo(enterPathVo.getRegNo());
        cpwPatPhase.setCpwNo(cpwNo);
        cpwPatPhase.setPeNo(1);
        cpwPatPhase.setCpDateNo(1);
        cpwPatPhase.setNowDay(sysDateShort);
        cpwPatPhase.setNowNo(1);
        cpwPatPhase.setConFlag("F");
        cpwPatPhase.setVariaFlag("0000");
        cpwPatPhase.setOperCode(enterPathVo.getInOper());
        cpwPatPhase.setOperDate(sysDateShort);
        cpwPatPhase.setOperTime(sysDateLong);
        cpwPatPhaseMapper.insert(cpwPatPhase);
        return cpwDefine.getCpwName();
    }

    /**
     * ??????his????????????cpw???????????????
     *
     * @param copdCode
     * @param tabName
     * @param description
     * @return
     */
    public String convertCode(String copdCode, String tabName, String description) {
        QueryWrapper<DefSystemInfcCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scope", "CPW")
                .eq("infc_table", tabName)
                .eq("copd_code", copdCode);
        DefSystemInfcCode defSystemInfcCode = defSystemInfcCodeMapper.selectOne(queryWrapper);
        if (defSystemInfcCode == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "???????????????????????????" + description + "(" + copdCode + ")");
        } else {
            if ("CODE_OPERATOR".equals(tabName) && Strings.endsWithIgnoreCase(description, "??????")) {
                //?????????????????????????????????
                QueryWrapper<DefOperatorRole> queryWrapperRole = new QueryWrapper<>();
                queryWrapperRole.eq("oper_code", defSystemInfcCode.getInfcCode())
                        .eq("role_code", "02");
                Long cntRole = defOperatorRoleMapper.selectCount(queryWrapperRole);
                if (cntRole < 1) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                            "???????????????????????????" + description + "(" + copdCode + ")?????????????????????");
                }
            }
        }
        return defSystemInfcCode.getInfcCode();
    }

    /**
     * ??????reg_no,pid????????????????????????
     *
     * @param regNo ????????????
     * @param pid   ????????????
     * @return
     */
    private boolean checkRegPid(String regNo, String pid) {
        QueryWrapper<CpwRegister> queryWrapperReg = new QueryWrapper<>();
        queryWrapperReg.eq("reg_no", regNo)
                .ne("cpwreg_flag", "91");
        Long cntReg = cpwRegisterMapper.selectCount(queryWrapperReg);
        if (cntReg > 0) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "????????????????????????????????????????????????????????????");
        }
        QueryWrapper<CpwRegister> queryWrapperPid = new QueryWrapper<>();
        queryWrapperPid.eq("pid", pid)
                .eq("cpwreg_flag", "00");
        Long cntPid = cpwRegisterMapper.selectCount(queryWrapperPid);
        if (cntPid > 0) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "?????????????????????????????????????????????????????????????????????");
        }
        return true;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param birthday ??????
     * @param inTime   ????????????
     * @return
     */
    private boolean checkDateRange(String birthday, String inTime) {
        boolean isBirthday = ValidUtils.isValidDate(birthday);
        Date date = ValidUtils.strToDate(birthday, "yyyy-MM-dd");
        Date sysDate = dualMapper.getSysDate().getSysDate();
        if (date.after(sysDate)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "????????????????????????????????????");
        }
        boolean isInTime = ValidUtils.isValidDatetime(inTime);
        Date inDate = ValidUtils.strToDate(inTime, "yyyy-MM-dd HH:mm:ss");
        if (inDate.after(sysDate)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "????????????????????????????????????");
        }
        return true;
    }
}
