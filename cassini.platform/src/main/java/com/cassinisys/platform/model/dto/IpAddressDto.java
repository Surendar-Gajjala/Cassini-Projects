package com.cassinisys.platform.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class IpAddressDto {

    public String description;
    public String address;
    public boolean addressActive;
}
