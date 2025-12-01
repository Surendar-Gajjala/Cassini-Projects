package com.cassinisys.plm.model.rm;

import lombok.Data;

import java.util.List;

/**
 * Created by subra on 29-06-2018.
 */
@Data
public class GlossaryRevisionHistoryDto {

    PLMGlossary plmGlossary;

    List<PLMGlossaryRevisionStatusHistory> revisionStatusHistories;


}
