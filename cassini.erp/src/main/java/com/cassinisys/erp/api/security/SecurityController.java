package com.cassinisys.erp.api.security;

import com.cassinisys.erp.api.BaseController;
import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.api.filtering.SessionPredicateBuilder;
import com.cassinisys.erp.config.TenantManager;
import com.cassinisys.erp.config.api.APIKeyGenerator;
import com.cassinisys.erp.converters.PageRequestConverter;
import com.cassinisys.erp.model.api.criteria.SessionCriteria;
import com.cassinisys.erp.model.api.paging.ERPPageRequest;
import com.cassinisys.erp.model.common.ERPMobileDevice;
import com.cassinisys.erp.model.common.ERPPerson;
import com.cassinisys.erp.model.security.*;
import com.cassinisys.erp.repo.common.PersonRepository;
import com.cassinisys.erp.repo.security.SessionRepository;
import com.cassinisys.erp.service.common.MobileDeviceService;
import com.cassinisys.erp.service.security.SecurityService;
import com.cassinisys.erp.service.security.SessionManager;
import com.mysema.query.types.Predicate;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by reddy on 7/16/15.
 */
@RestController
@RequestMapping("security")
@Api(name = "Security", description = "Security endpoint", group = "SECURITY")
public class SecurityController extends BaseController {

	@Autowired
	private SecurityService securityService;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private PageRequestConverter pageRequestConverter;

	@Autowired
	private SessionPredicateBuilder predicateBuilder;

	@Autowired
	private MobileDeviceService mobileDeviceService;


	@RequestMapping(value = "/login/{user}/{password}", method = RequestMethod.GET)
	public ERPSession login(@PathVariable("user") String user,
			@PathVariable("password") String password,
			HttpServletRequest request, HttpServletResponse response) {

		if(user.indexOf("@") != -1) {
			String[] arr = user.split("@");

			if(arr.length != 2) {
				throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
						"Login must be of the form username@companyname");
			}

			user = arr[0].trim();
			String tenantId = arr[1].trim();


			if(!TenantManager.get().isValidTenant(tenantId)) {
				throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
						"'" + tenantId + "' is not a valid customer name");
			}

