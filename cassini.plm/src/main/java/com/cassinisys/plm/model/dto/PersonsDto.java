package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.common.Person;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 07-01-2021.
 */
@Data
public class PersonsDto {
    List<Person> customers = new ArrayList<>();
    List<Person> supplierContacts = new ArrayList<>();
    List<Person> manpowers = new ArrayList<>();
    List<Person> manpowerContacts = new ArrayList<>();
}
