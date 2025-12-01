package com.cassinisys.platform.service.core;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.AutoNumberCriteria;
import com.cassinisys.platform.filtering.AutoNumberPredicateBuilder;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.QAutoNumber;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reddy on 7/1/15.
 */
@Service
public class AutoNumberService {

    @Autowired
    private AutoNumberRepository autoNumberRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AutoNumberPredicateBuilder autoNumberPredicateBuilder;

    @Transactional(readOnly = true)
    public List<AutoNumber> getAllAutonumbers() {
        return autoNumberRepository.findAllByOrderByIdDesc();
    }

    @Transactional(readOnly = true)
    public AutoNumber getAutoNumberById(Integer id) {
        return autoNumberRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public AutoNumber getByName(String name) {
        return autoNumberRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public AutoNumber getByPrefix(String prefix) {
        return autoNumberRepository.findByPrefix(prefix);
    }

    @Transactional
    public AutoNumber createAutoNumber(AutoNumber autoNumber) {
        if (autoNumber.getNextNumber() == -1) {
            autoNumber.setNextNumber(autoNumber.getStart());
        }
        AutoNumber num = autoNumberRepository.findByName(autoNumber.getName());
        if (num != null) {
            throw new CassiniException(messageSource.getMessage("this_name_is_already_exists_please_enter_another_one",
                    null, "This Name is already exists! please enter another one", LocaleContextHolder.getLocale()));
        }
    /*	AutoNumber number = autoNumberRepository.findByPrefix((autoNumber.getPrefix()));
        if (number != null) {
			throw new CassiniException(messageSource.getMessage("this_prefix_is_already_exists_please_enter_another_one",
					null, "This Prefix is already exists! please enter another one", LocaleContextHolder.getLocale()));
		}*/
        autoNumber.setNextNumber(autoNumber.getStart());
        return autoNumberRepository.save(autoNumber);
    }

    @Transactional
    public List<AutoNumber> save(List<AutoNumber> autoNumbers) {
        for (AutoNumber auto : autoNumbers) {
            if (auto.getNextNumber() == -1) {
                auto.setNextNumber(auto.getStart());
            }
            auto.setNextNumber(auto.getStart());
        }
        return autoNumberRepository.save(autoNumbers);
    }

    @Transactional
    public AutoNumber updateAutoNumber(AutoNumber autoNumber) {
        if (autoNumber.getPrefix() != null && autoNumber.getPrefix() != "") {
            AutoNumber number = autoNumberRepository.findByPrefixAndNotId(autoNumber.getId(), (autoNumber.getPrefix()));
            if (number != null) {
                throw new CassiniException(messageSource.getMessage("this_prefix_is_already_exists_please_enter_another_one",
                        null, "This Prefix is already exists! please enter another one", LocaleContextHolder.getLocale()));
            }
        }
        AutoNumber num = autoNumberRepository.findByNameAndNotId(autoNumber.getId(), (autoNumber.getName()));
        if (num != null) {
            throw new CassiniException(messageSource.getMessage("this_name_is_already_exists_please_enter_another_one",
                    null, "This Name is already exists! please enter another one", LocaleContextHolder.getLocale()));
        }
        autoNumber.setNextNumber(autoNumber.getStart());
        return autoNumberRepository.save(autoNumber);
    }

    @Transactional
    public String getNextAutoNumber(Integer autoNumberId) {
        String next = null;
        try {
            AutoNumber auto = autoNumberRepository.findOne(autoNumberId);
            next = auto.next();
            auto = autoNumberRepository.save(auto);
        } catch (Exception ex) {
            throw new CassiniException(ex.toString());
        }

        return next;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public String getNextNumber(Integer autoNumberId) {
        String next = null;
        try {
            AutoNumber auto = autoNumberRepository.findOne(autoNumberId);
            next = auto.next();
            auto = updateAutoNumberOnItemTypeSelection(auto);
        } catch (Exception ex) {
            throw new CassiniException(ex.toString());
        }

        return next;
    }

    @Transactional
    private AutoNumber updateAutoNumberOnItemTypeSelection(AutoNumber autoNumber) {
        autoNumber = autoNumberRepository.save(autoNumber);
        return autoNumber;
    }

    @Transactional
    public String getNextNumberWithoutUpdate(String name) {
        AutoNumber number = autoNumberRepository.findByName(name);
        if (number != null) {
            String nextNumber = getNextNumberWithoutUpdate(number.getId());
            return nextNumber;
        }
        return null;
    }

    @Transactional
    public String getNextNumberByName(String name) {
        AutoNumber number = autoNumberRepository.findByName(name);
        if (number != null) {
            String nextNumber = getNextNumber(number.getId());
            return nextNumber;
        }
        return null;
    }

    private String getNextNumberWithoutUpdate(Integer autoNumberId) {
        String next = null;
        try {
            AutoNumber auto = autoNumberRepository.findOne(autoNumberId);
            next = auto.readOnlyNext();
        } catch (Exception ex) {
            throw new CassiniException(ex.toString());
        }

        return next;
    }

    @Transactional
    public void saveNextNumber(Integer autoId, String number) {
        AutoNumber auto = autoNumberRepository.findOne(autoId);
        String[] strs = number.split("-");
        if (strs.length >= 2 && auto.getPrefix().equalsIgnoreCase(strs[0] + "-")) {
            strs[1] = strs[1].replaceAll("[^0-9]", "");
            String strPattern = "^0+(?!$)";
            Integer val1 = Integer.parseInt(strs[1].replaceAll(strPattern, ""));
            if (auto.getNextNumber() < val1) {
                auto.setNextNumber(val1 + 1);
            } else if (auto.getNextNumber().intValue() == val1.intValue()) {
                auto.next();
            }
        } else {
            String[] strs1 = number.split("(?<=\\D)(?=\\d)");
            if (strs1.length >= 2) {
                strs1[1] = strs1[1].replaceAll("[^0-9]", "");
                String strPattern = "^0+(?!$)";
                Integer val1 = Integer.parseInt(strs1[1].replaceAll(strPattern, ""));
                if (auto.getNextNumber() < val1) {
                    auto.setNextNumber(val1 + 1);
                } else if (auto.getNextNumber().intValue() == val1.intValue()) {
                    auto.next();
                }
            }
        }

        autoNumberRepository.save(auto);
    }

    @Transactional
    public List<String> getNextNumbers(Integer autoNumberId, Integer count) {
        ArrayList<String> next = new ArrayList<String>();
        AutoNumber auto = autoNumberRepository.findOne(autoNumberId);
        for (int i = 0; i < count; i++) {
            next.add(auto.next());
            autoNumberRepository.save(auto);
        }

        return next;
    }

    @Transactional
    public void deleteAutonumber(Integer id) {
        autoNumberRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<AutoNumber> findAll(Pageable pageable) {
        return autoNumberRepository.findAllByOrderByIdDesc(pageable);
    }

    public String createAutoNumberIfNotExist(String name, String description, Integer numbers, Integer start, Integer increment, String padwith, String prefix, Integer nextNumber) {
        String nextNumber1 = "";
        AutoNumber autoNumber = autoNumberRepository.findByName(name);
        if (autoNumber != null) {
            nextNumber1 = getNextNumber(autoNumber.getId());
        } else {
            autoNumber = new AutoNumber(name, description, numbers, start, increment, padwith, prefix, nextNumber);
            autoNumber = autoNumberRepository.save(autoNumber);
            nextNumber1 = getNextAutoNumber(autoNumber.getId());
        }
        return nextNumber1;
    }

    @Transactional(readOnly = true)
    public Page<AutoNumber> getAllAutoNumbers(Pageable pageable, AutoNumberCriteria autoNumberCriteria) {
        Predicate predicate = autoNumberPredicateBuilder.build(autoNumberCriteria, QAutoNumber.autoNumber);
        return autoNumberRepository.findAll(predicate, pageable);
    }

}