package com.tangue.cpw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.DemoPo;
import com.tangue.cpw.model.DemoVo;
import com.tangue.cpw.repository.DemoMapper;
import com.tangue.cpw.service.DemoService;
import com.tangue.cpw.utils.DozerUtils;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DemoServiceImpl implements DemoService {
    private DemoMapper deptMapper;
    private Mapper dozerMapper;

    public DemoServiceImpl(DemoMapper deptMapper, Mapper dozerMapper) {
        this.deptMapper = deptMapper;
        this.dozerMapper = dozerMapper;
    }

    //查找所有科室利用MP封装方法
    @Override
    public List<DemoVo> getAll() {
        List<DemoPo> departmentPos = deptMapper.selectList(null);
        return DozerUtils.mapList(departmentPos, DemoVo.class);
    }

    //利用XML映射新增方法
    @Override
    public List<DemoVo> getAllParent() {
        List<DemoPo> demoPos = deptMapper.selectAllParent();
        List<DemoVo> demoVos = DozerUtils.mapList(demoPos, DemoVo.class);
        return demoVos;
    }

    //插入科室
    @Override
    public void insertDept(DemoVo dept) {
        DemoPo deptPo = dozerMapper.map(dept, DemoPo.class);
        deptMapper.insert(deptPo);
    }

    //通过科室编号来更新科室
    @Override
    public void updateDept(DemoVo dept) {
        DemoVo demoVo = getDeptById(dept.getDeptCode());
        if (demoVo == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "指定科室不存在");
        }
        DemoPo deptPo = dozerMapper.map(dept, DemoPo.class);
        UpdateWrapper<DemoPo> wrapper = new UpdateWrapper<>();
        wrapper.lambda()
                .eq(DemoPo::getDeptCode, deptPo.getDeptCode());
        deptMapper.update(deptPo, wrapper);
    }

    //通过科室code来删除科室
    @Override
    public void deleteDept(String deptCode) {
        DemoVo deptVo = getDeptById(deptCode);
        if (deptVo == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "如下编号科室不存在:" + deptCode);
        }
        QueryWrapper<DemoPo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DemoPo::getDeptCode, deptCode);
        deptMapper.delete(wrapper);
    }

    @Override
    public DemoVo getDeptById(String deptCode) {
        DemoPo demoPo = deptMapper.selectById(deptCode);
        if (demoPo == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                    "没有发现科室记录deptCode:" + deptCode);
        }
        DemoVo demoVo = dozerMapper.map(demoPo, DemoVo.class);
        return demoVo;
    }
}
