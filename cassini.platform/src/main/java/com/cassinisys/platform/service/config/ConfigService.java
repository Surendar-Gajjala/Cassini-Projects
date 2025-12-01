package com.cassinisys.platform.service.config;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by reddy on 10/17/15.
 */
@Service
public class ConfigService {
    @Autowired
    private FileSystemService fileSystemService;

    @AutowiredLogger
    private Logger LOGGER;

    private Map<String, Config> tenantConfigs = new HashMap<>();

    @Transactional
    public Config getTenantConfig(String tenant) {
        Config config = tenantConfigs.get(tenant);
        if(config == null) {
            config = new Config(tenant, fileSystemService);
            tenantConfigs.put(tenant, config);
        }

        return config;
    }

    @Transactional
    public Config getCurrentTenantConfig() {
        return getTenantConfig(TenantManager.get().getTenantId());
    }

}
