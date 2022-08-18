package com.tangue.cpw.controller;

import com.tangue.cpw.exception.AjaxResponse;
import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.AuthenticationRequest;
import com.tangue.cpw.model.AuthenticationResponse;
import com.tangue.cpw.model.DemoVo;
import com.tangue.cpw.service.DemoService;
import com.tangue.cpw.service.impl.MyUserDetailService;
import com.tangue.cpw.utils.JwtUtil;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DemoController {
    private DemoService departmentService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private MyUserDetailService userDetailsService;
    @Resource
    private JwtUtil jwtTokenUtil;

    public DemoController(DemoService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/depts/findAll")
    public List<DemoVo> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/depts/findAllParents")
    public List<DemoVo> getDeptParent() {
        List<DemoVo> allParent = departmentService.getAllParent();
        return allParent;
    }

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

    //认证获得token jwt
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            //throw new Exception("Incorrect username or password", e);
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                    "认证失败，检查认证用户或密码信息是否正确");
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/filterException")
    public void rethrow(HttpServletRequest request) {
        Exception exception = (Exception) request.getAttribute("filterException");
        throw new CustomException(CustomExceptionType.OTHER_ERROR,
                "认证失败，无法请求链接" + exception.getMessage());
    }
}
