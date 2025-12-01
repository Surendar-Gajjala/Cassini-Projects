package com.cassinisys.tms.service;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.tms.model.TMSTrip;
import com.cassinisys.tms.repo.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by CassiniSystems on 20-10-2016.
 */
@Service
@Transactional
public class TripService implements CrudService<TMSTrip, Integer>, PageableService<TMSTrip, Integer> {


	@Autowired
	private TripRepository tripRepository;

	@Override
	public TMSTrip create(TMSTrip trip) {
		checkNotNull(trip);
		return tripRepository.save(trip);
	}

	@Override
	public TMSTrip update(TMSTrip trip) {
		checkNotNull(trip);
		checkNotNull(trip.getId());
		return tripRepository.save(trip);
	}

	@Override
	public void delete(Integer id) {

		checkNotNull(id);
		TMSTrip trip = tripRepository.findOne(id);
		if(trip == null){
			throw new ResourceNotFoundException();
		}
		tripRepository.delete(id);
	}

	@Override
	public TMSTrip get(Integer id) {

		checkNotNull(id);
		TMSTrip trip = tripRepository.findOne(id);
		if (trip == null){
			throw  new ResourceNotFoundException();
		}
		return trip;
	}

	@Override
	public List<TMSTrip> getAll() {
		return tripRepository.findAll();
	}

	@Override
	public Page<TMSTrip> findAll(Pageable pageable) {
		return findAll(pageable);
	}
}
