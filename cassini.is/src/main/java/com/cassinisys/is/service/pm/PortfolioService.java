package com.cassinisys.is.service.pm;

import com.cassinisys.is.model.pm.ISPortfolio;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.model.pm.PortfolioDto;
import com.cassinisys.is.model.pm.ProjectDto;
import com.cassinisys.is.repo.pm.PortfolioRepository;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.repo.common.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The class is for PortfolioService
 */
@Service
@Transactional
public class PortfolioService {

    @Autowired
    private PortfolioRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PersonRepository personRepository;

    /**
     * The method used to create  ISPortfolio
     **/
    @Transactional(readOnly = false)
    public ISPortfolio create(ISPortfolio portfolio) {
        checkNotNull(portfolio);
        portfolio.setId(null);
        return repository.save(portfolio);
    }

    /**
     * The method used to update  ISPortfolio
     **/
    @Transactional(readOnly = false)
    public ISPortfolio update(ISPortfolio portfolio) {
        checkNotNull(portfolio);
        checkNotNull(portfolio.getId());
        return repository.save(portfolio);
    }

    /**
     * The method used to delete
     **/
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        repository.delete(id);
    }

    /**
     * The method used to get ISPortfolio
     **/
    @Transactional(readOnly = true)
    public ISPortfolio get(Integer id) {
        checkNotNull(id);
        ISPortfolio portfolio = repository.findOne(id);
        if (portfolio == null) {
            throw new ResourceNotFoundException();
        }
        return portfolio;
    }

    /**
     * The method used to getAll for the list of  ISPortfolio
     **/
    @Transactional(readOnly = true)
    public List<ISPortfolio> getAll() {
        return repository.findAll();
    }

    /**
     * The method used to findAll for the page of  ISPortfolio
     **/
    @Transactional(readOnly = true)
    public Page<ISPortfolio> getPageablePortfolios(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ISPortfolio> findAllPortfolios() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PortfolioDto> getPortfolios() {
        List<PortfolioDto> portfolioDtos = new ArrayList<>();
        List<ISPortfolio> portfolios = repository.findAll();
        portfolios.forEach(isPortfolio -> {
            PortfolioDto portfolioDto = new PortfolioDto();
            portfolioDto.setPortfolio(isPortfolio);
            portfolioDto.getProjects().addAll(projectRepository.findByPortfolio(isPortfolio.getId()));
            portfolioDtos.add(portfolioDto);
        });
        return portfolioDtos;
    }

    public List<ProjectDto> getPortfolioProjects(Integer id) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        List<ISProject> projects = projectRepository.findByPortfolio(id);
        projects.forEach(project -> {
            ProjectDto projectDto = new ProjectDto();
            projectService.calculateProjectPercent(project);
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setDescription(project.getDescription());
            projectDto.setAssignedTo(personRepository.findOne(project.getProjectOwner()).getFullName());
            projectDto.setPlannedStartDate(project.getPlannedStartDate());
            projectDto.setPlannedFinishDate(project.getPlannedFinishDate());
            projectDto.setPercentComplete(project.getPercentComplete());
            projectDtoList.add(projectDto);
        });
        return projectDtoList;
    }

}
