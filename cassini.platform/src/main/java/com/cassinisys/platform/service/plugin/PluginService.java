package com.cassinisys.platform.service.plugin;

import com.cassinisys.platform.model.plugin.EnabledPlugin;
import com.cassinisys.platform.model.plugin.Plugin;
import com.cassinisys.platform.repo.plugin.EnabledPluginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Service
public class PluginService {
    @Value("${cassini.plugins.dir:#{null}}")
    private Optional<String> pluginsDir;
    private List<Plugin> plugins = new ArrayList<>();
    private Map<String, Plugin> pluginsMap = new HashMap<>();

    @Autowired
    private EnabledPluginRepository enabledPluginRepository;

    @PostConstruct
    public void loadPlugins() {
        if(pluginsDir.isPresent()) {
            File dir = new File(pluginsDir.get());
            if(dir.exists()) {
                try {
                    File[] children = dir.listFiles();
                    if (children != null) {
                        for (File child : children) {
                            File manifestFile = new File(child, "manifest.json");
                            if (manifestFile.exists()) {
                                ObjectMapper mapper = new ObjectMapper();
                                Plugin plugin = mapper.readValue(manifestFile, Plugin.class);
                                if (plugin != null) {
                                    plugin.setDirectory(child.getName());
                                    plugins.add(plugin);
                                    pluginsMap.put(plugin.getId(), plugin);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Plugin getPluginById(String id) {
        return pluginsMap.get(id);
    }

    @Transactional
    public Plugin enablePlugin(Plugin plugin) {
        EnabledPlugin enabledPlugin = enabledPluginRepository.findOne(plugin.getId());
        if(enabledPlugin == null) {
            enabledPlugin = new EnabledPlugin();
            enabledPlugin.setId(plugin.getId());
            enabledPluginRepository.save(enabledPlugin);
        }

        plugin.setEnabled(Boolean.TRUE);

        return plugin;
    }

    @Transactional
    public Plugin disablePlugin(Plugin plugin) {
        EnabledPlugin enabledPlugin = enabledPluginRepository.findOne(plugin.getId());
        if(enabledPlugin != null) {
            enabledPluginRepository.delete(enabledPlugin);
        }

        plugin.setEnabled(Boolean.FALSE);

        return plugin;
    }

    @Transactional(readOnly = true)
    public List<Plugin> getPlugins() {
        for(Plugin plugin :  plugins) {
            EnabledPlugin enabledPlugin = enabledPluginRepository.findOne(plugin.getId());
            plugin.setEnabled(enabledPlugin != null);
        }
        return this.plugins;
    }

    @Transactional(readOnly = true)
    public Boolean isPluginEnabled(String pluginId) {
        EnabledPlugin enabledPlugin = enabledPluginRepository.findOne(pluginId);
        return enabledPlugin != null;
    }
}
