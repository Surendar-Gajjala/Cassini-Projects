package com.cassinisys.is.model.pm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam on 19-02-2020.
 */
public class PortfolioDto {

    private ISPortfolio portfolio;

    private List<ISProject> projects = new ArrayList<>();

    public ISPortfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(ISPortfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<ISProject> getProjects() {
        return projects;
    }

    public void setProjects(List<ISProject> projects) {
        this.projects = projects;
    }
}
