package com.cassinisys.test.model;

import com.cassinisys.platform.model.core.CassiniObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by CassiniSystems on 10-09-2018.
 */
@Entity
@Table(name = "TEST_FILE")
@PrimaryKeyJoinColumn(name = "FILE_ID")
@Data
@EqualsAndHashCode
public class TestFile extends CassiniObject {
    @ApiObjectField(required = true)
    @Column(name = "NAME")
    private String name;
    @ApiObjectField(required = true)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SIZE", nullable = false)
    @ApiObjectField(required = true)
    private Long size;
    @Column(name = "VERSION", nullable = false)
    @ApiObjectField(required = true)
    private Integer version;
    @Column(name = "LATEST", nullable = false)
    @ApiObjectField(required = true)
    private Boolean latest = true;

    public TestFile() {
        super(TestObjectType.FILE);
    }
}


