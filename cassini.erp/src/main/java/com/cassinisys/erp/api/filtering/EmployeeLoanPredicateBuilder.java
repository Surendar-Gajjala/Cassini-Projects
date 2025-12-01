package com.cassinisys.erp.api.filtering;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cassinisys.erp.model.api.criteria.EmployeeLoanCriteria;
import com.cassinisys.erp.model.hrm.LoanStatus;
import com.cassinisys.erp.model.hrm.QERPEmployeeLoan;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
@Component
public class EmployeeLoanPredicateBuilder implements PredicateBuilder<EmployeeLoanCriteria, QERPEmployeeLoan> {
	
	 @Override
	    public Predicate build(EmployeeLoanCriteria criteria, QERPEmployeeLoan pathBase) {
	        List<Predicate> predicates = new ArrayList<>();

	        if(!Criteria.isEmpty(criteria.getReason())) {
	            predicates.add((pathBase.reason.containsIgnoreCase(criteria.getReason())));
	        }
	        
	        if(!Criteria.isEmpty(criteria.getApprovedBy())) {
	            String s = criteria.getApprovedBy().trim();
	            if(s.startsWith("=")) {
	                Integer d = Integer.parseInt(s.substring(1));
	                predicates.add(pathBase.approvedBy.eq(d));
	            }
	            if(s.startsWith(">")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.approvedBy.goe(d));
	            }
	            if(s.startsWith("<")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.approvedBy.loe(d));
	            }
	        }
	        
	        if(!Criteria.isEmpty(criteria.getType())) {
	            predicates.add((pathBase.type.name.containsIgnoreCase(criteria.getType())));
	        }

	        Predicate p = Criteria.getDateRangePredicate(pathBase.approvedDate, criteria.getApprovedDate());
	        if(p != null) {
	            predicates.add(p);
	        }
	        
	        Predicate p1 = Criteria.getDateRangePredicate(pathBase.requestedDate, criteria.getRequestedDate());
	        if(p1 != null) {
	            predicates.add(p1);
	        }
	        
	        Predicate p2 = Criteria.getDateRangePredicate(pathBase.paidOffDate, criteria.getPaidOffDate());
	        if(p2 != null) {
	            predicates.add(p2);
	        }
	        
	        if(!Criteria.isEmpty(criteria.getTerm())) {
	            String s = criteria.getTerm().trim();
	            if(s.startsWith("=")) {
	                Integer d = Integer.parseInt(s.substring(1));
	                predicates.add(pathBase.term.eq(d));
	            }
	            if(s.startsWith(">")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.term.goe(d));
	            }
	            if(s.startsWith("<")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.term.loe(d));
	            }
	        }
	        
	        if(!Criteria.isEmpty(criteria.getAmount())) {
	            String s = criteria.getAmount().trim();
	            if(s.startsWith("=")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.amount.eq(d));
	            }
	            if(s.startsWith(">")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.amount.goe(d));
	            }
	            if(s.startsWith("<")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.amount.loe(d));
	            }
	        }
	        
	        if(!Criteria.isEmpty(criteria.getBalance())) {
	            String s = criteria.getBalance().trim();
	            if(s.startsWith("=")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.balance.eq(d));
	            }
	            if(s.startsWith(">")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.balance.goe(d));
	            }
	            if(s.startsWith("<")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.balance.loe(d));
	            }
	        }
	        
	        if(!Criteria.isEmpty(criteria.getEmployee())) {
	            String s = criteria.getEmployee().trim();
	            if(s.startsWith("=")) {
	                Integer d = Integer.parseInt(s.substring(1));
	                predicates.add(pathBase.employee.eq(d));
	            }
	            if(s.startsWith(">")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.employee.goe(d));
	            }
	            if(s.startsWith("<")) {
	                Double d = Double.parseDouble(s.substring(1));
	                predicates.add(pathBase.employee.loe(d));
	            }
	        }
	        
	       
	       
	        if (!Criteria.isEmpty(criteria.getStatus())) {
	            String s = criteria.getStatus().trim();
	            String arr[] = s.split(",");
	            List<Predicate> statuses = new ArrayList<>();

	            for(String status : arr) {
	                statuses.add(pathBase.status.eq(LoanStatus.valueOf(status.trim())));
	            }
	            predicates.add(ExpressionUtils.anyOf(statuses));
	        }
	        
	       
	        return ExpressionUtils.allOf(predicates);
	    }

}
