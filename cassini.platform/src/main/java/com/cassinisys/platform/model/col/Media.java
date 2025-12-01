package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by swapna on 25/09/18.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MEDIA")
@PrimaryKeyJoinColumn(name = "MEDIA_ID")
public class Media extends CassiniObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "TYPE", nullable = false)
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.col.MediaType")})
    private MediaType mediaType;

    @Column(name = "FILENAME")
    private String fileName;

    @Column
    private String extension;

    @Column
    @JsonIgnore
    private byte[] data;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LOCATION")
    private Location location;

    @Column(name = "DESCRIPTION")
    private String description;

    public Media() {
        super(ObjectType.MEDIA);
    }


}
