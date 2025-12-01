package com.cassinisys.platform.service.core;

import com.cassinisys.platform.model.core.Currency;
import com.cassinisys.platform.repo.core.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Nageshreddy on 23-08-2017.
 */
@Service
public class CurrencyService implements CrudService<Currency, Integer> {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    @Transactional
    public Currency create(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public Currency update(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public void delete(Integer integer) {
        currencyRepository.delete(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public Currency get(Integer integer) {
        return currencyRepository.getOne(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAll() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencies;
    }
}
