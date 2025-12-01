package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by swapna on 12/30/17.
 */
@Entity
@Data
@Table(name = "PLM_EVENTS")
@JsonIgnoreProperties(ignoreUnknown = false)
public class PLMEvents implements Serializable {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column
    private Integer id;

    @Column
    private String event;

    @Column
    private Integer target;

    @Column
    private String script;

    public PLMEvents() {
    }


}
