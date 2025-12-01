package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.CustomerEvents;
import com.cassinisys.plm.filtering.CustomerPredicateBuilder;
import com.cassinisys.plm.filtering.CustomerSupplierCriteria;
import com.cassinisys.plm.filtering.SupplierPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.pqm.dto.ProblemReportsDto;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.CustomerFileRepository;
import com.cassinisys.plm.repo.pqm.PQMCustomerRepository;
import com.cassinisys.plm.repo.pqm.PQMSupplierRepository;
import com.cassinisys.plm.repo.pqm.ProblemReportRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 04-06-2020.
 */
@Service
public class CustomerSupplierService implements CrudService<PQMCustomer, Integer> {

    @Autowired
    private PQMCustomerRepository pqmCustomerRepository;
    @Autowired
    private PQMSupplierRepository pqmSupplierRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CustomerPredicateBuilder customerPredicateBuilder;
    @Autowired
    private SupplierPredicateBuilder supplierPredicateBuilder;
    @Autowired
    private ProblemReportRepository problemReportRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#customer,'create')")
    public PQMCustomer create(PQMCustomer customer) {
        PQMCustomer existCustomer = pqmCustomerRepository.findByName(customer.getName());
        if (existCustomer != null) {
            throw new CassiniException(messageSource.getMessage("customer_name_already_exists", null, "Customer Name already exist", LocaleContextHolder.getLocale()));
        }
        if(customer.getPerson().getId() == null){
            Person existPerson1 = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(customer.getPerson().getFirstName(), customer.getPerson().getLastName());
            if(existPerson1 != null) {
                String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existPerson1.getFirstName() + " " + existPerson1.getLastName());
                throw new CassiniException(result);
            }
        }
        Person person = personRepository.save(customer.getPerson());
        customer.setContactPerson(person.getId());
        customer = pqmCustomerRepository.save(customer);
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerCreatedEvent(customer));
        return customer;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#customer.id ,'edit')")
    public PQMCustomer update(PQMCustomer customer) {
        PQMCustomer oldCustomer = JsonUtils.cloneEntity(pqmCustomerRepository.findOne(customer.getId()), PQMCustomer.class);
        PQMCustomer existCustomer = pqmCustomerRepository.findByName(customer.getName());
        if (existCustomer != null && !existCustomer.getId().equals(customer.getId())) {
            throw new CassiniException(messageSource.getMessage("customer_name_already_exists", null, "Customer Name already exist", LocaleContextHolder.getLocale()));
        }
        Person person = personRepository.save(customer.getPerson());
        customer.setContactPerson(person.getId());
        customer = pqmCustomerRepository.save(customer);
        applicationEventPublisher.publishEvent(new CustomerEvents.CustomerBasicInfoUpdatedEvent(oldCustomer, customer));
        return customer;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {

        List<PQMProblemReport> problemReports = problemReportRepository.findByReporterTypeAndReportedBy(ReporterType.CUSTOMER, id);
        if (problemReports.size() > 0) {
            throw new CassiniException(messageSource.getMessage("customer_already_in_use", null, "Customer already in use", LocaleContextHolder.getLocale()));
        } else {
            PQMCustomer customer = pqmCustomerRepository.findOne(id);
            pqmCustomerRepository.delete(id);
            if (customer != null && customer.getContactPerson() != null) {
                personRepository.delete(customer.getContactPerson());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PQMCustomer get(Integer id) {
        PQMCustomer customer = pqmCustomerRepository.findOne(id);
        if (customer != null) {
            Person person = personRepository.findOne(customer.getContactPerson());
            customer.setPerson(person);
        }
        return customer;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMCustomer> getAll() {
        return pqmCustomerRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMCustomer> findMultiple(List<Integer> ids) {
        return pqmCustomerRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PQMCustomer> getAllCustomers(Pageable pageable, CustomerSupplierCriteria criteria) {
        Predicate predicate = customerPredicateBuilder.build(criteria, QPQMCustomer.pQMCustomer);
        Page<PQMCustomer> customers = pqmCustomerRepository.findAll(predicate, pageable);
        if (customers.getContent().size() > 0) {
            customers.getContent().forEach(customer -> {
                List<PQMProblemReport> problemReports = problemReportRepository.findByReporterTypeAndReportedBy(ReporterType.CUSTOMER, customer.getId());
                if (problemReports.size() > 0) {
                    customer.setUsed(true);
                }
            });
        }
        return customers;
    }

    @Transactional
    public ItemDetailsDto getCustomerTabCounts(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setProblemReports(problemReportRepository.getPrsByReportedBy(id));
        detailsDto.setItemFiles(customerFileRepository.findByCustomerAndFileTypeAndLatestTrueOrderByModifiedDateDesc(id, "FILE").size());
        detailsDto.setItemFiles(detailsDto.getItemFiles() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsDto;
    }

    @Transactional
    public PQMSupplier createSupplier(PQMSupplier customer) {

        PQMSupplier existSupplier = pqmSupplierRepository.findByName(customer.getName());
        if (existSupplier != null) {
            throw new CassiniException(messageSource.getMessage("supplier_name_already_exists", null, "Supplier Name already exist", LocaleContextHolder.getLocale()));
        }

        Person person = personRepository.save(customer.getPerson());
        customer.setContactPerson(person.getId());
        return pqmSupplierRepository.save(customer);
    }

    @Transactional
    public PQMSupplier updateSupplier(PQMSupplier supplier) {
        PQMSupplier existSupplier = pqmSupplierRepository.findByName(supplier.getName());
        if (existSupplier != null && !existSupplier.getId().equals(supplier.getId())) {
            throw new CassiniException(messageSource.getMessage("supplier_name_already_exists", null, "Supplier Name already exist", LocaleContextHolder.getLocale()));
        }
        Person person = personRepository.save(supplier.getPerson());
        supplier.setContactPerson(person.getId());
        return pqmSupplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Integer id) {
        List<PQMProblemReport> problemReports = problemReportRepository.findByReporterTypeAndReportedBy(ReporterType.SUPPLIER, id);
        if (problemReports.size() > 0) {
            throw new CassiniException(messageSource.getMessage("supplier_already_in_use", null, "Supplier already in use", LocaleContextHolder.getLocale()));
        } else {
            PQMSupplier supplier = pqmSupplierRepository.findOne(id);
            pqmSupplierRepository.delete(id);
            if (supplier != null && supplier.getContactPerson() != null) {
                personRepository.delete(supplier.getContactPerson());
            }
        }
    }

    @Transactional(readOnly = true)
    public PQMSupplier getSupplier(Integer id) {
        return pqmSupplierRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PQMSupplier> getSuppliers() {
        return pqmSupplierRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<PQMSupplier> findMultipleSuppliers(List<Integer> ids) {
        return pqmSupplierRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    public Page<PQMSupplier> getAllSuppliers(Pageable pageable, CustomerSupplierCriteria criteria) {
        Predicate predicate = supplierPredicateBuilder.build(criteria, QPQMSupplier.pQMSupplier);
        Page<PQMSupplier> suppliers = pqmSupplierRepository.findAll(predicate, pageable);

        if (suppliers.getContent().size() > 0) {
            suppliers.getContent().forEach(supplier -> {
                List<PQMProblemReport> problemReports = problemReportRepository.findByReporterTypeAndReportedBy(ReporterType.SUPPLIER, supplier.getId());
                if (problemReports.size() > 0) {
                    supplier.setUsed(true);
                }
            });
        }
        return suppliers;
    }

    @Transactional(readOnly = true)
    public List<ProblemReportsDto> getAllCustomerProblemReports(Integer customer) {
        List<PQMProblemReport> problemReports = problemReportRepository.findByReporterTypeAndReportedBy(ReporterType.CUSTOMER, customer);
        List<ProblemReportsDto> problemReportsDtos = new LinkedList<>();
        problemReports.forEach(problemReport -> {
            ProblemReportsDto dto = new ProblemReportsDto();
            dto.setId(problemReport.getId());
            dto.setProblem(problemReport.getProblem());
            dto.setPrNumber(problemReport.getPrNumber());
            dto.setDescription(problemReport.getDescription());
            if (problemReport.getProduct() != null) {
                PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(problemReport.getProduct());
                dto.setProduct(itemRepository.findOne(plmItemRevision.getItemMaster()).getItemName());
                dto.setRevision(plmItemRevision.getRevision());
            }
            dto.setPrType(problemReport.getPrType().getName());
            dto.setStepsToReproduce(problemReport.getStepsToReproduce());
            dto.setQualityAnalyst(personRepository.findOne(problemReport.getQualityAnalyst()).getFullName());
            dto.setReleased(problemReport.getReleased());
            dto.setReportedDate(problemReport.getReportedDate());
            dto.setReporterType(problemReport.getReporterType());
            dto.setFailureType(problemReport.getFailureType());
            dto.setSeverity(problemReport.getSeverity());
            dto.setStatus(problemReport.getStatus());
            dto.setStatusType(problemReport.getStatusType());
            dto.setDisposition(problemReport.getDisposition());
            problemReportsDtos.add(dto);
        });

        return problemReportsDtos;
    }
}
