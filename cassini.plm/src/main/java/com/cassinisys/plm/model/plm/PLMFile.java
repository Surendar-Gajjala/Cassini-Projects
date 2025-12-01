package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by reddy on 22/12/15.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_FILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMFile extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "VERSION", nullable = false)
    private Integer version;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Column(name = "LATEST", nullable = false)
    private Boolean latest = true;

    @Column(name = "LOCKED", nullable = false)
    private Boolean locked = false;

    @Column(name = "FILE_URN")
    private String urn;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "LOCKED_BY", nullable = false)
    private Integer lockedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE")
    private Date lockedDate;

    @Transient
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "REPLACEFILENAME")
    private String replaceFileName;

    @Column(name = "FILE_NO")
    private String fileNo;

    @Column(name = "PARENT_FILE")
    private Integer parentFile;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Transient
    private List<PLMFileDownloadHistory> downloadHistories = new ArrayList<>();

    @Transient
    private Integer count = 0;

    @Transient
    private PLMObjectType parentObject;

    @Transient
    private Integer oldVersion;

    @Transient
    private DocumentType documentType;
    @Transient
    private Integer previsionFileId;
    @Transient
    private List<PLMFile> folderChildren = new LinkedList<>();

    @Transient
    private String filePath;

    public PLMFile() {
        super(PLMObjectType.FILE);
    }


}
