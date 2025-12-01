package com.cassinisys.platform.model.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "CUSTOM_REVISIONED_OBJECT_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomRevisionedObjectFile extends CustomFile {
    @Column(name = "OBJECT")
    private Integer object;
}
