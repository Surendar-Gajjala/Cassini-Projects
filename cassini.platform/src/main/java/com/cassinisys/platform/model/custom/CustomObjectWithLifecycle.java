package com.cassinisys.platform.model.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CUSTOM_OBJECT_WITH_LIFECYCLE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectWithLifecycle extends CustomObject {
    @Column(name = "LIFECYCLE")
    private String lifecycle;
}
