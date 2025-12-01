package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_OBJECT_FILE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMObjectFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "OBJECT")
    private Integer object;

    @Column(name = "FILE")
    private Integer file;
}
