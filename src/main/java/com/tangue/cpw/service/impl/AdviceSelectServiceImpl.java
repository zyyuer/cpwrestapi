package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.*;
import com.tangue.cpw.repository.*;
import com.tangue.cpw.service.AdviceSelectService;
import com.tangue.cpw.service.CpwPatPhaseService;
import com.tangue.cpw.utils.SequenceGenerate;
import com.tangue.cpw.utils.ValidUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 患者入径后，医嘱选择接口
 */
@Service
public class AdviceSelectServiceImpl implements AdviceSelectService {
    private CpwRegisterMapper cpwRegisterMapper;
    private AdviceReturnMapper adviceReturnMapper;
    private DualMapper dualMapper;
    private CpwPatPhaseMapper cpwPatPhaseMapper;
    private DefSystemInfcCodeMapper defSystemInfcCodeMapper;
    private DefOperatorRoleMapper defOperatorRoleMapper;
    private CpwPatPhaseService cpwPatPhaseService;
    private SequenceGenerate sequenceGenerate;
    private CpwCodeCpwphaseMapper cpwCodeCpwphaseMapper;

    public AdviceSelectServiceImpl(CpwRegisterMapper cpwRegisterMapper,
                                   AdviceReturnMapper adviceReturnMapper,
                                   DualMapper dualMapper,
                                   CpwPatPhaseMapper cpwPatPhaseMapper,
                                   DefSystemInfcCodeMapper defSystemInfcCodeMapper,
                                   DefOperatorRoleMapper defOperatorRoleMapper,
                                   CpwPatPhaseService cpwPatPhaseService,
                                   SequenceGenerate sequenceGenerate,
                                   CpwCodeCpwphaseMapper cpwCodeCpwphaseMapper) {
        this.cpwRegisterMapper = cpwRegisterMapper;
        this.adviceReturnMapper = adviceReturnMapper;
        this.dualMapper = dualMapper;
        this.cpwPatPhaseMapper = cpwPatPhaseMapper;
        this.defSystemInfcCodeMapper = defSystemInfcCodeMapper;
        this.defOperatorRoleMapper = defOperatorRoleMapper;
        this.cpwPatPhaseService = cpwPatPhaseService;
        this.sequenceGenerate = sequenceGenerate;
        this.cpwCodeCpwphaseMapper = cpwCodeCpwphaseMapper;
    }

    @Override
    @Transactional
    public List<AdviceReturnDto> adviceSelect(AdviceSelectVo adviceSelectVo) {
        //检查接受参数有效性
        CpwRegister cpwRegister = checkPatient(adviceSelectVo);
        //获得peNO
        Integer peNo = getPeNo(cpwRegister, adviceSelectVo);
        List<AdviceReturnDto> adviceReturn = adviceReturnMapper
                .getAdviceReturn(adviceSelectVo.getRegNo(), adviceSelectVo.getPid(), peNo);
        return adviceReturn;

    }

