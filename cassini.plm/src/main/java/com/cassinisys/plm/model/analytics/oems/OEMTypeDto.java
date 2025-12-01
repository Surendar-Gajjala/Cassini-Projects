package com.cassinisys.plm.model.analytics.oems;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by CassiniSystems on 19-07-2020.
 */
@Data
public class OEMTypeDto {

	private List<Integer> partsByStatus = new LinkedList<>();

	private List<Integer> mfrByStatus = new LinkedList<>();

	private List<Integer> allPartsByStatus = new LinkedList<>();


}
