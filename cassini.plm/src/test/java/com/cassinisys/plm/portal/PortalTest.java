package com.cassinisys.plm.portal;

import com.cassinisys.plm.BaseTest;
import com.cassinisys.platform.model.portal.Authentication;
import com.cassinisys.platform.model.portal.CustomerSession;
import com.cassinisys.platform.model.portal.CustomerUser;
import com.cassinisys.platform.service.portal.PortalService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PortalTest extends BaseTest {
    @Autowired
    private PortalService portalService;

    @Test
    public void testPortalMethods() throws Exception {
        try {
            Authentication authentication = new Authentication("raghuram.tera@cassiniplm.com", "cassini");
            CustomerSession session = portalService.login(authentication);
            System.out.println(session);

            List<CustomerUser> users = portalService.getCustomerUsers(session.getLogin().getCustomer().getId());
            users.forEach(u -> System.out.println(u.getUsername()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
