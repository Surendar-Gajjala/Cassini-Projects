package com.cassinisys.plm.repo.plm;

import com.cassinisys.plm.model.plm.PLMFileDownloadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subra on 04-05-2018.
 */
@Repository
public interface FileDownloadHistoryRepository extends JpaRepository<PLMFileDownloadHistory, Integer> {

    List<PLMFileDownloadHistory> findByFileIdOrderByDownloadDateAsc(Integer fileId);

    List<PLMFileDownloadHistory> findByFileIdOrderByDownloadDateDesc(Integer fileId);
}
