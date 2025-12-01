package com.cassinisys.erp.api.filtering;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.EntityPathBase;

public interface PredicateBuilder<C extends Criteria, B extends EntityPathBase<?>> {

	public Predicate build(C criteria, B pathBase);

}