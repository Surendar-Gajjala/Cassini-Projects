package com.cassinisys.platform.model.activitystream;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.service.activitystream.ActivityStreamEntityListener;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "ACTIVITY_STREAM")
@EntityListeners(ActivityStreamEntityListener.class)
public class ActivityStream implements Serializable {
    @Id
    @SequenceGenerator(name = "ACTIVITYSTREAM_ID_SEQ", sequenceName = "ACTIVITYSTREAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITYSTREAM_ID_SEQ")
    private Long id;

    @Column(name = "ACTIVITY")
    private String activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACTOR")
    private Person actor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "OBJECT")
    private ActivityStreamObject object;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "SOURCE")
    private ActivityStreamObject source;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "TARGET")
    private ActivityStreamObject target;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "CONTEXT")
    private ActivityStreamObject context;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TIMESTAMP")
    private Date timestamp;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "CONVERTER")
    private String converter;

    @Column(name = "DATA")
    private String data;

    @Column(name = "SESSION")
    private Integer session;

    @Transient
    private String summary;

    @Transient
    private Session sessionObject;
}
