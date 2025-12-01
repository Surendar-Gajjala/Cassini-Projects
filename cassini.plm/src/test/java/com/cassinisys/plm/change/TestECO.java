/*
package com.cassinisys.plm.change;

import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.model.cm.PLMAffectedItem;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.wf.WorkflowStatusType;
import com.cassinisys.plm.repo.cm.AffectedItemRepository;
import com.cassinisys.plm.repo.cm.ECORepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by reddy on 6/7/17.
 *//*

public class TestECO extends BaseTest {
    @Autowired
    private ECORepository ecoRepository;

    @Autowired
    private AffectedItemRepository itemRepository;

    @Test
    public void testUnreleaseEcos() throws Exception {
        List<PLMECO> ecos = ecoRepository.findByStatusTypeNot(WorkflowStatusType.RELEASED);
        List<Integer> ids = new ArrayList<>();
        ecos.forEach(eco -> {
            ids.add(eco.getId());
        });

        List<PLMAffectedItem> items = itemRepository.findByChangeInAndItem(ids, 43);
        if(items.size() > 0) {
            System.out.println("There are pending ECOs for this item");
        }
        else {
            System.out.println("Item has no pending ECOs");
        }
    }
}
*/
