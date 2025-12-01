package com.cassinisys.tm.model;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.util.converter.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by reddy on 9/19/16.
 */
@Entity
@Table(name = "LAYOUT_DRAWING")
@PrimaryKeyJoinColumn(name = "LAYOUT_ID")
@ApiObject(name = "TM")
public class TMLayoutDrawing extends CassiniObject {

    @Column(name = "PROJECT")
    private Integer project;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "DATE", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    @ApiObjectField(required = true)
    private LocalDate date = LocalDate.now();

    public TMLayoutDrawing() {
        super(TMObjectType.LAYOUTDRAWING);
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