			TenantManager.get().setTenantId(tenantId);
			request.getSession().setAttribute("CASSINI_TENANT_IDENTIFIER", tenantId);
		}
		else {
			throw new ERPException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
					"Login must be of the form username@companyname");
		}

		ERPSession erpSession = getSessionWrapper().getSession();

		if (erpSession != null && erpSession.getLogin() != null) {
			ERPLogin login = securityService.getLoginByLoginName(erpSession.getLogin().getLoginName());
			erpSession.setLogin(login);
			return erpSession;
		} else {
			ERPLogin login = securityService.login(user, password);

			erpSession = new ERPSession();
			erpSession.setLogin(login);
			erpSession.setIpAddress(request.getRemoteAddr());
			erpSession.setLoginTime(new Date());
			erpSession = sessionRepository.save(erpSession);
			getSessionWrapper().setSession(erpSession);

			SessionManager.get().setSessionRepository(sessionRepository);
			SessionManager.get().addSession(erpSession);

			erpSession.setApiKey(APIKeyGenerator.get().generate());
			HttpSession session = request.getSession();
			session.setMaxInactiveInterval(60*60);

			request.getSession().setAttribute("CASSINI_SESSION", erpSession);


			return erpSession;
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Boolean logout(HttpServletRequest request,
			HttpServletResponse response) {
		SessionManager.get().removeSession(getSessionWrapper().getSession());
		request.getSession().invalidate();
		return true;
	}

	@RequestMapping(value = "/session/current", method = RequestMethod.GET)
	public ERPSession getCurrentSession(HttpServletRequest request,
			HttpServletResponse response) {
		ERPSession session = null;
		ERPSession erpSession = getSessionWrapper().getSession();
		if (erpSession != null && erpSession.getLogin() != null) {
			ERPLogin login = securityService.getLoginByLoginName(erpSession.getLogin().getLoginName());
			securityService.loadLoginRoles(login);
			erpSession.setLogin(login);
            getSessionWrapper().setSession(erpSession);
			session = erpSession;
		}
		return session;
	}

	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public Page<ERPSession> getAllSessions(SessionCriteria criteria, ERPPageRequest pageRequest) {
		Predicate predicate = predicateBuilder.build(criteria, QERPSession.eRPSession);
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return sessionRepository.findAll(predicate, pageable);
	}

	@RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET)
	public ERPSession getSession(@PathVariable("sessionId") Integer sessionId,
			HttpServletRequest request, HttpServletResponse response) {
		return sessionRepository.findOne(sessionId);
	}

	@RequestMapping(value = "/session/isactive", method = RequestMethod.GET)
	public ModelMap getSession(HttpServletRequest request,
			HttpServletResponse response) {
		ModelMap map = new ModelMap();
		map.addAttribute("active", Boolean.TRUE);
		ERPSession erpSession = getSessionWrapper().getSession();
		if (erpSession == null || erpSession.getLogin() == null) {
			map.addAttribute("active", Boolean.FALSE);
		}

		return map;
	}

	@RequestMapping (value = "/session/{sessionId}/mobiledevice", method = RequestMethod.POST)
	public ERPMobileDevice saveMobileDevice(@PathVariable("sessionId") Integer sessionId,
											@RequestBody ERPMobileDevice device) {
		ERPSession erpSession = getSessionWrapper().getSession();
		if (erpSession != null && erpSession.getLogin() != null && erpSession.getId().equals(sessionId)) {
			device = mobileDeviceService.create(device);
			erpSession.setMobileDevice(device);
			sessionRepository.save(erpSession);

			ERPPerson person = erpSession.getLogin().getPerson();
			person.setMobileDevice(device);

			personRepository.save(person);
		}
		return device;
	}


	@RequestMapping (value = "/session/{sessionId}/mobiledevice/disablenotification", method = RequestMethod.GET)
	public void disableMobileNotification(@PathVariable("sessionId") Integer sessionId) {
		ERPSession erpSession = getSessionWrapper().getSession();
		if (erpSession != null && erpSession.getLogin() != null && erpSession.getId().equals(sessionId)) {
			ERPMobileDevice device = erpSession.getMobileDevice();
			if(device != null) {
				device.setDisablePushNotification(Boolean.TRUE);
				mobileDeviceService.update(device);
			}
		}
	}

	@RequestMapping (value = "/session/{sessionId}/mobiledevice/enablenotification", method = RequestMethod.GET)
	public void enableMobileNotification(@PathVariable("sessionId") Integer sessionId) {
		ERPSession erpSession = getSessionWrapper().getSession();
		if (erpSession != null && erpSession.getLogin() != null && erpSession.getId().equals(sessionId)) {
			ERPMobileDevice device = erpSession.getMobileDevice();
			if(device != null) {
				device.setDisablePushNotification(Boolean.FALSE);
				mobileDeviceService.update(device);
			}
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Page<ERPLogin> getLogins(ERPPageRequest pageRequest) {
		Pageable pageable = pageRequestConverter.convert(pageRequest);
		return securityService.getLogins(pageable);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ERPLogin createLogin(@RequestBody @Valid ERPLogin login,
								@RequestParam("phone") String phone,
								@RequestParam("email") String email) {
		return securityService.createLogin(login, phone, email);
	}

	@RequestMapping(value = "/login/changepassword", method = RequestMethod.GET)
	public ERPLogin changePassword(String oldPassword, String newPassword) {
		return securityService.changePassword(oldPassword, newPassword);
	}

	@RequestMapping(value = "/login/resetpassword", method = RequestMethod.POST)
	public ERPLogin resetPassword(@RequestBody @Valid ERPLogin login) {
		return securityService.resetPassword(login);
	}

	@RequestMapping (value = "/role", method = RequestMethod.GET)
	public List<ERPRole> getAllRoles() {
		return securityService.getAllRoles();
	}

	@RequestMapping (value = "/role", method = RequestMethod.POST)
	public List<ERPRole> saveRoles(@RequestBody @Valid List<ERPRole> roles) {
		return securityService.saveRoles(roles);
	}

	@RequestMapping (value = "/role/{roleId}", method = RequestMethod.PUT)
	public ERPRole saveRole(@PathVariable("roleId") Integer roleId,
							@RequestBody @Valid ERPRole role) {
		return securityService.saveRole(role);
	}

	@RequestMapping (value = "/role/new", method = RequestMethod.POST)
	public ERPRole createRole(@RequestBody @Valid ERPRole role) {
		return securityService.createRole(role);
	}

	@RequestMapping (value = "/role/{roleId}", method = RequestMethod.DELETE)
	public void deleteRole(@PathVariable("roleId") Integer roleId) {
		securityService.deleteRole(roleId);
	}

	@RequestMapping (value = "/login/{loginId}/roles", method = RequestMethod.GET)
	public List<ERPLoginRole> getLoginRoles(@PathVariable("loginId") Integer loginId) {
		return securityService.getLoginRoles(loginId);
	}

	@RequestMapping (value = "/login/{loginId}/roles", method = RequestMethod.POST)
	public List<ERPLoginRole> saveLoginRoles(@PathVariable("loginId") Integer loginId,
											 @RequestBody @Valid List<ERPRole> roles) {
		return securityService.setLoginRoles(loginId, roles);
	}
}
