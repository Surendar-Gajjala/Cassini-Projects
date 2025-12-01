package com.cassinisys.pdm.repo;

import com.cassinisys.pdm.model.PDMItemFile;
import com.cassinisys.pdm.model.PDMItemFileVersionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyamreddy on 20-Jan-17.
 */
@Repository
public interface ItemFileVersionHistoryRepository extends JpaRepository<PDMItemFileVersionHistory, Integer>{
    PDMItemFileVersionHistory findByItemAndFileName(Integer item,String name);
    List<PDMItemFileVersionHistory> findAllByItemAndFileNameOrderByTimeStampDesc(Integer item,String name);
}
