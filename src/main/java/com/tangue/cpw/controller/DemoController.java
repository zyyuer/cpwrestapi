package com.tangue.cpw.controller;

import com.tangue.cpw.model.DemoVo;
import com.tangue.cpw.service.DemoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DemoController {
    private DemoService departmentService;

    public DemoController(DemoService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(summary = "所有科室", operationId = "getAll", description = "得到所有科室")
    @GetMapping("/depts/findAll")
    public List<DemoVo> getAll() {
        return departmentService.getAll();
    }

    @Operation(summary = "所有父科室", operationId = "getDeptParent", description = "得到所有父级科室")
    @GetMapping("/depts/findAllParents")
    public List<DemoVo> getDeptParent() {
        List<DemoVo> allParent = departmentService.getAllParent();
        return allParent;
    }

    @Operation(summary = "新建科室", operationId = "insertDept", description = "新建一个科室科室")
    @PostMapping("/depts/create")
    public void insertDept(@RequestBody DemoVo demoVo) {
        departmentService.insertDept(demoVo);
    }

    @PutMapping("/depts/update")
    public void updateDept(@RequestBody DemoVo demoVo) {
        departmentService.updateDept(demoVo);
    }

    @DeleteMapping("/depts/delete/{deptCode}")
    public void updateDept(@PathVariable("deptCode") String deptCode) {
        departmentService.deleteDept(deptCode);
    }

    @GetMapping("/depts/findOne/{deptCode}")
    public DemoVo getDeptById(@PathVariable("deptCode") String deptCode) {
        DemoVo demoVo = departmentService.getDeptById(deptCode);
        return demoVo;
    }


}
