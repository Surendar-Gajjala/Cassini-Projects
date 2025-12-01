package com.cassinisys.erp.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

/**
 * Created by reddy on 9/22/15.
 */
@Component
public class CassiniTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        if(TenantManager.get().getTenantId() != null) {
            return TenantManager.get().getTenantId();
        }
        return "";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
