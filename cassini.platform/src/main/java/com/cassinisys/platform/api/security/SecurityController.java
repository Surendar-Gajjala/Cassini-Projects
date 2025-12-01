package com.cassinisys.platform.api.security;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.LoginCriteria;
import com.cassinisys.platform.filtering.SessionCriteria;
import com.cassinisys.platform.filtering.SessionPredicateBuilder;
import com.cassinisys.platform.model.common.MobileDevice;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.UserPreferences;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.dto.SecurityPermissionDto;
import com.cassinisys.platform.model.security.LoginGroup;
import com.cassinisys.platform.model.security.LoginRole;
import com.cassinisys.platform.model.security.PersonRole;
import com.cassinisys.platform.model.security.Role;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.security.SessionRepository;
import com.cassinisys.platform.service.common.MobileDeviceService;
import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.service.security.SessionManager;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * Created by reddy on 7/16/15.
 */
@RestController
@RequestMapping("/security")
@Api(tags = "PLATFORM.SECURITY", description = "Security endpoints")
public class SecurityController extends BaseController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private SessionPredicateBuilder predicateBuilder;
    @Autowired
    private MobileDeviceService mobileDeviceService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(value = "/login/validate", method = RequestMethod.POST)
    public LoginDTO login(@RequestBody LoginDTO loginDTO,
                          HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        securityService.initTenantId(loginDTO.getLogin().getLoginName());
        LoginDTO loginDTO1 = securityService.login(loginDTO.getLogin().getLoginName(), loginDTO.getLogin().getPassword(), request, response);

        if (loginDTO.getMobileDevice() != null) {
            MobileDevice device = loginDTO.getMobileDevice();
            device.setPersonId(loginDTO1.getSession().getLogin().getPerson().getId());
            device.setSessionId(loginDTO1.getSession().getSessionId());
            mobileDeviceService.saveMobileDevice(device);
        }
        if (!loginDTO1.getSession().getLogin().getIsAdmin()) securityService.checkIpAddress(request);
        return loginDTO1;

    }

    @RequestMapping(value = "/mobile/login/validate", method = RequestMethod.POST)
    public LoginDTO mobileLogin(@RequestBody LoginDTO loginDTO,
                                HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        securityService.initTenantId(loginDTO.getLogin().getLoginName());
        LoginDTO loginDTO1 = securityService.login(loginDTO.getLogin().getLoginName(), loginDTO.getLogin().getPassword(), request, response);

        if (loginDTO.getMobileDevice() != null) {
            MobileDevice device = loginDTO.getMobileDevice();
            device.setPersonId(loginDTO1.getSession().getLogin().getPerson().getId());
            device.setSessionId(loginDTO1.getSession().getSessionId());
            mobileDeviceService.saveMobileDevice(device);
        }
        return loginDTO1;

    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Boolean logout(HttpServletRequest request,
                          HttpServletResponse response) {

        if (getSessionWrapper().getSession() != null) {
            SessionManager.get().removeSession(getSessionWrapper().getSession());
        }
        request.getSession().invalidate();
        return true;
    }

    @RequestMapping(value = "/session/current", method = RequestMethod.GET)
    public Session getCurrentSession(HttpServletRequest request,
                                     HttpServletResponse response) {
        return securityService.getCurrentSession(response);
    }

    @RequestMapping(value = "/mobile/session/current", method = RequestMethod.GET)
    public Session getMobileCurrentSession(HttpServletRequest request,
                                           HttpServletResponse response) {
        return securityService.getMobileCurrentSession(response);
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public Page<Session> getAllSessions(SessionCriteria criteria, PageRequest pageRequest) {
        Predicate predicate = predicateBuilder.build(criteria, QSession.session);
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return sessionRepository.findAll(predicate, pageable);
    }

    @RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET)
    public Session getSession(@PathVariable("sessionId") Integer sessionId,
                              HttpServletRequest request, HttpServletResponse response) {
        return sessionRepository.findOne(sessionId);
    }

    @RequestMapping(value = "/session/isactive", method = RequestMethod.GET)
    public ModelMap getSession(HttpServletRequest request,
                               HttpServletResponse response) {
        ModelMap map = new ModelMap();
        map.addAttribute("active", Boolean.TRUE);
        Session session = getSessionWrapper().getSession();
        if (session == null || session.getLogin() == null) {
            map.addAttribute("active", Boolean.FALSE);
        }

        return map;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Page<Login> getLogins(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return securityService.getLogins(pageable);
    }

    @RequestMapping(value = "/logins/search", method = RequestMethod.GET)
    public Page<Login> getFilteredLogins(PageRequest pageRequest, LoginCriteria loginCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return securityService.getFilteredLogins(pageable, loginCriteria);
    }

    @RequestMapping(value = "/login/active", method = RequestMethod.GET)
    public Page<Login> getAllActiveLogins(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return securityService.getAllActiveLogins(pageable);
    }

    @RequestMapping(value = "/login/active/search", method = RequestMethod.GET)
    public Page<Login> getAllFilteredActiveLogins(PageRequest pageRequest, LoginCriteria loginCriteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return securityService.getAllFilteredActiveLogins(pageable, loginCriteria);
    }

    @RequestMapping(value = "/logins/active", method = RequestMethod.GET)
    public List<Login> getActiveLogins() {
        return securityService.getActiveLogins();
    }

    @RequestMapping(value = "/logins/active/internal", method = RequestMethod.GET)
    public List<Login> getInternalActiveLogins() {
        return securityService.getInternalActiveLogins();
    }

    @RequestMapping(value = "/login/byActive/{active}", method = RequestMethod.GET)
    public Integer getIsActiveLogins(@PathVariable("active") Boolean active) {
        return securityService.getIsActiveLogins(active);
    }

    @RequestMapping(value = "/login/byActive/{active}/external/{external}", method = RequestMethod.GET)
    public Integer getIsActiveAndExternalLoginCount(@PathVariable("active") Boolean active, @PathVariable("external") Boolean external) {
        return securityService.findIsActiveAndExternalLoginsCount(active, external);
    }

    @RequestMapping(value = "/login/all", method = RequestMethod.GET)
    public List<Login> getLogins() {
        return securityService.getAllLogins();
    }


    @RequestMapping(value = "/login/{id}", method = RequestMethod.GET)
    public Login get(@PathVariable("id") Integer id) {
        return securityService.get(id);
    }

    @RequestMapping(value = "/loginName/{id}", method = RequestMethod.GET)
    public String getLoginName(@PathVariable("id") Integer id) {
        return securityService.getLoginName(id);
    }

    @RequestMapping(value = "/loginCustomerName/{loginName}", method = RequestMethod.GET)
    public Login getLoginCustomerName(@PathVariable("loginName") String loginName) {
        String[] arr = loginName.split("@");
        String user = arr[0].trim();

        return securityService.getLoginByCustomerLoginName(user);
    }

    @RequestMapping(value = "/{loginId}", method = RequestMethod.PUT)
    public Login updateLogin(@PathVariable("loginId") Integer loginId,
                             @RequestBody Login login) {
        login.setId(loginId);
        return securityService.updateLogin(login);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Login createLogin(@RequestBody @Valid Login login,
                             @RequestParam("phone") String phone,
                             @RequestParam("email") String email) {
        return securityService.createLogin(login, phone, email);
    }

    @RequestMapping(value = "/login/person", method = RequestMethod.POST)
    public Login createLoginPerson(@RequestBody @Valid Login login) {
        return securityService.createLoginPerson(login);
    }

    @RequestMapping(value = "/login/person/{personId}", method = RequestMethod.GET)
    public Login getLoginByPerson(@PathVariable("personId") Integer personId) {
        return securityService.getLoginByPerson(personId);
    }

    @RequestMapping(value = "/login/changepassword", method = RequestMethod.POST)
    public LoginDTO changePassword(@RequestBody LoginDTO loginDTO) {
        return securityService.changePassword(loginDTO.getOldPassword(), loginDTO.getNewPassword());
    }

    @RequestMapping(value = "/login/changepassword/{id}", method = RequestMethod.GET)
    public LoginDTO changePasswordByLoginId(@PathVariable("id") Integer id, @RequestBody LoginDTO loginDTO) {
        return securityService.changePasswordByPersonId(id, loginDTO.getOldPassword(), loginDTO.getNewPassword());
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Page<Role> getAllRoles(PageRequest page) {
        Pageable pageable = pageRequestConverter.convert(page);
        return securityService.findAllRoles(pageable);
    }

    @RequestMapping(value = "/role/all", method = RequestMethod.GET)
    public List<Role> allRoles() {
        return securityService.allRoles();
    }

    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    public Role getRoleById(@PathVariable("roleId") Integer roleId) {
        return securityService.getRoleById(roleId);
    }

    @RequestMapping(value = "/role/{personId}/users", method = RequestMethod.GET)
    public List<Role> getRolesByPersonId(@PathVariable("personId") Integer personId) {
        return securityService.getAllRolesByPersonId(personId);
    }

    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public List<Role> saveRoles(@RequestBody @Valid List<Role> roles) {
        return securityService.saveRoles(roles);
    }

    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.PUT)
    public Role saveRole(@PathVariable("roleId") Integer roleId,
                         @RequestBody @Valid Role role) {
        return securityService.saveRole(role);
    }

    @RequestMapping(value = "/role/new", method = RequestMethod.POST)
    public Role createRole(@RequestBody @Valid Role role) {
        return securityService.saveRole(role);
    }

    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.DELETE)
    public void deleteRole(@PathVariable("roleId") Integer roleId) {
        securityService.deleteRole(roleId);
    }

    @RequestMapping(value = "/role/personrole/{personId}/{roleId}", method = RequestMethod.DELETE)
    public void deletePersonRole(@PathVariable("personId") Integer personId, @PathVariable("roleId") Integer roleId) {
        securityService.deletePersonRole(personId, roleId);
    }

    @RequestMapping(value = "/role/persongrouprole/{groupId}/{roleId}", method = RequestMethod.DELETE)
    public void deletePersonGroupRole(@PathVariable("groupId") Integer groupId, @PathVariable("roleId") Integer roleId) {
        securityService.deletePersonGroupRole(groupId, roleId);
    }

    @RequestMapping(value = "/login/{loginId}/roles", method = RequestMethod.GET)
    public List<LoginRole> getLoginRoles(@PathVariable("loginId") Integer loginId) {
        return securityService.getLoginRoles(loginId);
    }

    @RequestMapping(value = "/person/{personId}/roles", method = RequestMethod.GET)
    public List<PersonRole> getPersonRoles(@PathVariable("personId") Integer personId) {
        return securityService.getPersonRoles(personId);
    }


    @RequestMapping(value = "/login/{loginId}/roles", method = RequestMethod.POST)
    public List<LoginRole> saveLoginRoles(@PathVariable("loginId") Integer loginId,
                                          @RequestBody @Valid List<Role> roles) {
        return securityService.setLoginRoles(loginId, roles);
    }


    @RequestMapping(value = "/group/leafNodes", method = RequestMethod.GET)
    public List<PersonGroup> getAllLeafGroups() {
        return securityService.getAllLeafGroups();
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public List<PersonGroup> getAllGroups() {
        return securityService.getAllGroups();
    }

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    public List<PersonGroup> saveGroups(@RequestBody @Valid List<PersonGroup> groups) {
        return securityService.saveGroups(groups);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.PUT)
    public PersonGroup saveGroup(@PathVariable("groupId") Integer groupId,
                                 @RequestBody @Valid PersonGroup group) {
        return securityService.saveGroup(group);
    }

    @RequestMapping(value = "/group/new", method = RequestMethod.POST)
    public PersonGroup createGroup(@RequestBody @Valid PersonGroup group) {
        return securityService.createGroup(group);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable("groupId") Integer groupId) {
        securityService.deleteGroup(groupId);
    }

    @RequestMapping(value = "/group/{groupId}/permissions", method = RequestMethod.GET)
    public PersonGroup getPermissionsByGroupId(@PathVariable("groupId") Integer groupId) {
        return securityService.loadGroupPermissionsByGrpId(groupId);
    }

    @RequestMapping(value = "/login/{loginId}/groups", method = RequestMethod.GET)
    public List<LoginGroup> getLoginGroups(@PathVariable("loginId") Integer loginId) {
        return securityService.getLoginGroups(loginId);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
    public PersonGroup getGroupById(@PathVariable("groupId") Integer groupId) {
        return securityService.getGroupById(groupId);
    }

    @RequestMapping(value = "/login/{loginId}/groups", method = RequestMethod.POST)
    public List<LoginGroup> saveLoginGroups(@PathVariable("loginId") Integer loginId,
                                            @RequestBody @Valid List<PersonGroup> groups) {
        return securityService.setLoginGroups(loginId, groups);
    }


    @RequestMapping(value = "/session/{sessionId}/mobiledevice", method = RequestMethod.POST)
    public MobileDevice saveMobileDevice(@PathVariable("sessionId") Integer sessionId,
                                         @RequestBody MobileDevice device) {
        Session session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null && session.getSessionId().equals(sessionId)) {
            device = mobileDeviceService.create(device);
            session.setMobileDevice(device);
            sessionRepository.save(session);
            Person person = session.getLogin().getPerson();
            person.setMobileDevice(device);
            personRepository.save(person);
        }
        return device;
    }

    @RequestMapping(value = "/session/{sessionId}/mobiledevice/disablenotification", method = RequestMethod.GET)
    public void disableMobileNotification(@PathVariable("sessionId") Integer sessionId) {
        Session session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null && session.getSessionId().equals(sessionId)) {
            MobileDevice device = session.getMobileDevice();
            if (device != null) {
                device.setDisablePushNotification(Boolean.TRUE);
                mobileDeviceService.update(device);
            }
        }
    }

    @RequestMapping(value = "/session/{sessionId}/mobiledevice/enablenotification", method = RequestMethod.GET)
    public void enableMobileNotification(@PathVariable("sessionId") Integer sessionId) {
        Session session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null && session.getSessionId().equals(sessionId)) {
            MobileDevice device = session.getMobileDevice();
            if (device != null) {
                device.setDisablePushNotification(Boolean.FALSE);
                mobileDeviceService.update(device);
            }
        }
    }


    @RequestMapping(value = "/login/resetpwd", method = RequestMethod.POST)
    public void resetPassword(@RequestBody LoginDTO loginDTO) {
        Login login = getLogin(loginDTO.getLoginName());
        if (login == null) {
            throw new CassiniException(loginDTO.getLoginName() + ": " +
                    messageSource.getMessage("user_not_exist", null, "User name does't exists", LocaleContextHolder.getLocale()));
        }

        securityService.resetPassword(login);
    }

    @RequestMapping(value = "/login/twofactorauthentication/reset", method = RequestMethod.POST)
    public void resetTwoFactorAuthenticationPassword(@RequestBody Integer loginId) {
        securityService.resetTwoFactorAuthenticationPassword(loginId);
    }

    @RequestMapping(value = "/login/{loginId}/twofactorauthentication/verify", method = RequestMethod.POST)
    public Session verifyTwoFactorAuthentication(@PathVariable("loginId") Integer loginId, @RequestBody String passcode) {
        return securityService.verifyTwoFactorAuthentication(loginId, passcode);
    }

    @RequestMapping(value = "/person/email/passcode", method = RequestMethod.POST)
    public void resendPersonEmailPasscode(@RequestBody Integer personId) {
        securityService.sendEmailVerifyPassword(personId);
    }

    @RequestMapping(value = "/person/{personId}/email/verify", method = RequestMethod.POST)
    public Person verifyPersonEmail(@PathVariable("personId") Integer personId, @RequestBody String passcode) {
        return securityService.verifyPersonEmail(personId, passcode);
    }

    @RequestMapping(value = "/login/resetpwd/verify", method = RequestMethod.POST)
    public Integer verifyOtp(@RequestBody LoginDTO loginDTO) {
        return securityService.verifyOtp(loginDTO.getLoginName(), loginDTO.getPasscode());
    }

    @RequestMapping(value = "/login/newpassword", method = RequestMethod.POST)
    public LoginDTO setNewPassword(@RequestBody LoginDTO loginDTO) {
        return securityService.setNewPassword(loginDTO.getLoginName(), loginDTO.getOtp(), loginDTO.getNewPassword());
    }

    public Login getLogin(String loginName) {
        return securityService.getLogin(loginName);
    }

    @RequestMapping(value = "/language/{language}", method = RequestMethod.GET)
    public void changeLanguage(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("language") String language) {
        localeResolver.setLocale(request, response, StringUtils.parseLocaleString(language));
    }

    @RequestMapping(value = "/login/multiple/[{ids}]", method = RequestMethod.GET)
    public List<Login> getMultipleLogins(@PathVariable Integer[] ids) {
        return securityService.findMultipleLogins(Arrays.asList(ids));
    }

    @RequestMapping(value = "/login/passwordReset", method = RequestMethod.POST)
    public Login passwordReset(@RequestBody LoginDTO loginDTO) {
        return securityService.passwordReset(loginDTO);
    }


    @RequestMapping(value = "/login/{loginId}/preferences", method = RequestMethod.GET)
    public UserPreferences getLoginPreferences(@PathVariable("loginId") Integer loginId) {
        return securityService.getLoginPreferences(loginId);
    }


    @RequestMapping(value = "/login/preferences", method = RequestMethod.POST)
    public UserPreferences updateUserPreferences(@RequestBody UserPreferences userPreferences) {
        return securityService.updateUserPreferences(userPreferences);
    }

    @RequestMapping(value = "/preferredPage", method = RequestMethod.POST)
    public UserPreferences savePreferredPage(@RequestBody UserPreferences userPreferences) {
        return securityService.savePreferredPage(userPreferences);
    }

    @RequestMapping(value = "/checkportal", method = RequestMethod.GET)
    public Boolean checkIfPortalAccountSetup(HttpServletRequest request) {
        return securityService.checkIfPortalAccountSetup();
    }

    @RequestMapping(value = "/checkportal", method = RequestMethod.POST)
    public Boolean setupPortalAccount(@RequestBody PortalAccount portalAccount) {
        return securityService.setupPortalAccount(portalAccount);
    }

    @RequestMapping(value = "/login/{loginId}/resetpwd", method = RequestMethod.POST)
    public Login resetUserPassword(@PathVariable("loginId") Integer loginId) {
        return securityService.resetUserPassword(loginId);
    }

    @RequestMapping(value = "/login/permissions/{groupId}", method = RequestMethod.GET)
    public SecurityPermissionDto getSecurityPermissionsFromGroupId(@PathVariable("groupId") Integer groupId) {
        return securityService.getSecurityPermissionsFromGroupId(groupId);
    }

    @RequestMapping(value = "/users/count", method = RequestMethod.GET)
    public Login getUserCounts() {
        return securityService.getUserCounts();
    }

}
