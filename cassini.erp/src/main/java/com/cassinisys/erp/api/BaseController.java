package com.cassinisys.erp.api;

import com.cassinisys.erp.api.exceptions.APIError;
import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.exceptions.UnauthorizedAccessException;
import com.cassinisys.erp.service.security.SessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by reddy on 12/16/14.
 */

@RestController
@RequestMapping("/api")
public abstract class BaseController {
	@Autowired
	private SessionWrapper sessionWrapper;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BaseController.class);

	protected SessionWrapper getSessionWrapper() {
		return sessionWrapper;
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ModelMap handleEmptyResultDataAccessException(
			EmptyResultDataAccessException ex, HttpServletResponse response)
			throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return new APIError(ErrorCodes.RESOURCE_NOT_FOUND.toString(),
				ex.getMessage());
	}

	@ExceptionHandler(ERPException.class)
	public ModelMap handleBusinessException(ERPException ex,
			HttpServletResponse response) throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		response.setStatus(ex.getStatus());
		return new APIError(ex.getCode().toString(), ex.getMessage());
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	public ModelMap handleUnauthorizedException(ERPException ex,
											HttpServletResponse response) throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		response.setStatus(ex.getStatus());
		return new APIError(ex.getCode().toString(), ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ModelMap handleAllException(Exception ex,
			HttpServletResponse response) throws Exception {
		LOGGER.error(ex.getMessage(), ex);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new APIError(ErrorCodes.GENERAL.toString(), ex.getMessage());
	}

}