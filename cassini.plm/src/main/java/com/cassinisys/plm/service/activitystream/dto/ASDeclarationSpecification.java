package com.cassinisys.plm.service.activitystream.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by subramanyam on 11-12-2020.
 */
@Data
@AllArgsConstructor
public class ASDeclarationSpecification implements Serializable {
    private String name;
    private String number;
}
