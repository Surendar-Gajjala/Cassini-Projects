package com.cassinisys.erp.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 9/24/15.
 */
public class TenantManager {
    private List<String> tenants = new ArrayList<>();
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

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

}
