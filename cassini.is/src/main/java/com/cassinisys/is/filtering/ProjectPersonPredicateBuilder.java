package com.cassinisys.is.filtering;
/**
 * The class is for ProjectPersonPredicateBuilder
 */

import com.cassinisys.is.model.pm.ISProjectPerson;
import com.cassinisys.is.model.pm.QISProjectPerson;
import com.cassinisys.is.repo.pm.ISProjectPersonRepository;
import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectPersonPredicateBuilder implements PredicateBuilder<ProjectPersonCriteria, QISProjectPerson> {
    /**
     * The method used to build Predicate
     */

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ISProjectPersonRepository projectPersonRepository;

    @Override
    public Predicate build(ProjectPersonCriteria projectPersonCriteria, QISProjectPerson pathBase) {
        if (projectPersonCriteria.getSearchQuery() != null) {
            return getFreeTextSearchPredicate(projectPersonCriteria, pathBase);
        } else {
            return getDefaultPredicate(projectPersonCriteria, pathBase);
        }
    }

    /**
     * The method used to getFreeTextSearchPredicate of Predicate
     */
    private Predicate getFreeTextSearchPredicate(ProjectPersonCriteria projectPersonCriteria, QISProjectPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        if (projectPersonCriteria.getSearchQuery() != null) {
            String[] arr = projectPersonCriteria.getSearchQuery().split(" ");
            for (String s : arr) {
                List<Integer> integers = projectPersonRepository.findPersonIdsByDistint();
                List<Person> persons = personRepository.findByIdIn(integers);
                for (Person person : persons) {
                    if (person.getFirstName().toLowerCase().contains(s.toLowerCase())) {
                        predicates.add(pathBase.person.in(person.getId()).and(pathBase.project.eq(projectPersonCriteria.getProject())));
                    } else if (person.getLastName() != null) {
                        if (person.getLastName().toLowerCase().contains(s.toLowerCase())) {
                            predicates.add(pathBase.person.in(person.getId()).and(pathBase.project.eq(projectPersonCriteria.getProject())));
                        }
                    } else if (person.getPhoneMobile() != null) {
                        if (person.getPhoneMobile().contains(s)) {
                            predicates.add(pathBase.person.in(person.getId()).and(pathBase.project.eq(projectPersonCriteria.getProject())));
                        }
                    } else if (person.getEmail() != null) {
                        if (person.getEmail().contains(s)) {
                            predicates.add(pathBase.person.in(person.getId()).and(pathBase.project.eq(projectPersonCriteria.getProject())));
                        }
                    }
                }
            }
        }
        return ExpressionUtils.anyOf(predicates);
    }

    /**
     * The method used to getDefaultPredicate of Predicate
     */
    public Predicate getDefaultPredicate(ProjectPersonCriteria projectPersonCriteria, QISProjectPerson pathBase) {
        List<Predicate> predicates = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        List<ISProjectPerson> projectPersons = projectPersonRepository.findByProject(projectPersonCriteria.getProject());
        for (ISProjectPerson projectPerson : projectPersons) {
            integers.add(projectPerson.getPerson());
        }
        List<Person> persons = personRepository.findByIdIn(integers);
        List<Predicate> predicates1 = new ArrayList();
        for (Person person : persons) {
            if (!Criteria.isEmpty(projectPersonCriteria.getFullName())) {
                if ((person.getFullName().toLowerCase()).contains((projectPersonCriteria.getFullName().toLowerCase())))
                    predicates1.add(pathBase.person.eq(person.getId()));
            }
            if (!Criteria.isEmpty(projectPersonCriteria.getPhoneMobile())) {
                if (person.getPhoneMobile() != null) {
                    if (person.getPhoneMobile().contains(projectPersonCriteria.getPhoneMobile())) {
                        predicates1.add(pathBase.person.eq(person.getId()));
                    }
                }
            }

        }
        if (predicates1.size() > 0) {
            predicates.add(ExpressionUtils.anyOf(predicates1));
        }
        return ExpressionUtils.allOf(predicates);

    }
}
