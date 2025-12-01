package com.cassinisys.platform.service.portal;

import com.cassinisys.platform.model.portal.Authentication;
import com.cassinisys.platform.model.portal.CustomerAuthKey;
import com.cassinisys.platform.model.portal.CustomerSession;
import com.cassinisys.platform.model.portal.CustomerUser;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface PortalService {

    @RequestLine("POST /auth/customer")
    @Headers("Content-Type: application/json")
    CustomerSession login(@RequestBody Authentication login);

    @RequestLine ("GET /auth/customer/logout")
    Boolean logout();

    @RequestLine("GET /customers/{id}/users")
    @Headers("Content-Type: application/json")
    List<CustomerUser> getCustomerUsers(@Param("id") Long id);

    @RequestLine("GET /customers/{id}/authkey")
    @Headers("Content-Type: application/json")
    CustomerAuthKey getCustomerAuthKey(@Param("id") Long id);
}
