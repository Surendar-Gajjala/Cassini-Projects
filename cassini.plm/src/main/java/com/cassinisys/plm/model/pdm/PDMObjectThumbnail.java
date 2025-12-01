package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_OBJECT_THUMBNAIL")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMObjectThumbnail implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "THUMBNAIL")
    private byte[] thumbnail;
}
