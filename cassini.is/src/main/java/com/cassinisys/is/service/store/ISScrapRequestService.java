package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.ScrapRequestCriteria;
import com.cassinisys.is.filtering.ScrapRequestPredicateBuilder;
import com.cassinisys.is.model.store.ISScrapRequest;
import com.cassinisys.is.model.store.ISScrapRequestItem;
import com.cassinisys.is.model.store.QISScrapRequest;
import com.cassinisys.is.repo.store.ISScrapRequestItemRepository;
import com.cassinisys.is.repo.store.ISScrapRequestRepository;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Varsha Malgireddy on 8/28/2018.
 */
@Service
public class ISScrapRequestService {

    @Autowired
    private ISScrapRequestRepository isScrapRequestRepository;
    @Autowired
    private ISScrapRequestItemRepository isScrapRequestItemRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ScrapRequestPredicateBuilder scrapRequestPredicateBuilder;

    @Transactional(readOnly = false)
    public ISScrapRequest createScrapRequest(ISScrapRequest isScrapRequest) {
        isScrapRequest = isScrapRequestRepository.save(isScrapRequest);
        for (ISScrapRequestItem item : isScrapRequest.getItems()) {
            item.setScrapRequest(isScrapRequest.getId());
            isScrapRequestItemRepository.save(item);
        }
        return isScrapRequest;
    }

    @Transactional(readOnly = false)
    public ISScrapRequest updateScrap(ISScrapRequest isScrapRequest) {
        return isScrapRequestRepository.save(isScrapRequest);
    }

    @Transactional(readOnly = true)
    public ISScrapRequest get(Integer id) {
        return isScrapRequestRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Page<ISScrapRequest> getAll(Pageable pageable) {
        return isScrapRequestRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getScrapAttributes(String objectType) {
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return objectTypeAttributes;
    }

    public Page<ISScrapRequest> freeTextSearch(Pageable pageable, ScrapRequestCriteria criteria) {
        Predicate predicate = scrapRequestPredicateBuilder.build(criteria, QISScrapRequest.iSScrapRequest);
        return isScrapRequestRepository.findAll(predicate, pageable);
    }
}
