package com.cassinisys.platform.service.core;

import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.config.WebAppConfig;
import com.cassinisys.platform.model.core.AppDetails;
import com.cassinisys.platform.model.core.SystemInformation;
import com.cassinisys.platform.repo.core.AppDetailsRepository;
import com.cassinisys.platform.util.DecryptSerializer;
import com.cassinisys.platform.util.EncryptDeserializer;
import com.cassinisys.platform.util.TimePresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Nageshreddy on 13-11-2019.
 */
@Service
public class AppDetailsService {

    @Autowired
    private AppDetailsRepository appDetailsRepository;
    @Autowired
    private DecryptSerializer decryptSerializer;
    @Autowired
    private EncryptDeserializer encryptDeserializer;
    private String updays;
    private String uphours;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private Environment environment;

    public TimePresentation getTimePresentation() {
        return TimePresentation.GetById(1);
    }

    @Transactional (readOnly = true)
    public List<AppDetails> getAppDetails() {
        List<AppDetails> detailsList = appDetailsRepository.findAll();
        for (AppDetails details : detailsList) {
            if (details.getOptionKey() == 12) {
                String value = decryptSerializer.decrypt(details.getValue());
                details.setValue(value);
            }
            if (details.getOptionKey() == 1) {
                String days = messageSource.getMessage("days", null, "Days", LocaleContextHolder.getLocale());
                String hours = messageSource.getMessage("hours", null, "Hours", LocaleContextHolder.getLocale());
                long startupTimeMilliSeconds = WebAppConfig.systemStartupTime;
                long systemTypeLong = System.currentTimeMillis() - startupTimeMilliSeconds;
                long secondsL = systemTypeLong / 1000;
                long minutesL = secondsL / 60;
                long hoursL = minutesL / 60;
                long daysL = hoursL / 24;
                this.updays = Long.toString(daysL);
                this.uphours = (hoursL % 24) + this.getTimePresentation().getDelimiter() + String.format("%02d", (minutesL % 60));
                details.setSystemUpTime(this.updays + " " + days + " " + this.uphours + " " + hours);
            }
        }

        return detailsList;
    }


    @Transactional (readOnly = true)
    public SystemInformation getSystemInformation() {
        SystemInformation systemInformation = new SystemInformation();
        String tenantId = TenantManager.get().getTenantId();
        Object[] object = appDetailsRepository.getSystemInformation(tenantId);
        Object[] obj = (Object[]) object[0];

        Long totalSpace = convertToLong(obj[0]);
        Long availSpace = convertToLong(obj[1]);
        Long usedSpace = convertToLong(obj[2]);
        double a = totalSpace - usedSpace;
        double b = a / totalSpace;
        double availableSpaceInPercentage = b * 100;
        double d = usedSpace.doubleValue() / totalSpace.doubleValue();
        double usedSpacePercentage = d * 100;
        String asp = String.format("%.2f", availableSpaceInPercentage) + "%";
        String usp = String.format("%.2f", usedSpacePercentage) + "%";
        systemInformation.setTotalStoragePercentage("100%");
        systemInformation.setAvailableStoragePercentage(asp);
        systemInformation.setUsedStoragePercentage(usp);
        systemInformation.setTotalStorage(totalSpace);
        systemInformation.setAvailableStorage(availSpace);
        systemInformation.setUsedStorage(usedSpace);

        String totSpace = getSize(totalSpace);
        String avSpace = getSize(availSpace);
        String usSpace = getSize(usedSpace);
        systemInformation.setTotalStorageReadableFormat(totSpace);
        systemInformation.setAvailableStorageReadableFormat(avSpace);
        systemInformation.setUsedStorageReadableFormat(usSpace);


        return systemInformation;
    }

    public Long convertToLong(Object o) {
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;

    }


    public String getSize(long size) {
        long n = 1000;
        String s = "";
        double kb = size / n;
        double mb = kb / n;
        double gb = mb / n;
        double tb = gb / n;
        if (size < n) {
            s = size + " Bytes";
        } else if (size >= n && size < (n * n)) {
            s = String.format("%.2f", kb) + " KB";
        } else if (size >= (n * n) && size < (n * n * n)) {
            s = String.format("%.2f", mb) + " MB";
        } else if (size >= (n * n * n) && size < (n * n * n * n)) {
            s = String.format("%.2f", gb) + " GB";
        } else if (size >= (n * n * n * n)) {
            s = String.format("%.2f", tb) + " TB";
        }
        return s;
    }

    @Transactional
    public AppDetails updateDetails(AppDetails appDetails) {
        return appDetailsRepository.save(appDetails);
    }

    public String saveIpAddress(String address) {
        AppDetails appDetails = appDetailsRepository.findByOptionName("IP_ADDRESS");
        if(address.equals("[]")) address = null;
        if(appDetails != null) {
            appDetails.setValue(address);
            appDetails.setModifiedDate(new Date());
            appDetails = appDetailsRepository.save(appDetails);
        } else {
            AppDetails appDetails1 = new AppDetails();
            appDetails1.setOptionName("IP_ADDRESS");
            appDetails1.setOptionKey(13);
            appDetails1.setValue(address);
            appDetails1.setCreatedDate(new Date());
            appDetails1.setModifiedDate(new Date());
            appDetails = appDetailsRepository.save(appDetails1);
        }
        return appDetails.getValue();
    }
}
