package com.tangue.cpw.controller;

import com.tangue.cpw.exception.CustomException;
import com.tangue.cpw.exception.CustomExceptionType;
import com.tangue.cpw.model.AuthenticationRequest;
import com.tangue.cpw.model.AuthenticationResponse;
import com.tangue.cpw.service.impl.AuthUserDetailService;
import com.tangue.cpw.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private AuthUserDetailService userDetailsService;
    @Resource
    private JwtUtil jwtTokenUtil;

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
}
