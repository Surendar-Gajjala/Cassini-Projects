package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by subramanyamreddy on 024 24-Jul -17.
 */
@Entity
@Data
@Table(name = "ATTRIBUTE_ATTACHMENT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttributeAttachment extends Attachment {

    @Column(name = "ATTRIBUTEDEF", nullable = false)
    private Integer attributeDef;


}
