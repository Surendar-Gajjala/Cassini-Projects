package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.transactions.QRequest;
import com.cassinisys.drdo.model.transactions.RequestStatus;
import com.cassinisys.drdo.repo.transactions.RequestItemRepository;
import com.cassinisys.drdo.repo.transactions.RequestRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.filtering.PersonPredicateBuilder;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.QPerson;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subramanyam reddy on 27-11-2018.
 */
@Component
public class RequestPredicateBuilder implements PredicateBuilder<RequestCriteria, QRequest> {

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestItemRepository requestItemRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonPredicateBuilder personPredicateBuilder;

    @Override
    public Predicate build(RequestCriteria criteria, QRequest request) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getSearchQuery())) {
            PersonCriteria personCriteria = new PersonCriteria();
            personCriteria.setSearchQuery(criteria.getSearchQuery());
            personCriteria.setFreeTextSearch(true);
            Pageable pageable = new PageRequest(0, 100,
                    new Sort(new Sort.Order(Sort.Direction.ASC, "firstName")));
            Predicate predicate = personPredicateBuilder.build(personCriteria, QPerson.person);
            Page<Person> persons = personRepository.findAll(predicate, pageable);
            List<Integer> personIds = new ArrayList<>();
            if (persons.getContent().size() > 0) {
                persons.getContent().forEach(person -> {
                    personIds.add(person.getId());
                });
            }

            predicates.add(request.reqNumber.containsIgnoreCase(criteria.getSearchQuery())
                    .or(request.bomInstance.item.instanceName.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(request.notes.containsIgnoreCase(criteria.getSearchQuery()))
                    .or(request.createdBy.in(personIds))
                    .and(request.issued.eq(criteria.getIssued())));
        }

//        List<Predicate> predicates1 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getStatus())) {
            predicates.add(request.status.eq(RequestStatus.valueOf(criteria.getStatus())).and(request.issued.eq(criteria.getIssued())));
        }

        if (criteria.getNotification()) {
            predicates.add(request.status.eq(RequestStatus.REQUESTED).and(request.issued.eq(criteria.getIssued())));
        } else {
            if (criteria.getIssued()) {
                predicates.add(request.status.eq(RequestStatus.APPROVED).and(request.issued.eq(criteria.getIssued())));
            } else {
                predicates.add(request.status.eq(RequestStatus.REQUESTED).and(request.issued.eq(criteria.getIssued())));
            }
        }



        /*if (!criteria.getNotification() && criteria.getAdminPermission()) {
            if (criteria.getIssued().equals(true)) {
                predicates1.add(request.status.eq(RequestStatus.APPROVED)
                        .or(request.status.eq(RequestStatus.COLLECTED)).and(request.issued.eq(criteria.getIssued())));
            } else {
                predicates1.add(request.status.ne(RequestStatus.APPROVED)
                        .or(request.status.ne(RequestStatus.COLLECTED)).and(request.issued.eq(criteria.getIssued())));
            }

        }

        if (criteria.getNotification() && criteria.getBdlApprove() && !criteria.getAdminPermission()) {
            predicates1.add(request.status.eq(RequestStatus.BDL_MANAGER).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));

            List<Request> requests = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_ACCEPTED, criteria.getVersity());

            requests.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });

            List<Request> requestList = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_APPROVED, criteria.getVersity());

            requestList.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });
        }

        if (criteria.getNotification() && criteria.getVersityApprove() && !criteria.getAdminPermission()) {
            predicates1.add(request.status.eq(RequestStatus.VERSITY_MANAGER).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));

            List<Request> requests = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_ACCEPTED, criteria.getVersity());

            requests.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });

            List<Request> requestList = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_APPROVED, criteria.getVersity());

            requestList.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });
        }

        if (!criteria.getNotification() && criteria.getBdlApprove() && !criteria.getAdminPermission()) {
            predicates1.add(request.status.eq(RequestStatus.BDL_MANAGER).and(request.issued.eq(criteria.getIssued())));
            predicates1.add(request.status.eq(RequestStatus.CAS_MANAGER).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
            predicates1.add(request.status.eq(RequestStatus.APPROVED).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));

            List<Request> requests = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_ACCEPTED, criteria.getVersity());

            requests.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });

            List<Request> requestList = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_APPROVED, criteria.getVersity());

            requestList.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });
        }

        if (!criteria.getNotification() && criteria.getVersityApprove() && !criteria.getAdminPermission()) {
            predicates1.add(request.status.eq(RequestStatus.VERSITY_MANAGER).and(request.issued.eq(criteria.getIssued())));
            predicates1.add(request.status.eq(RequestStatus.CAS_MANAGER).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));

            predicates1.add(request.status.eq(RequestStatus.APPROVED).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));

            List<Request> requests = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_ACCEPTED, criteria.getVersity());

            requests.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });

            List<Request> requestList = requestRepository.getRequestsByStatusAndVersity(RequestStatus.PARTIALLY_APPROVED, criteria.getVersity());

            requestList.forEach(request1 -> {
                List<RequestItem> requestItems = requestItemRepository.getPendingItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued()).and(request.versity.eq(criteria.getVersity()))));
                }
            });
        }

        if (criteria.getNotification() && criteria.getCasApprove() && !criteria.getAdminPermission()) {

            List<Request> requests = requestRepository.getRequestsByStatus(RequestStatus.BDL_MANAGER);
            requests.addAll(requestRepository.getRequestsByStatus(RequestStatus.VERSITY_MANAGER));
            requests.addAll(requestRepository.getRequestsByStatus(RequestStatus.CAS_MANAGER));
            requests.addAll(requestRepository.getRequestsByStatus(RequestStatus.PARTIALLY_ACCEPTED));
            requests.addAll(requestRepository.getRequestsByStatus(RequestStatus.PARTIALLY_APPROVED));

            for (Request request1 : requests) {
                List<RequestItem> requestItems = requestItemRepository.getAcceptedItems(request1.getId());

                if (requestItems.size() > 0) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued())));
                }
            }

        }

        if (criteria.getNotification() && (criteria.getBdlQcApprove() || criteria.getBdlPpcReceive()) && !criteria.getAdminPermission()) {

            predicates1.add(request.status.eq(RequestStatus.BDL_MANAGER)
                    .or(request.status.eq(RequestStatus.VERSITY_MANAGER))
                    .or(request.status.eq(RequestStatus.CAS_MANAGER))
                    .or(request.status.eq(RequestStatus.PARTIALLY_ACCEPTED))
                    .or(request.status.eq(RequestStatus.PARTIALLY_APPROVED))
                    .or(request.status.eq(RequestStatus.APPROVED))
                    .and(request.issued.eq(criteria.getIssued())));
        }

        if (!criteria.getNotification() && (criteria.getBdlQcApprove() || criteria.getBdlPpcReceive()) && !criteria.getAdminPermission()) {

            predicates1.add(request.status.eq(RequestStatus.BDL_MANAGER)
                    .or(request.status.eq(RequestStatus.VERSITY_MANAGER))
                    .or(request.status.eq(RequestStatus.CAS_MANAGER))
                    .or(request.status.eq(RequestStatus.PARTIALLY_ACCEPTED))
                    .or(request.status.eq(RequestStatus.PARTIALLY_APPROVED))
                    .or(request.status.eq(RequestStatus.APPROVED))
                    .or(request.status.eq(RequestStatus.REJECTED))
                    .and(request.issued.eq(criteria.getIssued())));
        }

        if (!criteria.getNotification() && criteria.getCasApprove() && !criteria.getAdminPermission()) {
            predicates1.add(request.status.ne(RequestStatus.COLLECTED));
        }

        if (!criteria.getAdminPermission() && !criteria.getCasApprove() && !criteria.getBdlApprove() && !criteria.getVersityApprove() && !criteria.getStoreApprove() && !criteria.getSsqagApprove()) {
            predicates1.add(request.requestedBy.id.eq(sessionWrapper.getSession().getLogin().getPerson().getId()).and(request.issued.eq(criteria.getIssued())));
        }

        if (!criteria.getAdminPermission() && criteria.getStoreApprove() && !criteria.getCasApprove()) {

            if (criteria.getIssued()) {
                predicates.add(request.issued.eq(criteria.getIssued()));
            } else {
                List<Request> requests = requestRepository.getRequestsWithoutByStatus(RequestStatus.REJECTED);

                for (Request request1 : requests) {
                    List<RequestItem> requestItems = requestItemRepository.findByRequest(request1);
                    Boolean approvedItemsExist = false;

                    for (RequestItem requestItem : requestItems) {
                        if (requestItem.getApproved()) {
                            approvedItemsExist = true;
                        }
                    }

                    if (approvedItemsExist) {
                        predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued())));
                    }
                }
            }

            *//*List<Request> requests = requestRepository.getRequestsWithoutByStatus(RequestStatus.REJECTED);

            for (Request request1 : requests) {
                List<RequestItem> requestItems = requestItemRepository.findByRequest(request1);
                Boolean approvedItemsExist = false;

                for (RequestItem requestItem : requestItems) {
                    if (requestItem.getApproved()) {
                        approvedItemsExist = true;
                    }
                }

                if (approvedItemsExist) {
                    predicates1.add(request.id.eq(request1.getId()).and(request.issued.eq(criteria.getIssued())));
                }
            }*//*
//            predicates1.add(request.status.eq(RequestStatus.APPROVED).and(request.issued.eq(criteria.getIssued())));
        }*/

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(request.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(request.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        if (!Criteria.isEmpty(criteria.getMonth())) {
            try {
                Date dt = new SimpleDateFormat("MM/yyyy").parse(criteria.getMonth());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String firstDate = dateFormat.format(dt);

                LocalDate lastDate = LocalDate.parse(firstDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                lastDate = lastDate.withDayOfMonth(lastDate.getMonth().length(lastDate.isLeapYear()));

                Date lastDateOfMonth = java.sql.Date.valueOf(lastDate);
                Date tomorrow = new Date(lastDateOfMonth.getTime() + (1000 * 60 * 60 * 24));

                predicates2.add(request.createdDate.after(dt));
                predicates2.add(request.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

//        Predicate predicate = ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
        return ExpressionUtils.and(ExpressionUtils.allOf(predicates), ExpressionUtils.anyOf(predicates2));
    }
}
