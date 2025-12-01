package com.cassinisys.platform.service.security;

import com.cassinisys.platform.config.APIKeyGenerator;
import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ErrorCodes;
import com.cassinisys.platform.exceptions.InvalidLoginException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.LoginCriteria;
import com.cassinisys.platform.filtering.LoginPredicateBuilder;
import com.cassinisys.platform.model.common.*;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.model.dto.IpAddressDto;
import com.cassinisys.platform.model.dto.PMListDto;
import com.cassinisys.platform.model.dto.SecurityPermissionDto;
import com.cassinisys.platform.model.portal.Authentication;
import com.cassinisys.platform.model.portal.CustomerAuthKey;
import com.cassinisys.platform.model.portal.CustomerSession;
import com.cassinisys.platform.model.portal.SaasSession;
import com.cassinisys.platform.model.security.*;
import com.cassinisys.platform.repo.common.*;
import com.cassinisys.platform.repo.core.AppDetailsRepository;
import com.cassinisys.platform.repo.core.UserAttemptRepository;
import com.cassinisys.platform.repo.security.*;
import com.cassinisys.platform.security.EvaluationConstraints;
import com.cassinisys.platform.security.jwt.JwtTokenProvider;
import com.cassinisys.platform.service.common.PersonGroupService;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.common.ProfileService;
import com.cassinisys.platform.service.core.ItemRevisionTypeService;
import com.cassinisys.platform.service.core.PortalAccountService;
import com.cassinisys.platform.service.core.ProgramTypeService;
import com.cassinisys.platform.service.core.ProgramTypeSystem;
import com.cassinisys.platform.service.core.SharedTypeService;
import com.cassinisys.platform.service.portal.PortalException;
import com.cassinisys.platform.service.portal.PortalService;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.RandomString;
import com.cassinisys.platform.util.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 7/27/15.
 */
@Service
public class SecurityService {
    ExpressionParser parser = new SpelExpressionParser();
    private Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LoginRoleRepository loginRoleRepository;
    @Autowired
    private LoginGroupRepository loginGroupRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private GroupPermissionRepository groupPermissionRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private PersonRoleRepository personRoleRepository;
    @Autowired
    private PersonGroupRoleRepository personGroupRoleRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupSecurityPermissionRepository groupSecurityPermissionRepository;
    @Autowired
    private OneTimePasswordRepository oneTimePasswordRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailService mailService;
    @Autowired
    private Environment environment;
    @Autowired
    private UserAttemptRepository userAttemptRepository;
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;
    @Autowired
    private PortalAccountService portalAccountService;
    @Autowired
    private PortalService portalService;
    @Autowired
    private MobileDeviceRepository deviceRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PersonService personService;
    @Autowired
    private LoginPredicateBuilder loginPredicateBuilder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private SharedTypeService sharedTypeService;
    @Autowired
    private ItemRevisionTypeService itemRevisionTypeService;
    @Autowired
    private LicenseService licenseService;
    @Autowired
    private PersonGroupService personGroupService;
    @Autowired
    private AppDetailsRepository appDetailsRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private LoginSecurityPermissionRepository loginSecurityPermissionRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ProgramTypeService programTypeService;

    protected SessionWrapper getSessionWrapper() {
        return sessionWrapper;
    }

