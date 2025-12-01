package com.cassinisys.platform.api.core;

import com.cassinisys.platform.model.core.Currency;
import com.cassinisys.platform.service.core.CurrencyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Nageshreddy on 23-08-2017.
 */
@RestController
@RequestMapping("/core/currencies")
@Api(tags = "PLATFORM.CORE",description = "Core endpoints")
public class CurrencyController extends BaseController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Currency> getAll() {
        return currencyService.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Currency get(@PathVariable("id") Integer id) {
        return currencyService.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        currencyService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Currency save(@RequestBody Currency currency) {
        return currencyService.create(currency);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Currency put(@PathVariable("id") Integer id, @RequestBody Currency currency) {
        currency.setId(id);
        return currencyService.update(currency);
    }

}
