package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.plm.dto.FileDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smukka on 30-08-2022.
 */
@Data
public class ShareObjectFileDto {
    private List<FileDto> files = new ArrayList<>();
    private List<ProgramProjectFolderDto> objects = new ArrayList<>();
}
