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
     * 进入路径接口
     *
     * @param enterPathVo
     */
    @Override
    @Transactional
    public String enterPath(EnterPathVo enterPathVo) {
        /**
         * 业务校验
         */
        String birthday = enterPathVo.getBirthday();
        String inTime = enterPathVo.getInTime();
        //生日校验和入院日期范围校验
        checkDateRange(birthday, inTime);
        //检查患者路径状态
        String pid = enterPathVo.getPid();
        String regNo = enterPathVo.getRegNo();
        checkRegPid(regNo, pid);
        //检查科室及医护人员及患者费别类型
        String inDept = enterPathVo.getInDept();
        String inOper = enterPathVo.getInOper();
        String drOper = enterPathVo.getDrOper();
        String nuOper = enterPathVo.getNuOper();
        String feeCode = enterPathVo.getFeeCode();
        String cpInDept = convertCode(inDept, "CODE_DEPARTMENT", "入径科室");
        String cpInOper = convertCode(inOper, "CODE_OPERATOR", "入径医生");
        String cpDrOper = convertCode(drOper, "CODE_OPERATOR", "主管医生");
        String cpNuOper = convertCode(nuOper, "CODE_OPERATOR", "经管护士");
        String cpFeeCode = convertCode(feeCode, "CODE_FEE_CLASS", "患者费别类型");
        //检验诊断编码入径条件
        String icdCode = enterPathVo.getIcdCode();
        CpwCodeCpwDefine cpwDefine = cpwCodeCpwDefineMapper.getCpwDefine(cpInDept, icdCode);
        if (cpwDefine == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "科室没有符合诊断的路径：" + cpInDept + "(" + icdCode + ")");
        }

        /**
         * 整理插入
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
     * 通过his编码查找cpw编码并返回
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
                    "传入数据校验错误：" + description + "(" + copdCode + ")");
        } else {
            if ("CODE_OPERATOR".equals(tabName) && Strings.endsWithIgnoreCase(description, "医生")) {
                //用户存在无医生角色检查
                QueryWrapper<DefOperatorRole> queryWrapperRole = new QueryWrapper<>();
                queryWrapperRole.eq("oper_code", defSystemInfcCode.getInfcCode())
                        .eq("role_code", "02");
                Long cntRole = defOperatorRoleMapper.selectCount(queryWrapperRole);
                if (cntRole < 1) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                            "传入数据校验错误：" + description + "(" + copdCode + ")无住院医生权限");
                }
            }
        }
        return defSystemInfcCode.getInfcCode();
    }

    /**
     * 检查reg_no,pid是否满足入径条件
     *
     * @param regNo 住院流水
     * @param pid   患者编号
     * @return
     */
    private boolean checkRegPid(String regNo, String pid) {
        QueryWrapper<CpwRegister> queryWrapperReg = new QueryWrapper<>();
        queryWrapperReg.eq("reg_no", regNo)
                .ne("cpwreg_flag", "91");
        Long cntReg = cpwRegisterMapper.selectCount(queryWrapperReg);
        if (cntReg > 0) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "患者已出院出径或已在路径中，无法进入路径");
        }
        QueryWrapper<CpwRegister> queryWrapperPid = new QueryWrapper<>();
        queryWrapperPid.eq("pid", pid)
                .eq("cpwreg_flag", "00");
        Long cntPid = cpwRegisterMapper.selectCount(queryWrapperPid);
        if (cntPid > 0) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "检查历史记录患者已经在路径中，无法再次进入路径");
        }
        return true;
    }

    /**
     * 检查出生日期和入院时间范围有效性
     *
     * @param birthday 生日
     * @param inTime   入院时间
     * @return
     */
    private boolean checkDateRange(String birthday, String inTime) {
        boolean isBirthday = ValidUtils.isValidDate(birthday);
        Date date = ValidUtils.strToDate(birthday, "yyyy-MM-dd");
        Date sysDate = dualMapper.getSysDate().getSysDate();
        if (date.after(sysDate)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "出生日期不能大于系统日期");
        }
        boolean isInTime = ValidUtils.isValidDatetime(inTime);
        Date inDate = ValidUtils.strToDate(inTime, "yyyy-MM-dd HH:mm:ss");
        if (inDate.after(sysDate)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "入院时间不能大于系统日期");
        }
        return true;
    }
}
