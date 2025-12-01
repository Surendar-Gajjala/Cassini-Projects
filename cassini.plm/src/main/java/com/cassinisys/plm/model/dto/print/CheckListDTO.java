package com.cassinisys.plm.model.dto.print;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CheckListDTO {
    private String title;
    private String summary;
    private String params;
    private Integer level;


}