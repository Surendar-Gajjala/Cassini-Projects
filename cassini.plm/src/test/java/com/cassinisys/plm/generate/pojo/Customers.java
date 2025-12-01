package com.cassinisys.plm.generate.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.ConstructorProperties;

/**
 * Created by Nageshreddy on 25-11-2021.
 */

@Data
@AllArgsConstructor
public class Customers extends BaseClass{

    private String cName;
    private String phoneNumber;
    private String address;
    private String email;
    private String notes;
    private String firstName;
    private String lastname;
    private String cEmail;
    private String cPhoneNumber;

}
