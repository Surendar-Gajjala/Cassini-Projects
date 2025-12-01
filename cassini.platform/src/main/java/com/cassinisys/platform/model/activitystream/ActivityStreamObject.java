package com.cassinisys.platform.model.activitystream;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ACTIVITY_STREAM_OBJECT")
public class ActivityStreamObject implements Serializable {
    @Id
    @SequenceGenerator(name = "ACTIVITYSTREAM_OBJECT_ID_SEQ", sequenceName = "ACTIVITYSTREAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITYSTREAM_OBJECT_ID_SEQ")
    private Long id;

    @Column(name = "OBJECT")
    private Integer object;

    @Column(name = "TYPE")
    private String type;
}
