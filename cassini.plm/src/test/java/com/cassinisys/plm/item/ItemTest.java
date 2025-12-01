/*
package com.cassinisys.plm.item;

import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.filtering.ItemAdvancedCriteria;
import com.cassinisys.plm.filtering.ParameterCriteria;
import com.cassinisys.plm.model.plm.PLMItem;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.service.plm.ItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

*/
/**
 * Created by reddy on 6/7/16.
 *//*

public class ItemTest extends BaseTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ItemAdvancedCriteria itemAdvancedCriteria;

    @Test
    @Rollback(false)
    public void testNewRevision() throws Exception {
        */
/*PLMItem item = itemService.findByItemNumberAndRevision("ASSY-00002", "C");
        if (item != null) {
            PLMItem newRev = itemService.reviseItem(item);
            *//*
*/
/*System.out.println(newRev.getRevision());*//*
*/
/*
        }*//*

    }

    @Test
    public void testJoinQueries() throws Exception {

        String jpql = "select it from PDMItem it where it.id in (" +
                "select distinct attr.id.objectId from PDMItemAttribute attr where attr.id.attributeDef = 2 and attr.integerValue = 10) and " +
                "it.id in (select distinct attr.id.objectId from PDMItemAttribute attr where attr.id.attributeDef = 1 and attr.stringValue = 'Steel')";

        List<PLMItem> results = entityManager.createQuery(jpql).getResultList();
        System.out.println();
        for (PLMItem item : results) {
            */
/*System.out.println(item.getItemNumber() + ":" + item.getRevision());*//*

            System.out.println(item.getItemNumber());
        }
        
        System.out.println();
    }

    @Test
    public void criteriaBuilderTest() throws Exception {

        ParameterCriteria parameterCriteria1 = new ParameterCriteria();
        parameterCriteria1.setField("item.itemNumber");
        parameterCriteria1.setOperator("contains");
        parameterCriteria1.setValue("007");
        ParameterCriteria parameterCriteria2 = new ParameterCriteria();
        parameterCriteria2.setField("item.revision");
        parameterCriteria2.setOperator("startswith");
        parameterCriteria2.setValue("A");
        ParameterCriteria parameterCriteria3 = new ParameterCriteria();
        parameterCriteria3.setField("item.description");
        parameterCriteria3.setOperator("endswith");
        parameterCriteria3.setValue("ska");
        ParameterCriteria parameterCriteria4 = new ParameterCriteria();
        parameterCriteria4.setField("bom.notes");
        parameterCriteria4.setOperator("equals");
        parameterCriteria4.setValue("aaa");

        ParameterCriteria parameterCriteria5 = new ParameterCriteria();
        parameterCriteria5.setField("attribute.doubleValue");
        parameterCriteria5.setOperator("greterthan");
        parameterCriteria5.setValue("45.5");

        ParameterCriteria parameterCriteria6 = new ParameterCriteria();
        parameterCriteria6.setField("attribute.attributeDef");
        parameterCriteria6.setOperator("lessthan");
        parameterCriteria6.setValue("45");

        ParameterCriteria parameterCriteria8 = new ParameterCriteria();
        parameterCriteria8.setField("reference.revision");
        parameterCriteria8.setOperator("equals");
        parameterCriteria8.setValue("A");

        ParameterCriteria parameterCriteria9 = new ParameterCriteria();
        parameterCriteria9.setField("reference.itemNumber");
        parameterCriteria9.setOperator("contains");
        parameterCriteria9.setValue("006");

        ParameterCriteria parameterCriteria10 = new ParameterCriteria();
        parameterCriteria10.setField("reference.status");
        parameterCriteria10.setOperator("contains");
        parameterCriteria10.setValue("nary");

        ParameterCriteria parameterCriteria7 = new ParameterCriteria();
        parameterCriteria7.setField("null");
        parameterCriteria7.setOperator("null");
        parameterCriteria7.setValue("null");

        ParameterCriteria[] parameterCriterias =
                new ParameterCriteria[]{parameterCriteria1, parameterCriteria2, parameterCriteria3, parameterCriteria4,
                parameterCriteria5, parameterCriteria6, parameterCriteria7, parameterCriteria8, parameterCriteria9,
                        parameterCriteria10};

        TypedQuery<PLMItem> typedQuery = itemAdvancedCriteria.getItemTypeQuery(parameterCriterias);
        List<PLMItem> resultlist1 = typedQuery.getResultList();

        for (PLMItem o : resultlist1) {
            PLMItem e = (PLMItem) o;
            System.out.println("EID : " + e.getItemNumber() + " Ename : " + e.getItemType());
        }
    }
}*/
