package com.cassinisys.platform.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_PREFERENCE")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UserPreferences implements Serializable {
    @Id
    @SequenceGenerator(name = "USER_PREFERENCE_ID_GEN", sequenceName = "USER_PREFERENCE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_PREFERENCE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "USER_THEME")
    private String userTheme;

    @Column(name = "PREFERRED_PAGE")
    private String preferredPage;

    @Column(name = "USER_DATE_FORMAT")
    private String userDateFormat;

    @Column(name = "LOGIN")
    private Integer login;

    @Column(name = "SHORT_DATE_FORMAT")
    private String shortDateFormat;

    @Column(name = "USER_WIDGET_JSON")
    private String userWidgetJson;
}