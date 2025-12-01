package com.cassinisys.plm.model.pqm;

import com.cassinisys.plm.model.plm.PLMFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by GSR on 13-12-2020.
 */
@Entity
@Table(name = "PQM_CUSTOMER_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class PQMCustomerFile extends PLMFile {

    @Column(name = "CUSTOMER")
    private Integer customer;

}
