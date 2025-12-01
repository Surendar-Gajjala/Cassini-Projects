package com.cassinisys.pdm.model;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Entity
@Table(name = "PDM_ITEMREVISIONHISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "PDM")
public class PDMItemRevisionHistory implements Serializable{

    @Id
    @SequenceGenerator(
            name = "ITEMREVISIONHISTORY_ID_GEN",
            sequenceName = "ITEMREVISIONHISTORY_ID_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ITEMREVISIONHISTORY_ID_GEN"
    )
    @Column(name = "ROWID")
    private Integer rowId;

    @ApiObjectField(required = true)
    @Column(name = "ITEM")
    private Integer item;

    @Column(name = "OLD_REVISION")
    private String oldVersion;

    @Column(name = "NEW_REVISION")
    private String newVersion;

    @ApiObjectField(required = true)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Column(name = "TIMESTAMP")
    private Date timeStamp;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
}
