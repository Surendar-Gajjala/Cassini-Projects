package com.cassinisys.platform.model.core;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "USER_ATTEMPTS")
public class UserAttempts implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "USER_ATTEMPT_ID_GEN",
            sequenceName = "USER_ATTEMPT_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "USER_ATTEMPT_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USERNAME", nullable = false)
    private String userName;

    @Column(name = "ATTEMPTS", nullable = false)
    private Integer attempts;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED", nullable = false)
    private Date lastModified;


    /*public UserAttempts(String userName, Integer attempts, Date lastModified) {
        this.userName = userName;
        this.attempts = attempts;
        this.lastModified = lastModified;
    }*/


}