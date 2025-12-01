package com.cassinisys.erp.service.security;

import com.cassinisys.erp.model.security.ERPSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;
import java.io.Serializable;

/**
 * Created by reddy on 7/31/15.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionWrapper  implements Serializable {
    private ERPSession erpSession;
    private Authorization authorization;
    private String tenantId;

    public void setSession(ERPSession session) {
        this.erpSession = session;
        this.authorization = new Authorization(session);
    }

    public ERPSession getSession() {
        return this.erpSession;
    }

    public Authorization getAuthorization() {
        return this.authorization;
    }

    @PreDestroy
    public void closeSession() {
        SessionManager.get().removeSession(erpSession);
        erpSession = null;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