    private String setDefaultSchema() {
        String defaultSchema = environment.getProperty("cassini.default.tenantid");
        if (defaultSchema == null || defaultSchema.equals("")) {
            throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("default_schema_not_assigned", null, "Insufficient login information",
                            LocaleContextHolder.getLocale()));
        } else {
            TenantManager.get().setTenantId(defaultSchema);
        }
        return defaultSchema;
    }

    @Transactional(readOnly = true)
    public Login getLoginByLoginName(String loginName) {
        Login login = loginRepository.findByLoginName(loginName);
        return login;
    }

    @Transactional(readOnly = true)
    public Login getLoginByCustomerLoginName(String loginName) {
        return loginRepository.findByLoginName(loginName);
    }

    @Transactional
    public String initTenantId(String user) {
        String tenantId = null;
        if (user.indexOf("@") != -1) {
            String[] arr = user.split("@");
            if (arr.length != 2) {
                tenantId = setDefaultSchema();
                /*
                 * throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
                 * messageSource.getMessage("login_must_be_of_the_form_username", null,
                 * "Login must be of the form username@companyname",
                 * LocaleContextHolder.getLocale()));
                 */
            } else {
                tenantId = arr[1].trim();
                TenantManager.get().setTenantId(tenantId);
            }

            if (!TenantManager.get().isValidTenant(tenantId)) {
                /*
                 * throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
                 * messageSource.getMessage("incorrect_username_or_password", null,
                 * "Incorrect username or password", LocaleContextHolder.getLocale()));
                 */
                throw new CassiniException(messageSource.getMessage("incorrect_username_or_password", null,
                        "Incorrect username or password", LocaleContextHolder.getLocale()));
            }

        } else {
            tenantId = setDefaultSchema();
            if (!TenantManager.get().isValidTenant(tenantId)) {
                /*
                 * throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
                 * messageSource.getMessage("incorrect_username_or_password", null,
                 * "Incorrect username or password", LocaleContextHolder.getLocale()));
                 */
                throw new CassiniException(messageSource.getMessage("incorrect_username_or_password", null,
                        "Incorrect username or password", LocaleContextHolder.getLocale()));
            }
            /*
             * throw new CassiniException(HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED,
             * messageSource.getMessage("login_must_be_of_the_form_username", null,
             * "Login must be of the form username@companyname",
             * LocaleContextHolder.getLocale()));
             */
        }

        return tenantId;
    }

    private void saveMobileDevice(String deviceId, String senderId, String os) {
        MobileDevice mobileDevice = new MobileDevice();
        mobileDevice.setDeviceId(deviceId);
        deviceRepository.save(mobileDevice);
    }

    @Transactional
    public LoginDTO login(String user, String password, HttpServletRequest request, HttpServletResponse response) {
        LoginDTO loginDTO = new LoginDTO();
        String tenantId = initTenantId(user);

        if (user.indexOf("@") != -1) {
            String[] arr = user.split("@");
            user = arr[0];
        }

        if (request != null) {
            request.getSession().setAttribute("CASSINI_TENANT_IDENTIFIER", tenantId);
        }
        Session session = sessionWrapper.getSession();
        if (session != null && session.getLogin() != null && !session.getLogin().getLoginName().equals(user)) {
            session = null;
            sessionWrapper.setSession(null);
        }
        Preference twoFactorAuthentication = preferenceRepository
                .findByPreferenceKey("TWO_FACTOR_AUTHENTICATION_ENABLED");

        if (session != null && session.getLogin() != null) {
            Login login = getLoginByLoginName(session.getLogin().getLoginName());
            session.setTwoFactorChecked(false);
            if (twoFactorAuthentication != null && twoFactorAuthentication.getBooleanValue()) {
                if (login.getPerson().getEmail() != null) {
                    if (login.getPerson().getEmailVerified()) {
                        resetTwoFactorAuthenticationPassword(login.getId());
                    } else {
                        sendEmailVerifyPassword(login.getPerson().getId());
                    }
                }
                session.setHasTwoFactorAuthentication(true);
            } else if (login.getPerson().getEmail() != null && !login.getPerson().getEmailVerified()) {
                sendEmailVerifyPassword(login.getPerson().getId());
            }

            session.setLogin(login);
            UserPreferences userPreference = userPreferencesRepository.findByLogin(login.getId());
            Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.DATE.FORMAT");
            if (userPreference != null && userPreference.getUserDateFormat() != null) {
                session.setPreferredDateFormat(userPreference.getUserDateFormat());
                session.setPreferredShortDateFormat(userPreference.getShortDateFormat());
            } else if (preference != null && preference.getStringValue() != null) {
                session.setPreferredDateFormat(preference.getStringValue());
                session.setPreferredShortDateFormat(preference.getStringValue().substring(0, 10));
            }
            if (response != null) {
                response.addHeader(EvaluationConstraints.JWTTOKEN, session.getAccessToken());
                response.addHeader(EvaluationConstraints.REFRESHTOKEN, session.getRefreshToken());
                response.addHeader("Access-Control-Allow-Headers",
                        "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
            }
            loginDTO.setSession(session);
            return loginDTO;
        } else {
            Login login = login(user, password);
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user, password));
            String jwt = jwtTokenProvider.generateJwtToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            if (response != null) {
                response.addHeader(EvaluationConstraints.JWTTOKEN, jwt);
                response.addHeader(EvaluationConstraints.REFRESHTOKEN, refreshToken);
                response.addHeader("Access-Control-Allow-Headers",
                        "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
            }

            session = new Session();
            session.setLogin(login);
            if (request != null) {
                session.setIpAddress(request.getRemoteAddr());
            } else {
                try {
                    session.setIpAddress(Inet4Address.getLocalHost().toString());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            session.setLoginTime(new Date());
            session = sessionRepository.save(session);
            session.setTenantId(tenantId);
            session.setAccessToken(jwt);
            session.setRefreshToken(refreshToken);
            UserPreferences userPreference = userPreferencesRepository.findByLogin(login.getId());
            Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.DATE.FORMAT");
            if (userPreference != null && userPreference.getUserDateFormat() != null) {
                session.setPreferredDateFormat(userPreference.getUserDateFormat());
                session.setPreferredShortDateFormat(userPreference.getShortDateFormat());
            } else if (preference != null && preference.getStringValue() != null) {
                session.setPreferredDateFormat(preference.getStringValue());
                session.setPreferredShortDateFormat(preference.getStringValue().substring(0, 10));
            }
            if (twoFactorAuthentication != null && twoFactorAuthentication.getBooleanValue()) {
                session.setHasTwoFactorAuthentication(true);
                if (login.getPerson().getEmail() != null) {
                    if (login.getPerson().getEmailVerified()) {
                        resetTwoFactorAuthenticationPassword(login.getId());
                    } else {
                        sendEmailVerifyPassword(login.getPerson().getId());
                    }
                }
            } else if (login.getPerson().getEmail() != null && !login.getPerson().getEmailVerified()) {
                sendEmailVerifyPassword(login.getPerson().getId());
            }
            sessionWrapper.setSession(session);
            sessionWrapper.setTenantId(tenantId);

            SessionManager.get().setSessionRepository(sessionRepository);
            SessionManager.get().addSession(session);

            session.setApiKey(APIKeyGenerator.get().generate());
            loginDTO.setSession(session);

            if (request != null) {
                request.getSession().setAttribute("CASSINI_SESSION", session);
            }
            loginDTO.getSession().getLogin().setGroups(
                    personGroupService.getPersonGroups(loginDTO.getSession().getLogin().getPerson().getId()));
            return loginDTO;
        }
    }

    @Transactional(readOnly = true)
    public List<SecurityPermission> loadPersonSecurityPermissions(Login login) {
       
        List<SecurityPermission> securityPermissions = new ArrayList<>();

        // List <SecurityPermission> pmPermission = PMPermissions(login);

        // securityPermissions.addAll(pmPermission);

        List<GroupMember> groupMembers = groupMemberRepository.findByPerson(login.getPerson());
        HashMap<Integer, SecurityPermission> permissionHashMap = new HashMap<>();
        List<Integer> groupIds = new ArrayList<>();

        groupMembers.forEach(groupMember -> {
            groupIds.add(groupMember.getPersonGroup().getGroupId());
        });
        if (groupIds.size() > 0) {
            List<GroupSecurityPermission> groupPermissions = groupSecurityPermissionRepository
                    .getSecurityPermissionsByGroupIds(groupIds);
            groupPermissions.forEach(groupPermission -> {
                permissionHashMap.put(groupPermission.getId().getSecurityPermission().getId(),
                        groupPermission.getId().getSecurityPermission());
            });
        }
        for (SecurityPermission securityPermission : permissionHashMap.values()) {
            securityPermissions.add(securityPermission);
        }
        return securityPermissions;
    }
    

    public List<SecurityPermission> PMPermissions(Login login) {
        PMListDto pmDto = programTypeService.getTypeSystem(EvaluationConstraints.PROGRAMOBJECT)
                .getPMToList(login.getPerson().getId());

        List<SecurityPermission> securityPermissions = new ArrayList<>();

        if (pmDto.getProjectMembersList().size() > 0) {
            pmDto.getProjectMembersList().forEach(projectMember -> {

                //System.out.print(projectMember);

                Integer personId = parser.parseExpression("person").getValue(projectMember, Integer.class);
                if (login.getPerson().getId().equals(personId)) {
                    Integer projectId = parser.parseExpression("project").getValue(projectMember, Integer.class);
                    securityPermissions.add(getPersonSecurityPermission("project", null, "view", null, null,
                            "object.id == " + projectId.toString(), false, PrivilegeType.GRANTED, personId.toString()));
                }

            });
            securityPermissions.add(getSecurityPermission("file", null, "teamCreate", null, null,
                            null, false, PrivilegeType.GRANTED));
                    
            securityPermissions.add(getSecurityPermission("file", null, "teamDownload", null, null,
                            null, false, PrivilegeType.GRANTED));
        }

        if (pmDto.getProjectList().size() > 0) {
            pmDto.getProjectList().forEach(project -> {
                Integer projectManager = parser.parseExpression("projectManager").getValue(project, Integer.class);
                Integer id = parser.parseExpression("id").getValue(project, Integer.class);
                securityPermissions.add(getPersonSecurityPermission("project", null, "all", null, null,
                        "object.id == " + id, false, PrivilegeType.GRANTED, projectManager.toString()));
            });
        }

        if (pmDto.getProjectActivityList().size() > 0) {
            pmDto.getProjectActivityList().forEach(activity -> {
                Integer assignedTo = parser.parseExpression("assignedTo").getValue(activity, Integer.class);
                Integer id = parser.parseExpression("id").getValue(activity, Integer.class);
                if(assignedTo != null) {
                securityPermissions.add(getPersonSecurityPermission("projectactivity", null, "all", null, null,
                        "object.id == " + id, false, PrivilegeType.GRANTED, assignedTo.toString()));
                }
                });
        }

        if (pmDto.getProjectTaskList().size() > 0) {
            pmDto.getProjectTaskList().forEach(task -> {
                Integer assignedTo = parser.parseExpression("assignedTo").getValue(task, Integer.class);
                Integer id = parser.parseExpression("id").getValue(task, Integer.class);
                if(assignedTo != null) {
                securityPermissions.add(getPersonSecurityPermission("projecttask", null, "all", null, null,
                        "object.id == " + id, false, PrivilegeType.GRANTED, assignedTo.toString()));
                }
            });
        }


        //---------------------------------------- program -------------------------------------------

        if (pmDto.getProgramList().size() > 0) {
            pmDto.getProgramList().forEach(program -> {
                Integer programManager = parser.parseExpression("programManager").getValue(program, Integer.class);
                Integer id = parser.parseExpression("id").getValue(program, Integer.class);
                securityPermissions.add(getPersonSecurityPermission("program", null, "all", null, null,
                        "object.id == " + id, false, PrivilegeType.GRANTED, programManager.toString()));
                securityPermissions.add(getPersonSecurityPermission("pmobjecttype", null, "view", null, null,null, false, PrivilegeType.GRANTED, programManager.toString()));
            });
        }

        if (pmDto.getProgramResourceList().size() > 0) {
            pmDto.getProgramResourceList().forEach(programResource -> {
                Integer personId = parser.parseExpression("person").getValue(programResource, Integer.class);
                    Integer programId = parser.parseExpression("program").getValue(programResource, Integer.class);
                    securityPermissions.add(getPersonSecurityPermission("program", null, "view", null, null,
                            "object.id == " + programId.toString(), false, PrivilegeType.GRANTED, personId.toString()));
                
            });

            securityPermissions.add(getSecurityPermission("file", null, "teamCreate", null, null,
                            null, false, PrivilegeType.GRANTED));
                    
            securityPermissions.add(getSecurityPermission("file", null, "teamDownload", null, null,
                            null, false, PrivilegeType.GRANTED));
        }






        return securityPermissions;
    }

    @Transactional(readOnly = true)
    public List<Permission> loadPersonPermissions() {

        List<GroupMember> groupMembers = groupMemberRepository
                .findByPerson(sessionWrapper.getSession().getLogin().getPerson());
        HashMap<String, Permission> permissionHashMap = new HashMap<>();
        List<Integer> groupIds = new ArrayList<>();

        groupMembers.forEach(groupMember -> {
            groupIds.add(groupMember.getPersonGroup().getGroupId());
        });
        List<Permission> permissions = new ArrayList<>();
        if (groupIds.size() > 0) {
            List<GroupPermission> groupPermissions = groupPermissionRepository.getPermissionsByGroupIds(groupIds);
            groupPermissions.forEach(groupPermission -> {
                permissionHashMap.put(groupPermission.getId().getPermission().getId(),
                        groupPermission.getId().getPermission());
            });
        }

        for (Permission permission : permissionHashMap.values()) {
            permissions.add(permission);
        }

        return permissions;
    }

    public List<SecurityPermission> loadExternalPermission(Login login) {
        List objects = new ArrayList<>();
        objects.addAll(sharedTypeService.getTypeSystem(EvaluationConstraints.SHAREDOBJECT)
                .getSharedToList(login.getPerson().getId()));
        objects.addAll(sharedTypeService.getTypeSystem(EvaluationConstraints.SHAREDOBJECT)
                .getSharedToList(login.getPerson().getDefaultGroup()));

        Set<SecurityPermission> securityPermissions = new HashSet<>();
        objects.stream().forEach(o -> {
            String objectType = parser.parseExpression("sharedObjectType.toString()").getValue(o, String.class);
            Integer objectId = parser.parseExpression("objectId").getValue(o, Integer.class);
            String permission = parser.parseExpression("permission.toString()").getValue(o, String.class);
            if (permission.equalsIgnoreCase("read")) {
                securityPermissions.add(getSecurityPermission(objectType, null, "view", null, null,
                        "object.id == " + objectId, true, PrivilegeType.GRANTED));
                if (objectType.equals("ITEM")) {
                    List<Integer> itemRevisionIds = new ArrayList<>();
                    itemRevisionIds.addAll(itemRevisionTypeService.getTypeSystem(EvaluationConstraints.ITEM)
                            .getItemRevisionsIdsByItem(objectId));
                    itemRevisionIds.forEach(id -> {
                        securityPermissions.add(getSecurityPermission("ITEMREVISION", null, "view", null, null,
                                "object.id == " + id, true, PrivilegeType.GRANTED));
                    });
                }
            }
            if (permission.equalsIgnoreCase("write")) {
                if (objectType.equals("ITEM")) {
                    List<Integer> itemRevisionIds = new ArrayList<>();
                    itemRevisionIds.addAll(itemRevisionTypeService.getTypeSystem(EvaluationConstraints.ITEM)
                            .getItemRevisionsIdsByItem(objectId));
                    itemRevisionIds.forEach(id -> {
                        securityPermissions.add(getSecurityPermission("ITEMREVISION", null, "view", null, null,
                                "object.id == " + id, true, PrivilegeType.GRANTED));
                    });
                }
                if (objectType.equalsIgnoreCase("PGCDECLARATION")) {
                    List<SecurityPermission> securityPermissions1 = new ArrayList<>();
                    securityPermissions1.add(getSecurityPermission("PGCDECLARATION", null, "all", null, null, null,
                            true, PrivilegeType.GRANTED));
                    securityPermissions1.add(getSecurityPermission("PGCSUBSTANCETYPE", null, "all", null, null, null,
                            true, PrivilegeType.GRANTED));
                    securityPermissions1.add(getSecurityPermission("PGCSPECIFICATION", null, "all", null, null, null,
                            true, PrivilegeType.GRANTED));
                    securityPermissions1.add(getSecurityPermission("PGCSUBSTANCE", null, "all", null, null, null, true,
                            PrivilegeType.GRANTED));
                    securityPermissions.addAll(securityPermissions1);
                } else {
                    securityPermissions.add(getSecurityPermission(objectType, null, "view,edit", null, null,
                            "object.id == " + objectId, true, PrivilegeType.GRANTED));
                    // securityPermissions.add(getSecurityPermission(EvaluationConstraints.FILE,
                    // null, "all", null, null, "object.id == " + objectId, true,
                    // PrivilegeType.GRANTED));
                }
            }
        });
        List<SecurityPermission> securityPermissions1 = securityPermissions.stream().collect(Collectors.toList());
        return securityPermissions1;
    }

    @Transactional
    public Login login(String loginName, String password) {
        if (loginName == null || loginName.trim().isEmpty() || loginName.trim().equalsIgnoreCase("null") ||
                password == null || password.trim().isEmpty() || password.trim().equalsIgnoreCase("null")) {
            throw new InvalidLoginException(messageSource.getMessage("user_pass_error",
                    null, "Username or Password can't be empty", LocaleContextHolder.getLocale()));
        }

        Login login = loginRepository.findByLoginName(loginName);
        if (login == null) {
            throw new InvalidLoginException(messageSource.getMessage("incorrect_username_or_password", null,
                    "Incorrect username or password", LocaleContextHolder.getLocale()));
        }

        if (login.getIsActive() != true) {
            throw new InvalidLoginException(messageSource.getMessage("user_account_in_active_message", null,
                    "Your account is In-Active. Please contact Admin", LocaleContextHolder.getLocale()));
        }

        if (!BCrypt.checkpw(password, login.getPassword())) {
            throw new InvalidLoginException(messageSource.getMessage("incorrect_username_or_password", null,
                    "Incorrect username or password", LocaleContextHolder.getLocale()));
        }

        if (login.getIsSuperUser() == false && login.getIsActive()) {
            UserAttempts userAttempts = getUserAttempts(loginName);
            if (userAttempts != null) {
                if (login.isLocked() == true) {
                    throw new CassiniException(messageSource.getMessage("login_attempts_reached", null,
                            "Benutzerkonto ist gesperrt. Bitte wenden Sie sich an admin.",
                            LocaleContextHolder.getLocale()));

                } else {
                    userAttemptRepository.delete(userAttempts.getId());
                }
            }

        }

        return login;

    }

    @Transactional(readOnly = true)
    public UserAttempts getUserAttempts(String username) {

        try {

            UserAttempts userAttempts = userAttemptRepository.findByUserName(username);
            return userAttempts;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Transactional
    public Login createLogin(Login login, String phone, String email) {
        Login existingLogin = loginRepository.findByPersonId(login.getPerson().getId());

        if (existingLogin != null) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("person_already_has_permissions_to_login", null,
                            "Person already has permissions to Login", LocaleContextHolder.getLocale()));
        }

        existingLogin = loginRepository.findByLoginName(login.getLoginName());
        if (existingLogin != null) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("login_name_already_exists", null, "Login Name already exists",
                            LocaleContextHolder.getLocale()));
        }

        String pwd = login.getPassword();
        if (pwd == null || pwd.length() <= 0) {
            pwd = RandomString.get().getAlphaNumeric(8);
        }
        // Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        login.setPassword(hashed);
        // login.setIsActive(Boolean.valueOf(true));
        login = this.loginRepository.save(login);
        sendLoginCreatedEmail(login, pwd, "NEW");
        return login;
    }

    private Boolean checkExistingUser(Login login) {
        if (login.getId() != null) {
            Login login1 = loginRepository.findOne(login.getId());
            if (login1 != null && login1.getIsActive().equals(login.getIsActive())) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private void checklicence(Login login) {

        if (login.getIsActive() && !login.getExternal() && checkExistingUser(login)) {

            Integer activeLicense = findIsActiveAndExternalLoginsCount(true, false);
            if (licenseService.getLicense() != null) {
                Integer customerLicenses = licenseService.getLicense().getLicenses();
                if (customerLicenses != null) {
                    if (activeLicense >= customerLicenses) {
                        throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                                messageSource.getMessage("max_licences_reached_error", null,
                                        "Your license limit is reached.Please contact Admin",
                                        LocaleContextHolder.getLocale()));
                    }
                }
            }
        }

    }

    private void checkIfLoginNameExist(Login login) {
        Login existingLogin = loginRepository.findByLoginName(login.getLoginName());
        if (existingLogin != null) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("login_name_already_exists", null, "Login Name already exists",
                            LocaleContextHolder.getLocale()));
        }
    }

    private void checkIfPersonExist(Login login) {
        Person existPerson = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                login.getPerson().getFirstName(), login.getPerson().getLastName());
        if (existPerson != null) {
            String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist",
                    LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".",
                    existPerson.getFirstName() + " " + existPerson.getLastName());
            throw new CassiniException(result);
        }
    }

    @Transactional
    public Login createLoginPerson(Login login) {
        checklicence(login);
        checkIfLoginNameExist(login);
        if (!login.getExistPerson()) {
            checkIfPersonExist(login);
        }
        // checkIfPersonExist(login);
        Person person;
        if (login.getExistPerson()) {
            person = this.personService.update(login.getPerson());
        } else {
            person = this.personService.create(login.getPerson());
        }

        login.setPerson(person);
        String pwd = login.getPassword();
        if (pwd == null || pwd.length() <= 0) {
            pwd = RandomString.get().getAlphaNumeric(8);
        }
        // Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        login.setPassword(hashed);
        // login.setIsActive(Boolean.valueOf(true));
        login = this.loginRepository.save(login);
        sendLoginCreatedEmail(login, pwd, "NEW");
        return login;
    }

    @Async
    public void sendLoginCreatedEmail(Login login, String pwd, String type) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        String userName = login.getLoginName() + "@" + TenantManager.get().getTenantId();
        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("cssIncludes", getCss(host));
        model.put("name", login.getPerson().getFullName());
        model.put("username", userName);
        model.put("password", pwd);
        Mail mail = new Mail();
        mail.setMailTo(login.getPerson().getEmail());
        if (type.equals("NEW")) {
            mail.setMailSubject("Welcome to CassiniPLM!");
            mail.setTemplatePath("email/security/newUser.html");
        } else if (type.equals("RESET")) {
            mail.setMailSubject("CassiniPLM " + " - Reset Password");
            mail.setTemplatePath("email/security/passwordReset.html");
        }
        mail.setModel(model);
        mailService.sendEmail(mail);
    }

    @Transactional(readOnly = true)
    public Login getLoginByPerson(Integer personId) {
        return loginRepository.findByPersonId(personId);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> getPermissionsForLoginUser(Login login) {
        Map<String, Boolean> userPermissions = new HashMap();
        // load all permissions
        List<Permission> permissions = permissionRepository.findAll();

        // set all permissions in map with default as false
        if (permissions != null && permissions.size() > 0) {

            for (Permission permission : permissions) {
                userPermissions.put(permission.getId(), false);
            }
        }

        List<GroupPermission> groupPermissions = null;

        Set<Integer> groupIds = new HashSet<Integer>();
        // get all roles for user
        List<Integer> grpIds = groupMemberRepository.findGroupIdsByPerson(login.getPerson().getId());

        if (grpIds != null && grpIds.size() > 0) {
            for (Integer gId : grpIds) {
                groupIds.add(gId);
            }
        }

        if (groupIds != null && groupIds.size() > 0)
            groupPermissions = groupPermissionRepository.findBygroupIds(groupIds);

        if (groupPermissions != null && groupPermissions.size() > 0) {

            for (GroupPermission groupPermission : groupPermissions) {

                // loop all role permissions and make true for existing permission
                if (!userPermissions.get(groupPermission.getId().getPermission().getId())) {

                    userPermissions.put(groupPermission.getId().getPermission().getId(), true);

                }
            }
        }

        return userPermissions;
    }

    @Transactional
    public LoginDTO changePassword(String oldPassword, String newPassword) {
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();
        Login login = this.sessionWrapper.getSession().getLogin();
        return changePassword(login, oldPassword, newPassword);
    }

    @Transactional
    public LoginDTO changePasswordByPersonId(Integer personId, String oldPassword, String newPassword) {
        oldPassword = oldPassword.trim();
        newPassword = newPassword.trim();
        Login login = loginRepository.findByPersonId(personId);
        return changePassword(login, oldPassword, newPassword);
    }

    @Transactional
    public LoginDTO changePassword(Login login, String oldPassword, String newPassword) {
        LoginDTO loginDTO = new LoginDTO();
        if (!BCrypt.checkpw(oldPassword, login.getPassword())) {
            throw new InvalidLoginException(
                    messageSource.getMessage("incorrect_old_password", null, "Incorrect old password",
                            LocaleContextHolder.getLocale()));
        } else if (oldPassword.equals(newPassword)) {
            throw new InvalidLoginException(
                    messageSource.getMessage("old_and_new_passwords_cannot_be_same", null,
                            "Old and new passwords cannot be same", LocaleContextHolder.getLocale()));
        } else {

            login.setLocked(false);
            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            login.setPassword(hashed);
            Login loginElement = loginRepository.save(login);
            UserAttempts userAttempts = userAttemptRepository.findByUserName(loginElement.getLoginName());
            if (userAttempts != null) {
                userAttemptRepository.delete(userAttempts.getId());
            }
            loginDTO.setLogin(loginElement);
            return loginDTO;
        }
    }

    @Transactional(readOnly = true)
    public Page<Login> getLogins(Pageable pageable) {
        return loginRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Login> getFilteredLogins(Pageable pageable, LoginCriteria loginCriteria) {
        List<Login> logins = new LinkedList<>();

        List<Login> loginList = loginRepository.getLoginsByAlphabeticalOrder();

        loginCriteria.setIsActive(true);
        loginCriteria.setExternal(false);
        Predicate predicate = loginPredicateBuilder.build(loginCriteria, QLogin.login);
        Page<Login> activeUsers = loginRepository.findAll(predicate, pageable);
        loginCriteria.setIsActive(true);
        loginCriteria.setExternal(true);
        predicate = loginPredicateBuilder.build(loginCriteria, QLogin.login);
        Page<Login> externalUsers = loginRepository.findAll(predicate, pageable);
        loginCriteria.setIsActive(false);
        predicate = loginPredicateBuilder.build(loginCriteria, QLogin.login);
        Page<Login> inActiveUsers = loginRepository.findAll(predicate, pageable);

        logins.addAll(activeUsers.getContent());
        logins.addAll(externalUsers.getContent());
        logins.addAll(inActiveUsers.getContent());

        return new PageImpl<Login>(logins, pageable, loginList.size());
    }

    @Transactional(readOnly = true)
    public Page<Login> getAllActiveLogins(Pageable pageable) {
        return loginRepository.findByIsActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Login> getAllFilteredActiveLogins(Pageable pageable, LoginCriteria loginCriteria) {
        loginCriteria.setIsActive(true);
        Predicate predicate = loginPredicateBuilder.getAllActiveLoginsPredicates(loginCriteria, QLogin.login);
        return loginRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<Login> getActiveLogins() {
        return loginRepository.findByIsActive(Boolean.TRUE);
    }

    @Transactional(readOnly = true)
    public List<Login> getInternalActiveLogins() {
        return loginRepository.findByIsActiveTrueAndExternalFalse();
    }

    @Transactional(readOnly = true)
    public Integer getIsActiveLogins(Boolean aBoolean) {
        return loginRepository.findIsActiveLogins(aBoolean);
    }

    @Transactional(readOnly = true)
    public Integer findIsActiveAndExternalLoginsCount(Boolean aBoolean, Boolean external) {
        return loginRepository.findIsActiveAndExternalLoginsCount(aBoolean, external);
    }

    @Transactional
    public List<Login> getAllLogins() {
        List<Login> loginExternalUsers = new ArrayList<>();
        List<Login> loginActiveUsers = new ArrayList<>();
        List<Login> loginInActiveUsers = new ArrayList<>();
        List<Login> logins = loginRepository.getLoginsByAlphabeticalOrder();
        for (Login login : logins) {
            if (login.getIsActive() == true && login.getExternal() == true) {
                loginExternalUsers.add(login);
            }
        }

        for (Login login : logins) {
            if (login.getIsActive() == true && login.getExternal() == false) {
                loginActiveUsers.add(login);
            }
        }
        for (Login login : logins) {
            if (login.getIsActive() == false) {
                loginInActiveUsers.add(login);
            }
        }
        List<Login> loginFInalResult = Stream.of(loginActiveUsers, loginExternalUsers, loginInActiveUsers)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return loginFInalResult;
    }

    @Transactional(readOnly = true)
    public Login get(Integer id) {
        checkNotNull(id);
        Login login = loginRepository.findOne(id);
        if (login == null) {
            throw new ResourceNotFoundException();
        }
        return login;
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRolesByPersonId(Integer personId) {
        checkNotNull(personId);
        Person person = personRepository.findOne(personId);

        List<Role> roles = new ArrayList<Role>();
        // for a person roles exist in PersonRoles and in PersonGroupRoles

        List<PersonRole> personRoles = personRoleRepository.findByPersonId(personId);
        if (personRoles != null && personRoles.size() > 0) {

            for (PersonRole personRole : personRoles) {

                roles.add(roleRepository.findOne(personRole.getRoleId()));
            }

        }

        List<GroupMember> personInGroups = groupMemberRepository.findByPerson(person);
        if (personInGroups != null && personInGroups.size() > 0) {

            for (GroupMember groupMember : personInGroups) {

                List<PersonGroupRole> personGroupRoles = personGroupRoleRepository
                        .findByGroupId(groupMember.getPersonGroup().getGroupId());

                if (personGroupRoles != null && personGroupRoles.size() > 0) {

                    for (PersonGroupRole personGroupRole : personGroupRoles) {

                        Role role = roleRepository.findOne(personGroupRole.getRoleId());
                        boolean roleExists = false;
                        for (Role addedRole : roles) {

                            if (role.getId() == addedRole.getId()) {

                                roleExists = true;
                                continue;
                            }
                        }
                        if (!roleExists) {
                            roles.add(role);
                            roleExists = false;
                        }
                    }
                }
            }
        }

        return roles;
    }

    @Transactional(readOnly = true)
    public Role getRoleById(Integer id) {
        checkNotNull(id);
        Role role = roleRepository.findOne(id);
        if (role == null) {
            throw new ResourceNotFoundException();
        }

        List<PersonRole> personRoles = null;
        List<PersonGroupRole> personGroupRoles = null;

        if (role != null) {
            personRoles = personRoleRepository.findByRoleId(role.getId());
            personGroupRoles = personGroupRoleRepository.findByRoleId(role.getId());

            for (PersonRole personRole : personRoles) {

                if (personRole != null) {

                    personRole.setPerson(personRepository.findOne(personRole.getPersonId()));

                    Login login = loginRepository.findByPersonId(personRole.getPerson().getId());

                    if (login != null) {

                        personRole.setLogin(login);
                    }
                }
            }
            for (PersonGroupRole personGroupRole : personGroupRoles) {
                if (personGroupRole != null) {
                    personGroupRole.setPersonGroup(personGroupRepository.findOne(personGroupRole.getGroupId()));
                }
            }

            role.setPersonRoles(personRoles);
            role.setPersonGroupRoles(personGroupRoles);

        }

        return role;
    }

    @Transactional(readOnly = true)
    public Permission getPermissionById(String id) {
        return permissionRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Page<Role> findAllRoles(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        return roleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> getAllLeafGroups() {
        List<PersonGroup> activePersonGroups = new ArrayList<>();
        List<PersonGroup> personGroups = personGroupRepository.findByParentIsNotNullOrderByGroupIdAsc();
        for (PersonGroup personGroup : personGroups) {
            personGroup.setGroupChildren(personGroupRepository.findByParent(personGroup.getGroupId()));
            if (personGroup.getIsActive() == true && personGroup.getGroupChildren().size() == 0) {
                activePersonGroups.add(personGroup);
            }
        }
        return activePersonGroups;
    }

    @Transactional(readOnly = true)
    public List<PersonGroup> getAllGroups() {
        List<PersonGroup> personGroups = new ArrayList<>();

        personGroups = personGroupRepository.findByParentIsNullOrderByGroupIdAsc();
        if (personGroups.size() != 0) {
            for (PersonGroup personGroup : personGroups) {
                personGroup.setLevel(0);
                loadPersonGroupChildrens(personGroup);
            }
        }
        return personGroups;
    }

    @Transactional(readOnly = true)
    public void loadPersonGroupChildrens(PersonGroup personGroup) {
        List<PersonGroup> groupChildren = personGroupRepository.findByParentOrderByGroupIdAsc(personGroup.getGroupId());
        if (groupChildren.size() != 0) {
            personGroup.setGroupChildren(groupChildren);
            for (PersonGroup personGroup1 : groupChildren) {
                personGroup1.setLevel(personGroup.getLevel() + 1);
                // securityService.loadGroupPermissions(personGroup1);
                loadPersonGroupChildrens(personGroup1);
            }
        }
    }

    @Transactional
    public List<Role> saveRoles(List<Role> roles) {

        List<Role> savedRoles = new ArrayList<>();
        for (Role role : roles) {
            savedRoles.add(saveRole(role));
        }
        return savedRoles;
    }

    @Transactional
    public List<PersonGroup> saveGroups(List<PersonGroup> groups) {
        List<PersonGroup> savedGroups = new ArrayList<>();
        for (PersonGroup group : groups) {
            savedGroups.add(saveGroup(group));
        }
        return savedGroups;
    }

    @Transactional
    public Role saveRole(Role role) {

        checkNotNull(role);

        List<PersonRole> personRoles = role.getPersonRoles();
        List<PersonGroupRole> personGroupRoles = role.getPersonGroupRoles();

        role = roleRepository.save(role);

        if (personRoles != null && personRoles.size() > 0) {

            for (PersonRole personRole : personRoles) {

                if (personRole != null) {

                    personRole.setRoleId(role.getId());
                }

            }
        }

        if (personGroupRoles != null && personGroupRoles.size() > 0) {

            for (PersonGroupRole personGrpRole : personGroupRoles) {

                if (personGrpRole != null) {

                    personGrpRole.setRoleId(role.getId());
                }

            }
        }
        personRoleRepository.save(personRoles);
        personGroupRoleRepository.save(personGroupRoles);

        role.setPersonGroupRoles(personGroupRoles);
        role.setPersonRoles(personRoles);
        return role;

    }

    @Transactional
    public PersonGroup saveGroup(PersonGroup group) {
        if (group.getGroupId() != null) {
            deleteGroupPermissions(group);
        }
        setGroupPermissions(group, group.getPermissions());
        PersonGroup savedGroup = personGroupRepository.save(group);
        savedGroup.setPermissions(group.getPermissions());
        return savedGroup;
    }

    @Transactional
    public void deleteRole(Integer id) {
        roleRepository.delete(id);
    }

    @Transactional
    public void deleteGroup(Integer id) {
        personGroupRepository.delete(id);
    }

    @Transactional
    public void loadRolePermissions(Role role) {
        List<Permission> permissions = new ArrayList<>();
        List<RolePermission> rolePermissions = getRolePermissions(role);
        for (RolePermission rp : rolePermissions) {
            permissions.add(rp.getId().getPermission());
        }

        role.setPermissions(permissions);
    }

    @Transactional(readOnly = true)
    public void loadGroupPermissions(PersonGroup group) {
        List<Permission> permissions = new ArrayList<>();

        List<GroupPermission> groupPermissions = getGroupPermissions(group);
        for (GroupPermission gp : groupPermissions) {
            permissions.add(gp.getId().getPermission());
        }

        group.setPermissions(permissions);
    }

    @Transactional(readOnly = true)
    public List<PersonRole> getPersonRoles(Integer personId) {
        return personRoleRepository.findByPersonId(personId);
    }

    @Transactional(readOnly = true)
    public List<Role> allRoles() {

        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PersonGroup loadGroupPermissionsByGrpId(Integer groupId) {

        PersonGroup group = personGroupRepository.findOne(groupId);

        List<Permission> permissions = new ArrayList<>();
        List<GroupPermission> groupPermissions = getGroupPermissions(group);
        for (GroupPermission gp : groupPermissions) {
            permissions.add(gp.getId().getPermission());
        }

        group.setPermissions(permissions);
        return group;
    }

    @Transactional(readOnly = true)
    public void loadLoginRoles(Login login) {
        List<LoginRole> loginRoles = loginRoleRepository.findLoginRoles(login);
        if (loginRoles.size() > 0) {
            List<Role> roles = new ArrayList<>();
            for (LoginRole loginRole : loginRoles) {
                roles.add(loginRole.getId().getRole());
            }
            login.setRoles(roles);
        }
    }

    @Transactional(readOnly = true)
    public void loadLoginGroups(Login login) {
        List<PersonGroup> personGroups = new ArrayList();
        List<Permission> permissions = new LinkedList<>();
        if (login.getPerson().getId() != 1) {
            personGroups = groupMemberRepository.findGroupsByPerson(login.getPerson().getId());
        } else if (login.getPerson().getId() == 1) {
            personGroups = personGroupRepository.findAll();
        }
        if (personGroups.size() > 0) {
            for (PersonGroup personGroup : personGroups) {
                login.getPermissionsMap().put(personGroup.getGroupId(), this.getGroupPermissionsByUser(personGroup));
            }
        }
    }

    public List<Permission> getGroupPermissionsByUser(PersonGroup group) {
        List<Permission> permissions = new ArrayList<>();
        List<GroupPermission> groupPermissions = getGroupPermissions(group);
        for (GroupPermission gp : groupPermissions) {
            permissions.add(gp.getId().getPermission());
        }

        return permissions;
    }

    @Transactional
    public PersonGroup createGroup(PersonGroup group) {
        return personGroupRepository.save(group);
    }

    @Transactional
    public Role updateRole(Role role) {
        role = roleRepository.save(role);
        loadRolePermissions(role);
        return role;
    }

    @Transactional
    public void deleteRole(Role role) {
        // First delete all role assignments
        loginRoleRepository.deleteAllByRole(role);

        // Then delete the role permissions
        rolePermissionRepository.deleteRolePermissions(role);

        // Then delete the actual role
        roleRepository.delete(role);
    }

    @Transactional
    public void deletePersonRole(Integer personId, Integer roleId) {
        personRoleRepository.deletePersonRole(personId, roleId);
    }

    @Transactional
    public void deletePersonGroupRole(Integer groupId, Integer roleId) {
        personGroupRoleRepository.deletePersonGroupRole(groupId, roleId);
    }

    @Transactional(readOnly = true)
    public List<RolePermission> getRolePermissions(Role role) {
        return rolePermissionRepository.findRolePermissions(role);
    }

    @Transactional(readOnly = true)
    public List<GroupPermission> getGroupPermissions(PersonGroup group) {
        return groupPermissionRepository.findGroupPermissions(group);
    }

    @Transactional
    public RolePermission addRolePermission(Role role, Permission permission) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(new RolePermissionId(role, permission));

        return rolePermissionRepository.save(rolePermission);
    }

    @Transactional
    public GroupPermission addGroupPermission(PersonGroup group, Permission permission) {
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(new GroupPermissionId(group, permission));

        return groupPermissionRepository.save(groupPermission);
    }

    @Transactional
    public List<RolePermission> setRolePermissions(Role role, List<Permission> permissions) {
        deleteRolePermissions(role);

        List<RolePermission> rolePermissions = new ArrayList<>();

        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(new RolePermissionId(role, permission));

            rolePermissions.add(rolePermission);
        }

        return rolePermissionRepository.save(rolePermissions);
    }

    @Transactional
    public List<GroupPermission> setGroupPermissions(PersonGroup group, List<Permission> permissions) {
        deleteGroupPermissions(group);

        List<GroupPermission> groupPermissions = new ArrayList<>();

        for (Permission permission : permissions) {
            GroupPermission groupPermission = new GroupPermission();
            groupPermission.setId(new GroupPermissionId(group, permission));
            groupPermissions.add(groupPermission);
        }

        return groupPermissionRepository.save(groupPermissions);
    }

    @Transactional
    public void deleteRolePermissions(Role role) {
        rolePermissionRepository.deleteRolePermissions(role);
    }

    @Transactional
    public void deleteGroupPermissions(PersonGroup group) {
        groupPermissionRepository.deleteGroupPermissions(group);
    }

    @Transactional
    public void deleteRolePermission(Role role, Permission permission) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(new RolePermissionId(role, permission));
        rolePermissionRepository.delete(rolePermission);
    }

    @Transactional
    public void deleteGroupPermission(PersonGroup group, Permission permission) {
        GroupPermission groupPermission = new GroupPermission();
        groupPermission.setId(new GroupPermissionId(group, permission));
        groupPermissionRepository.delete(groupPermission);
    }

    @Transactional(readOnly = true)
    public List<LoginRole> getLoginRoles(Login login) {
        return loginRoleRepository.findLoginRoles(login);
    }

    @Transactional(readOnly = true)
    public List<LoginGroup> getLoginGroups(Login login) {
        return loginGroupRepository.findLoginGroups(login);
    }

    @Transactional(readOnly = true)
    public List<LoginRole> getLoginRoles(Integer loginId) {
        return loginRoleRepository.findLoginRoles(loginRepository.findOne(loginId));
    }

    @Transactional(readOnly = true)
    public List<LoginGroup> getLoginGroups(Integer loginId) {
        return loginGroupRepository.findLoginGroups(loginRepository.findOne(loginId));
    }

    @Transactional(readOnly = true)
    public PersonGroup getGroupById(Integer groupId) {
        return personGroupRepository.findOne(groupId);
    }

    @Transactional
    public Login updateLogin(Login login) {
        if (!login.getCheckedLicence()) {
            this.checklicence(login);
        }

        checkNotNull(login);
        checkNotNull(login.getId());
        String pwd = login.getPassword();

        if (login.getFlag() != null && login.getFlag() == true) {

            Login loginPasswordCheck = loginRepository.findOne(login.getId());
            if (loginPasswordCheck != null) {
                if (!loginPasswordCheck.getPassword().equals(login.getPassword())) {
                    String encryptedPwd = BCrypt.hashpw(login.getPassword(), BCrypt.gensalt());
                    login.setPassword(encryptedPwd);
                }
            }
        }
        Login existingLogin = loginRepository.findByLoginName(login.getLoginName());
        if (existingLogin != null && !existingLogin.getId().equals(login.getId())) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("login_name_already_exists", null, "Login Name already exists",
                            LocaleContextHolder.getLocale()));
        }
        login.setLocked(false);
        login = loginRepository.save(login);

        UserAttempts userAttempts = userAttemptRepository.findByUserName(login.getLoginName());
        if (userAttempts != null) {
            userAttemptRepository.delete(userAttempts.getId());
        }
        return login;
    }

    @Transactional
    public LoginRole addLoginRole(Login login, Role role) {
        LoginRole loginRole = new LoginRole();
        loginRole.setId(new LoginRoleId(login, role));

        return loginRoleRepository.save(loginRole);
    }

    @Transactional
    public LoginGroup addLoginGroup(Login login, PersonGroup group) {
        LoginGroup loginGroup = new LoginGroup();
        loginGroup.setId(new LoginGroupId(login, group));

        return loginGroupRepository.save(loginGroup);
    }

    @Transactional
    public List<LoginRole> setLoginRoles(Login login, List<Role> roles) {

        List<LoginRole> currentLoginRoles = getLoginRoles(login);

        List<LoginRole> loginRoles = new ArrayList<>();
        for (Role role : roles) {
            LoginRole loginRole = new LoginRole();
            loginRole.setId(new LoginRoleId(login, role));
            loginRoles.add(loginRole);
        }

        Map<String, LoginRole> map = new HashMap<>();
        for (LoginRole lr : loginRoles) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getRole().getId();
            map.put(key, lr);
        }

        List<LoginRole> deleted = new ArrayList<>();
        for (LoginRole lr : currentLoginRoles) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getRole().getId();
            if (map.get(key) == null) {
                deleted.add(lr);
            }
        }

        currentLoginRoles.removeAll(deleted);
        currentLoginRoles.addAll(loginRoles);

        loginRoleRepository.delete(deleted);

        return loginRoleRepository.save(loginRoles);
    }

    @Transactional
    public List<LoginGroup> setLoginGroups(Login login, List<PersonGroup> groups) {

        List<LoginGroup> currentLoginGroups = getLoginGroups(login);

        List<LoginGroup> loginGroups = new ArrayList<>();
        for (PersonGroup group : groups) {
            LoginGroup loginGroup = new LoginGroup();
            loginGroup.setId(new LoginGroupId(login, group));
            loginGroups.add(loginGroup);
        }

        Map<String, LoginGroup> map = new HashMap<>();
        for (LoginGroup lr : loginGroups) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getGroup().getGroupId();
            map.put(key, lr);
        }

        List<LoginGroup> deleted = new ArrayList<>();
        for (LoginGroup lr : currentLoginGroups) {
            String key = lr.getId().getLogin().getId() + "-" + lr.getId().getGroup().getGroupId();
            if (map.get(key) == null) {
                deleted.add(lr);
            }
        }

        currentLoginGroups.removeAll(deleted);
        currentLoginGroups.addAll(loginGroups);

        loginGroupRepository.delete(deleted);

        return loginGroupRepository.save(loginGroups);
    }

    @Transactional
    public List<LoginRole> setLoginRoles(Integer loginId, List<Role> roles) {
        Login login = loginRepository.findOne(loginId);
        return setLoginRoles(login, roles);
    }

    @Transactional
    public List<LoginGroup> setLoginGroups(Integer loginId, List<PersonGroup> groups) {
        Login login = loginRepository.findOne(loginId);
        return setLoginGroups(login, groups);
    }

    @Transactional(readOnly = true)
    public String getLoginName(Integer personId) {
        Login login = loginRepository.findByPersonId(personId);
        return login.getLoginName();
    }

    @Transactional
    public void deleteLoginRoles(Login login) {
        loginRoleRepository.deleteAllByLogin(login);
    }

    @Transactional
    public void deleteLoginRole(Login login, Role role) {
        LoginRole loginRole = new LoginRole();
        loginRole.setId(new LoginRoleId(login, role));
        loginRoleRepository.delete(loginRole);
    }

    @Transactional(readOnly = true)
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @PostConstruct
    public void initialize() {

    }

    @Transactional(readOnly = true)
    public Login getLogin(String loginName) {
        if (loginName.indexOf("@") != -1) {
            String[] arr = loginName.split("@");

            if (arr.length != 2) {
                throw new CassiniException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.GENERAL,
                        messageSource.getMessage("incorrect_username", null, "Incorrect username",
                                LocaleContextHolder.getLocale()));
            }

            String user = arr[0].trim();
            String tenantId = arr[1].trim();

            if (!TenantManager.get().isValidTenant(tenantId)) {
                throw new CassiniException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.GENERAL, messageSource
                        .getMessage("incorrect_username", null, "Incorrect username", LocaleContextHolder.getLocale()));
            }

            TenantManager.get().setTenantId(tenantId);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            request.getSession().setAttribute("CASSINI_TENANT_IDENTIFIER", tenantId);

            return getLoginByLoginName(user);
        } else {
            throw new CassiniException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.GENERAL,
                    messageSource.getMessage("incorrect_username", null, "Incorrect username",
                            LocaleContextHolder.getLocale()));
        }

    }

    @Transactional
    public OneTimePassword resetPassword(Login login) {
        // First expire and invalidate all old OTPs
        List<OneTimePassword> list = oneTimePasswordRepository.findByLoginAndVerifiedFalse(login.getId());
        list.forEach(o -> o.setVerified(Boolean.TRUE));
        oneTimePasswordRepository.save(list);

        OneTimePassword otp = saveOTP(login.getId());

        sendOtp(login, otp, "forgot", null);

        return otp;
    }

    @Transactional(readOnly = false)
    public void sendEmailVerifyPassword(Integer personId) {
        // First expire and invalidate all old OTPs
        Login login = loginRepository.findByPersonId(personId);

        OneTimePassword otp = saveOTP(login.getId());
        sendOtp(login, otp, "verify", 30);
    }

    @Transactional(readOnly = false)
    public OneTimePassword saveOTP(Integer login) {
        OneTimePassword otp = new OneTimePassword();
        otp.setLogin(login);
        otp.setExpires(DateUtils.addMinutes(new Date(), 30));
        otp.setOtp(Utils.OTP());
        otp = oneTimePasswordRepository.save(otp);
        return otp;
    }

    @Transactional
    public void resetTwoFactorAuthenticationPassword(Integer loginId) {
        // First expire and invalidate all old OTPs
        Login login = loginRepository.findOne(loginId);
        Preference twoFactorAuthentication = preferenceRepository
                .findByPreferenceKey("TWO_FACTOR_AUTHENTICATION_ENABLED");

        Integer expiry = 15;
        OneTimePassword otp = new OneTimePassword();
        otp.setLogin(login.getId());
        if (twoFactorAuthentication != null && twoFactorAuthentication.getIntegerValue() != null) {
            otp.setExpires(DateUtils.addMinutes(new Date(), twoFactorAuthentication.getIntegerValue()));
            expiry = twoFactorAuthentication.getIntegerValue();
        } else {
            otp.setExpires(DateUtils.addMinutes(new Date(), 30));
        }
        otp.setOtp(Utils.OTP());
        otp = oneTimePasswordRepository.save(otp);

        sendOtp(login, otp, "twoFactor", expiry);
    }

    @Transactional
    public Integer verifyOtp(String loginName, String otp) {
        Login login = getLogin(loginName);
        if (login == null) {
            throw new CassiniException(messageSource.getMessage("user_not_exist", null, "User name does not exist",
                    LocaleContextHolder.getLocale()));
        }

        OneTimePassword otpObject = oneTimePasswordRepository.findByLoginAndOtp(login.getId(), otp);
        if (otpObject == null) {
            throw new CassiniException(messageSource.getMessage("incorrect_one_time_passcode", null,
                    "Incorrect one time passcode", LocaleContextHolder.getLocale()));
        }

        if (otpObject.getVerified()) {
            throw new CassiniException(messageSource.getMessage("entered_one_time_passcode_already_used", null,
                    "Entered one time passcode already used", LocaleContextHolder.getLocale()));
        }

        Date now = new Date();

        if (now.after(otpObject.getExpires())) {
            throw new CassiniException(messageSource.getMessage("one_time_passcode_expired", null,
                    "One time passcode expired", LocaleContextHolder.getLocale()));
        }

        otpObject.setVerified(Boolean.TRUE);
        return oneTimePasswordRepository.save(otpObject).getId();
    }

    @Transactional
    public Session verifyTwoFactorAuthentication(Integer loginId, String passcode) {

        OneTimePassword otpObject = oneTimePasswordRepository.findByLoginAndOtp(loginId, passcode);
        if (otpObject == null) {
            throw new CassiniException(messageSource.getMessage("incorrect_one_time_passcode", null,
                    "Incorrect one time passcode", LocaleContextHolder.getLocale()));
        }
        if (otpObject.getVerified()) {
            throw new CassiniException(messageSource.getMessage("entered_one_time_passcode_already_used", null,
                    "Entered one time passcode already used", LocaleContextHolder.getLocale()));
        }
        Date now = new Date();
        if (now.after(otpObject.getExpires())) {
            throw new CassiniException(messageSource.getMessage("one_time_passcode_expired", null,
                    "One time passcode expired", LocaleContextHolder.getLocale()));
        }

        deleteOTPs(loginId);
        sessionWrapper.getSession().setTwoFactorChecked(true);
        return sessionWrapper.getSession();
    }

    @Transactional(readOnly = false)
    public void deleteOTPs(Integer login) {
        List<OneTimePassword> list = oneTimePasswordRepository.findByLogin(login);
        oneTimePasswordRepository.delete(list);
    }

    @Transactional(readOnly = false)
    public Person verifyPersonEmail(Integer personId, String passcode) {
        Login login = loginRepository.findByPersonId(personId);
        Person person = personRepository.findOne(personId);
        OneTimePassword otpObject = oneTimePasswordRepository.findByLoginAndOtp(login.getId(), passcode);
        if (otpObject == null) {
            throw new CassiniException(messageSource.getMessage("incorrect_one_time_passcode", null,
                    "Incorrect one time passcode", LocaleContextHolder.getLocale()));
        }

        if (otpObject.getVerified()) {
            throw new CassiniException(messageSource.getMessage("entered_one_time_passcode_already_used", null,
                    "Entered one time passcode already used", LocaleContextHolder.getLocale()));
        }

        Date now = new Date();

        if (now.after(otpObject.getExpires())) {
            throw new CassiniException(messageSource.getMessage("one_time_passcode_expired", null,
                    "One time passcode expired", LocaleContextHolder.getLocale()));
        }

        person.setEmailVerified(true);
        person = personRepository.save(person);
        sessionWrapper.getSession().getLogin().getPerson().setEmailVerified(true);
        return person;
    }

    private void sendOtp(Login login, OneTimePassword otp, String passcodeType, Integer expiry) {
        String email = login.getPerson().getEmail();
        if (email == null) {
            throw new CassiniException(
                    messageSource.getMessage("no_email_specified_for_your_account_please_contact_your_administrator",
                            null, "No email specified for your account. Please contact your administrator.",
                            LocaleContextHolder.getLocale()));
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));

        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("cssIncludes", getCss(host));
        model.put("otp", otp);
        model.put("expiry", expiry);

        Mail mail = new Mail();
        mail.setMailTo(email);

        String appName = environment.getProperty("app.name");
        if (appName == null) {
            appName = "Cassini";
        }

        if (passcodeType.equals("forgot")) {
            model.put("passwordType", "forgot");
            mail.setMailSubject(appName + " - Reset Password");
        } else if (passcodeType.equals("twoFactor")) {
            model.put("passwordType", "twoFactor");
            mail.setMailSubject(appName + " - Passcode");
        } else if (passcodeType.equals("verify")) {
            model.put("passwordType", "verify");
            mail.setMailSubject(appName + " - Passcode");
        }
        mail.setTemplatePath("email/security/resetPassword.html");
        mail.setModel(model);

        mailService.sendEmail(mail);
    }

    @Transactional
    public LoginDTO setNewPassword(String loginName, Integer otp, String newPassword) {
        LoginDTO loginDTO = new LoginDTO();
        Login login = getLogin(loginName);
        if (login == null) {
            throw new CassiniException(messageSource.getMessage("login_name_does_not_exist", null,
                    "Login name does not exist", LocaleContextHolder.getLocale()));
        }

        OneTimePassword o = oneTimePasswordRepository.findOne(otp);
        if (o == null) {
            throw new CassiniException(messageSource.getMessage("invalid_one_time_passcode", null,
                    "Invalid one time passcode", LocaleContextHolder.getLocale()));
        }

        if (!o.getVerified()) {
            throw new CassiniException(
                    messageSource.getMessage("one_time_passcode_hasnot_been_verified_yet", null,
                            "One time passcode hasn't been verified yet", LocaleContextHolder.getLocale()));
        }

        if (o.getApplied()) {
            throw new CassiniException(messageSource.getMessage(
                    "this_one_time_passcode_already_was_applied_to_an_earlier_password_reset_request",
                    null, "This one time passcode already was applied to an earlier password reset request",
                    LocaleContextHolder.getLocale()));
        }

        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        login.setPassword(hashed);
        login.setLocked(false);
        loginRepository.save(login);
        UserAttempts userAttempts = userAttemptRepository.findByUserName(login.getLoginName());
        if (userAttempts != null) {
            userAttemptRepository.delete(userAttempts.getId());
        }

        o.setApplied(Boolean.TRUE);
        oneTimePasswordRepository.save(o);
        return loginDTO;
    }

    private String getCss(String host) {
        String css = "";

        String[] urls = {
                host + "/app/assets/bower_components/bootstrap/dist/css/bootstrap.min.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/bootstrap-override.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/style.default.css",
                host + "/app/assets/bower_components/cassini-platform/template/css/style.katniss.css"
        };

        try {
            for (String url : urls) {
                css += IOUtils.toString(new URL(url), (Charset) null);
                css += "\n\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return css;
    }

    @Transactional(readOnly = true)
    public List<Login> findMultipleLogins(List<Integer> ids) {
        return loginRepository.findByPersonIn(ids);
    }

    @Transactional
    public Login passwordReset(LoginDTO loginDTO) {
        Login login = loginRepository.findOne(loginDTO.getLogin().getId());

        String pwd = loginDTO.getNewPassword();
        if (pwd == null || pwd.length() <= 0) {
            pwd = RandomString.get().getAlphaNumeric(8);
        }
        // Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        login.setPassword(hashed);
        login.setLocked(false);
        Login login1 = loginRepository.save(login);
        UserAttempts userAttempts = userAttemptRepository.findByUserName(login1.getLoginName());
        if (userAttempts != null) {
            userAttemptRepository.delete(userAttempts.getId());
        }
        sendLoginCreatedEmail(login, pwd, "RESET");
        return login1;
    }

    private void sendEmail(LoginDTO loginDTO, String password) {
        String email = loginDTO.getLogin().getPerson().getEmail();
        if (email == null) {
            throw new CassiniException(
                    messageSource.getMessage("no_email_specified_for_your_account_please_contact_your_administrator",
                            null, "No email specified for your account. Please contact your administrator.",
                            LocaleContextHolder.getLocale()));
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));

        Map<String, Object> model = new HashMap<>();
        model.put("host", host);
        model.put("cssIncludes", getCss(host));
        model.put("password", password);

        Mail mail = new Mail();
        mail.setMailTo(email);

        String appName = environment.getProperty("app.name");
        if (appName == null) {
            appName = "Cassini";
        }

        mail.setMailSubject(appName + " - Reset Password");
        mail.setTemplatePath("email/security/passwordReset.html");
        mail.setModel(model);

        mailService.sendEmail(mail);
    }

    @Transactional(readOnly = true)
    public UserPreferences getLoginPreferences(Integer loginId) {
        UserPreferences userPreference = userPreferencesRepository.findByLogin(loginId);
        if (userPreference == null) {
            userPreference = new UserPreferences();
            userPreference.setId(null);
            userPreference.setLogin(loginId);
            userPreference.setUserTheme(null);
            userPreference.setUserDateFormat(null);
            userPreference.setPreferredPage(null);
        }
        return userPreference;
    }

    @Transactional
    public UserPreferences updateUserPreferences(UserPreferences userPreferences) {

        userPreferences = userPreferencesRepository.save(userPreferences);

        return userPreferences;
    }

    @Transactional
    public UserPreferences savePreferredPage(UserPreferences userPreferences) {
        return userPreferencesRepository.save(userPreferences);
    }

    @Transactional(readOnly = true)
    public Boolean checkIfPortalAccountSetup() {
        PortalAccount portalAccount = portalAccountService.getPortalAccount(null);
        if (portalAccount != null) {
            validatePortalInfo(portalAccount);
        }
        return true;
    }

    @Transactional
    public Boolean setupPortalAccount(PortalAccount portalAccount) {
        Authentication auth = new Authentication(portalAccount.getEmail(), portalAccount.getPassword());
        CustomerSession session = portalService.login(auth);

        validatePortalInfo(portalAccount);

        portalAccountService.createPortalAccount(portalAccount);
        return Boolean.TRUE;
    }

    private Boolean validatePortalInfo(PortalAccount portalAccount) {
        Authentication auth = new Authentication(portalAccount.getEmail(), portalAccount.getPassword());
        CustomerSession session = portalService.login(auth);

        if (session != null) {
            CustomerAuthKey authKey = portalService.getCustomerAuthKey(session.getLogin().getCustomer().getId());
            if (authKey != null) {
                if (!authKey.getAuthKey().equals(portalAccount.getAuthKey())) {
                    throw new PortalException("Invalid auth key");
                }
            }
        }

        return true;
    }

    @Transactional
    public Login resetUserPassword(Integer loginId) {
        Person person = personRepository.findOne(loginId);
        Login login = loginRepository.findByPersonId(person.getId());
        String pwd = RandomString.get().getAlphaNumeric(8);
        // Hash it
        String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
        login.setPassword(hashed);
        login.setLocked(false);
        login = this.loginRepository.save(login);
        UserAttempts userAttempts = this.userAttemptRepository.findByUserName(login.getLoginName());
        if (userAttempts != null) {
            this.userAttemptRepository.delete(userAttempts.getId());
        }
        sendLoginCreatedEmail(login, pwd, "RESET");

        return login;
    }

    @Transactional(readOnly = true)
    public UserPreferences getUserPreference(Integer loginId) {
        UserPreferences userPreferences = userPreferencesRepository.findByLogin(loginId);
        return userPreferences;
    }

    public Session getCurrentSession(HttpServletResponse response) {
        Session session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null) {
            if ((session.getHasTwoFactorAuthentication() && !session.getTwoFactorChecked())
                    || !session.getLogin().getPerson().getEmailVerified()) {
                return null;
            } else {
                Login login = getLoginByLoginName(session.getLogin().getLoginName());
                loadLoginGroups(login);
                login.setPermissions(getPermissionsForLoginUser(login));
                if (login.getExternal()) {
                    login.setGroupSecurityPermissions(loadExternalPermission(login));
                    login.getGroupSecurityPermissions().addAll(loadPersonSecurityPermissions(login));
                    login.getLoginSecurityPermissions()
                            .addAll(loginSecurityPermissionRepository.findByPerson(login.getPerson()));
                } else {
                    login.setGroupSecurityPermissions(loadPersonSecurityPermissions(login));
                  //  login.getGroupSecurityPermissions().addAll(PMPermissions(login));
                    login.getLoginSecurityPermissions()
                            .addAll(loginSecurityPermissionRepository.findByPerson(login.getPerson()));
                }
                List<Permission> permissions = loadPersonPermissions();
                if (permissions.size() > 0) {
                    login.getGroupPermissions().addAll(permissions);
                }
                session.setLogin(login);
                response.addHeader(EvaluationConstraints.JWTTOKEN, session.getAccessToken());
                response.addHeader(EvaluationConstraints.REFRESHTOKEN, session.getRefreshToken());
                response.addHeader("Access-Control-Allow-Headers",
                        "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
                UserPreferences userPreference = userPreferencesRepository.findByLogin(login.getId());
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.DATE.FORMAT");
                if (userPreference != null && userPreference.getUserDateFormat() != null) {
                    session.setPreferredDateFormat(userPreference.getUserDateFormat());
                    session.setPreferredShortDateFormat(userPreference.getShortDateFormat());
                } else if (preference != null && preference.getStringValue() != null) {
                    session.setPreferredDateFormat(preference.getStringValue());
                    session.setPreferredShortDateFormat(preference.getStringValue().substring(0, 10));
                }
                getSessionWrapper().setSession(session);
                session.getLogin().setGroups(
                        personGroupService.getPersonGroupPermissions(session.getLogin().getPerson().getId()));
            }
        }
        return session;
    }

    public Session getMobileCurrentSession(HttpServletResponse response) {
        Session session = getSessionWrapper().getSession();
        if (session != null && session.getLogin() != null) {
            Login login = getLoginByLoginName(session.getLogin().getLoginName());
            session.setLogin(login);
            response.addHeader(EvaluationConstraints.JWTTOKEN, session.getAccessToken());
            response.addHeader(EvaluationConstraints.REFRESHTOKEN, session.getRefreshToken());
            response.addHeader("Access-Control-Allow-Headers",
                    "Content-Type, Access-Control-Allow-Headers, Authorization, X-Refresh-Token, X-Jwt-Token");
            UserPreferences userPreference = getUserPreference(login.getId());
            if (userPreference != null) {
                session.setPreferredDateFormat(userPreference.getUserDateFormat());
                session.setPreferredShortDateFormat(userPreference.getShortDateFormat());
            }
            getSessionWrapper().setSession(session);
        }
        return session;
    }

    @Transactional(readOnly = true)
    public SecurityPermission getSecurityPermission(String objectType, String subType, String privilage,
            String attribute, String attributeGroup, String critiera, Boolean isExternal, PrivilegeType privilegeType) {
        SecurityPermission securityPermission = new SecurityPermission();
        securityPermission.setObjectType(objectType);
        securityPermission.setSubType(subType);
        securityPermission.setPrivilege(privilage);
        securityPermission.setAttribute(attribute);
        securityPermission.setAttributeGroup(attributeGroup);
        securityPermission.setCriteria(critiera);
        securityPermission.setIsExternal(isExternal);
        securityPermission.setPrivilegeType(privilegeType);
        return securityPermission;
    }

    @Transactional(readOnly = true)
    public SecurityPermission getPersonSecurityPermission(String objectType, String subType, String privilage,
            String attribute, String attributeGroup, String critiera, Boolean isExternal, PrivilegeType privilegeType,
            String personId) {
        SecurityPermission securityPermission = new SecurityPermission();
        securityPermission.setObjectType(objectType);
        securityPermission.setSubType(subType);
        securityPermission.setPrivilege(privilage);
        securityPermission.setAttribute(attribute);
        securityPermission.setAttributeGroup(attributeGroup);
        securityPermission.setCriteria(critiera);
        securityPermission.setIsExternal(isExternal);
        securityPermission.setPerson(personId);
        securityPermission.setPrivilegeType(privilegeType);
        return securityPermission;
    }

    @Transactional(readOnly = true)
    public SecurityPermissionDto getSecurityPermissionsFromGroupId(Integer groupId) {
        SecurityPermissionDto securityPermissionDto = new SecurityPermissionDto();
        securityPermissionDto
                .setSecurityPermissions(groupSecurityPermissionRepository.getSecurityPermissionsByGroupId(groupId));
        securityPermissionDto
                .setObjectTypes(groupSecurityPermissionRepository.getSecurityPermissionsObjectTypeByGroupId(groupId));
        securityPermissionDto
                .setSubTypes(groupSecurityPermissionRepository.getSecurityPermissionsSubTypeByGroupId(groupId));
        return securityPermissionDto;
    }

    @Transactional(readOnly = true)
    public Login getUserCounts() {
        Login login = new Login();
        login.setActiveUsers(loginRepository.findByIsActiveAndExternal(true, false).size());
        login.setInActiveUsers(loginRepository.findByIsActiveAndExternal(false, false).size());
        login.setActiveExternalUsers(loginRepository.findByIsActiveAndExternal(true, true).size());
        login.setInActiveExternalUsers(loginRepository.findByIsActiveAndExternal(false, true).size());
        login.setTotalUsers(loginRepository.findAll().size());
        return login;
    }

    public void checkIpAddress(HttpServletRequest request) throws JsonProcessingException {
        Set<String> whitelist = new HashSet<>();
        String userIp = request.getRemoteAddr();
        List<AppDetails> appDetailses = appDetailsRepository.findAll();
        if (appDetailses.stream().filter(appDetail -> appDetail.getOptionName().equals("IP_ADDRESS")).findFirst()
                .isPresent()) {
            AppDetails appDetails = appDetailses.stream()
                    .filter(appDetail -> appDetail.getOptionName().equals("IP_ADDRESS")).findFirst().get();
            ObjectMapper objectMapper = new ObjectMapper();
            if (appDetails.getValue() != null) {
                List<IpAddressDto> ipAddressDtos = Arrays
                        .asList(objectMapper.readValue(appDetails.getValue(), IpAddressDto[].class));
                if (ipAddressDtos.get(0).isAddressActive()) {
                    whitelist.addAll(ipAddressDtos.stream().map(IpAddressDto::getAddress).collect(Collectors.toList()));
                    if (!whitelist.contains(userIp)) {
                        if (getSessionWrapper().getSession() != null) {
                            SessionManager.get().removeSession(getSessionWrapper().getSession());
                        }
                        request.getSession().invalidate();
                        throw new BadCredentialsException("You do not have access from this IP address.");
                    }
                }
            }
        }
    }

}
