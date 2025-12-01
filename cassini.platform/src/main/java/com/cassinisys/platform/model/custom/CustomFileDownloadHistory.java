package com.cassinisys.platform.model.custom;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by GSR on 16-10-2020.
 */
@Entity
@Table(name = "CUSTOM_FILE_DOWNLOAD_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomFileDownloadHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "FILE_DOWNLOAD_ID_GEN", sequenceName = "FILE_DOWNLOAD_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_DOWNLOAD_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FILE_ID")
    private Integer fileId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSON")
    private Person person;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DOWNLOAD_DATE")
    private Date downloadDate;

}
