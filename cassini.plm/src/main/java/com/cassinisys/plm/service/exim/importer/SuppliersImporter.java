package com.cassinisys.plm.service.exim.importer;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.dto.RowData;
import com.cassinisys.platform.model.dto.TableData;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.LovService;
import com.cassinisys.plm.controller.mfr.SupplierTypeController;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.mfr.PLMSupplier;
import com.cassinisys.plm.model.mfr.PLMSupplierPart;
import com.cassinisys.plm.model.mfr.PLMSupplierType;
import com.cassinisys.plm.model.plm.ItemClass;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.SupplierPartRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.mfr.SupplierTypeRepository;
import com.cassinisys.plm.service.classification.PLMSupplierTypeService;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.LifeCycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class SuppliersImporter {
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private Importer importer;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierTypeRepository supplierTypeRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PLMSupplierTypeService supplierTypeService;

    private LovService lovService;

    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private LifeCycleService lifecycleService;

    public static ConcurrentMap<String, PLMSupplierType> rootSupplierTypesMap = new ConcurrentHashMap<>();
    public static ConcurrentMap<String, PLMSupplierType> supplierTypesMapByPath = new ConcurrentHashMap<>();
    private static PLMLifeCycle plmLifeCycle;

    private void getDefaultSupplierLifecycle() {
        plmLifeCycle = lifecycleService.findLifecycleByName("Default Supplier Lifecycle");
    }


    public void loadSupplierClassificationTree() {
        getDefaultSupplierLifecycle();
        rootSupplierTypesMap = new ConcurrentHashMap<>();
        supplierTypesMapByPath = new ConcurrentHashMap<>();
        List<PLMSupplierType> rootTypes = supplierTypeService.getClassificationTree();
        for (PLMSupplierType rootType : rootTypes) {
            rootSupplierTypesMap.put(rootType.getName(), rootType);
            supplierTypesMapByPath.put(rootType.getName(), rootType);
        }
    }

    public PLMSupplierType getSupplierTypeByPath(String path) {
        int index = path.indexOf('/');
        String name;
        if (index != -1) {
            name = path.substring(0, index);
            PLMSupplierType supplierType = rootSupplierTypesMap.get(name);
            if (supplierType != null) {
                return supplierType.getChildTypeByPath(path.substring(index + 1));
            } else {
                return supplierType;
            }

        } else {
            name = path;
            return rootSupplierTypesMap.get(name);
        }
    }

    public PLMSupplierType getSupplierType(String typePath) {
        PLMSupplierType supplierType = supplierTypesMapByPath.get(typePath);
        if (supplierType == null) {
            supplierType = getSupplierTypeByPath(typePath);
            if (supplierType == null) {
                supplierType = createSupplierTypeByPath(null, typePath);
            }

            supplierTypesMapByPath.put(typePath, supplierType);
        }
        return supplierType;
    }


    public PLMSupplierType createSupplierTypeByPath(PLMSupplierType parentType, String path) {
        String name;
        int index = path.indexOf('/');
        String restOfPath = null;
        if (index != -1) {
            name = path.substring(0, index);
            restOfPath = path.substring(index + 1);
        } else {
            name = path;
        }

        if (parentType == null) {
            parentType = rootSupplierTypesMap.get(name);
            if (parentType == null) {
                parentType = new PLMSupplierType();
                parentType.setName(name);
                parentType.setDescription(name);
                parentType.setLifecycle(plmLifeCycle);
                parentType = supplierTypeService.create(parentType);
                rootSupplierTypesMap.put(name, parentType);

            }
            if (restOfPath != null) {
                parentType = createSupplierTypeByPath(parentType, restOfPath);
            }
        } else if (parentType.getChildTypeByPath(name) == null) {
            PLMSupplierType childsupplierType = new PLMSupplierType();
            childsupplierType.setParentType(parentType.getId());
            childsupplierType.setName(name);
            childsupplierType.setDescription(name);
            childsupplierType.setLifecycle(parentType.getLifecycle());
            childsupplierType = supplierTypeService.create(childsupplierType);
            parentType.getChildren().add(childsupplierType);
            if (restOfPath != null) {
                parentType = createSupplierTypeByPath(childsupplierType, restOfPath);
            } else {
                return childsupplierType;
            }
        } else if (parentType.getChildTypeByPath(name) != null) {
            if (restOfPath != null) {
                parentType = createSupplierTypeByPath(parentType, restOfPath);
            } else {
                return parentType.getChildTypeByPath(name);
            }
        }
        return parentType;
    }

    public void importSupplierAndSupplierParts(TableData tableData) throws ParseException {
        this.loadSupplierClassificationTree();
        Map<String, PLMSupplierType> dbMSupplierTypesMap = new LinkedHashMap();
        Map<String, PLMSupplier> dbSuppliersMap = new LinkedHashMap();
        Map<String, PLMManufacturerPart> dbManuPartsMap = new LinkedHashMap();
        Map<String, PLMSupplierPart> dbSupplierPartsMap = new LinkedHashMap();
        List<PLMSupplier> dbSuppliers = supplierRepository.findAll();
        List<PLMSupplierPart> dbSupplierParts = supplierPartRepository.findAll();
        List<PLMManufacturerPart> dbManufactureParts = manufacturerPartRepository.findAll();
        List<PLMSupplierType> dbMSupplierTypes = supplierTypeRepository.findAll();
        dbMSupplierTypesMap = dbMSupplierTypes.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        dbSuppliersMap = dbSuppliers.stream().collect(Collectors.toMap(r -> r.getName(), r -> r));
        dbSupplierPartsMap = dbSupplierParts.stream().collect(Collectors.toMap(r -> r.getSupplier().toString() + r.getManufacturerPart().getId().toString(), r -> r));
        dbManuPartsMap = dbManufactureParts.stream().collect(Collectors.toMap(r -> r.getPartNumber(), r -> r));
        List<PLMSupplier> suppliers = createSuppliers(tableData, dbSuppliersMap, dbMSupplierTypesMap);
        if (suppliers.size() > 0) {
            createSupplierParts(tableData, suppliers, dbSupplierPartsMap, dbManuPartsMap);
        }


    }


    private List<PLMSupplier> createSuppliers(TableData tableData, Map<String, PLMSupplier> dbManufacturersMap, Map<String, PLMSupplierType> dbManufacturerTypesMap) {
        List<PLMSupplier> suppliers2 = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {

            if (stringListHashMap.containsKey("Supplier Name".trim()) && stringListHashMap.containsKey("Supplier Type".trim()) || stringListHashMap.containsKey("Type Path".trim())) {
                String name = stringListHashMap.get("Supplier Name".trim());
                if (name != null && !name.trim().equals("")) {
                    String supplierTypeName = stringListHashMap.get("Supplier Type".trim());
                    String typePath = stringListHashMap.get("Type Path".trim());
                    if (supplierTypeName != null && supplierTypeName != "") {
                        PLMSupplierType supplierType = dbManufacturerTypesMap.get(supplierTypeName);
                        if (supplierType != null) {
                            PLMSupplier supplier = dbManufacturersMap.get(name);
                            if (supplier != null) {
                                suppliers2.add(supplier);
                            } else {
                                PLMSupplier manufacturer1 = createSupplier(i, name, supplierType, stringListHashMap, dbManufacturersMap);
                                suppliers2.add(manufacturer1);
                            }

                        } else {
                            if (typePath != null && typePath != "") {
                                PLMSupplierType plmSupplierType = this.getSupplierType(typePath);
                                dbManufacturerTypesMap.put(plmSupplierType.getName(), plmSupplierType);
                                if (plmSupplierType != null) {
                                    PLMSupplier supplier = dbManufacturersMap.get(name);
                                    if (supplier != null) {
                                        suppliers2.add(supplier);
                                    } else {
                                        PLMSupplier manufacturer1 = createSupplier(i, name, plmSupplierType, stringListHashMap, dbManufacturersMap);
                                        suppliers2.add(manufacturer1);
                                    }
                                } else {
                                    throw new CassiniException(messageSource.getMessage("please_provide_supplier_type_path_for_row_number" + (i),
                                            null, "Please provide Supplier Type Path or Supplier Type for row number:" + (i), LocaleContextHolder.getLocale()));
                                }
                            }
                        }
                    } else {

                        if (typePath != null && typePath != "") {
                            PLMSupplierType plmSupplierType = this.getSupplierType(typePath);
                            dbManufacturerTypesMap.put(plmSupplierType.getName(), plmSupplierType);
                            if (plmSupplierType != null) {
                                PLMSupplier supplier = dbManufacturersMap.get(name);
                                if (supplier != null) {
                                    suppliers2.add(supplier);
                                } else {
                                    PLMSupplier manufacturer1 = createSupplier(i, name, plmSupplierType, stringListHashMap, dbManufacturersMap);
                                    suppliers2.add(manufacturer1);
                                }
                            }

                        } else {
                            throw new CassiniException(messageSource.getMessage("please_provide_supplier_type_path_for_row_number" + (i),
                                    null, "Please provide Supplier Type Path or Supplier Type for row number:" + (i), LocaleContextHolder.getLocale()));
                        }

                    }


                } else {
                    throw new CassiniException("Please provide Supplier Name for row :" + i);
                }

            } else {
                throw new CassiniException("Please provide Supplier Name and Supplier Type Column also");
            }
            i++;
        }
        if (suppliers2.size() > 0) {
            suppliers2 = supplierRepository.save(suppliers2);
        }


        return suppliers2;
    }

    /*
    * Create Suppliers
    * */
    private PLMSupplier createSupplier(Integer i, String name, PLMSupplierType supplierType, RowData stringListHashMap, Map<String, PLMSupplier> dbManufacturersMap) {
        String description = stringListHashMap.get("Supplier Description".trim());
        String address = stringListHashMap.get("Address").trim();
        String city = stringListHashMap.get("City".trim());
        String country = stringListHashMap.get("Country".trim());
        String postalCode = stringListHashMap.get("Postal Code".trim());
        String phoneNumber = stringListHashMap.get("Phone Number".trim());
        String mobileNumber = stringListHashMap.get("Mobile Number".trim());
        String faxNumber = stringListHashMap.get("Fax Number".trim());
        String email = stringListHashMap.get("E-Mail".trim());
        String website = stringListHashMap.get("Website".trim());
        String lifeCycle = stringListHashMap.get("Supplier LifeCycle".trim());
        PLMSupplier supplier = new PLMSupplier();
        supplier.setSupplierType(supplierType);
        supplier.setName(name);
        supplier.setAddress(address);
        supplier.setCity(city);
        supplier.setCountry(country);
        supplier.setPostalCode(postalCode);
        supplier.setMobileNumber(mobileNumber);
        supplier.setFaxNumber(faxNumber);
        supplier.setEmail(email);
        supplier.setWebSite(website);
        supplier.setPhoneNumber(phoneNumber);
        supplier.setDescription(description);
        PLMLifeCyclePhase phase = this.importer.getPhaseByName(supplierType.getLifecycle(), lifeCycle != null ? lifeCycle : "Unqualified");

        if (phase != null) {
            supplier.setLifeCyclePhase(phase);
        }
        dbManufacturersMap.put(supplier.getName(), supplier);

        return supplier;
    }

    private List<PLMSupplierPart> createSupplierParts(TableData tableData, List<PLMSupplier> suppliers, Map<String, PLMSupplierPart> dbSupplierPartsMap, Map<String, PLMManufacturerPart> dbManufacturerPartsMap) {
        List<PLMSupplierPart> supplierParts = new LinkedList<>();
        int i = 0;
        for (RowData stringListHashMap : tableData.getRows()) {
            if (stringListHashMap.containsKey("Supplier Part Number".trim()) && stringListHashMap.containsKey("Manufacturer Part Number".trim())) {

                String number = stringListHashMap.get("Manufacturer Part Number".trim());
                if (number != null && number != "") {
                    PLMSupplier supplier = suppliers.get(i);
                    PLMManufacturerPart manufacturerPart = dbManufacturerPartsMap.get(number);
                    if (supplier != null && manufacturerPart != null) {
                        PLMSupplierPart existPart = dbSupplierPartsMap.get(supplier.getId().toString() + manufacturerPart.getId());
                        if (existPart == null) {
                            PLMSupplierPart supplierPartExist = dbSupplierPartsMap.get(number);
                            if (supplierPartExist == null) {
                                PLMSupplierPart supplierPart = supplierPart(i, number, stringListHashMap, supplier.getId(), manufacturerPart, dbSupplierPartsMap);
                                if (supplierPart != null) {
                                    supplierParts.add(supplierPart);
                                }
                            }


                        } else {
                            supplierParts.add(existPart);
                        }


                    }
                }

            } else {
                throw new CassiniException("Please provide Supplier Part Number and Manufacturer Part Number Column also");
            }

            i++;
        }

        if (supplierParts.size() > 0) {
            supplierParts = supplierPartRepository.save(supplierParts);
        }
        return supplierParts;
    }

    /*
    * Create Supplier Parts
    * */
    private PLMSupplierPart supplierPart(Integer i, String number, RowData stringListHashMap, Integer supplierId, PLMManufacturerPart manufacturerPart, Map<String, PLMSupplierPart> dbSupplierPartHashMap) {
        String supplierPartNumber = stringListHashMap.get("Supplier Part Number".trim());
        if (supplierPartNumber == null || supplierPartNumber == "") {
            supplierPartNumber = number;
        }
        PLMSupplierPart supplierNewPart = new PLMSupplierPart();
        supplierNewPart.setSupplier(supplierId);
        supplierNewPart.setManufacturerPart(manufacturerPart);
        supplierNewPart.setPartNumber(supplierPartNumber);
        dbSupplierPartHashMap.put(supplierPartNumber, supplierNewPart);
        return supplierNewPart;

    }

}
