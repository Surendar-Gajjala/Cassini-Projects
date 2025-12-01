package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;

import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.repo.production.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;


@Service
@Transactional
public class BOMItemService {

	@Autowired
	BOMItemRepository bomItemRepository;

	@Autowired
	MaterialTypeRepository materialTypeRepository;

	@Autowired
	MaterialCategoryRepository materialCategoryRepository;

	@Autowired
	MaterialRepository materialRepository;

	@Autowired
	BOMRepository bomRepository;
	
	public ERPBomItem createBOMItem(ERPBomItem bomItem) {
		return bomItemRepository.save(bomItem);

	}
	
	public ERPBomItem updateBOMItem(ERPBomItem bomItem) {
		checkNotNull(bomItem);
		checkNotNull(bomItem.getRowId());
		if (bomItemRepository.findOne(bomItem.getRowId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return bomItemRepository.save(bomItem);

	}

	
	public ERPBomItem getBOMItemById(Integer id) {
		return bomItemRepository.findOne(id);
	}

	
	public void deleteBOMItem(Integer id) {
		checkNotNull(id);
		bomItemRepository.delete(id);
	}



	public List<ERPBomItem> getAllBOMItems() {
		List<ERPBomItem> bomItems=null;

		bomItems=bomItemRepository.findAll();

		for(ERPBomItem bomItem: bomItems){

			if(bomItem!=null){

				if(bomItem.getItemType()== BomItemType.TYPE){

					ERPMaterialType type= materialTypeRepository.findOne(bomItem.getItemId());

					if(type!=null){
						bomItem.setBomItemObj(type.getName());
					}
				}else if(bomItem.getItemType()==BomItemType.CATEGORY){

					ERPMaterialCategory cat= materialCategoryRepository.findOne(bomItem.getItemId());

					if(cat!=null){
						bomItem.setBomItemObj(cat.getName());

					}
				}else if(bomItem.getItemType()==BomItemType.MATERIAL){

					ERPMaterial material=materialRepository.findOne(bomItem.getItemId());

					if(material!=null){
						bomItem.setBomItemObj(material.getName());

					}

				}

			}
		}

		return bomItems;

	}



	public List<ERPBomItem> getAllBOMItemsByBom(Integer bomId) {
		List<ERPBomItem> bomItems=null;

		ERPBom bom=bomRepository.findOne(bomId);


		if(bom!=null){
			bomItems= bomItemRepository.findByBom(bom);
		}

		for(ERPBomItem bomItem: bomItems){

			if(bomItem!=null){

				if(bomItem.getItemType()== BomItemType.TYPE){

					ERPMaterialType type= materialTypeRepository.findOne(bomItem.getItemId());

					if(type!=null){
						bomItem.setBomItemObj(type.getName());
					}
				}else if(bomItem.getItemType()==BomItemType.CATEGORY){

					ERPMaterialCategory cat= materialCategoryRepository.findOne(bomItem.getItemId());

					if(cat!=null){
						bomItem.setBomItemObj(cat.getName());

					}
				}else if(bomItem.getItemType()==BomItemType.MATERIAL){

					ERPMaterial material=materialRepository.findOne(bomItem.getItemId());

					if(material!=null){
						bomItem.setBomItemObj(material.getName());

					}

				}

			}
		}

		return bomItems;

	}



}
