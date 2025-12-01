package com.cassinisys.plm.model.plm;

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
 * Created by subramanyam on 04-05-2018.
 */
@Entity
@Data
@Table(name = "PLM_FILEDOWNLOADHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMFileDownloadHistory implements Serializable {

    @Id
    @SequenceGenerator(name = "FILEDOWNLOAD_ID_GEN", sequenceName = "FILEDOWNLOAD_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILEDOWNLOAD_ID_GEN")
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
