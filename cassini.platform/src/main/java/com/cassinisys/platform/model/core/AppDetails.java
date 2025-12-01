package com.cassinisys.platform.model.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Nageshreddy on 13-11-2019.
 */

@Entity
@Table(name = "APPLICATION_DETAILS")
@Getter
@Setter
public class AppDetails implements Serializable {

    @Id
    @SequenceGenerator(
            name = "APPLICATION_SEQUENCE_GEN",
            sequenceName = "APPLICATION_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "APPLICATION_SEQUENCE_GEN"
    )
    @Column(
            name = "ID",
            nullable = false
    )
    private Integer id;

    @Column(name = "OPTION_KEY")
    private Integer optionKey;

    @Column(name = "OPTION_NAME")
    private String optionName;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "VALUE")
    private String value;
    @Transient
    private String systemUpTime;

    public String getSystemUpTime() {
        return systemUpTime;
    }

    public void setSystemUpTime(String systemUpTime) {
        this.systemUpTime = systemUpTime;
    }
}
