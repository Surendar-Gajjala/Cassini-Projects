package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.plm.model.plm.PLMRelationshipAttribute;
import com.cassinisys.plm.repo.plm.RelationshipAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by subramanyamreddy on 022 22-Jan -18.
 */
@Service
public class RelationshipAttributeService implements CrudService<PLMRelationshipAttribute, Integer> {

    @Autowired
    private RelationshipAttributeRepository relationshipAttributeRepository;

    @Override
    public PLMRelationshipAttribute create(PLMRelationshipAttribute plmRelationshipAttribute) {
        return relationshipAttributeRepository.save(plmRelationshipAttribute);
    }

    @Override
    public PLMRelationshipAttribute update(PLMRelationshipAttribute plmRelationshipAttribute) {
        return relationshipAttributeRepository.save(plmRelationshipAttribute);
    }

    @Override
    public void delete(Integer id) {
        relationshipAttributeRepository.delete(id);
    }

    @Override
    public PLMRelationshipAttribute get(Integer id) {
        return relationshipAttributeRepository.findOne(id);
    }

    @Override
    public List<PLMRelationshipAttribute> getAll() {
        return relationshipAttributeRepository.findAll();
    }

    public List<PLMRelationshipAttribute> getAllAttributesByRelationship(Integer relationshipId) {
        return relationshipAttributeRepository.findByRelationship(relationshipId);
    }
}
