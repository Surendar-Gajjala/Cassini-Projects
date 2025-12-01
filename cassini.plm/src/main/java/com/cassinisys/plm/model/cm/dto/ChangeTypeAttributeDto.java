package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.plm.model.cm.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 14-06-2020.
 */
@Data
public class ChangeTypeAttributeDto {

	private List<PLMChangeAttribute> changeAttributes = new ArrayList<>();

	private List<ObjectAttribute> objectAttributes = new ArrayList<>();

	private PLMDCO plmdco;

	private PLMDCR plmdcr;

	private PLMECR plmecr;

	private PLMMCO mco;

	private PLMItemMCO itemMco;

	private PLMManufacturerMCO manufacturerMCO;


}
