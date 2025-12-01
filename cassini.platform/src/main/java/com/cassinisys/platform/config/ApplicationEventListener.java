package com.cassinisys.platform.config;

import com.cassinisys.platform.service.config.ConfigService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 8/24/15.
 */
@Component
public class ApplicationEventListener implements ApplicationListener<ContextRefreshedEvent>{
    @AutowiredLogger
    private Logger LOGGER;

    @Autowired
    private Environment env;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private ConfigService configService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadTenants();
    }

    private void loadTenants() {
        String s = env.getProperty("cassini.tenants");
        if(s != null && !s.isEmpty()) {
            String[] arr = s.split(",");
            List<String> list = new ArrayList<>();
            for(String t : arr) {
                list.add(t.trim());
            }

            TenantManager.get().setTenants(list);
        }

        fileSystemService.initialize();
    }

}
