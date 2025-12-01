package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity(name = "CUSTOM_FILE")
@PrimaryKeyJoinColumn(name = "ID")
public class CustomFile extends CassiniObject {
    @Column(name = "NUMBER")
    private String number;

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

    @Column(name = "LOCKED_BY", nullable = false)
    private Integer lockedBy;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCKED_DATE")
    private Date lockedDate;

    @Column(name = "REPLACEFILENAME")
    private String replaceFileName;

    @Column(name = "PARENT_FILE")
    private Integer parentFile;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_URN")
    private String urn;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Transient
    private Integer count = 0;

    @Transient
    private ObjectType parentObject;

    @Transient
    private Integer oldVersion;

    @Transient
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private List<CustomFileDownloadHistory> downloadHistories = new ArrayList<>();

    public CustomFile() {
        super.setObjectType(ObjectType.CUSTOMFILE);
    }
}
