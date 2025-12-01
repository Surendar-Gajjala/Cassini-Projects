package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 22/12/15.
 */
@Entity
@Data
@Table(name = "PLM_ITEMREVISIONHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMItemStatusHistory {
    @Id
    @SequenceGenerator(name = "ROW_ID_GEN", sequenceName = "ROW_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROW_ID_GEN")
    @Column(name = "ROWID")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private PLMItem item;

    @Column(name = "OLD_STATUS")
    private String oldStatus;


    @Column(name = "NEW_STATUS")
    private String newStatus;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;


}
