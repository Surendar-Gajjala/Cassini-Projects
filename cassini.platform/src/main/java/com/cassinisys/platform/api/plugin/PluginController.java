package com.cassinisys.platform.api.plugin;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.plugin.Plugin;
import com.cassinisys.platform.service.plugin.PluginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;


@RestController
@RequestMapping("/plugins")
@Api(tags = "PLATFORM.PLUGIN",description = "Plugin endpoints")
public class PluginController extends BaseController {
    @Value("${cassini.plugins.dir:#{null}}")
    private String pluginsDir;

    @Autowired
    private PluginService pluginService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Plugin> getPlugins() {
        return pluginService.getPlugins();
    }

    @RequestMapping(value = "/enable", method = RequestMethod.PUT)
    public Plugin enablePlugin(@RequestParam String id) {
        if(id == null || id.trim().isEmpty()) {
            throw new CassiniException("Plugin id is required");
        }

        Plugin plugin = pluginService.getPluginById(id);
        if(plugin != null) {
            plugin = pluginService.enablePlugin(plugin);
        }

        return plugin;
    }

    @RequestMapping(value = "/disable", method = RequestMethod.PUT)
    public Plugin disablePlugin(@RequestParam String id) {
        if(id == null || id.trim().isEmpty()) {
            throw new CassiniException("Plugin id is required");
        }

        Plugin plugin = pluginService.getPluginById(id);
        if(plugin != null) {
            plugin = pluginService.disablePlugin(plugin);
        }

        return plugin;
    }

    @RequestMapping(value = "/extensions", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public String getExtensions(@RequestParam String id) {
        if(id == null || id.trim().isEmpty()) {
            throw new CassiniException("Plugin id is required to get extensions");
        }
        if(pluginsDir != null) {
            Plugin plugin = pluginService.getPluginById(id);
            if(plugin != null) {
                File dir = new File(pluginsDir, plugin.getDirectory());
                if(dir.exists()) {
                    File extFile = new File(dir, "extensions.json");
                    if (extFile.exists()) {
                        try {
                            return new String(Files.readAllBytes(extFile.toPath()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return "{}";
    }
}
