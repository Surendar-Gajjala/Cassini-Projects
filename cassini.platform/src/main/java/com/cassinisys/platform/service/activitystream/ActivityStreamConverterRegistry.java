package com.cassinisys.platform.service.activitystream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ActivityStreamConverterRegistry implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired  @Lazy
    private List<ActivityStreamConverter> converters;

    private Map<String, ActivityStreamConverter> converterMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ActivityStreamConverter getConverter(String key) {
        if(converterMap == null) {
            converterMap = new HashMap<>();
            if(converters != null) {
                converters.forEach(c -> {
                    converterMap.put(c.getConverterKey(), c);
                });
            }
        }

        return converterMap.get(key);
    }
}
