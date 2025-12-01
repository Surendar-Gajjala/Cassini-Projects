package com.cassinisys.platform.repo.custom;

import com.cassinisys.platform.model.custom.CustomFileDownloadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by GSR on 16-10-2020.
 */
@Repository
public interface CustomFileDownloadHistoryRepository extends JpaRepository<CustomFileDownloadHistory, Integer> {

    List<CustomFileDownloadHistory> findByFileIdOrderByDownloadDateAsc(Integer fileId);

    List<CustomFileDownloadHistory> findByFileIdOrderByDownloadDateDesc(Integer fileId);
}
