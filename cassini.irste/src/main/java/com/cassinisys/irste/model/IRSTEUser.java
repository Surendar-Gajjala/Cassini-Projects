package com.cassinisys.irste.model;

import com.cassinisys.platform.model.common.Person;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Nageshreddy on 20-11-2018.
 */

@Data
@Entity
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "IRSTE_USER")
public class IRSTEUser extends Person {

    @Column(name = "TRAINEE_ID")
    private String traineeId;

    @Column(name = "DESIGNATION")
    private String designation;

    @Transient
    private List<String> utilities;


    public IRSTEUser() {
        super();
    }
}
