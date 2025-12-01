package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.InvalidLoginException;
import com.cassinisys.platform.model.common.DefaultValueDto;
import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by subramanyam on 22-04-2020.
 */
@Service
public class PreferenceService implements CrudService<Preference, Integer> {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private PersonGroupRepository personGroupRepository;

    @Override
    @Transactional
    public Preference create(Preference preference) {
        return preferenceRepository.save(preference);
    }

    @Override
    @Transactional
    public Preference update(Preference preference) {
        return preferenceRepository.save(preference);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        preferenceRepository.delete(id);
    }

    @Override
    public Preference get(Integer id) {
        return preferenceRepository.findOne(id);
    }

    @Override
    public List<Preference> getAll() {
        return preferenceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Preference getChangeApprovalPasswordByLogin(Integer loginId) {
        Preference preference = preferenceRepository.findByPreferenceKey(loginId + "_CHANGE_APPROVAL");
        return preference;
    }

    @Transactional
    public Preference createChangeApprovalPassword(Preference preference) {

        if (preference.getStringValue() != null && !preference.getStringValue().equals("")) {
            String pwd = preference.getStringValue();
            String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
            preference.setStringValue(hashed);
            preference = preferenceRepository.save(preference);
        } else {
            preference = preferenceRepository.save(preference);
        }
        return preference;
    }

    @Transactional
    public Preference updateChangeApprovalPassword(Integer preferenceId, Preference preference) {
        if (preference.getStringValue() != null && !preference.getStringValue().equals("")) {
            String pwd = preference.getStringValue();
            String hashed = BCrypt.hashpw(pwd, BCrypt.gensalt());
            preference.setStringValue(hashed);
            preference = preferenceRepository.save(preference);
        }
        return preference;
    }

    @Transactional(readOnly = true)
    public Preference getPreferenceByKey(String key) {
        Preference preference = preferenceRepository.findByPreferenceKey(key);
        return preference;
    }

    @Transactional(readOnly = true)
    public List<Preference> getPreferencesByContext(String context) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Preference> preferences = preferenceRepository.findByContextOrderByIdAsc(context);
        for (Preference preference : preferences) {
            if (preference.getJsonValue() != null) {
                try {
                    DefaultValueDto valueDto = objectMapper.readValue(preference.getJsonValue(), new TypeReference<DefaultValueDto>() {
                    });
                    preference.setDefaultValue(valueDto);
                    if (valueDto != null && valueDto.getTypeId() != null) {
                        if (valueDto.getType().equals("LOV")) {
                            Lov lov = lovRepository.findOne(valueDto.getTypeId());
                            if (lov != null) {
                                preference.setDefaultValueName(lov.getName());
                            }
                        } else if (valueDto.getType().equals("AUTONUMBER")) {
                            AutoNumber autoNumber = autoNumberRepository.findOne(valueDto.getTypeId());
                            if (autoNumber != null) {
                                preference.setDefaultValueName(autoNumber.getName());
                            }
                        } else if (valueDto.getType().equals("ROLE")) {
                            PersonGroup personGroup = personGroupRepository.findOne(valueDto.getTypeId());
                            if (personGroup != null) {
                                preference.setDefaultValueName(personGroup.getName());
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return preferences;
    }

    @Transactional(readOnly = true)
    public Preference checkPassword(Integer loginId, String password) {

        Preference preference = preferenceRepository.findByPreferenceKey(loginId + "_CHANGE_APPROVAL");

        if (!BCrypt.checkpw(password, preference.getStringValue())) {
            throw new InvalidLoginException(messageSource.getMessage("incorrect_password", null, "Incorrect password!", LocaleContextHolder.getLocale()));
        }

        return preference;
    }

    @Transactional
    public List<Preference> saveMultiplePreferences(List<Preference> preferences) {
        return preferenceRepository.save(preferences);
    }

    public Preference saveHolidays(String holidayList) {
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.HOLIDAY_LIST");
        if (preference != null) {
            preference.setJsonValue(holidayList);
            preference = create(preference);
        } else {
            Preference preference1 = new Preference();
            preference1.setContext("APPLICATION");
            preference1.setPreferenceKey("APPLICATION.HOLIDAY_LIST");
            preference1.setJsonValue(holidayList);
            preference = create(preference1);
        }
        return preference;
    }

    public Preference saveWorkingDays(Integer days) {
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.WORKING_DAYS");
        if (preference != null) {
            preference.setIntegerValue(days);
            preference = create(preference);
        } else {
            Preference preference1 = new Preference();
            preference1.setContext("APPLICATION");
            preference1.setPreferenceKey("APPLICATION.WORKING_DAYS");
            preference1.setIntegerValue(days);
            preference = create(preference1);
        }
        return preference;
    }
}

