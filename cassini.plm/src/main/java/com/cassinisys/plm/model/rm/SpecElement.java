package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RM_SPECELEMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecElement extends CassiniObject {

    @Column(name = "SEQ_NUMBER")
    private String seqNumber;

    @Column(name = "SPECIFICATION")
    private Integer specification;

    @Column(name = "TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters =
            {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.plm.model.rm.SpecElementType")})
    private SpecElementType type = SpecElementType.SECTION;

    @Column(name = "PARENT")
    private Integer parent;

    @Transient
    private Integer reqEditsLength;
    @Transient
    private List<SpecElement> children = new ArrayList<>();

    public SpecElement() {
    }

    public SpecElement(PLMObjectType type) {
        super(type);
    }


}
