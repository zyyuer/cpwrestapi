package com.tangue.cpw.controller;

import com.tangue.cpw.model.EnterPathVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/clinicalPathway")
public class EnterPathController {
    @PostMapping("/enter")
    public void enter(@RequestBody @Valid EnterPathVo enterPathVo) {

    }
}
