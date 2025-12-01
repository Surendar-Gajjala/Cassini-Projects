package com.cassinisys.plm.model.rm;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 16-09-2018.
 */
@Data
public class GlossaryDto {
    List<PLMGlossaryEntryItem> entryItems = new ArrayList<>();
    private PLMGlossary glossary;


}
