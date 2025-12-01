package com.cassinisys.platform.service.security;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ErrorCodes;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.dto.LicenseDto;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.util.AES;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LicenseService {
    public static final String LICENSE_PREFERENCE_KEY = "SYSTEM.CASSINI.LICENSE";
    public static final String SECRET_KEY = "tYgCqskZPkKE8LJT2EIyiPuYzCnefpru";

    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Environment environment;
    private Integer noOfDays;
    @Autowired
    private LoginRepository loginRepository;

    public LicenseDto getLicense() {
        LicenseDto license = null;
        String tenant = TenantManager.get().getTenantId();
        Preference preference = preferenceRepository.findByPreferenceKey(LICENSE_PREFERENCE_KEY);
        if (preference != null) {
            String json = preference.getStringValue();
            try {
                json = AES.decrypt(json, SECRET_KEY);
                if (json == null || json.equals("")) {
                    throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                            messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
                }
                license = objectMapper.readValue(json, LicenseDto.class);
                if (license.getCustomer().equalsIgnoreCase(tenant)) {
                    license.setExpirationDate(license.getExpiration());
                    license.setGracePeriod(getGracePeriod());
                    return license;
                }
            } catch (JsonProcessingException e) {
            }
        }

        return license;
    }

    public Boolean saveLicense(String encLicense) {
        try {
            String decrypted = AES.decrypt(encLicense, SECRET_KEY);
            if (decrypted == null || decrypted.equals("")) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
            }
            LicenseDto licenseDto = objectMapper.readValue(decrypted, LicenseDto.class);
            String tenant = TenantManager.get().getTenantId();
            if (!licenseDto.getCustomer().equalsIgnoreCase(tenant)) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("customer_id_not_match", null, "Invalid licence key. Please enter valid licence key", LocaleContextHolder.getLocale()));
            }
            if (licenseDto.getExpiration().before(getGlobalDate())) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("license_key_date_expired", null, "Licence Key date expired. Please enter valid licence key", LocaleContextHolder.getLocale()));
            }

            Preference preference = preferenceRepository.findByPreferenceKey(LICENSE_PREFERENCE_KEY);
            if (preference == null) {
                preference = new Preference();
                preference.setPreferenceKey(LICENSE_PREFERENCE_KEY);
                preference.setContext("SYSTEM");
            }
            preference.setStringValue(encLicense);
            preferenceRepository.save(preference);
            return true;
        } catch (JsonProcessingException e) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
        }
    }

    public LicenseDto isLicenseValid() {
        String tenant = TenantManager.get().getTenantId();
        Preference preference = preferenceRepository.findByPreferenceKey(LICENSE_PREFERENCE_KEY);
        LicenseDto license;
        if (preference != null) {
            String json = preference.getStringValue();
            try {
                if (json.trim().equals("") || json.equals(null)) {
                    throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                            messageSource.getMessage("no_license_found", null, "No license key found", LocaleContextHolder.getLocale()));
                }
                json = AES.decrypt(json, SECRET_KEY);
                if (json == null || json.equals("")) {
                    throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                            messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
                }
                license = (LicenseDto) this.objectMapper.readValue(json, LicenseDto.class);
                license.setValid(true);
                if (!license.getCustomer().equalsIgnoreCase(tenant)) {
                    throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                            messageSource.getMessage("invalid_license_customer_id_not_match", null, "Invalid license. Please enter valid licence key", LocaleContextHolder.getLocale()));
                }

                if (license.getExpiration().before(getGlobalDate())) {
                    if (getGracePeriod() > 0 && addGracePeriod(license.getExpiration()).before(getGlobalDate())) {
                        throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                                messageSource.getMessage("license_expired_with_grace_period", null, "License Expired with Grace Period", LocaleContextHolder.getLocale()));
                    } else if (getGracePeriod() > 0) {
                        noOfDays = getDaysDifference(addGracePeriod(license.getExpiration()), getGlobalDate());
                        license.setValid(false);
                    } else {
                        throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                                messageSource.getMessage("license_expired", null, "License Expired", LocaleContextHolder.getLocale()));
                    }
                }

                Integer activeUsers = loginRepository.findIsActiveAndExternalLoginsCount(true, false);

                if (activeUsers > license.getLicenses()) {
                    license.setLicensesFraud(true);
                    /*throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                            messageSource.getMessage("license_key_active_license_count", null, "License Key Does't have required active licenses, Please enter valid license.", LocaleContextHolder.getLocale()));*/
                }
            } catch (JsonProcessingException e) {
                throw new CassiniException(e.getMessage());
            }
        } else {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("no_license_expired", null, "No license key found", LocaleContextHolder.getLocale()));
        }
        return license;
    }

    public Integer getNoOfDays() {
        return noOfDays;
    }

    private Date getGlobalDate() {
       /* try {
            String TIME_SERVER = "time-a.nist.gov";
            NTPUDPClient ntpudpClient = new NTPUDPClient();
            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            TimeInfo timeInfo = ntpudpClient.getTime(inetAddress);
            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            Date dt = new Date(returnTime);
            return dt;
        } catch (IOException e) {
            throw new CassiniException(e.getMessage());
        }*/
        return new Date();
    }

    private Date addGracePeriod(Date expiration) {
        Integer gracePeriod = getGracePeriod();
        long ltime = expiration.getTime() + (gracePeriod + 1) * 24 * 60 * 60 * 1000;
        return new Date(ltime);
    }

    private Integer getGracePeriod() {
        String value = this.environment.getProperty("license.grace.period");
        if (value == null || value.equals("")) {
            return 0;
        } else {
            return Integer.parseInt(value);
        }
    }

    private Integer getDaysDifference(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /*
    * Checking Active users are more than Licenses
    * */
    public LicenseDto checkingActiveUserLicenses(LicenseDto licenseDto) {
        try {
            String decrypted = AES.decrypt(licenseDto.getLicenseKey(), SECRET_KEY);
            if (decrypted == null || decrypted.equals("")) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
            }
            LicenseDto licenseDtoObject = objectMapper.readValue(decrypted, LicenseDto.class);
            String tenant = TenantManager.get().getTenantId();
            if (!licenseDtoObject.getCustomer().equalsIgnoreCase(tenant)) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("customer_id_not_match", null, "Invalid license key. Please enter valid licence key", LocaleContextHolder.getLocale()));
            }
            if (licenseDtoObject.getExpiration().before(getGlobalDate())) {
                throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                        messageSource.getMessage("license_key_date_expired", null, "Licence Key date expired. Please enter valid licence key", LocaleContextHolder.getLocale()));
            }
            Integer activeUsers = loginRepository.findIsActiveAndExternalLoginsCount(true, false);
            licenseDto.setLicenses(licenseDtoObject.getLicenses());
            if (activeUsers > licenseDtoObject.getLicenses()) {
                licenseDto.setValid(true);
            }
         } catch (JsonProcessingException e) {
            throw new CassiniException(HttpStatus.FORBIDDEN, ErrorCodes.UNAUTHORIZED,
                    messageSource.getMessage("no_valid_license_key_found", null, "No Valid license key found", LocaleContextHolder.getLocale()));
        }

        return licenseDto;
    }

}
