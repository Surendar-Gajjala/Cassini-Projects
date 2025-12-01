package com.cassinisys.drdo.model.bom;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.inventory.Storage;
import com.cassinisys.drdo.model.procurement.Manufacturer;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.CustomShortDateDeserializer;
import com.cassinisys.platform.util.CustomShortDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 07-10-2018.
 */
@Entity
@Table(name = "ITEMINSTANCE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class ItemInstance extends CassiniObject {

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM", nullable = false)
    private ItemRevision item;

    @ApiObjectField
    @Column(name = "SECTION")
    private Integer section;

    @ApiObjectField
    @Column(name = "BOM")
    private Integer bom;

    @ApiObjectField
    @Column(name = "INSTANCE_NAME", nullable = false)
    private String instanceName;

    @ApiObjectField
    @Column(name = "OEM_NUMBER")
    private String oemNumber;

    @ApiObjectField
    @Column(name = "UPN_NUMBER")
    private String upnNumber;

    @ApiObjectField
    @Column(name = "LOT_NUMBER")
    private String lotNumber;

    @ApiObjectField
    @Column(name = "LOT_SIZE")
    private Double lotSize;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STORAGE")
    private Storage storage;

    @Column(name = "REASON")
    private String reason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RETURN_BY")
    private Person returnBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MANUFACTURER")
    private Manufacturer manufacturer;

    @Column(name = "HAS_FAILED")
    private Boolean hasFailed = Boolean.FALSE;

    @Column(name = "UNIQUE_CODE")
    private String uniqueCode;

    @Transient
    private String lotSequence;

    @Transient
    private List<LotInstance> lotInstanceList;

    @ApiObjectField
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.drdo.model.bom.ItemInstanceStatus")})
    @Column(name = "STATUS")
    private ItemInstanceStatus status = ItemInstanceStatus.NEW;

    @ApiObjectField
    @Column(name = "INITIAL_UPN")
    private String initialUpn;

    @ApiObjectField
    @Column(name = "REVIEW")
    private Boolean review = Boolean.FALSE;

    @Column(name = "PROVISIONAL_ACCEPT")
    private Boolean provisionalAccept = Boolean.FALSE;

    @Column(name = "PRESENT_STATUS")
    private String presentStatus;

    @Column(name = "RETURNING_PART")
    private Boolean returningPart = Boolean.FALSE;

    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;

    @Column(name = "ROOTCARD_NO")
    private String rootCardNo;

    @Transient
    private Inward inward;

    @Transient
    private String certificateNumber;

    @Transient
    private String storagePath;

    @Transient
    private Double lotIssuedQuantity = 0.0;

    @Transient
    private String sectionName;

    @Transient
    private Double remainingQuantity = 0.0;


    @JsonSerialize(using = CustomShortDateSerializer.class)
    @JsonDeserialize(using = CustomShortDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Transient
    private Date issuedDate;


    public ItemInstance() {
        super(DRDOObjectType.ITEMINSTANCE);
    }

    public ItemRevision getItem() {
        return item;
    }

    public void setItem(ItemRevision item) {
        this.item = item;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public Integer getBom() {
        return bom;
    }

    public void setBom(Integer bom) {
        this.bom = bom;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getOemNumber() {
        return oemNumber;
    }

    public void setOemNumber(String oemNumber) {
        this.oemNumber = oemNumber;
    }

    public String getUpnNumber() {
        return upnNumber;
    }

    public void setUpnNumber(String upnNumber) {
        this.upnNumber = upnNumber;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Double getLotSize() {
        return lotSize;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public ItemInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(ItemInstanceStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Person getReturnBy() {
        return returnBy;
    }

    public void setReturnBy(Person returnBy) {
        this.returnBy = returnBy;
    }

    public String getInitialUpn() {
        return initialUpn;
    }

    public void setInitialUpn(String initialUpn) {
        this.initialUpn = initialUpn;
    }

    public Boolean getReview() {
        return review;
    }

    public void setReview(Boolean review) {
        this.review = review;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Inward getInward() {
        return inward;
    }

    public void setInward(Inward inward) {
        this.inward = inward;
    }

    public String getPresentStatus() {
        return presentStatus;
    }

    public void setPresentStatus(String presentStatus) {
        this.presentStatus = presentStatus;
    }

    public Boolean getReturningPart() {
        return returningPart;
    }

    public void setReturningPart(Boolean returningPart) {
        this.returningPart = returningPart;
    }

    public Boolean getHasFailed() {
        return hasFailed;
    }

    public void setHasFailed(Boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    public Boolean getProvisionalAccept() {
        return provisionalAccept;
    }

    public void setProvisionalAccept(Boolean provisionalAccept) {
        this.provisionalAccept = provisionalAccept;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Double getLotIssuedQuantity() {
        return lotIssuedQuantity;
    }

    public void setLotIssuedQuantity(Double lotIssuedQuantity) {
        this.lotIssuedQuantity = lotIssuedQuantity;
    }

    public List<LotInstance> getLotInstanceList() {
        return lotInstanceList;
    }

    public void setLotInstanceList(List<LotInstance> lotInstanceList) {
        this.lotInstanceList = lotInstanceList;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getLotSequence() {
        return lotSequence;
    }

    public void setLotSequence(String lotSequence) {
        this.lotSequence = lotSequence;
    }

    public Double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getRootCardNo() {
        return rootCardNo;
    }

    public void setRootCardNo(String rootCardNo) {
        this.rootCardNo = rootCardNo;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }
}

