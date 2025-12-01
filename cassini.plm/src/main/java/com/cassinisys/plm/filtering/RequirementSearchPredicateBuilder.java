package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.platform.filtering.PredicateBuilder;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.plm.model.dto.AttributeSearchDto;
import com.cassinisys.plm.model.rm.QRequirement;
import com.cassinisys.plm.model.rm.QSpecRequirement;
import com.cassinisys.plm.model.rm.RequirementStatus;
import com.cassinisys.plm.model.rm.SpecRequirement;
import com.cassinisys.plm.repo.rm.RequirementRepository;
import com.cassinisys.plm.repo.rm.SpecRequirementRepository;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by subra on 23-10-2018.
 */
@Component
public class RequirementSearchPredicateBuilder implements PredicateBuilder<RequirementSearchCriteria, QSpecRequirement> {
	@Autowired
	private RequirementRepository requirementRepository;

	@Autowired
	private SpecRequirementRepository specRequirementRepository;

	@Autowired
	private ObjectTypeAttributeRepository objectTypeAttributeRepository;

	@Autowired
	private ObjectAttributeRepository objectAttributeRepository;

	@Override
	@Transactional(readOnly = true)
	public Predicate build(RequirementSearchCriteria criteria, QSpecRequirement pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (!criteria.getAttributeSearch()) {
			if (!Criteria.isEmpty(criteria.getSearchQuery())) {
				predicates.add(pathBase.requirement.objectNumber.containsIgnoreCase(criteria.getSearchQuery())
						.or(pathBase.requirement.name.containsIgnoreCase(criteria.getSearchQuery()))
						.or(pathBase.requirement.description.containsIgnoreCase(criteria.getSearchQuery()))
						.or(pathBase.requirement.assignedTo.firstName.containsIgnoreCase(criteria.getSearchQuery()))
						.or(pathBase.requirement.assignedTo.lastName.containsIgnoreCase(criteria.getSearchQuery()))
						.and(pathBase.specification.eq(criteria.getSpecification())));

			} else if (!Criteria.isEmpty(criteria.getName())) {
				predicates.add(pathBase.requirement.name.containsIgnoreCase(criteria.getName()).and(pathBase.specification.eq(criteria.getSpecification())));
			} else if (!Criteria.isEmpty(criteria.getVersion())) {
				predicates.add(pathBase.requirement.version.eq(criteria.getVersion()).and(pathBase.specification.eq(criteria.getSpecification())));
			} else if (!Criteria.isEmpty(criteria.getDescription())) {
				predicates.add(pathBase.requirement.description.containsIgnoreCase(criteria.getDescription()).and(pathBase.specification.eq(criteria.getSpecification())));
			} else if (!Criteria.isEmpty(criteria.getObjectNumber())) {
				predicates.add(pathBase.requirement.objectNumber.containsIgnoreCase(criteria.getObjectNumber()).and(pathBase.specification.eq(criteria.getSpecification())));
			} else if (!Criteria.isEmpty(criteria.getStatus())) {
				predicates.add(pathBase.requirement.status.eq(RequirementStatus.valueOf(criteria.getStatus())).and(pathBase.specification.eq(criteria.getSpecification())));
			} else if (criteria.getAssignedTo().length > 0) {
				for (Integer person : criteria.getAssignedTo()) {
					predicates.add(pathBase.requirement.assignedTo.id.eq(person)
							.and(pathBase.specification.eq(criteria.getSpecification())));
				}

			} else if (!Criteria.isEmpty(criteria.getPlannedFinishdate())) {
				Date requirement = requirementRepository.getMinDate();
				String date = "";
				String pattern = "dd/MM/yyyy";
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
				date = simpleDateFormat.format(requirement);
				String concatDate = date + ":" + criteria.getPlannedFinishdate();
				Predicate datePredicate = Criteria.getDatePredicate(pathBase.requirement.plannedFinishDate, concatDate);
				if (datePredicate != null) {
					List<Predicate> predicateList = new ArrayList();
					predicateList.add(datePredicate);
					predicateList.add(pathBase.specification.eq(criteria.getSpecification()));
					predicates.add(ExpressionUtils.allOf(predicateList));
				}

			}
		}
		if (criteria.getAttributeSearch()) {
			List<SpecRequirement> specRequirements = specRequirementRepository.findBySpecification(criteria.getSpecification());
			if (specRequirements.size() > 0) {
				List<Integer> ints = new ArrayList<>();
				for (SpecRequirement specRequirement : specRequirements) {
					for (AttributeSearchDto attributeSearchDto : criteria.getSearchAttributes()) {
						if (attributeSearchDto.getText() != null && !Criteria.isEmpty(attributeSearchDto.getText())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getText());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}

						}
						if (attributeSearchDto.getLongText() != null && !Criteria.isEmpty(attributeSearchDto.getLongText())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getLongText());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getInteger() != null && !Criteria.isEmpty(attributeSearchDto.getInteger())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getInteger());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getList() != null && !attributeSearchDto.getObjectTypeAttribute().isListMultiple() && !Criteria.isEmpty(attributeSearchDto.getList())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getList());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getMListValue().length > 0 && attributeSearchDto.getObjectTypeAttribute().isListMultiple()) {
							List<Predicate> predicates1 = new ArrayList();
							String[] strs = attributeSearchDto.getMListValue();
							for (String str : strs) {
								List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndmListValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), str);
								if (integers.size() > 0) {
									predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
								}
							}
							predicates.add((ExpressionUtils.allOf(predicates1)));
						}
						if (attributeSearchDto.getABoolean() != null && attributeSearchDto.getBooleanSearch()) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndBooleanValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getABoolean());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getADouble() != 0.0 && attributeSearchDto.getDoubleSearch()) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndDoubleValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(),
									attributeSearchDto.getADouble());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getCurrency() != null && !Criteria.isEmpty(attributeSearchDto.getCurrency())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getCurrency());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
						if (attributeSearchDto.getDate() != null && !Criteria.isEmpty(attributeSearchDto.getDate())) {
							try {
								Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(attributeSearchDto.getDate());
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
								String parsedDate = formatter.format(initDate);
								List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), parsedDate);
								if (integers.size() > 0) {
									predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}

						}
						if (attributeSearchDto.getTime() != null && !Criteria.isEmpty(attributeSearchDto.getTime())) {
							List<Integer> integers = objectAttributeRepository.findObjectIdsByObjectIdAndAttValue(specRequirement.getRequirement().getId(), attributeSearchDto.getObjectTypeAttribute().getId(), attributeSearchDto.getTime());
							if (integers.size() > 0) {
								predicates.add(pathBase.requirement.id.in(integers).and(pathBase.specification.eq(criteria.getSpecification())));
							}
						}
					}
				}


				/*predicates.add(pathBase.requirement.id.in(integers));*/
			}
		}
		return ExpressionUtils.anyOf(predicates);
	}

	public Predicate getDefaultPredicate(RequirementSearchCriteria criteria, QRequirement pathBase) {
		List<Predicate> predicates = new ArrayList<>();
		if (!Criteria.isEmpty(criteria.getSearchQuery())) {
			predicates.add(pathBase.objectNumber.containsIgnoreCase(criteria.getSearchQuery())
					.or(pathBase.name.containsIgnoreCase(criteria.getSearchQuery()))
					.or(pathBase.description.containsIgnoreCase(criteria.getSearchQuery())));
		}
		predicates.add(pathBase.latest.eq(true));
		return ExpressionUtils.allOf(predicates);
	}
}

