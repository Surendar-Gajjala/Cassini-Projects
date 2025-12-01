package com.cassinisys.plm.model.dto;

import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 26-05-2020.
 */
@Data
public class ItemMfrPartsDto {

	private Integer itemId;
	private String revision;
	private String itemNumber;
	private String itemName;
    private PLMItemManufacturerPart itemManufacturerPart;
	private List<PLMItemManufacturerPart> itemManufacturerParts = new ArrayList<>();

}
