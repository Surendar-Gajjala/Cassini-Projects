package com.cassinisys.plm.model.mfr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 04-07-2020.
 */
@Entity
@Table(name = "PLM_MANUFACTURER_PART_HISTORY")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PLMManufacturerPartHistory implements Serializable {
	@Id
	@SequenceGenerator(name = "MANUFACTURER_PART_HISTORY_ID_GEN", sequenceName = "MANUFACTURER_PART_HISTORY_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANUFACTURER_PART_HISTORY_ID_GEN")
	@Column(name = "ID")
	private Integer id;

	@Column(name = "AFFECTED_PART")
	private Integer affectedPart;

	@Column(name = "ITEM_SOURCE")
	@Type(type = "com.cassinisys.platform.util.converter.IntArrayUserType")
	private Integer[] itemSources = new Integer[0];


}