    /**
     * 根据当前日期获得路径所属的peNo
     * 如果没有的话整理peNo
     *
     * @param cpwRegister
     * @return
     */
    private Integer getPeNo(CpwRegister cpwRegister, AdviceSelectVo adviceSelectVo) {
        Date sysDate = dualMapper.getSysDate().getSysDate();
        String dateStr = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd");
        String dateStrLong = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd HH:mm:ss");
        QueryWrapper<CpwPatPhase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cpw_no", cpwRegister.getCpwNo())
                .eq("now_day", dateStr);
        CpwPatPhase cpwPatPhase = cpwPatPhaseMapper.selectOne(queryWrapper);
        //当天没有阶段记录
        if (cpwPatPhase == null) {
            CpwPatPhase cpwPatPhaseMax = cpwPatPhaseMapper.getMaxNowDay(cpwRegister.getCpwNo());
            //异常患者没有任何阶段记录
            if (cpwPatPhaseMax == null) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "联系管理员：患者没有阶段记录cpw_no(" + cpwRegister.getCpwNo() + ")");
            } else {
                //更新确认标示
                updateConfFlag(cpwRegister, adviceSelectVo);
                //患者有阶段记录，从最大的阶段到现在进行整理插入
                try {
                    List<CpwPatPhase> cpwPatPhases = fixPhase(cpwPatPhaseMax, cpwRegister);
                    //cpwPatPhases.forEach(System.out::println);
                    //需要操作表具有id字段
                    cpwPatPhaseService.saveOrUpdateBatch(cpwPatPhases);
                    return cpwPatPhases.get(cpwPatPhases.size() - 1).getPeNo();

                } catch (ParseException e) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                            "联系管理员：循环阶段now_day失败");
                }
            }
        } else {
            return cpwPatPhase.getPeNo();
        }
    }

    /**
     * 更新患者阶段确认表示，确认人，确认时间等
     *
     * @param cpwRegister
     * @param adviceSelectVo
     */
    private void updateConfFlag(CpwRegister cpwRegister, AdviceSelectVo adviceSelectVo) {
        Date sysDate = dualMapper.getSysDate().getSysDate();
        String dateStr = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd");
        String dateStrLong = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd HH:mm:ss");
        LambdaUpdateWrapper<CpwPatPhase> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(CpwPatPhase::getCpwNo, cpwRegister.getCpwNo())
                .set(CpwPatPhase::getConFlag, "T")
                .set(CpwPatPhase::getConOper, adviceSelectVo.getOperCode())
                .set(CpwPatPhase::getConDate, dateStr)
                .set(CpwPatPhase::getConTime, dateStrLong);
        try {
            cpwPatPhaseMapper.update(null, lambdaUpdateWrapper);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "updateConfFlag方法执行更新数据失败");
        }
        /*UpdateWrapper<CpwPatPhase> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("cpw_no", cpwRegister.getCpwNo());
        CpwPatPhase cpwPatPhaseUpdate = new CpwPatPhase();
        cpwPatPhaseUpdate.setConFlag("T");
        cpwPatPhaseUpdate.setConDate(dateStr);
        cpwPatPhaseUpdate.setConTime(dateStrLong);
        cpwPatPhaseUpdate.setConOper(adviceSelectVo.getOperCode());
        cpwPatPhaseMapper.update(cpwPatPhaseUpdate, updateWrapper);*/
    }

    /**
     * 通过时间差来补充阶段信息
     *
     * @param cpwPatPhaseMax 现有最大的阶段信息
     * @param cpwRegister    患者路径信息
     * @throws ParseException
     */
    public List<CpwPatPhase> fixPhase(CpwPatPhase cpwPatPhaseMax, CpwRegister cpwRegister) throws ParseException {
        String maxDay = cpwPatPhaseMax.getNowDay();
        Date sysDate = dualMapper.getSysDate().getSysDate();
        String dateStr = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd");
        String dateStrLong = ValidUtils.DateToStr(sysDate, "yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        Date begindate = simple.parse(maxDay);
        Date enddate = simple.parse(dateStr);
        Date tmp = begindate;
        List<CpwPatPhase> phaseList = new ArrayList<>();
        Integer nowNo = cpwPatPhaseMax.getNowNo();
        while (!tmp.equals(enddate)) {
            String s1 = simple.format(tmp);
            c.setTime(tmp);
            c.add(Calendar.DATE, 1);
            tmp = c.getTime();
            //从当前最大阶段日期+1到当前日期 s2
            String s2 = simple.format(tmp);

            nowNo++;
            CpwPatPhase cpwPatPhase = new CpwPatPhase();
            String seq_cpw_pat_phase = sequenceGenerate.getSequenceNo("SEQ_CPW_PAT_PHASE");
            cpwPatPhase.setPhaseNo(seq_cpw_pat_phase);
            cpwPatPhase.setRegNo(cpwRegister.getRegNo());
            cpwPatPhase.setCpwNo(cpwRegister.getCpwNo());
            cpwPatPhase.setNowDay(s2);
            cpwPatPhase.setNowNo(nowNo);
            cpwPatPhase.setConFlag("F");
            cpwPatPhase.setVariaFlag("0000");
            cpwPatPhase.setOperCode(cpwRegister.getInOper());
            cpwPatPhase.setOperDate(dateStr);
            cpwPatPhase.setOperTime(dateStrLong);
            cpwPatPhase.setCpDateNo(nowNo);

            QueryWrapper<CpwCodeCpwphase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cpw_ver", cpwRegister.getCpwVer())
                    .eq("cpw_code", cpwRegister.getCpwCode())
                    .le("beg_day", nowNo)
                    .ge("end_day", nowNo);
            CpwCodeCpwphase cpwCodeCpwphase = cpwCodeCpwphaseMapper.selectOne(queryWrapper);
            if (cpwCodeCpwphase != null) {
                cpwPatPhase.setPeNo(cpwCodeCpwphase.getPeNo());
            } else {
                CpwCodeCpwphase codePhase = cpwCodeCpwphaseMapper.getMaxNowNo(cpwRegister.getCpwVer(), cpwRegister.getCpwCode());
                if (codePhase.getEndDay() < nowNo) {
                    cpwPatPhase.setPeNo(codePhase.getPeNo());
                }
            }
            phaseList.add(cpwPatPhase);
        }
        return phaseList;
    }

    /**
     * 医嘱选择请求有效性校验
     *
     * @param adviceSelectVo
     * @return
     */
    private CpwRegister checkPatient(AdviceSelectVo adviceSelectVo) {
        String pid = adviceSelectVo.getPid();
        String regNo = adviceSelectVo.getRegNo();
        String position = adviceSelectVo.getPosition();

        QueryWrapper<CpwRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reg_no", regNo)
                .eq("pid", pid)
                .eq("cpwreg_flag", "00");
        CpwRegister cpwRegister = cpwRegisterMapper.selectOne(queryWrapper);
        if (cpwRegister == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "患者编号或者患者住院流水号传入错误");
        }
        if (!"01".equals(position)) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "调用位置传入错误参考值01");
        }
        convertCode(adviceSelectVo.getOperCode(), "CODE_OPERATOR", "主管医生");

        return cpwRegister;
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
}