package com.cassinisys.drdo.filtering;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.cassinisys.drdo.model.transactions.*;
import com.cassinisys.drdo.repo.bom.BomItemInstanceRepository;
import com.cassinisys.drdo.repo.bom.ItemInstanceRepository;
import com.cassinisys.drdo.repo.transactions.IssueItemRepository;
import com.cassinisys.drdo.repo.transactions.IssueRepository;
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
 * Created by Nageshreddy on 14-02-2019.
 */
@Component
public class IssuePredicateBuilder implements PredicateBuilder<IssueCriteria, QIssue> {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueItemRepository issueItemRepository;

    @Autowired
    private BomItemInstanceRepository bomItemInstanceRepository;

    @Autowired
    private ItemInstanceRepository itemInstanceRepository;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonPredicateBuilder personPredicateBuilder;

    @Override
    public Predicate build(IssueCriteria criteria, QIssue pathBase) {

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicates1 = new ArrayList<>();

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


            predicates.add(pathBase.number.containsIgnoreCase(criteria.getSearchQuery())
                    .or(pathBase.modifiedBy.in(personIds))
                    .or(pathBase.request.createdBy.in(personIds))
                    .or(pathBase.request.reqNumber.containsIgnoreCase(criteria.getSearchQuery())));
        }

        if (criteria.getNotification() && !criteria.getAdminPermission() && (criteria.getBdlQcApprove() || criteria.getVersityQc())) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                List<Issue> issues = new ArrayList<>();
                if (criteria.getBdlQcApprove()) {
                    issues = issueRepository.findByVersity(criteria.getVersity());
                } else {
                    issues = issueRepository.findByVersity(criteria.getVersity());
                }

                List<Integer> issueIds = new ArrayList<>();
                issues.forEach(issue -> {
                    if ((issue.getStatus().equals(IssueStatus.BDL_QC) || issue.getStatus().equals(IssueStatus.VERSITY_QC) || issue.getStatus().equals(IssueStatus.ITEM_RESET)) && issue.getVersity().equals(criteria.getVersity())) {
                        issueIds.add(issue.getId());
                    } else {
                        List<IssueItem> issueItems = issueItemRepository.getIssueItemByIssuedAndVersity(issue.getId(), criteria.getVersity());
                        Boolean provisionalAcceptExist = false;
                        for (IssueItem issueItem : issueItems) {
                            ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                            if (itemInstance != null && (itemInstance.getProvisionalAccept() || issueItem.getStatus().equals(IssueItemStatus.PENDING))) {
                                provisionalAcceptExist = true;
                            }
                        }

                        if (provisionalAcceptExist) {
                            issueIds.add(issue.getId());
                        }
                    }
                });

