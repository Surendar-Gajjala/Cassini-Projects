package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Entity
@Data
@Table(name = "PLM_SHAREDFOLDER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSharedFolder implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    private Integer rowId;

    @Column(name = "FOLDER")
    private Integer folder;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SHARED_ON")
    private Date sharedOn;

    @Column(name = "SHARED_WITH")
    private Integer sharedWith;

    @Column(name = "SHARED_BY")
    private Integer sharedBy;

    @Column(name = "SHARE_SUBFOLDERS")
    private Boolean sharedSubFolders;



}
