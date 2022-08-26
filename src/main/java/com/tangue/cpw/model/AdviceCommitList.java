package com.tangue.cpw.model;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class AdviceCommitList {
    @Valid
    private List<AdviceCommitDto> advices;
}
