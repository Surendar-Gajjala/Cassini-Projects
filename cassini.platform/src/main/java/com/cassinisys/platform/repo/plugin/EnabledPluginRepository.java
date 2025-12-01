package com.cassinisys.platform.repo.plugin;

import com.cassinisys.platform.model.plugin.EnabledPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnabledPluginRepository extends JpaRepository<EnabledPlugin, String> {

}