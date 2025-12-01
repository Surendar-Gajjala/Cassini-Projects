
package com.cassinisys.plm.model.mes.dto;

import lombok.Data;

/**
 * Created by smukka on 27-07-2022.
 */
@Data
public class BOPOperationResourceDto {
    private Integer id;
    private Integer bopOperation;
    private Integer operation;
    private Integer resourceType;
    private Integer resource;
    private String type;
    private String notes;

    private String number;
    private String name;
    private String description;
    private String typeName;
}
