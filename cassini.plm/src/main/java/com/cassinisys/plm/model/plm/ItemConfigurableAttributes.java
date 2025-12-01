package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 14-05-2020.
 */
@Entity
@Data
@Table(name = "ITEM_CONFIGURABLEATTRIBUTES")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemConfigurableAttributes implements Serializable {

    @Id
    @SequenceGenerator(name = "ITEM_CONFIG_ID_GEN", sequenceName = "ITEM_CONFIG_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_CONFIG_ID_GEN")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ITEM")
    private Integer item;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ATTRIBUTE")
    private PLMItemTypeAttribute attribute;

    @Column(name = "VALUES", nullable = false)
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] values = new String[0];

}
