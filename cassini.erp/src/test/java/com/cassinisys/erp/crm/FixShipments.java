package com.cassinisys.erp.crm;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.model.crm.ERPConsignment;
import com.cassinisys.erp.model.crm.ERPCustomerOrderShipment;
import com.cassinisys.erp.repo.crm.ConsignmentRepository;
import com.cassinisys.erp.repo.crm.CustomerOrderShipmentRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by reddy on 4/18/16.
 */
public class FixShipments extends BaseTest {

    @Autowired
    private CustomerOrderShipmentRepository shipmentRepository;

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Rollback(false)
    public void fixShipments() throws Exception {
        String jpql = "SELECT s.id, s.order.orderNumber, s.invoiceAmount FROM ERPCustomerOrderShipment s WHERE s.consignment IS NULL AND s.status = 'SHIPPED' ORDER BY s.modifiedDate DESC";
        List<Object[]> shipments = entityManager.createQuery(jpql).getResultList();

        for(Object[] shipment : shipments) {
            Double invoiceAmount = (Double) shipment[2];
            Integer shipmentId = (Integer) shipment[0];

            jpql = "SELECT c FROM ERPConsignment c WHERE c.value = " + invoiceAmount;
            try {
                List<ERPConsignment> consignments = entityManager.createQuery(jpql).getResultList();
                if (consignments.size() == 1) {
                    System.out.println(shipment[1] + " - " + consignments.get(0).getNumber());
                    ERPCustomerOrderShipment s = shipmentRepository.findOne(shipmentId);
                    if(s != null) {
                        s.setConsignment(consignments.get(0));
                        shipmentRepository.save(s);
                    }
                }
            }catch(Exception e) {

            }
        }
    }
}
