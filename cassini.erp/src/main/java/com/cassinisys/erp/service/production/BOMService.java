package com.cassinisys.erp.service.production;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.cassinisys.erp.model.production.*;
import com.cassinisys.erp.repo.production.MaterialCategoryRepository;
import com.cassinisys.erp.repo.production.MaterialRepository;
import com.cassinisys.erp.repo.production.MaterialTypeRepository;
import com.cassinisys.erp.repo.production.ProductCategoryRepository;
import com.cassinisys.erp.repo.production.ProductRepository;
import com.cassinisys.erp.repo.production.ProductTypeRepository;
import com.cassinisys.erp.repo.production.ProductionOrderDetailsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cassinisys.erp.api.exceptions.ERPException;
import com.cassinisys.erp.api.exceptions.ErrorCodes;
import com.cassinisys.erp.repo.production.BOMRepository;


@Service
@Transactional
public class BOMService {

	@Autowired
	BOMRepository bomRepository;

	@Autowired
	BOMItemService bomItemService;

	@Autowired
	ProductTypeRepository productTypeRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	MaterialTypeRepository materialTypeRepository;

	@Autowired
	MaterialCategoryRepository materialCategoryRepository;


	@Autowired
	ProductionOrderDetailsRepository productionOrderDetailsRepository;

	@Autowired
	MaterialRepository materialRepository;

	@Autowired
	ProductCategoryRepository productCategoryRepository;

	public ERPBom createBOM(ERPBom bom) {

		if(bom!=null && bom.getBomItems()!=null && bom.getBomItems().size()>0){

			for(ERPBomItem item: bom.getBomItems()){

				if(item!=null){
					item.setBom(bom);
				}
			}

		}
		return bomRepository.save(bom);

	}

	public ERPBom updateBOM(ERPBom bom) {
		checkNotNull(bom);
		checkNotNull(bom.getBomId());
		if (bomRepository.findOne(bom.getBomId()) == null) {
			throw new ERPException(HttpStatus.NOT_FOUND,
					ErrorCodes.RESOURCE_NOT_FOUND);
		}
		return bomRepository.save(bom);

	}


	public ERPBom getBOMById(Integer id) {
		return bomRepository.findOne(id);
	}


	public void deleteBOM(Integer id) {
		checkNotNull(id);
		bomRepository.delete(id);
	}


	public List<ERPBom> getAllBOMs() {
		List<ERPBom> boms=null;

		boms=bomRepository.findAll();

		for(ERPBom bom: boms){

			if(bom!=null){

				if(bom.getType()== BomType.TYPE){

					ERPProductType type= productTypeRepository.findOne(bom.getObjId());

					if(type!=null){
						bom.setBomObj(type.getName());
					}
				}else if(bom.getType()==BomType.CATEGORY){

					ERPProductCategory cat= productCategoryRepository.findOne(bom.getObjId());

					if(cat!=null){
						bom.setBomObj(cat.getName());

					}
				}else if(bom.getType()==BomType.PRODUCT){

					ERPProduct product=productRepository.findOne(bom.getObjId());

					if(product!=null){
						bom.setBomObj(product.getName());

					}

				}

			}
		}

		return boms;
	}

	public Page<ERPBom> findAll(Pageable pageable) {

		return bomRepository.findAll(pageable);
	}

