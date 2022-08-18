package com.tangue.cpw.service;

import com.tangue.cpw.model.DemoVo;

import java.util.List;

public interface DemoService {
    public List<DemoVo> getAll();

    public List<DemoVo> getAllParent();

    public void insertDept(DemoVo dept);

    public void updateDept(DemoVo dept);

    public void deleteDept(String deptCode);

    public DemoVo getDeptById(String deptCode);
}
