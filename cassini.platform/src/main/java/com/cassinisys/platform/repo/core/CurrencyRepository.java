package com.cassinisys.platform.repo.core;

import com.cassinisys.platform.model.core.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Nageshreddy on 23-08-2017.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

}
