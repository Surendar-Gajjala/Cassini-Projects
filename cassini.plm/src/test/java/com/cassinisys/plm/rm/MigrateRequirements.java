package com.cassinisys.plm.rm;

import com.cassinisys.plm.BaseTest;
import com.cassinisys.plm.model.rm.SpecElement;
import com.cassinisys.plm.model.rm.SpecElementType;
import com.cassinisys.plm.model.rm.SpecSection;
import com.cassinisys.plm.model.rm.Specification;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.service.rm.SpecificationsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MigrateRequirements extends BaseTest {

    @Autowired
    private SpecificationsService specificationsService;

    @Autowired
    private SpecificationTypeRepository specificationTypeRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private SpecSectionRepository specSectionRepository;

    @Autowired
    private SpecRequirementRepository specRequirementRepository;

    @Autowired
    private SpecElementRepository specElementRepository;

    @Test
    public void migrateRequirements() throws Exception {
        List<Specification> specs = specificationRepository.findAll();
        for (Specification spec : specs) {
            List<SpecElement> specElements = new ArrayList<>();//specificationsService.getRootTypeSpecElements(spec.getId());
            migrateChildren(null, specElements);
        }
    }

    private void migrateChildren(SpecElement parentElement, List<SpecElement> children) {
        for (SpecElement specElement : children) {
            if(specElement.getType() == SpecElementType.SECTION) {
                specElement.setType(SpecElementType.REQUIREMENT);
                SpecSection specSection = specSectionRepository.findOne(specElement.getId());
                if(specSection != null) {

                }
            }
        }
    }
}
