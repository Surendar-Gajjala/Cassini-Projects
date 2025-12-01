package com.cassinisys.platform.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.*;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by raghav on 18-06-2019.
 */
public class SecurityMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private PermissionCacheOptimizer permissionCacheOptimizer = null;

    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        MethodSecurityExpressionOperations rootObject = (MethodSecurityExpressionOperations) ctx.getRootObject().getValue();
        boolean debug = this.logger.isDebugEnabled();
        ArrayList retainList;

        if (debug) {
            this.logger.debug("Filtering with expression: " + filterExpression.getExpressionString());
        }
        if (filterTarget instanceof Page) {
            List<Object> arrayList = new ArrayList<>();
            for (Object val : ((Page) filterTarget).getContent()) {
                arrayList.add(val);
            }
            PageImpl page = (PageImpl) filterTarget;
            int number = page.getNumber();
            int size = page.getSize();
            Sort sort = page.getSort();
            filterTarget = arrayList;
            Collection var12 = (Collection) filterTarget;
            retainList = new ArrayList(var12.size());
            if (debug) {
                this.logger.debug("Filtering collection with " + var12.size() + " elements");
            }

            if (this.permissionCacheOptimizer != null) {
                this.permissionCacheOptimizer.cachePermissionsFor(rootObject.getAuthentication(), var12);
            }

            Iterator var13 = ((Collection) filterTarget).iterator();

            while (var13.hasNext()) {
                Object var14 = var13.next();
                rootObject.setFilterObject(var14);
                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(var14);
                }
            }

            if (debug) {
                this.logger.debug("Retaining elements: " + retainList);
            }
            PageRequest pageRequest = new PageRequest(number, size, sort);
            Pageable pageable1 = pageRequest;
            Page page1 = new PageImpl<>(retainList, pageable1, page.getTotalElements());
            return page1;
        }
        if (filterTarget instanceof Collection) {
            Collection var12 = (Collection) filterTarget;
            retainList = new ArrayList(var12.size());
            if (debug) {
                this.logger.debug("Filtering collection with " + var12.size() + " elements");
            }

            if (this.permissionCacheOptimizer != null) {
                this.permissionCacheOptimizer.cachePermissionsFor(rootObject.getAuthentication(), var12);
            }

            Iterator var13 = ((Collection) filterTarget).iterator();

            while (var13.hasNext()) {
                Object var14 = var13.next();
                rootObject.setFilterObject(var14);
                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(var14);
                }
            }

            if (debug) {
                this.logger.debug("Retaining elements: " + retainList);
            }

            var12.clear();
            var12.addAll(retainList);
            return filterTarget;
        } else if (!filterTarget.getClass().isArray()) {
            throw new IllegalArgumentException("Filter target must be a collection or array type, but was " + filterTarget);
        } else {
            Object[] array = (Object[]) ((Object[]) filterTarget);
            retainList = new ArrayList(array.length);
            if (debug) {
                this.logger.debug("Filtering array with " + array.length + " elements");
            }

            if (this.permissionCacheOptimizer != null) {
                this.permissionCacheOptimizer.cachePermissionsFor(rootObject.getAuthentication(), Arrays.asList(array));
            }

            Object[] filtered = array;
            int i = array.length;

            for (int var10 = 0; var10 < i; ++var10) {
                Object o = filtered[var10];
                rootObject.setFilterObject(o);
                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(o);
                }
            }

            if (debug) {
                this.logger.debug("Retaining elements: " + retainList);
            }

            filtered = (Object[]) ((Object[]) Array.newInstance(filterTarget.getClass().getComponentType(), retainList.size()));

            for (i = 0; i < retainList.size(); ++i) {
                filtered[i] = retainList.get(i);
            }

            return filtered;
        }

    }
}
