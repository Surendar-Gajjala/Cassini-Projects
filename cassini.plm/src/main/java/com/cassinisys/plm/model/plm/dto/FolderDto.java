package com.cassinisys.plm.model.plm.dto;

import com.cassinisys.plm.model.plm.PLMFolder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 25-05-2020.
 */
@Data
public class FolderDto {

    private List<PLMFolder> publicFolders = new LinkedList<>();

    private List<PLMFolder> myFolders = new LinkedList<>();


}
