package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.*;
import com.tangue.cpw.repository.CpwPatDradviceMapper;
import com.tangue.cpw.repository.CpwRegisterMapper;
import com.tangue.cpw.repository.DefOperatorRoleMapper;
import com.tangue.cpw.repository.DefSystemInfcCodeMapper;
import com.tangue.cpw.service.AdviceCommitService;
import com.tangue.cpw.utils.ValidUtils;
import com.tangue.cpw.utils.ValidableList;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdviceCommitServiceImpl implements AdviceCommitService {
    private CpwRegisterMapper cpwRegisterMapper;
    private DefSystemInfcCodeMapper defSystemInfcCodeMapper;
    private DefOperatorRoleMapper defOperatorRoleMapper;
    private CpwPatDradviceMapper cpwPatDradviceMapper;

    public AdviceCommitServiceImpl(CpwRegisterMapper cpwRegisterMapper,
                                   DefSystemInfcCodeMapper defSystemInfcCodeMapper,
                                   DefOperatorRoleMapper defOperatorRoleMapper,
                                   CpwPatDradviceMapper cpwPatDradviceMapper) {
        this.cpwRegisterMapper = cpwRegisterMapper;
        this.defSystemInfcCodeMapper = defSystemInfcCodeMapper;
        this.defOperatorRoleMapper = defOperatorRoleMapper;
        this.cpwPatDradviceMapper = cpwPatDradviceMapper;
    }

    @Override
    public String adviceCommit(ValidableList<AdviceCommitDto> adviceCommitDto) {
        //检查接受参数有效性
        CpwRegister cpwRegister = checkPatient(adviceCommitDto);
        //整理插入
        List<CpwPatDradvice> advices = getAdviceList(adviceCommitDto, cpwRegister);


        return null;
    }

    private List<CpwPatDradvice> getAdviceList(ValidableList<AdviceCommitDto> adviceCommitDto, CpwRegister cpwRegister) {
        List<CpwPatDradvice> advices = new ArrayList<>();
        for (AdviceCommitDto commitDto : adviceCommitDto) {
            CpwPatDradvice cpwPatDradvice = new CpwPatDradvice();
            cpwPatDradvice.setRegNo(commitDto.getRegNo());
            cpwPatDradvice.setCpwNo(cpwRegister.getCpwNo());

        }
        return null;
    }

    /**
     * 请求体业务逻辑校验
     *
     * @param adviceCommitDto
     */
    private CpwRegister checkPatient(ValidableList<AdviceCommitDto> adviceCommitDto) {
        CpwRegister cpwRegisterRtn = new CpwRegister();
        for (int i = 0; i < adviceCommitDto.size(); i++) {
            String pid = adviceCommitDto.get(i).getPid();
            String regNo = adviceCommitDto.get(i).getRegNo();

            QueryWrapper<CpwRegister> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("reg_no", regNo)
                    .eq("pid", pid)
                    .eq("cpwreg_flag", "00");
            CpwRegister cpwRegister = cpwRegisterMapper.selectOne(queryWrapper);
            cpwRegisterRtn = cpwRegister;
            if (cpwRegister == null) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "list[" + i + "]患者编号或者患者住院流水号传入错误");
            }
            //检查科室及医生
            convertCode(adviceCommitDto.get(i).getRegDept(), "CODE_DEPARTMENT",
                    "医嘱下达科室", i);
            convertCode(adviceCommitDto.get(i).getRegOper(), "CODE_OPERATOR",
                    "医嘱下达人", i);
            convertCode(adviceCommitDto.get(i).getOperCode(), "CODE_OPERATOR",
                    "医嘱录入人", i);
            //医嘱类型检测
            String adviceClass = adviceCommitDto.get(i).getAdviceClass();
            if (",01,02,03,11,13,".contains("," + adviceClass + ",") == false) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "list[" + i + "]医嘱类别传入错误,01普通，02护理，03手术，11检查，12检验");
            }
            //相关日期检测
            //长期医嘱开始时间不能为空
            String begTime = adviceCommitDto.get(i).getBegTime();
            String endTime = adviceCommitDto.get(i).getEndTime();
            String hisTime = adviceCommitDto.get(i).getHisTime();
            Boolean validBegTime = ValidUtils.isValidDatetime(begTime);
            Boolean validHisTime = ValidUtils.isValidDatetime(hisTime);
            if (endTime != null && endTime.length() != 0) {
                Boolean validEndTime = ValidUtils.isValidDatetime(endTime);
                if (validEndTime == false) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                            "list[" + i + "]：长期医嘱结束日期内容有错误");
                }
            }
            if (validBegTime == false) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "list[" + i + "]：长期医嘱开始时间传递有错误");
            }
            if (validHisTime == false) {
                throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                        "list[" + i + "]：录入时间传递有错误");
            }
        }
        return cpwRegisterRtn;
    }

    /**
     * 通过his编码查找cpw编码并返回
     *
     * @param copdCode
     * @param tabName
     * @param description
     * @return
     */
    public String convertCode(String copdCode, String tabName, String description, int i) {
        QueryWrapper<DefSystemInfcCode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scope", "CPW")
                .eq("infc_table", tabName)
                .eq("copd_code", copdCode);
        DefSystemInfcCode defSystemInfcCode = defSystemInfcCodeMapper.selectOne(queryWrapper);
        if (defSystemInfcCode == null) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                    "list[]:" + i + "传入数据校验错误：" + description + "(" + copdCode + ")");
        } else {
            if ("CODE_OPERATOR".equals(tabName) && Strings.endsWithIgnoreCase(description, "医生")) {
                //用户存在无医生角色检查
                QueryWrapper<DefOperatorRole> queryWrapperRole = new QueryWrapper<>();
                queryWrapperRole.eq("oper_code", defSystemInfcCode.getInfcCode())
                        .eq("role_code", "02");
                Long cntRole = defOperatorRoleMapper.selectCount(queryWrapperRole);
                if (cntRole < 1) {
                    throw new CustomException(CustomExceptionType.SYSTEM_ERROR,
                            "list[]:" + i + "传入数据校验错误：" + description + "(" + copdCode + ")无住院医生权限");
                }
            }
        }
        return defSystemInfcCode.getInfcCode();
    }
}
