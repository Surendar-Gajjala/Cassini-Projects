package com.cassinisys.erp.service.common;

import com.cassinisys.erp.model.common.ERPObjectGeoLocation;
import com.cassinisys.erp.model.core.ObjectType;
import com.cassinisys.erp.repo.common.ObjectGeoLocationRepository;
import com.cassinisys.erp.service.paging.CrudService;
import com.cassinisys.erp.util.GeoLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ObjectGeoLocationService implements CrudService<ERPObjectGeoLocation, Integer>{
	
	@Autowired
	ObjectGeoLocationRepository objectGeoLocationRepository;

	@Override
	public ERPObjectGeoLocation create(ERPObjectGeoLocation geoLocation) {
		return objectGeoLocationRepository.save(geoLocation);
	}

	@Override
	public ERPObjectGeoLocation update(ERPObjectGeoLocation geoLocation) {
		return objectGeoLocationRepository.save(geoLocation);
	}

	@Override
	public void delete(Integer integer) {

	}

	@Override
	public ERPObjectGeoLocation get(Integer id) {
		return objectGeoLocationRepository.findOne(id);
	}

	public ERPObjectGeoLocation get(Integer id, ObjectType type) {
		return objectGeoLocationRepository.findByIdAndObjectType(id, type);
	}

	@Override
	public List<ERPObjectGeoLocation> getAll() {
		return objectGeoLocationRepository.findAll();
	}

    public List<ERPObjectGeoLocation> radiusSearch(Double latitude, Double longitude, float radius) {
        List<Object[]> arr = objectGeoLocationRepository.radiusSearch(latitude, longitude, radius * 1000);
        return buildGeoLocationsFromArray(arr);
    }

    public List<ERPObjectGeoLocation> boxSearch(GeoLocation ne, GeoLocation sw) {
        List<Object[]> arr = objectGeoLocationRepository.boxSearch(ne.latitude, ne.longitude,
                sw.latitude, sw.longitude);
        return buildGeoLocationsFromArray(arr);
    }

    public List<ERPObjectGeoLocation> radiusSearch(Double latitude, Double longitude, float radius, ObjectType objectType) {
        List<Object[]> arr = objectGeoLocationRepository.radiusSearch(latitude, longitude, radius * 1000, objectType.getType());
        return buildGeoLocationsFromArray(arr);
    }

    public List<ERPObjectGeoLocation> boxSearch(GeoLocation ne, GeoLocation sw, ObjectType objectType) {
        List<Object[]> arr = objectGeoLocationRepository.boxSearch(ne.latitude, ne.longitude,
                sw.latitude, sw.longitude, objectType.getType());
        return buildGeoLocationsFromArray(arr);
    }

    private List<ERPObjectGeoLocation> buildGeoLocationsFromArray(List<Object[]> arr) {
        List<ERPObjectGeoLocation> geoLocations = new ArrayList<>();
        for(Object[] item : arr) {
            ERPObjectGeoLocation geo = new ERPObjectGeoLocation();
            geo.setId((Integer)item[0]);
            geo.setObjectType(ObjectType.CUSTOMER);
            geo.setLatitude((Double) item[2]);
            geo.setLongitude((Double) item[3]);

            geoLocations.add(geo);
        }

        return geoLocations;
    }


}
