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
@PrimaryKeyJoinColumn(name = "COLUMN_ID")
@Table(name = "CUSTOMTABLECOLUMN")
public class CustomTableColumn extends ObjectTypeAttribute {

    @Column(name = "TABLE_ID")
    private Integer tableId;


}