                predicates1.add(pathBase.id.in(issueIds));
            } else {
                List<Issue> issues = new ArrayList<>();
                if (criteria.getBdlQcApprove()) {
                    issues = issueRepository.getIssuesByBomInstanceAndVersity(criteria.getMissile(), criteria.getVersity());
                } else {
                    issues = issueRepository.getIssuesByBomInstanceAndVersity(criteria.getMissile(), criteria.getVersity());
                }

                List<Integer> issueIds = new ArrayList<>();
                issues.forEach(issue -> {
                    if ((issue.getStatus().equals(IssueStatus.BDL_QC) || issue.getStatus().equals(IssueStatus.VERSITY_QC) || issue.getStatus().equals(IssueStatus.ITEM_RESET)) && issue.getVersity().equals(criteria.getVersity())) {
                        issueIds.add(issue.getId());
                    } else {
                        List<IssueItem> issueItems = issueItemRepository.getIssueItemByIssuedAndVersity(issue.getId(), criteria.getVersity());
                        Boolean provisionalAcceptExist = false;
                        for (IssueItem issueItem : issueItems) {
                            ItemInstance itemInstance = itemInstanceRepository.findOne(issueItem.getBomItemInstance().getItemInstance());

                            if (itemInstance != null && (itemInstance.getProvisionalAccept() || issueItem.getStatus().equals(IssueItemStatus.PENDING))) {
                                provisionalAcceptExist = true;
                            }
                        }

                        if (provisionalAcceptExist) {
                            issueIds.add(issue.getId());
                        }
                    }
                });

                predicates1.add(pathBase.id.in(issueIds));
            }
        }

        if (!criteria.getNotification() && !criteria.getAdminPermission() && (criteria.getBdlQcApprove() || criteria.getVersityQc())) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                List<Issue> issues = issueRepository.findByVersity(criteria.getVersity());
                List<Integer> issueIds = new ArrayList<>();

                issues.forEach(issue -> {
                    issueIds.add(issue.getId());
                });

                predicates1.add(pathBase.id.in(issueIds));
            } else {
                List<Issue> issues = issueRepository.getIssuesByBomInstanceAndVersity(criteria.getMissile(), criteria.getVersity());
                List<Integer> issueIds = new ArrayList<>();

                issues.forEach(issue -> {
                    issueIds.add(issue.getId());
                });

                predicates1.add(pathBase.id.in(issueIds));
            }

        }

        if (criteria.getNotification() && !criteria.getAdminPermission() && criteria.getStoreApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                        .or(pathBase.status.eq(IssueStatus.VERSITY_PPC))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.RECEIVED)));
            } else {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                        .or(pathBase.status.eq(IssueStatus.VERSITY_PPC))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile())));
            }
        }
        if (!criteria.getNotification() && !criteria.getAdminPermission() && criteria.getStoreApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_QC)
                        .or(pathBase.status.eq(IssueStatus.BDL_PPC))
                        .or(pathBase.status.eq(IssueStatus.VERSITY_QC))
                        .or(pathBase.status.eq(IssueStatus.VERSITY_PPC))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.RECEIVED)));
            } else {
                List<Issue> issues = issueRepository.getReceivedIssuesByBomInstance(criteria.getMissile());
                List<Integer> issueIds = new ArrayList<>();

                issues.forEach(issue -> {
                    issueIds.add(issue.getId());
                });

                predicates1.add(pathBase.id.in(issueIds));
            }
        }

        if (criteria.getNotification() && !criteria.getAdminPermission() && (criteria.getBdlPpcReceive() || criteria.getVersityPpc())) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                if (criteria.getBdlPpcReceive()) {
                    predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .and(pathBase.versity.eq(criteria.getVersity())));
                } else {
                    predicates1.add(pathBase.status.eq(IssueStatus.VERSITY_PPC)
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .and(pathBase.versity.eq(criteria.getVersity())));
                }
            } else {
                if (criteria.getBdlPpcReceive()) {
                    predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
                } else {
                    predicates1.add(pathBase.status.eq(IssueStatus.VERSITY_PPC)
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
                }
            }
        }

        if (!criteria.getNotification() && !criteria.getAdminPermission() && (criteria.getBdlPpcReceive() || criteria.getVersityPpc())) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                if (criteria.getBdlPpcReceive()) {
                    predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                            .or(pathBase.status.eq(IssueStatus.RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .and(pathBase.versity.eq(criteria.getVersity())));
                } else {
                    predicates1.add(pathBase.status.eq(IssueStatus.VERSITY_PPC)
                            .or(pathBase.status.eq(IssueStatus.RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .and(pathBase.versity.eq(criteria.getVersity())));
                }
            } else {
                if (criteria.getBdlPpcReceive()) {
                    predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                            .or(pathBase.status.eq(IssueStatus.RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
                } else {
                    predicates1.add(pathBase.status.eq(IssueStatus.VERSITY_PPC)
                            .or(pathBase.status.eq(IssueStatus.RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                            .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                            .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
                }
            }
        }

        if (criteria.getNotification() && !criteria.getAdminPermission() && criteria.getCasApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                        .or(pathBase.status.eq(IssueStatus.BDL_QC))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED)));
            } else {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_PPC)
                        .or(pathBase.status.eq(IssueStatus.BDL_QC))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile())));
            }
        }
        if (!criteria.getNotification() && !criteria.getAdminPermission() && criteria.getCasApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_QC)
                        .or(pathBase.status.eq(IssueStatus.BDL_PPC))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.RECEIVED)));
            } else {
                predicates1.add(pathBase.status.eq(IssueStatus.BDL_QC)
                        .or(pathBase.status.eq(IssueStatus.BDL_PPC))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_APPROVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.RECEIVED))
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile())));
            }
        }

        if (criteria.getNotification() && criteria.getAdminPermission()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.ne(IssueStatus.RECEIVED).and(pathBase.status.ne(IssueStatus.REJECTED)));
            } else {
                predicates1.add(pathBase.status.ne(IssueStatus.RECEIVED)
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile())));
            }
        }

        if (!criteria.getNotification() && criteria.getAdminPermission()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                List<Issue> issues = issueRepository.findAll();
                List<Integer> issueIds = new ArrayList<>();

                issues.forEach(issue -> {
                    issueIds.add(issue.getId());
                });

                predicates1.add(pathBase.id.in(issueIds));
            } else {
                predicates1.add(pathBase.bomInstance.id.eq(criteria.getMissile()));
            }
        }

        if (!criteria.getCasApprove() && !criteria.getBdlQcApprove() && !criteria.getBdlApprove() && !criteria.getVersityQc() && !criteria.getVersityPpc()) {
            Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
            List<Integer> issueIds = new ArrayList<>();
            if (personId != null) {
                List<Request> requests = requestRepository.getRequestedByRequests(personId);

                requests.forEach(request -> {
                    if (Criteria.isEmpty(criteria.getMissile())) {
                        List<Issue> issues = issueRepository.findByRequest(request.getId());

                        issues.forEach(issue -> {
                            issueIds.add(issue.getId());
                        });
                    } else {
                        List<Issue> issues = issueRepository.findByRequestAndBomInstance(request.getId(), criteria.getMissile());

                        issues.forEach(issue -> {
                            issueIds.add(issue.getId());
                        });
                    }

                });
            }
            predicates1.add(pathBase.id.in(issueIds));
        }

        if (!criteria.getAdminPermission() && criteria.getBdlApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.RECEIVED)
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.STORE)).or(pathBase.status.eq(IssueStatus.BDL_QC))
                        .and(pathBase.versity.eq(criteria.getVersity())));
            } else {
                predicates1.add(pathBase.status.eq(IssueStatus.RECEIVED)
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.STORE)).or(pathBase.status.eq(IssueStatus.BDL_QC))
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
            }
        }

        if (!criteria.getAdminPermission() && criteria.getVersityApprove()) {
            if (Criteria.isEmpty(criteria.getMissile())) {
                predicates1.add(pathBase.status.eq(IssueStatus.RECEIVED)
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.STORE)).or(pathBase.status.eq(IssueStatus.VERSITY_QC))
                        .and(pathBase.versity.eq(criteria.getVersity())));
            } else {
                predicates1.add(pathBase.status.eq(IssueStatus.RECEIVED)
                        .or(pathBase.status.eq(IssueStatus.PARTIALLY_RECEIVED))
                        .or(pathBase.status.eq(IssueStatus.ITEM_RESET))
                        .or(pathBase.status.eq(IssueStatus.STORE)).or(pathBase.status.eq(IssueStatus.VERSITY_QC))
                        .and(pathBase.bomInstance.id.eq(criteria.getMissile()).and(pathBase.versity.eq(criteria.getVersity()))));
            }
        }

        List<Predicate> predicates2 = new ArrayList<>();

        if (!Criteria.isEmpty(criteria.getFromDate()) && !Criteria.isEmpty(criteria.getToDate())) {
            try {
                Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getToDate());
                Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
                predicates2.add(pathBase.createdDate.after(new SimpleDateFormat("dd/MM/yyyy").parse(criteria.getFromDate())));
                predicates2.add(pathBase.createdDate.before(tomorrow));
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

                predicates2.add(pathBase.createdDate.after(dt));
                predicates2.add(pathBase.createdDate.before(tomorrow));
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        Predicate predicate = ExpressionUtils.and(ExpressionUtils.anyOf(predicates), ExpressionUtils.anyOf(predicates1));
        return ExpressionUtils.and(ExpressionUtils.anyOf(predicate), ExpressionUtils.allOf(predicates2));
    }
}
