package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class SystemInformation {
    private Long totalStorage;
    private Long usedStorage;
    private Long availableStorage;
    private String totalStorageReadableFormat;
    private String usedStorageReadableFormat;
    private String availableStorageReadableFormat;

    private String totalStoragePercentage;
    private String usedStoragePercentage;
    private String availableStoragePercentage;


}