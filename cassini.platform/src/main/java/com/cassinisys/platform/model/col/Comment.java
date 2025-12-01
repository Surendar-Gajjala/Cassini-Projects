package com.cassinisys.platform.model.col;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author reddy
 */
@Entity
@Data
@Table(name = "COMMENT")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "COMMENT_ID_GEN",
            sequenceName = "COMMENT_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "COMMENT_ID_GEN")
    @Column(name = "COMMENT_ID", nullable = false)
    private Integer id;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "COMMENTED_DATE", nullable = false)
    private Date commentedDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COMMENTED_BY", nullable = false)
    private Person commentedBy;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.core.ObjectType")})
    @Column(name = "OBJECT_TYPE", nullable = true)
    private ObjectType objectType;

    @Column(name = "OBJECT_ID", nullable = true)
    private Integer objectId;

    @Column(name = "REPLY_TO")
    private Integer replyTo;

    @Transient
    private CassiniObject object;

    @Transient
    private List<Media> images = new ArrayList<>();

    @Transient
    private List<Media> videos = new ArrayList<>();
    @Transient
    private String type;
    @Transient
    private Integer parentObjectId;
    @Transient
    private String number;
    @Transient
    private Boolean read = Boolean.TRUE;
    @Transient
    private Integer parentId;

    @Transient
    private List<Attachment> attachments = new ArrayList<>();

    public Comment() {
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
