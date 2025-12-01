package com.cassinisys.plm.model.mro.dto;

import com.cassinisys.plm.model.mes.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class AssetResourcesDto {
    List<MESPlant> plants = new ArrayList<>();
    List<MESWorkCenter> workCenters = new ArrayList<>();
    List<MESMachine> machines = new ArrayList<>();
    List<MESEquipment> equipments = new ArrayList<>();
    List<MESInstrument> instruments = new ArrayList<>();
    List<MESJigsFixture> jigsFixtures = new ArrayList<>();
    List<MESTool> tools = new ArrayList<>();


}