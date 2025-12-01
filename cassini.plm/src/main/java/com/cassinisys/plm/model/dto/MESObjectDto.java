package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.JigsFixtureDto;
import com.cassinisys.plm.model.mes.dto.MaterialDto;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * Created by CassiniSystems on 27-10-2020.
 */
@Data
public class MESObjectDto {

    private Page<MESMachine> machines;
    private Page<MESEquipment> equipments;
    private Page<MESInstrument> instruments;
    private Page<MESTool> tools;
    private Page<JigsFixtureDto> jigsFixtures;
    private Page<MaterialDto> materials;
    private Page<MESManpower> manpowers;
}
