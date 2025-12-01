package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "DM_FOLDER_PERMISSION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DMFolderPermission implements Serializable {
	
	@Id
	@SequenceGenerator(name = "DMFOLDERPERMISSION_ID_GEN", sequenceName = "DMFOLDERPERMISSION_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DMFOLDERPERMISSION_ID_GEN")
	@Column(name = "PERMISSION_ID")
	private Integer permissionId;

	@Column(name = "FOLDER_ID")
	private Integer folderId;

	@Column(name = "GROUP_ID")
	private Integer groupId;

	@Column(name = "ACTIONS")
	private String actions;

	@Column(name = "IS_SUB_FOLDER")
	private Boolean isSubFolder = Boolean.FALSE;

}
