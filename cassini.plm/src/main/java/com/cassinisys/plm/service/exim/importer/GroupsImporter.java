package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.model.common.PersonGroup;
import com.cassinisys.platform.model.security.Permission;
import com.cassinisys.platform.repo.common.PersonGroupRepository;
import com.cassinisys.platform.repo.security.GroupPermissionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
@Transactional
public class GroupsImporter extends AbstractImporter {
    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @Override
    protected void importData(byte[] bytes) {
        try {
            List<PersonGroup> groups = getObjectMapper().readValue(bytes, new TypeReference<List<PersonGroup>>() {
            });
            for (PersonGroup group : groups) {
                PersonGroup found = personGroupRepository.findByName(group.getName());
                if (found == null) {
                    createNew(group);
                } else {
                    updateGroup(group);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNew(PersonGroup group) {
        group.setGroupId(null);
        List<Permission> permissions = group.getPermissions();
        List<PersonGroup> children = group.getGroupChildren();

    }

    private void updateGroup(PersonGroup group) {
    }
}
