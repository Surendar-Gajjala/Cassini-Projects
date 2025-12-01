package com.cassinisys.platform.filtering;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.EntityPathBase;

/**
 * @author reddy
 */
public interface PredicateBuilder<C extends Criteria, B extends EntityPathBase<?>> {

	public Predicate build(C criteria, B pathBase);

}
