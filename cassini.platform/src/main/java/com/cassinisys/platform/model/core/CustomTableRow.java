package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Nageshreddy on 02-11-2018.
 */
@Entity
@Data
@PrimaryKeyJoinColumn(name = "ROW_ID")
@Table(name = "CUSTOMTABLEROW")
public class CustomTableRow extends CassiniObject {

    @Column(name = "TABLE_ID")
    private Integer tableId;


}
