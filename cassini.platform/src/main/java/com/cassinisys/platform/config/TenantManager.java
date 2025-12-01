package com.cassinisys.platform.config;

import com.cassinisys.platform.exceptions.CassiniException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/24/15.
 */
public class TenantManager {
    private List<String> tenants = new ArrayList<>();
    private InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    private static TenantManager INSTANCE = new TenantManager();

    private TenantManager() {}

    public static TenantManager get() {
        return INSTANCE;
    }

    public void setTenantId(String id) {
        threadLocal.set(id);
    }

    public String getTenantId() {
        return threadLocal.get();
    }


    public void setTenants(List<String> tenants) {
        this.tenants = tenants;
    }

    public boolean isValidTenant(String tenant) {
        return tenants.contains(tenant);
    }

    public void initTenantFromRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username != null && username.contains("@")) {
            String[] arr = username.split("@");
            String tenantId = arr[1];
            if(isValidTenant(tenantId)) {
                setTenantId(arr[1]);
            }
            else {
                throw new CassiniException("Invalid tenant id");
            }
        }
    }

}
