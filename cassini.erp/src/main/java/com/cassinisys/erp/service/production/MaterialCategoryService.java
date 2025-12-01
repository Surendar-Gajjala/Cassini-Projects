package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.cassinisys.erp.model.production.ERPProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.model.production.ERPMaterialCategory;
import com.cassinisys.erp.repo.production.MaterialCategoryRepository;

@Service
@Transactional
public class MaterialCategoryService {

	@Autowired
	MaterialCategoryRepository materialCategoryRepository;

	public ERPMaterialCategory createMaterialCategory(
			ERPMaterialCategory materialCategory) {
		return materialCategoryRepository.save(materialCategory);
	}


	public ERPMaterialCategory updateMaterialCategory(
			ERPMaterialCategory materialCategory) {

		checkNotNull(materialCategory);
		checkNotNull(materialCategory.getId());
		if (materialCategoryRepository.findOne(materialCategory.getId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return materialCategoryRepository.save(materialCategory);

	}


	public ERPMaterialCategory getMaterialCategory(Integer id) {
		return materialCategoryRepository.findOne(id);
	}


	public void deleteMaterialCategory(Integer id) {
		checkNotNull(id);
		materialCategoryRepository.delete(id);
	}


	public List<ERPMaterialCategory> getAllMaterialCategories() {
		return materialCategoryRepository.findAll();
	}


	public List<ERPMaterialCategory> getChildren(Integer parent) {
		return materialCategoryRepository.findByParent(parent);
	}

	public List<ERPMaterialCategory> getCategoryTree() {
		List<ERPMaterialCategory> roots = getChildren(null);
		if(roots != null) {
			for(ERPMaterialCategory root : roots) {
				visitChildren(root);
			}
		}

		return roots;
	}

	private void visitChildren(ERPMaterialCategory category) {
		List<ERPMaterialCategory> children = getChildren(category.getId());
		if(children != null) {
			for(ERPMaterialCategory child : children) {
				visitChildren(child);
			}

			category.setChildren(children);

		}
	}

	public List<Integer> flattenCategoryPath(ERPMaterialCategory root) {
		List<Integer> collector = new ArrayList<>();
		collectChildren(root, collector);

		return collector;

	}

	private void collectChildren(ERPMaterialCategory parent, List<Integer> collector) {
		if(parent != null) {
			collector.add(parent.getId());
			List<ERPMaterialCategory> children = getChildren(parent.getId());
			for (ERPMaterialCategory child : children) {
				collectChildren(child, collector);
			}
		}
	}

}


