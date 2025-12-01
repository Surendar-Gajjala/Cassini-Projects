package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by CassiniSystems on 21-08-2020.
 */
@Data
@AllArgsConstructor
public class ASSpecSubstanceDelete {
    private String spec;
    private String number;
}
