package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.common.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.List;
@Data
public class RequirementRevisionHistoryDto {
    Requirement requirement;
    List<RequirementRevisionStatusHistory> requirementRevisionStatusHistories;

    @Transient
    private Person person;


}