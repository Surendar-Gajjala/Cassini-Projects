package com.cassinisys.plm.model.mes;

import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyam on 17-11-2020.
 */
@Entity
@Table(name = "MES_MANUFACTURER_DATA")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MESManufacturerData implements Serializable {

    @Id
    @Column(name = "OBJECT")
    private Integer object;

    @Column(name = "MFR_NAME")
    private String mfrName;

    @Column(name = "MFR_DESCRIPTION")
    private String mfrDescription;

    @Column(name = "MFR_MODEL_NUMBER")
    private String mfrModelNumber;

    @Column(name = "MFR_PART_NUMBER")
    private String mfrPartNumber;

    @Column(name = "MFR_SERIAL_NUMBER")
    private String mfrSerialNumber;

    @Column(name = "MFR_LOT_NUMBER")
    private String mfrLotNumber;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.DATE)
    @Column(name = "MFR_DATE")
    private Date mfrDate;
}
