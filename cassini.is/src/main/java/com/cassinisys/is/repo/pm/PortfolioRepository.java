package com.cassinisys.is.repo.pm;
/**
 * The Class is for PortfolioRepository
 **/

import com.cassinisys.is.model.pm.ISPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends
        JpaRepository<ISPortfolio, Integer> {

}
