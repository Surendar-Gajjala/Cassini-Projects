package com.cassinisys.pdm.service;
import com.cassinisys.pdm.model.PDMLockable;
import com.cassinisys.pdm.repo.LockableRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by GSR on 15-02-2017.
 */
@Service
@Transactional
public class PDMLockableService implements CrudService<PDMLockable,Integer> {

	@Autowired
	private LockableRepository lockableRepository;


	@Override
	public PDMLockable create(PDMLockable pdmLockable) {
		checkNotNull(pdmLockable);
		return lockableRepository.save(pdmLockable);
	}

	@Override
	public PDMLockable update(PDMLockable pdmLockable) {
		checkNotNull(pdmLockable);
		return lockableRepository.save(pdmLockable);
	}

	@Override
	public void delete(Integer id) {
		checkNotNull(id);
		PDMLockable pdmLockable = lockableRepository.findOne(id);
		if(pdmLockable == null){
			throw new ResourceNotFoundException();
		}
		lockableRepository.delete(id);

	}

	@Override
	public PDMLockable get(Integer id) {
		checkNotNull(id);
		return lockableRepository.getOne(id);
	}

	@Override
	public List<PDMLockable> getAll() {
		return lockableRepository.findAll();
	}

}
