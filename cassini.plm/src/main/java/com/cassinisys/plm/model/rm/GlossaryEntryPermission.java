package com.cassinisys.plm.model.rm;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Gsr on 23-11-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYENTRYPERMISSION")
public class GlossaryEntryPermission implements Serializable {

    @Id
    @SequenceGenerator(name = "GLOSSARYENTRYPERMISSION_ID_GEN", sequenceName = "GLOSSARYENTRYPERMISSION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOSSARYENTRYPERMISSION_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GLOSSARY")
    private Integer glossary;

    @Column(name = "GLOSSARYUSER")
    private Integer glossaryUser;

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
