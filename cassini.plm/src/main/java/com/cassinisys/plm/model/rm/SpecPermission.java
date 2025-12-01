package com.cassinisys.plm.model.rm;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Gsr on 19-11-2018.
 */
@Entity
@Data
@Table(name = "RM_SPECPERMISSION")
public class SpecPermission implements Serializable {

    @Id
    @SequenceGenerator(name = "SPECPERMISSION_ID_GEN", sequenceName = "SPECPERMISSION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPECPERMISSION_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SPECIFICATION")
    private Integer specification;

    @Column(name = "SPECUSER")
    private Integer specUser;

    @Column(name = "EDIT_PERMISSION")
    private Boolean editPermission = Boolean.FALSE;

    @Column(name = "DELETE_PERMISSION")
    private Boolean deletePermission = Boolean.FALSE;

    @Column(name = "ACCEPT_REJECT_PERMISSION")
    private Boolean acceptRejectPermission = Boolean.FALSE;

    @Column(name = "STATUS_CHANGE_PERMISSION")
    private Boolean statusChangePermission = Boolean.FALSE;

    @Column(name = "IMPORT_PERMISSION")
    private Boolean importPermission = Boolean.FALSE;

    @Column(name = "EXPORT_PERMISSION")
    private Boolean exportPermission = Boolean.FALSE;


}

