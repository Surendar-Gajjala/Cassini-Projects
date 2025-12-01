package com.cassinisys.is.api.pm;
/**
 * The Class is for PortfolioController
 **/

import com.cassinisys.is.model.pm.ISPortfolio;
import com.cassinisys.is.model.pm.PortfolioDto;
import com.cassinisys.is.model.pm.ProjectDto;
import com.cassinisys.is.service.pm.PortfolioService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.jsondoc.core.annotation.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(name = "Portfolio", description = "Portfolio endpoint")
@RestController
@RequestMapping("/portfolio")
public class PortfolioController extends BaseController {

    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private PortfolioService portfolioService;

    /**
     * The method used for creating the ISPortfolio
     **/
    @RequestMapping(method = RequestMethod.POST)
    public ISPortfolio create(@RequestBody ISPortfolio portfolio) {
        portfolio.setId(null);
        return portfolioService.create(portfolio);
    }

    /**
     * The method used for updating the ISPortfolio
     **/
    @RequestMapping(method = RequestMethod.PUT)
    public ISPortfolio update(@RequestBody ISPortfolio portfolio) {
        return portfolioService.update(portfolio);
    }

    /**
     * The method used for deleting the ISPortfolio
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        portfolioService.delete(id);
    }

    /**
     * The method used get the value of ISPortfolio
     **/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ISPortfolio get(@PathVariable("id") Integer id) {
        return portfolioService.get(id);
    }

    /**
     * The method used getall the values of ISPortfolio
     **/
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public Page<ISPortfolio> getPageablePortfolios(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return portfolioService.getPageablePortfolios(pageable);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ISPortfolio> getAllPortfolios() {
        return portfolioService.findAllPortfolios();
    }

    /*--- From Mobile App ---*/
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<PortfolioDto> getPortfolios() {
        return portfolioService.getPortfolios();
    }

    @RequestMapping(value = "/{id}/projects", method = RequestMethod.GET)
    public List<ProjectDto> getPortfolioProjects(@PathVariable("id") Integer id) {
        return portfolioService.getPortfolioProjects(id);
    }

}
