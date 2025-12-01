package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.plm.SharePermission;
import com.cassinisys.plm.model.plm.ShareType;
import com.cassinisys.plm.model.plm.dto.FileDto;
import com.cassinisys.plm.model.plm.dto.FolderDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by spolamreddy on 17-03-2022.
 */
@Data
public class SharedFolderObjectDto {
    private Integer id;
    private FileDto fileDto=new FileDto();
    private ShareType shareType;
    private SharePermission permission;
    private String sharedBy;
    private String sharedTo;
    private String type;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sharedOn;
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
}
