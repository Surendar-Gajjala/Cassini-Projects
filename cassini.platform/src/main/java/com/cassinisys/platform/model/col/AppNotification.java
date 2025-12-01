package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 15-10-2018.
 */

@Entity
@Data
@Table(name = "APPNOTIFICATION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppNotification implements Serializable {

	@Id
	@SequenceGenerator(name = "APPNOTIFICATION_ID_GEN",
			sequenceName = "APPNOTIFICATION_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "APPNOTIFICATION_ID_GEN")
	@Column(name = "APPNOTIFICATION_ID", nullable = false)
	private Integer id;


	@Column(name = "EVENT_NAME")
	private String eventName;


	@Column(name = "EVENT_DESCRIPTION")
	private String eventDescription;


	@Column(name = "ENABLED")
	private Boolean enabled = Boolean.FALSE;



}
