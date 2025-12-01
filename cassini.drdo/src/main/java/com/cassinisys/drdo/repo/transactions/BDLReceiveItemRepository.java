package com.cassinisys.drdo.repo.transactions;

import com.cassinisys.drdo.model.transactions.BDLReceiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by subramanyam on 17-07-2019.
 */
@Repository
public interface BDLReceiveItemRepository extends JpaRepository<BDLReceiveItem, Integer> {

    List<BDLReceiveItem> findByReceive(Integer receive);

    BDLReceiveItem findByIssueItem(Integer issueItem);
}
