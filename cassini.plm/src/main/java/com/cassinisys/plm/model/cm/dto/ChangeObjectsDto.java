package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.plm.model.cm.*;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * Created by CassiniSystems on 26-05-2021.
 */
@Data
public class ChangeObjectsDto {

    private Page<PLMECO> ecos;

    private Page<PLMECR> ecrs;

    private Page<PLMDCO> dcos;

    private Page<PLMDCR> dcrs;

    private Page<PLMMCO> mcos;

    private Page<PLMVariance> variances;

    private Enum changeType;
}
