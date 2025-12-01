package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.plm.model.plm.PLMLibrary;
import com.cassinisys.plm.model.plm.PLMLibraryItem;
import com.cassinisys.plm.repo.plm.LibraryItemRepository;
import com.cassinisys.plm.repo.plm.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */
@Service
public class LibraryService implements CrudService<PLMLibrary, Integer>,
        PageableService<PLMLibrary, Integer> {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryItemRepository libraryItemRepository;

    @Override
    @Transactional
    public PLMLibrary create(PLMLibrary PLMLibrary) {
        return libraryRepository.save(PLMLibrary);
    }

    public PLMLibraryItem createLibraryItem(PLMLibraryItem PLMLibraryItem) {
        return libraryItemRepository.save(PLMLibraryItem);
    }

    @Override
    @Transactional
    public PLMLibrary update(PLMLibrary PLMLibrary) {
        return libraryRepository.save(PLMLibrary);
    }

    @Transactional
    public PLMLibraryItem updateLibraryItem(PLMLibraryItem PLMLibraryItem) {
        return libraryItemRepository.save(PLMLibraryItem);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        libraryRepository.delete(id);
    }

    @Transactional
    public void deleteLibraryItem(Integer id) {
        libraryItemRepository.delete(id);
    }

    @Override
    public PLMLibrary get(Integer id) {
        return libraryRepository.findOne(id);
    }

    public PLMLibraryItem getLibraryItem(Integer id) {
        return libraryItemRepository.findOne(id);
    }

    @Override
    public List<PLMLibrary> getAll() {
        return libraryRepository.findAll();
    }

    public List<PLMLibraryItem> getAllLibraryItems() {
        return libraryItemRepository.findAll();
    }

    @Override
    public Page<PLMLibrary> findAll(Pageable pageable) {
        return libraryRepository.findAll(pageable);
    }

    public Page<PLMLibraryItem> findAllLibraryItems(Pageable pageable) {
        return libraryItemRepository.findAll(pageable);
    }
}
