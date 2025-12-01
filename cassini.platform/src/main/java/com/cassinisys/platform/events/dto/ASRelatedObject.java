package com.cassinisys.platform.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ASRelatedObject implements Serializable {
    private String number;
    private String notes = "";
}
