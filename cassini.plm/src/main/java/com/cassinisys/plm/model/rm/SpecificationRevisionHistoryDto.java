package com.cassinisys.plm.model.rm;

import com.cassinisys.platform.model.common.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.List;
@Data
public class SpecificationRevisionHistoryDto {
    Specification specification;
    List<SpecificationRevisionStatusHistory> specificationRevisionStatusHistories;
    @Transient
    private Person person;


}