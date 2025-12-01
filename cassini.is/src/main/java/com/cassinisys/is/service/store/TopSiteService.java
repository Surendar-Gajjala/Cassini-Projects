package com.cassinisys.is.service.store;

import com.cassinisys.is.model.store.ISTopSite;
import com.cassinisys.is.repo.store.ISTopSiteRepository;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subramanyamreddy on 017 17-Nov -17.
 */
@Service
public class TopSiteService implements CrudService<ISTopSite, Integer> {

    @Autowired
    private ISTopSiteRepository topSiteRepository;

    @Override
    @Transactional(readOnly = false)
    public ISTopSite create(ISTopSite site) {
        return topSiteRepository.save(site);
    }

    @Override
    @Transactional(readOnly = false)
    public ISTopSite update(ISTopSite site) {
        return topSiteRepository.save(site);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        topSiteRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ISTopSite get(Integer id) {
        return topSiteRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ISTopSite> getAll() {
        return topSiteRepository.findAll();
    }
}