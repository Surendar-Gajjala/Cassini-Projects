package com.cassinisys.erp.service.production;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cassinisys.erp.model.production.ERPProductCategory;
import com.cassinisys.erp.repo.production.ProductCategoryRepository;
import com.cassinisys.erp.service.paging.CrudService;

/**
 * Created by reddy on 8/4/15.
 */
@Service
@Transactional
public class ProductCategoryService implements CrudService<ERPProductCategory, Integer> {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @Override
    public ERPProductCategory create(ERPProductCategory erpProductCategory) {
        return productCategoryRepository.save(erpProductCategory);
    }

    @Override
    public ERPProductCategory update(ERPProductCategory erpProductCategory) {
        return productCategoryRepository.save(erpProductCategory);
    }

    @Override
    public void delete(Integer id) {
        productCategoryRepository.delete(id);
    }

    @Override
    public ERPProductCategory get(Integer id) {
        return productCategoryRepository.findOne(id);
    }

    @Override
    public List<ERPProductCategory> getAll() {
        return productCategoryRepository.findAll();
    }

    public List<ERPProductCategory> getChildren(Integer parent) {
        return productCategoryRepository.findByParent(parent);
    }

    public List<ERPProductCategory> getCategoryTree() {
        List<ERPProductCategory> roots = getChildren(null);
        if(roots != null) {
            for(ERPProductCategory root : roots) {
                visitChildren(root);
            }
        }

        return roots;
    }

    private void visitChildren(ERPProductCategory category) {
        List<ERPProductCategory> children = getChildren(category.getId());
        if(children != null) {
            for(ERPProductCategory child : children) {
                visitChildren(child);
            }

            category.setChildren(children);

        }
    }

    public List<Integer> flattenCategoryPath(ERPProductCategory root) {
        List<Integer> collector = new ArrayList<>();
        collectChildren(root, collector);

        return collector;

    }

    private void collectChildren(ERPProductCategory parent, List<Integer> collector) {
        if(parent != null) {
            collector.add(parent.getId());
            List<ERPProductCategory> children = getChildren(parent.getId());
            for (ERPProductCategory child : children) {
                collectChildren(child, collector);
            }
        }
    }

}
