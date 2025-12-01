package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMFileVersionHistory;
import com.cassinisys.plm.model.plm.PLMItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileVersionHistoryRepository extends JpaRepository<PLMFileVersionHistory, Integer> {
    List<PLMFileVersionHistory> findByFile(PLMItemFile file);
}
