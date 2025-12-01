package com.cassinisys.plm.model.pqm.dto;

import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.plm.model.plm.dto.FileDto;
import lombok.Data;

import java.util.*;

/**
 * Created by subramanyam on 09-06-2020.
 */
@Data
public class ObjectFileDto {
    private List<Media> checklistImages = new ArrayList<>();
    private List<Attachment> checklistAttachments = new ArrayList<>();
    private Map<String, Boolean> dmPermissions = new HashMap<>();
    private List<FileDto> objectFiles = new LinkedList<>();
    private FileDto objectFile;
}
