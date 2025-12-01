package com.cassinisys.plm.model.pgc;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 23-10-2020.
 */
@Data
public class PGCObjectTypesDto {

    List<PGCSubstanceType> substanceTypes = new ArrayList<>();

    List<PGCSubstanceGroupType> substanceGroupTypes = new ArrayList<>();

    List<PGCSpecificationType> specificationTypes = new ArrayList<>();

    List<PGCDeclarationType> declarationTypes = new ArrayList<>();

}
