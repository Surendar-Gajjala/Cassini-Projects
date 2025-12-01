package com.cassinisys.is.service.tm;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.security.Role;
import com.cassinisys.platform.repo.security.RoleRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class is for RoleService
 **/
@Service
@Transactional
public class RoleService implements CrudService<Role, Integer>,
        PageableService<Role, Integer> {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * The method used to create the role
     **/
    @Override
    @Transactional(readOnly = false)
    public Role create(Role role) {
        checkNotNull(role);
        role.setId(null);
        role = roleRepository.save(role);
        return role;
    }

    /**
     * The method used to update the role
     **/
    @Override
    @Transactional(readOnly = false)
    public Role update(Role role) {
        checkNotNull(role);
        checkNotNull(role.getId());
        role = roleRepository.save(role);
        return role;
    }

    /**
     * The method used to delete the role
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        Role role = roleRepository.findOne(id);
        if (role == null) {
            throw new ResourceNotFoundException();
        }
        roleRepository.delete(role);

    }

    /**
     * The method used to get the role
     **/
    @Transactional(readOnly = true)
    @Override
    public Role get(Integer id) {
        checkNotNull(id);
        Role role = roleRepository.findOne(id);
        if (role == null) {
            throw new ResourceNotFoundException();
        }
        return role;
    }

    /**
     * The method used to getAll the  List of Role
     **/
    @Transactional(readOnly = true)
    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    /**
     * The method used to findall the  page of Role
     **/
    @Transactional(readOnly = true)
    @Override
    public Page<Role> findAll(Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order("name")));
        }
        return roleRepository.findAll(pageable);
    }

}