	/*public ProductionItemsBomDTO getAllBomItems(List<Integer> ids){

		ProductionItemsBomDTO dto= new ProductionItemsBomDTO();

		for(Integer id:ids){

			if(id>0){

				List<ERPBomItem> items=	bomItemService.getAllBOMItemsByBom(id);

				if(items==null ){
					items= new ArrayList<ERPBomItem>();
				}else{

					for(ERPBomItem bomItem:items){

						if(bomItem!=null){
							if(bomItem.getItemType()== BomItemType.MATERIAL){

								bomItem.setMaterial(materialRepository.findOne(bomItem.getItemId()));
							}else if(bomItem.getItemType()== BomItemType.TYPE){

								ERPMaterialType type=materialTypeRepository.findOne(bomItem.getItemId());

								List<ERPMaterial> mats= materialRepository.findByType(type);

								if(mats!=null){
									bomItem.setMaterialList(mats);
								}
							}else if(bomItem.getItemType()==BomItemType.CATEGORY){

								ERPMaterialCategory category=materialCategoryRepository.findOne(bomItem.getItemId());


								List<ERPMaterial> mats= materialRepository.findByCategory(category);

								if(mats!=null){
									bomItem.setMaterialList(mats);
								}

							}
						}
					}

					dto.getBomItems().put(id,items);

				}

			}


		}
		return dto;

	}
*/
	public List<ERPProductionOrderItem> getAllProductionOrderBomItems(List<ERPProductionOrderItem> orderItems){

		List<ERPProductionOrderItem> finalOrderItems= new ArrayList<ERPProductionOrderItem>();

		for(ERPProductionOrderItem orderItem:orderItems){

			if(orderItem!=null  && orderItem.getItemId()!=null && orderItem.getItemId()>0 && orderItem.getBom()!=null && orderItem.getBom().getBomId()>0){

				ERPProductionOrderItem existOrderItem=	productionOrderDetailsRepository.findOne(orderItem.getItemId());

				if(existOrderItem.getBom().getBomId()==orderItem.getBom().getBomId()){

				//List<ERPProductionItemBom>	pItemBoms=  new  ArrayList<ERPProductionItemBom>(newOrderItem.getItemBoms());// newOrderItem.getItemBoms().toArray(pItemBoms);
				orderItem.setItemBoms(new HashSet<ERPProductionItemBom>());
				for(ERPProductionItemBom pItemBom: existOrderItem.getItemBoms()){

					if(pItemBom!=null){

						if(pItemBom.getBomItem().getItemType()== BomItemType.TYPE){

							ERPMaterialType type=materialTypeRepository.findOne(pItemBom.getBomItem().getItemId());

							List<ERPMaterial> mats= materialRepository.findByType(type);

							if(mats!=null){
								pItemBom.setMaterialList(mats);
							}
						}else if(pItemBom.getBomItem().getItemType()==BomItemType.CATEGORY){

							ERPMaterialCategory category=materialCategoryRepository.findOne(pItemBom.getBomItem().getItemId());


							List<ERPMaterial> mats= materialRepository.findByCategory(category);

							if(mats!=null){
								pItemBom.setMaterialList(mats);
							}

						}

					}
					orderItem.getItemBoms().add(pItemBom);
				}
				}else{
						orderItem.setItemBoms(new HashSet<ERPProductionItemBom>());

						List<ERPBomItem> items=	bomItemService.getAllBOMItemsByBom(orderItem.getBom().getBomId());

						if(items==null ){
							items= new ArrayList<ERPBomItem>();
						}else{

							for(ERPBomItem bomItem:items){
								ERPProductionItemBom pItemBom= new ERPProductionItemBom();

								if(bomItem!=null){

									pItemBom.setBomItem(bomItem);

									if(bomItem.getItemType()== BomItemType.MATERIAL){
										ERPMaterial material=materialRepository.findOne(bomItem.getItemId());
										pItemBom.setMaterial(material);
									}else if(bomItem.getItemType()== BomItemType.TYPE){

										ERPMaterialType type=materialTypeRepository.findOne(bomItem.getItemId());

										List<ERPMaterial> mats= materialRepository.findByType(type);

										if(mats!=null){
											pItemBom.setMaterialList(mats);
										}
									}else if(bomItem.getItemType()==BomItemType.CATEGORY){

										ERPMaterialCategory category=materialCategoryRepository.findOne(bomItem.getItemId());


										List<ERPMaterial> mats= materialRepository.findByCategory(category);

										if(mats!=null){
											pItemBom.setMaterialList(mats);
										}

									}
								}

								orderItem.getItemBoms().add(pItemBom);
							}

						}




				}

				finalOrderItems.add(orderItem);


			}else if(orderItem!=null  && (orderItem.getItemId()==null || orderItem.getItemId()<0)){

				List<ERPBomItem> items=	bomItemService.getAllBOMItemsByBom(orderItem.getBom().getBomId());

				if(items==null ){
					items= new ArrayList<ERPBomItem>();
				}else{

					for(ERPBomItem bomItem:items){
						ERPProductionItemBom pItemBom= new ERPProductionItemBom();

						if(bomItem!=null){


							pItemBom.setBomItem(bomItem);

							if(bomItem.getItemType()== BomItemType.MATERIAL){
								ERPMaterial material=materialRepository.findOne(bomItem.getItemId());
								pItemBom.setMaterial(material);
							}else if(bomItem.getItemType()== BomItemType.TYPE){

								ERPMaterialType type=materialTypeRepository.findOne(bomItem.getItemId());

								List<ERPMaterial> mats= materialRepository.findByType(type);

								if(mats!=null){
									pItemBom.setMaterialList(mats);
								}
							}else if(bomItem.getItemType()==BomItemType.CATEGORY){

								ERPMaterialCategory category=materialCategoryRepository.findOne(bomItem.getItemId());


								List<ERPMaterial> mats= materialRepository.findByCategory(category);

								if(mats!=null){
									pItemBom.setMaterialList(mats);
								}

							}
						}

						orderItem.getItemBoms().add(pItemBom);
					}

				}

				finalOrderItems.add(orderItem);
			}


		}
		return finalOrderItems;

	}
}
