package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 15-10-2018.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "NOTIFICATIONTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationTo implements Serializable {

	@Id
	@SequenceGenerator(name = "NOTIFICATIONTO_ID_GEN",
			sequenceName = "NOTIFICATIONTO_ID_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "NOTIFICATIONTO_ID_GEN")
	@Column(name = "NOTIFICATIONTO_ID", nullable = false)
	private Integer id;

	@Column(name = "NOTIFICATION")
	private Integer notification;


	@Column(name = "NOTIFIED_TYPE")
	@Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.col.NotifiedType")})
	private NotifiedType notifiedType;


	@Column(name = "NOTIFIED_TO")
	private Integer notifiedTo;



}
