package com.cassinisys.platform.repo.common;

import com.cassinisys.platform.model.common.AppMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Repository
public interface AppMenuRepository extends JpaRepository<AppMenu, String> {



}