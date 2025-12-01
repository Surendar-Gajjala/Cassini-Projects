package com.cassinisys.plm.model.dto;

import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import lombok.Data;

/**
 * Created by CassiniSystems on 26-02-2019.
 */
@Data
public class AttributeSearchDto {

    private ObjectTypeAttribute objectTypeAttribute;

    private String text;

    private String longText;

    private String integer;

    private String list;

    private Boolean aBoolean = Boolean.FALSE;

    private Boolean booleanSearch = Boolean.FALSE;

    private Double aDouble = 0.0;

    private Boolean doubleSearch = Boolean.FALSE;

    private String currency;

    private String date;

    private String time;

	/*private List<String> mListValue = new ArrayList<>();*/

    private String[] mListValue;

}
