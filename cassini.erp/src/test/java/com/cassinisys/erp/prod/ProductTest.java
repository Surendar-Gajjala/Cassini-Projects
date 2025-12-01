package com.cassinisys.erp.prod;

import com.cassinisys.erp.BaseTest;
import com.cassinisys.erp.api.filtering.ProductPredicateBuilder;
import com.cassinisys.erp.model.api.criteria.ProductCriteria;
import com.cassinisys.erp.model.crm.ERPCustomer;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.production.ERPProduct;
import com.cassinisys.erp.repo.crm.CustomerOrderRepository;
import com.cassinisys.erp.repo.crm.CustomerRepository;
import com.cassinisys.erp.service.production.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by reddy on 8/18/15.
 */
public class ProductTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private ProductPredicateBuilder predicateBuilder;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testTextSearch() throws Exception {
        ProductCriteria criteria = new ProductCriteria();
        criteria.setName("science");
        PageRequest pageable = new PageRequest(1,25);
        Page<ERPProduct> page = productService.findAll(criteria, pageable);

        System.out.println(page);
    }

    @Test
    public void getAllOrders() throws Exception {
        PageRequest pageRequest = new PageRequest(0, 20, new Sort(Sort.Direction.ASC, "orderedDate"));
        Page<ERPCustomerOrder> page = customerOrderRepository.findAll(pageRequest);
        System.out.println(page);
    }

    @Test
    public void getCustomer() throws Exception {
        ERPCustomer customer = customerRepository.findOne(37);
        System.out.println(customer);
    }
}
