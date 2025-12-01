package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 20-07-2021.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CUSTOM_OBJECT_BOM")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectBom extends CassiniObject {

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARENT")
    private CustomObject parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CHILD")
    private CustomObject child;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private List<CustomObjectBom> children = new ArrayList<>();

    public CustomObjectBom() {
        super.setObjectType(ObjectType.CUSTOMOBJECTBOM);
    }
}
