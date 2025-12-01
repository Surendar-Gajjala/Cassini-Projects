package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GSR on 19-06-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYENTRYHISTORY")
public class PLMGlossaryEntryHistory implements Serializable {
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "GLOSSARYENTRYHISTORY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ENTRY")
    private Integer entry;

    @Column(name = "FROM_VERSION")
    private Integer fromVersion;

    @Column(name = "TO_VERSION")
    private Integer toVersion;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "NOTES")
    private String notes;

}
