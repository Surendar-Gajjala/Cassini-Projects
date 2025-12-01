package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_FOLDER")
@PrimaryKeyJoinColumn(name = "FOLDER_ID ")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMFolder extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "OWNER")
    private Integer owner;

    @Column(name = "PARENT")
    private Integer parent;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.plm.FolderType")})
    @Column(name = "TYPE", nullable = true)
    private FolderType type;

    @Transient
    private List<PLMFolder> children = new ArrayList<>();

    @Transient
    private Boolean objectsExist = Boolean.FALSE;

    protected PLMFolder() {
        super(PLMObjectType.FOLDER);
    }


}
