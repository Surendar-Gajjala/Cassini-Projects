package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.model.plm.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam on 23-09-2021.
 */
@Data
public class FileDto {
    private Integer id;
    private String name;

    private String description;

    private Integer version;

    private Long size;

    private Boolean latest = true;

    private Boolean locked = false;
    private Boolean shared = false;

    private String urn;

    private String thumbnail;

    private Integer lockedBy;
    private String lockedByName;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockedDate;

    private List<Comment> comments = new ArrayList<>();

    private String replaceFileName;
    private String parentObjectName;
    private PLMObjectType type;
    private Integer parentObjectId;

    private String fileNo;

    private Integer parentFile;

    private String fileType;

    private List<PLMFileDownloadHistory> downloadHistories = new ArrayList<>();

    private Integer count = 0;

    private PLMObjectType objectType;

    private PLMObjectType parentObject;

    private Integer oldVersion;

    private Integer object;
    private Integer folder;
    private String createdByName;
    private String modifiedByName;
    private Integer createdBy;
    private Boolean external = false;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String filePath;

    private String revision;

    private PLMLifeCyclePhase lifeCyclePhase;
    private Integer signOffCount;
    private List<PLMDocumentReviewer> reviewers = new ArrayList<>();
    private List<FileDto> children = new ArrayList<>();
    private Integer childFileCount = 0;
}
