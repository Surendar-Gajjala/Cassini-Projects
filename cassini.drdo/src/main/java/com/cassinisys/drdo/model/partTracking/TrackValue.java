package com.cassinisys.drdo.model.partTracking;

import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "TRACKVALUE")
public class TrackValue extends CassiniObject {

    @Column(name = "PARTTRACKING")
    private Integer partTracking;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "ATTACHMENT")
    private Integer attachment;

    @Column(name = "CHECKED_DEPT")
    private String checkedDept;

    @Column(name = "CHECKED")
    private Boolean checked;

    @Column(name = "SCAN")
    private Boolean scan;

    @Transient
    private PartTrackingItems partTrackingItems;

    @Transient
    private String checkedPersonName;

    @Transient
    private String attachmentName;

    protected TrackValue() {
        super(DRDOObjectType.TRACKVALUE);
    }

    public Integer getPartTracking() {
        return partTracking;
    }

    public void setPartTracking(Integer partTracking) {
        this.partTracking = partTracking;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getAttachment() {
        return attachment;
    }

    public void setAttachment(Integer attachment) {
        this.attachment = attachment;
    }

    public String getCheckedDept() {
        return checkedDept;
    }

    public void setCheckedDept(String checkedDept) {
        this.checkedDept = checkedDept;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getScan() {
        return scan;
    }

    public void setScan(Boolean scan) {
        this.scan = scan;
    }

    public PartTrackingItems getPartTrackingItems() {
        return partTrackingItems;
    }

    public void setPartTrackingItems(PartTrackingItems partTrackingItems) {
        this.partTrackingItems = partTrackingItems;
    }

    public String getCheckedPersonName() {
        return checkedPersonName;
    }

    public void setCheckedPersonName(String checkedPersonName) {
        this.checkedPersonName = checkedPersonName;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
