package com.cassinisys.plm.generate.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by CassiniSystems on 27-11-2021.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Manpowers extends BaseClass {

    private String manpowerType;
    private String manpowerNumber;
    private String typePath;
    private String manpowerName;
    private String manpowerDescription;
    private String newPerson;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

}
