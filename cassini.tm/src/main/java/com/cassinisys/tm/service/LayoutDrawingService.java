package com.cassinisys.tm.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tm.model.TMLayoutDrawing;
import com.cassinisys.tm.repo.LayoutDrawingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 15-09-2016.
 */

@Service
@Transactional
public class LayoutDrawingService implements CrudService<TMLayoutDrawing, Integer>, PageableService<TMLayoutDrawing, Integer> {

    @Autowired
    private LayoutDrawingRepository layoutDrawingRepository;

    @Override
    public TMLayoutDrawing create(TMLayoutDrawing tmLayoutDrawing) {
        checkNotNull(tmLayoutDrawing);
        return layoutDrawingRepository.save(tmLayoutDrawing);
    }

    @Override
    public TMLayoutDrawing update(TMLayoutDrawing tmLayoutDrawing) {
        checkNotNull(tmLayoutDrawing);
        return layoutDrawingRepository.save(tmLayoutDrawing);
    }

    @Override
    public void delete(Integer id) {
        checkNotNull(id);
        TMLayoutDrawing tmLayoutDrawing = layoutDrawingRepository.findOne(id);
        if (tmLayoutDrawing == null) {
            throw new ResourceNotFoundException();
        }
        layoutDrawingRepository.delete(id);
    }


    @Override
    public TMLayoutDrawing get(Integer id) {
        checkNotNull(id);
        TMLayoutDrawing layoutDrawing = layoutDrawingRepository.findOne(id);
        if (layoutDrawing == null) {
            throw new ResourceNotFoundException();
        }
        return layoutDrawing;
    }

    @Override
    public List<TMLayoutDrawing> getAll() {
        return layoutDrawingRepository.findAll();
    }

    @Override
    public Page<TMLayoutDrawing> findAll(Pageable pageable) {
        return layoutDrawingRepository.findAll(pageable);
    }

    public List<TMLayoutDrawing> findByDate(String date) {
        String[] dates = date.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(dates[0]),Integer.parseInt(dates[1]),Integer.parseInt(dates[2]));

        return layoutDrawingRepository.findByDateIn(localDate);
    }

}
