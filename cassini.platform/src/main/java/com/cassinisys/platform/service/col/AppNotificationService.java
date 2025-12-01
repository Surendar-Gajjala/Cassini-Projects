package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.AppNotification;
import com.cassinisys.platform.repo.col.AppNotificationRepository;
import com.cassinisys.platform.repo.col.NotificationToRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by GSR on 15-10-2018.
 */
@Service
public class AppNotificationService implements CrudService<AppNotification, Integer>,
		PageableService<AppNotification, Integer> {


	@Autowired
	private AppNotificationRepository appNotificationRepository;

	@Autowired
	private NotificationToRepository notificationToRepository;

	@Override
	@Transactional
	public AppNotification create(AppNotification appNotification) {
		appNotification = appNotificationRepository.save(appNotification);
		return appNotification;
	}

	@Override
	@Transactional
	public AppNotification update(AppNotification appNotification) {
		appNotification = appNotificationRepository.save(appNotification);
		return appNotification;
	}

	@Override
	@Transactional
	public void delete(Integer id) {
		AppNotification appNotification = appNotificationRepository.findOne(id);
		if (appNotification == null) {
			throw new ResourceNotFoundException();
		}
		appNotificationRepository.delete(appNotification);

	}

	@Override
	@Transactional(readOnly = true)
	public AppNotification get(Integer id) {
		AppNotification appNotification = appNotificationRepository.findOne(id);
		if (appNotification == null) {
			throw new ResourceNotFoundException();
		}
		return appNotification;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppNotification> getAll() {
		return appNotificationRepository.findAll();
	}


	@Override
	@Transactional(readOnly = true)
	public Page<AppNotification> findAll(Pageable pageable) {

		return appNotificationRepository.findAll(pageable);
	}
}
