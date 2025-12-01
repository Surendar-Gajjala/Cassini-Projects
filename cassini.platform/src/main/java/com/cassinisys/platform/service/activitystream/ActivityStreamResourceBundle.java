package com.cassinisys.platform.service.activitystream;

import lombok.Data;
import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ResourceBundle;

@Data
@Component
public class ActivityStreamResourceBundle {
    private String lang = LocaleContextHolder.getLocale().toString();
    private ResourceBundle resourceBundle;

    @PostConstruct
    public void init() {
        try {
            if (resourceBundle == null) {
                resourceBundle = new YamlResourceBundle(new ClassPathResource("i18n/activitystream.yml").getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        String s = null;
        try {
            key = key + "." + LocaleContextHolder.getLocale().toString();
            s = resourceBundle.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

}
