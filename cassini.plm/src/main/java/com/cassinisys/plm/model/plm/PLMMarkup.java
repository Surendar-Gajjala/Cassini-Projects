package com.cassinisys.plm.model.plm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "Markup")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMMarkup implements Serializable {

    @Id
    @SequenceGenerator(name = "MARKUP_ID_GEN", sequenceName = "MARKUP_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARKUP_ID_GEN")
    @Column(name = "MarkupID")
    private Integer markupId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "FILE_ID")
    private Integer fileId;

    @Column(name = "REG_DATE")
    private Date reg_date;

    @Column(name = "SVG_TEXT")
    private String svgText;

    public PLMMarkup() {
    }

}
