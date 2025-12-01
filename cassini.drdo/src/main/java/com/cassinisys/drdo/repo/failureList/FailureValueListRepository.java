package com.cassinisys.drdo.repo.failureList;

import com.cassinisys.drdo.model.failureList.DRDOFailureValueList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Nageshreddy on 02-01-2019.
 */
@Repository
public interface FailureValueListRepository extends
        JpaRepository<DRDOFailureValueList, Integer> {

    List<DRDOFailureValueList> findByItemOrderByIdAsc(Integer item);

    DRDOFailureValueList findByInstanceAndFailureStep(Integer instance, Integer step);

    List<DRDOFailureValueList> findByItemAndInstanceOrderByIdAsc(Integer item, Integer upn);

    List<DRDOFailureValueList> findByItemAndLotInstanceOrderByIdAsc(Integer item, Integer upn);
}
