package com.cassinisys.platform.model.wfm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 12/02/16.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table (name = "WORKFLOWHISTORY")
public class WorkflowHistory {

    @Id
    @SequenceGenerator(name = "ROW_ID_GEN",
            sequenceName = "ROW_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ROW_ID_GEN")
    private Integer id;

    @Column (name = "WORKFLOW")
    private Integer workflow;

    @Column (name = "FROM_ACTIVITY")
    private Integer fromActivity;

    @Column (name = "ACTION")
    private Integer action;

    @Column (name = "TO_ACTIVITY")
    private Integer toActivity;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP", nullable = false)
    private Date timestamp = new Date();

    @Column (name = "PERSON")
    private Integer person;


}
