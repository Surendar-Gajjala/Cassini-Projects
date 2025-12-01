package com.cassinisys.erp.service.notification.push;

import com.cassinisys.erp.config.TenantManager;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.crm.ERPCustomerOrder;
import com.cassinisys.erp.model.security.ERPLogin;
import com.cassinisys.erp.model.security.ERPRole;
import com.cassinisys.erp.repo.security.LoginRoleRepository;
import com.cassinisys.erp.repo.security.RolePermissionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by reddy on 18/02/16.
 */
@Component
public class MessageSender {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private LoginRoleRepository loginRoleRepository;

    @Autowired
    private GCMPushNotification pushNotification;


    public void sendNewOrderPushNotification(final ERPCustomerOrder customerOrder) {
        final String tenantId = TenantManager.get().getTenantId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TenantManager.get().setTenantId(tenantId);

                List<String> permissions = Arrays.asList("permission.all", "permission.crm.all",
                        "permission.crm.order.all", "permission.crm.order.approve");
                List<ERPRole> roles = rolePermissionRepository.getRolesWithPermission(permissions);
                List<ERPLogin> logins = loginRoleRepository.findLoginsByRoles(roles);

                for(ERPLogin login : logins) {
                    ERPPerson person = login.getPerson();
                    if(person.getMobileDevice() != null &&
                            person.getMobileDevice().getDisablePushNotification() == Boolean.FALSE) {
                        String senderId = person.getMobileDevice().getSenderId();

                        if(senderId != null) {
                            ObjectMapper mapper = new ObjectMapper();
                            ObjectNode data = mapper.createObjectNode();

                            String message = "New order {0} from {1} with order amound {2}";
                            message = MessageFormat.format(message, customerOrder.getOrderNumber(),
                                            customerOrder.getCustomer().getName(),
                                            customerOrder.getOrderTotal());

                            ObjectNode order = mapper.createObjectNode();
                            order.put("id", customerOrder.getId());
                            order.put("orderNumber", customerOrder.getOrderNumber());
                            order.put("customer", customerOrder.getCustomer().getName());

                            data.put("type", "crm.order.new");
                            data.put("message", message);
                            data.putPOJO("object", order);

                            pushNotification.sendMessage(new GCMMessage(senderId, data));
                        }
                    }
                }
            }
        }).start();
    }
}
