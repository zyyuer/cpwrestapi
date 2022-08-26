package com.tangue.cpw.controller;

import com.tangue.cpw.model.*;
import com.tangue.cpw.service.AdviceCommitService;
import com.tangue.cpw.service.AdviceSelectService;
import com.tangue.cpw.service.EnterPathService;
import com.tangue.cpw.utils.ValidableList;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clinicalPathway")
public class CpwApiController {
    private EnterPathService enterPathService;
    private AdviceSelectService adviceSelectService;
    private AdviceCommitService adviceCommitService;

    public CpwApiController(EnterPathService enterPathService,
                            AdviceSelectService adviceSelectService,
                            AdviceCommitService adviceCommitService) {
        this.enterPathService = enterPathService;
        this.adviceSelectService = adviceSelectService;
        this.adviceCommitService = adviceCommitService;
    }

    @PostMapping("/enter")
    public Map<String, String> enter(@RequestBody @Valid EnterPathVo enterPathVo) {
        String cpwName = enterPathService.enterPath(enterPathVo);
        Map<String, String> data = new HashMap<>();
        data.put("msg", "进入路径成功:" + cpwName);
        return data;
    }

    @PostMapping("/adviceSelect")
    public List<AdviceReturnDto> adviceSelect(@RequestBody @Valid AdviceSelectVo adviceSelectVo) {
        return adviceSelectService.adviceSelect(adviceSelectVo);

    }

    @PostMapping("/adviceCommit")
    public Map<String, String> adviceCommit(
            @RequestBody @Valid ValidableList<AdviceCommitDto> adviceCommitList) {
        adviceCommitService.adviceCommit(adviceCommitList);
        Map<String, String> data = new HashMap<>();
        data.put("msg", "医嘱提交成功");
        return data;
    }
}
