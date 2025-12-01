package com.cassinisys.plm.model.cm.dto;

import com.cassinisys.platform.model.common.Person;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by spolamreddy on 14-10-2022.
 */
@Data
public class ChangeObjectsFilterDto {
    List<String> status = new LinkedList<>();
    List<String> urgencies = new LinkedList<>();
    List<String> changeReasonTypes = new LinkedList<>();
    List<Person> originators = new LinkedList<>();
    List<Person> changeAnalysts = new LinkedList<>();
    List<Person> requestedBys = new LinkedList<>();


}
