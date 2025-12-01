package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.plm.model.cm.PLMECO;
import com.cassinisys.plm.model.dto.ItemToItemCompareDTO;
import com.cassinisys.plm.model.mfr.PLMManufacturer;
import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.wf.PLMWorkflowDefinition;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.ProjectRepository;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 25-08-2020.
 */
@Service
public class ComparisonService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemAttributeRepository itemAttributeRepository;
    @Autowired
    private ItemRevisionAttributeRepository itemRevisionAttributeRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ECORepository ecoRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository workFlowDefinitionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;

    @Transactional(readOnly = true)
    public ItemToItemCompareDTO getObjectsForSameRevisionItemsCompare(PLMItemRevision fromRev, PLMItemRevision
            toRev) throws InterruptedException {

        ItemToItemCompareDTO finalDto = new ItemToItemCompareDTO();
        List<ItemToItemCompareDTO> finalList = new LinkedList<>();
        /*
        * Same Item With Different Revision
        * */
        if (fromRev.getItemMaster().equals(toRev.getItemMaster())) {
            PLMItemRevision fromRevItem = null;
            PLMItemRevision toRevItem = null;
            if (fromRev.getCreatedDate().after(toRev.getCreatedDate())) {
                fromRevItem = toRev;
                toRevItem = fromRev;
            }
            if (fromRev.getCreatedDate().before(toRev.getCreatedDate())) {
                fromRevItem = fromRev;
                toRevItem = toRev;
            }

            PLMItem fromItem = itemRepository.findOne(fromRevItem.getItemMaster());
            PLMItem toItem = itemRepository.findOne(toRevItem.getItemMaster());
            finalDto.setFromItem(fromItem.getItemNumber() + " - " + fromRevItem.getRevision() + " - " + fromRevItem.getLifeCyclePhase().getPhase());
            finalDto.setToItem(toItem.getItemNumber() + " - " + toRevItem.getRevision() + " - " + toRevItem.getLifeCyclePhase().getPhase());

            finalList = compareSameRevItems(fromRevItem, toRevItem, fromItem, toItem);
            List<PLMItemAttribute> fromAttributes = getItemAttributesForComparision(fromItem.getId());
            List<PLMItemAttribute> toAttributes = getItemAttributesForComparision(toItem.getId());
            for (int i = 0; i < fromAttributes.size(); i++) {
                PLMItemAttribute plmFromAttr = fromAttributes.get(i);
                PLMItemAttribute plmToAttr = toAttributes.get(i);
                PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromAttr.getId().getAttributeDef());
                ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromAttr, plmToAttr);
                finalList.add(dto);
            }
            Thread.sleep(100);
            List<PLMItemRevisionAttribute> fromRevAttributes = getItemRevisionAttributesForComparision(fromRevItem.getId());
            List<PLMItemRevisionAttribute> toRevAttributes = getItemRevisionAttributesForComparision(toRevItem.getId());
            if (fromRevAttributes.size() == toRevAttributes.size()) {
                for (int i = 0; i < fromRevAttributes.size(); i++) {
                    PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                    PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                    ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                    finalList.add(dto);
                }
            }
            if (fromRevAttributes.size() > toRevAttributes.size()) {
                for (int i = 0; i < fromRevAttributes.size(); i++) {
                    if (i <= toRevAttributes.size() - 1) {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);
                    } else {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = new PLMItemRevisionAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);
                    }

                }

            }

            if (toRevAttributes.size() > fromRevAttributes.size()) {
                for (int i = 0; i < toRevAttributes.size(); i++) {
                    if (i <= fromRevAttributes.size() - 1) {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);

                    } else {
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemRevisionAttribute plmFromRevAttr = new PLMItemRevisionAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmToRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);

                    }

                }

            }
            finalDto.setListOfItemsCompared(finalList);

        }
        /*
        * Individual Items Compare
        * */
        else {
            PLMItemRevision fromRevItem = fromRev;
            PLMItemRevision toRevItem = toRev;
            PLMItem fromItem = itemRepository.findOne(fromRevItem.getItemMaster());
            PLMItem toItem = itemRepository.findOne(toRevItem.getItemMaster());
            finalDto.setFromItem(fromItem.getItemNumber() + " - " + fromRevItem.getRevision() + " - " + fromRevItem.getLifeCyclePhase().getPhase());
            finalDto.setToItem(toItem.getItemNumber() + " - " + toRevItem.getRevision() + " - " + toRevItem.getLifeCyclePhase().getPhase());

            finalList = compareIndividualItemsSameItemType(fromRevItem, toRevItem, fromItem, toItem);
            List<PLMItemAttribute> fromAttributes = getItemAttributesForComparision(fromItem.getId());
            List<PLMItemAttribute> toAttributes = getItemAttributesForComparision(toItem.getId());

            if (fromAttributes.size() == toAttributes.size()) {
                for (int i = 0; i < fromAttributes.size(); i++) {
                    PLMItemAttribute plmFromAttr = fromAttributes.get(i);
                    PLMItemAttribute plmToAttr = toAttributes.get(i);
                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromAttr.getId().getAttributeDef());
                    ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromAttr, plmToAttr);
                    finalList.add(dto);
                }
            }
            if (fromAttributes.size() > toAttributes.size()) {
                for (int i = 0; i < fromAttributes.size(); i++) {
                    if (i <= toAttributes.size() - 1) {
                        PLMItemAttribute plmFromAttr = fromAttributes.get(i);
                        PLMItemAttribute plmToAttr = toAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromAttr, plmToAttr);
                        finalList.add(dto);
                    } else {
                        PLMItemAttribute plmFromAttr = fromAttributes.get(i);
                        PLMItemAttribute plmToRevAttr = new PLMItemAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromAttr, plmToRevAttr);
                        finalList.add(dto);
                    }

                }

            }

            if (toAttributes.size() > fromAttributes.size()) {
                for (int i = 0; i < toAttributes.size(); i++) {
                    if (i <= fromAttributes.size() - 1) {
                        PLMItemAttribute plmFromAttr = fromAttributes.get(i);
                        PLMItemAttribute plmToAttr = toAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromAttr, plmToAttr);
                        finalList.add(dto);

                    } else {
                        PLMItemAttribute plmToAttr = toAttributes.get(i);
                        PLMItemAttribute plmFromRevAttr = new PLMItemAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmToAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToAttr);
                        finalList.add(dto);
                    }
                }
            }
            Thread.sleep(100);
            List<PLMItemRevisionAttribute> fromRevAttributes = getItemRevisionAttributesForComparision(fromRevItem.getId());
            List<PLMItemRevisionAttribute> toRevAttributes = getItemRevisionAttributesForComparision(toRevItem.getId());

            if (fromRevAttributes.size() == toRevAttributes.size()) {
                for (int i = 0; i < fromRevAttributes.size(); i++) {
                    PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                    PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                    ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                    finalList.add(dto);
                }
            }
            if (fromRevAttributes.size() > toRevAttributes.size()) {
                for (int i = 0; i < fromRevAttributes.size(); i++) {
                    if (i <= toRevAttributes.size() - 1) {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);
                    } else {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = new PLMItemRevisionAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);
                    }

                }

            }

            if (toRevAttributes.size() > fromRevAttributes.size()) {
                for (int i = 0; i < toRevAttributes.size(); i++) {
                    if (i <= fromRevAttributes.size() - 1) {
                        PLMItemRevisionAttribute plmFromRevAttr = fromRevAttributes.get(i);
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmFromRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);

                    } else {
                        PLMItemRevisionAttribute plmToRevAttr = toRevAttributes.get(i);
                        PLMItemRevisionAttribute plmFromRevAttr = new PLMItemRevisionAttribute();
                        PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findOne(plmToRevAttr.getId().getAttributeDef());
                        ItemToItemCompareDTO dto = showRevItemAttributesDiff(itemTypeAttribute, plmFromRevAttr, plmToRevAttr);
                        finalList.add(dto);

                    }

                }

            }
            finalDto.setListOfItemsCompared(finalList);

        }

        return finalDto;
    }

    public List<ItemToItemCompareDTO> compareSameRevItems(PLMItemRevision fromRevision, PLMItemRevision
            toRevision, PLMItem fromItem, PLMItem toItem) {
        List<ItemToItemCompareDTO> finalList = new LinkedList<>();
        ItemToItemCompareDTO itemNumberDto = compareItemNumber(fromItem, toItem);
        finalList.add(itemNumberDto);
        ItemToItemCompareDTO itemNameDto = compareItemName(fromItem, toItem);
        finalList.add(itemNameDto);
        ItemToItemCompareDTO itemDescriptionDto = compareItemDesc(fromItem, toItem);
        finalList.add(itemDescriptionDto);
        ItemToItemCompareDTO itemRevDto = compareItemRev(fromRevision.getRevision(), toRevision.getRevision());
        finalList.add(itemRevDto);
        ItemToItemCompareDTO itemLifeCycleDto =
                compareItemLifeCycle(fromRevision.getLifeCyclePhase().getPhase(), toRevision.getLifeCyclePhase().getPhase());
        finalList.add(itemLifeCycleDto);
        ItemToItemCompareDTO itemReleasedDto =
                compareItemReleasedDate(fromRevision.getReleasedDate(), toRevision.getReleasedDate());
        finalList.add(itemReleasedDto);
        return finalList;
    }

    public ItemToItemCompareDTO compareItemNumber(PLMItem fromItem, PLMItem toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        String propName = messageSource.getMessage("itemnumber", null, " ", LocaleContextHolder.getLocale());
        if (fromItem.getItemNumber() != null && !fromItem.getItemNumber().isEmpty()) {
            if (toItem.getItemNumber() != null && toItem.getItemNumber() != "") {
                if (!fromItem.getItemNumber().equals(toItem.getItemNumber())) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getItemNumber());
                    dto.setOldValue(toItem.getItemNumber());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getItemNumber());
                    dto.setOldValue(toItem.getItemNumber());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromItem.getItemNumber());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getItemNumber() != null && !toItem.getItemNumber().isEmpty()) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toItem.getItemNumber());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
    * ItemName Compare method
    * */
    public ItemToItemCompareDTO compareItemName(PLMItem fromItem, PLMItem toItem) {
        String propName = messageSource.getMessage("itemname", null, " ", LocaleContextHolder.getLocale());
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getItemName() != null && !fromItem.getItemName().isEmpty()) {
            if (toItem.getItemName() != null && !toItem.getItemName().isEmpty()) {
                if (!fromItem.getItemName().equals(toItem.getItemName())) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getItemName());
                    dto.setOldValue(toItem.getItemName());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getItemName());
                    dto.setOldValue(toItem.getItemName());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromItem.getItemName());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getItemName() != null && !toItem.getItemName().isEmpty()) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toItem.getItemName());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
   * Item Desc Compare method
   * */
    public ItemToItemCompareDTO compareItemDesc(PLMItem fromItem, PLMItem toItem) {
        String propName = messageSource.getMessage("description", null, " ", LocaleContextHolder.getLocale());
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getDescription() != null && !fromItem.getDescription().isEmpty()) {
            if (toItem.getDescription() != null && !toItem.getDescription().isEmpty()) {
                if (!fromItem.getDescription().equals(toItem.getDescription())) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getDescription());
                    dto.setOldValue(toItem.getDescription());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromItem.getDescription());
                    dto.setOldValue(toItem.getDescription());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromItem.getDescription());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getDescription() != null && !toItem.getDescription().isEmpty()) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toItem.getDescription());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
  * Item Rev Compare method
  * */
    public ItemToItemCompareDTO compareItemRev(String fromRev, String toRev) {
        String propName = messageSource.getMessage("revision", null, " ", LocaleContextHolder.getLocale());
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromRev != null && !fromRev.isEmpty()) {
            if (toRev != null && !toRev.isEmpty()) {
                if (!fromRev.equals(toRev)) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev);
                    dto.setOldValue(toRev);
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev);
                    dto.setOldValue(toRev);
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromRev);
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toRev != null && !toRev.isEmpty()) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toRev);
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
 * Item Life Cycle Compare method
 * */
    public ItemToItemCompareDTO compareItemLifeCycle(String fromRev, String toRev) {
        String propName = messageSource.getMessage("lifecycle", null, " ", LocaleContextHolder.getLocale());
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromRev != null && !fromRev.isEmpty()) {
            if (toRev != null && !toRev.isEmpty()) {
                if (!fromRev.equals(toRev)) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev);
                    dto.setOldValue(toRev);
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev);
                    dto.setOldValue(toRev);
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromRev);
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toRev != null && !toRev.isEmpty()) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toRev);
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
* Item Released Date Compare method
* */
    public ItemToItemCompareDTO compareItemReleasedDate(Date fromRev, Date toRev) {
        String propName = messageSource.getMessage("releasedate", null, " ", LocaleContextHolder.getLocale());
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromRev != null) {
            if (toRev != null) {
                if (!fromRev.equals(toRev)) {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev.toString());
                    dto.setOldValue(toRev.toString());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(propName);
                    dto.setNewValue(fromRev.toString());
                    dto.setOldValue(toRev.toString());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(propName);
                dto.setNewValue(fromRev.toString());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toRev != null) {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue(toRev.toString());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(propName);
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    @Transactional(readOnly = true)
    public List<PLMItemAttribute> getItemAttributesForComparision(Integer itemId) {
        return itemAttributeRepository.getByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<PLMItemRevisionAttribute> getItemRevisionAttributesForComparision(Integer revisionId) {
        return itemRevisionAttributeRepository.getByItemId(revisionId);
    }

    @Transactional
    public ItemToItemCompareDTO showItemAttributesDiff(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {
        PLMItemTypeAttribute itemTypeAttribute = typeAttribute;
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        String dataType = itemTypeAttribute.getDataType().toString();
        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String newList = "";
            String mListOld = "";
            if (itemTypeAttribute.isListMultiple()) {

                if (fAttribute.getMListValue() != null && fAttribute.getMListValue().length >= 0) {
                    if (tAttribute.getMListValue() != null && tAttribute.getMListValue().length >= 0) {
                        if (tAttribute.getMListValue().length != tAttribute.getMListValue().length) {
                            for (String lElement : tAttribute.getMListValue()) {
                                if (mListOld == "") {
                                    mListOld = lElement;
                                } else {
                                    mListOld = mListOld + " ," + lElement;
                                }
                            }
                            for (String lElement : fAttribute.getMListValue()) {
                                if (newList == "") {
                                    newList = lElement;
                                } else {
                                    newList = newList + " ," + lElement;
                                }

                            }

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(mListOld);
                            dto.setNewValue(newList);
                            dto.setChangesMade(true);

                        } else {

                            for (String lElement : tAttribute.getMListValue()) {
                                if (mListOld == "") {
                                    mListOld = lElement;
                                } else {
                                    mListOld = mListOld + " ," + lElement;
                                }
                            }
                            for (String lElement : fAttribute.getMListValue()) {
                                if (newList == "") {
                                    newList = lElement;
                                } else {
                                    newList = newList + " ," + lElement;
                                }

                            }

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(mListOld);
                            dto.setNewValue(newList);
                            dto.setChangesMade(false);

                        }

                    } else {

                        for (String lElement : fAttribute.getMListValue()) {
                            if (newList == "") {
                                newList = lElement;
                            } else {
                                newList = newList + " ," + lElement;
                            }

                        }
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(newList);
                        dto.setChangesMade(true);

                    }

                } else {
                    if (tAttribute.getMListValue() != null && tAttribute.getMListValue().length >= 0) {
                        for (String lElement : tAttribute.getMListValue()) {
                            if (mListOld == "") {
                                mListOld = lElement;
                            } else {
                                mListOld = mListOld + " ," + lElement;
                            }
                        }
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(mListOld);
                        dto.setNewValue("");
                        dto.setChangesMade(true);
                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    }
                }

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    if (fAttribute.getListValue() != null && !fAttribute.getListValue().isEmpty()) {
                        if (tAttribute.getListValue() != null && !tAttribute.getListValue().isEmpty()) {
                            if (!fAttribute.getListValue().equals(tAttribute.getListValue())) {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(true);
                            } else {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(false);
                            }

                        } else {

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue(fAttribute.getListValue().toString());
                            dto.setChangesMade(true);
                        }

                    } else {
                        if (tAttribute.getListValue() != null && !tAttribute.getListValue().isEmpty()) {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(tAttribute.getListValue().toString());
                            dto.setNewValue("");
                            dto.setChangesMade(true);
                        } else {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue("");
                            dto.setChangesMade(false);
                        }

                    }

                    if (tAttribute == null) {
                        if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue(fAttribute.getListValue().toString());
                            dto.setChangesMade(true);
                        }
                    } else {
                        if ((fAttribute.getListValue() != null && fAttribute.getListValue() != "") && (tAttribute.getListValue() != null && tAttribute.getListValue() != "")) {

                            if (!fAttribute.getListValue().equals(tAttribute.getListValue())) {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(true);
                            } else {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(false);
                            }

                        } else {
                            if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {

                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue("");
                                dto.setNewValue(fAttribute.getListValue());
                                dto.setChangesMade(true);
                            }
                            if (tAttribute.getListValue() != null && tAttribute.getListValue() != "") {
                                if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {
                                    dto.setPropertyName(itemTypeAttribute.getName());
                                    dto.setOldValue("");
                                    dto.setNewValue(fAttribute.getListValue());
                                    dto.setChangesMade(true);
                                } else {
                                    dto.setPropertyName(itemTypeAttribute.getName());
                                    dto.setOldValue(tAttribute.getListValue().toString());
                                    dto.setNewValue(fAttribute.getListValue());
                                    dto.setChangesMade(true);
                                }
                            }
                        }
                    }
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMItem fromplmItem = itemRepository.findOne(fAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMItem fromplmItem = itemRepository.findOne(fAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMItem plmItem = itemRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getItemNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMItem plmItem = itemRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getItemNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                            PLMItem fromplmItem = itemRepository.findOne(itemRevision.getItemMaster());

                            PLMItemRevision itemRevisionOld = itemRevisionRepository.findOne(tAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(itemRevisionOld.getItemMaster());

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMItemRevision itemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                            PLMItem fromplmItem = itemRepository.findOne(itemRevision.getItemMaster());

                            PLMItemRevision itemRevisionOld = itemRevisionRepository.findOne(tAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(itemRevisionOld.getItemMaster());

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getItemNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMItemRevision itemRevision = itemRevisionRepository.findOne(tAttribute.getRefValue());
                        PLMItem plmItem = itemRepository.findOne(itemRevision.getItemMaster());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getItemNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMECO fromPlmItem = ecoRepository.findOne(fAttribute.getRefValue());
                            PLMECO toPlmItemOld = ecoRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toPlmItemOld.getEcoNumber().toString());
                            dto.setNewValue(fromPlmItem.getEcoNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMECO fromPlmItem = ecoRepository.findOne(fAttribute.getRefValue());
                            PLMECO toPlmItemOld = ecoRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toPlmItemOld.getEcoNumber().toString());
                            dto.setNewValue(fromPlmItem.getEcoNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMECO plmItem = ecoRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getEcoNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMECO plmItem = ecoRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getEcoNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                            PLMWorkflowDefinition toworkflowOldValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toworkflowOldValue.getName());
                            dto.setNewValue(fromworkflownewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                            PLMWorkflowDefinition toworkflowOldValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toworkflowOldValue.getName());
                            dto.setNewValue(fromworkflownewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMWorkflowDefinition workflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(workflownewValue.getName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMWorkflowDefinition workflownewValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(workflownewValue.getName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getName());
                            dto.setNewValue(manufacturernewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getName());
                            dto.setNewValue(manufacturernewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturernewValue.getName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturerOldValue.getName().toString());
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getPartName());
                            dto.setNewValue(manufacturernewValue.getPartName());
                            dto.setChangesMade(true);
                        } else {
                            PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getPartName());
                            dto.setNewValue(manufacturernewValue.getPartName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturernewValue.getPartName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(manufacturerOldValue.getPartName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                            PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getName());
                            dto.setNewValue(personNewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                            PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getName());
                            dto.setNewValue(personNewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(personNewValue.getName());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(personOldValue.getName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                            Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getFullName());
                            dto.setNewValue(personNewValue.getFullName());
                            dto.setChangesMade(true);
                        } else {
                            Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                            Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getFullName());
                            dto.setNewValue(personNewValue.getFullName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(personNewValue.getFullName());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(personOldValue.getFullName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }
        }

        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = compareTextValues(itemTypeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = compareLongTextValues(itemTypeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = compareRichTextValues(itemTypeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = compareHyperLinkValues(itemTypeAttribute, fAttribute, tAttribute);
        }

        return dto;

    }

    public List<ItemToItemCompareDTO> compareIndividualItemsSameItemType(PLMItemRevision fromRevision, PLMItemRevision toRevision, PLMItem fromItem, PLMItem toItem) {
        List<ItemToItemCompareDTO> finalList = new LinkedList<>();
        ItemToItemCompareDTO itemNumberDto = compareItemNumber(fromItem, toItem);
        finalList.add(itemNumberDto);
        ItemToItemCompareDTO itemNameDto = compareItemName(fromItem, toItem);
        finalList.add(itemNameDto);
        ItemToItemCompareDTO itemDescriptionDto = compareItemDesc(fromItem, toItem);
        finalList.add(itemDescriptionDto);
        ItemToItemCompareDTO itemRevDto = compareItemRev(fromRevision.getRevision(), toRevision.getRevision());
        finalList.add(itemRevDto);
        ItemToItemCompareDTO itemLifeCycleDto =
                compareItemLifeCycle(fromRevision.getLifeCyclePhase().getPhase(), toRevision.getLifeCyclePhase().getPhase());
        finalList.add(itemLifeCycleDto);
        ItemToItemCompareDTO itemReleasedDto =
                compareItemReleasedDate(fromRevision.getReleasedDate(), toRevision.getReleasedDate());
        finalList.add(itemReleasedDto);
        return finalList;
    }

    @Transactional
    public ItemToItemCompareDTO showRevItemAttributesDiff(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {
        PLMItemTypeAttribute itemTypeAttribute = typeAttribute;
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        String dataType = itemTypeAttribute.getDataType().toString();
        if (itemTypeAttribute.getDataType().toString().equals("INTEGER")) {
            dto = getIntegerValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("DOUBLE")) {
            dto = getDoubleValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("DATE")) {
            dto = getDateValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIME")) {
            dto = getTimeValues(typeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("TIMESTAMP")) {
            dto = getTimeStampValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("BOOLEAN")) {
            dto = getBooleanValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("CURRENCY")) {
            dto = getCurrencyValues(typeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("TEXT")) {
            dto = compareTextValues(itemTypeAttribute, fAttribute, tAttribute);

        }
        if (itemTypeAttribute.getDataType().toString().equals("LONGTEXT")) {
            dto = compareLongTextValues(itemTypeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("RICHTEXT")) {
            dto = compareRichTextValues(itemTypeAttribute, fAttribute, tAttribute);
        }
        if (itemTypeAttribute.getDataType().toString().equals("HYPERLINK")) {
            dto = compareHyperLinkValues(itemTypeAttribute, fAttribute, tAttribute);
        }

        if (itemTypeAttribute.getDataType().toString().equals("LIST")) {
            String newList = "";
            String mListOld = "";
            if (itemTypeAttribute.isListMultiple()) {

                if (fAttribute.getMListValue() != null && fAttribute.getMListValue().length >= 0) {
                    if (tAttribute.getMListValue() != null && tAttribute.getMListValue().length >= 0) {
                        if (tAttribute.getMListValue().length != tAttribute.getMListValue().length) {
                            for (String lElement : tAttribute.getMListValue()) {
                                if (mListOld == "") {
                                    mListOld = lElement;
                                } else {
                                    mListOld = mListOld + " ," + lElement;
                                }
                            }
                            for (String lElement : fAttribute.getMListValue()) {
                                if (newList == "") {
                                    newList = lElement;
                                } else {
                                    newList = newList + " ," + lElement;
                                }

                            }

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(mListOld);
                            dto.setNewValue(newList);
                            dto.setChangesMade(true);

                        } else {

                            for (String lElement : tAttribute.getMListValue()) {
                                if (mListOld == "") {
                                    mListOld = lElement;
                                } else {
                                    mListOld = mListOld + " ," + lElement;
                                }
                            }
                            for (String lElement : fAttribute.getMListValue()) {
                                if (newList == "") {
                                    newList = lElement;
                                } else {
                                    newList = newList + " ," + lElement;
                                }

                            }

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(mListOld);
                            dto.setNewValue(newList);
                            dto.setChangesMade(false);

                        }

                    } else {

                        for (String lElement : fAttribute.getMListValue()) {
                            if (newList == "") {
                                newList = lElement;
                            } else {
                                newList = newList + " ," + lElement;
                            }

                        }
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(newList);
                        dto.setChangesMade(true);

                    }

                } else {
                    if (tAttribute.getMListValue() != null && tAttribute.getMListValue().length >= 0) {
                        for (String lElement : tAttribute.getMListValue()) {
                            if (mListOld == "") {
                                mListOld = lElement;
                            } else {
                                mListOld = mListOld + " ," + lElement;
                            }
                        }
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(mListOld);
                        dto.setNewValue("");
                        dto.setChangesMade(true);
                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    }
                }

            } else {
                if (!itemTypeAttribute.isListMultiple()) {
                    if (fAttribute.getListValue() != null && !fAttribute.getListValue().isEmpty()) {
                        if (tAttribute.getListValue() != null && !tAttribute.getListValue().isEmpty()) {
                            if (!fAttribute.getListValue().equals(tAttribute.getListValue())) {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(true);
                            } else {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(false);
                            }

                        } else {

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue(fAttribute.getListValue().toString());
                            dto.setChangesMade(true);
                        }

                    } else {
                        if (tAttribute.getListValue() != null && !tAttribute.getListValue().isEmpty()) {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(tAttribute.getListValue().toString());
                            dto.setNewValue("");
                            dto.setChangesMade(true);
                        } else {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue("");
                            dto.setChangesMade(false);
                        }

                    }

                    if (tAttribute == null) {
                        if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue("");
                            dto.setNewValue(fAttribute.getListValue().toString());
                            dto.setChangesMade(true);
                        }
                    } else {
                        if ((fAttribute.getListValue() != null && fAttribute.getListValue() != "") && (tAttribute.getListValue() != null && tAttribute.getListValue() != "")) {

                            if (!fAttribute.getListValue().equals(tAttribute.getListValue())) {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(true);
                            } else {
                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue(tAttribute.getListValue().toString());
                                dto.setNewValue(fAttribute.getListValue().toString());
                                dto.setChangesMade(false);
                            }

                        } else {
                            if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {

                                dto.setPropertyName(itemTypeAttribute.getName());
                                dto.setOldValue("");
                                dto.setNewValue(fAttribute.getListValue());
                                dto.setChangesMade(true);
                            }
                            if (tAttribute.getListValue() != null && tAttribute.getListValue() != "") {
                                if (fAttribute.getListValue() != null && fAttribute.getListValue() != "") {
                                    dto.setPropertyName(itemTypeAttribute.getName());
                                    dto.setOldValue("");
                                    dto.setNewValue(fAttribute.getListValue());
                                    dto.setChangesMade(true);
                                } else {
                                    dto.setPropertyName(itemTypeAttribute.getName());
                                    dto.setOldValue(tAttribute.getListValue().toString());
                                    dto.setNewValue(fAttribute.getListValue());
                                    dto.setChangesMade(true);
                                }
                            }
                        }
                    }
                }

            }

        }
        if (itemTypeAttribute.getDataType().toString().equals("OBJECT")) {
            if (itemTypeAttribute.getRefType().toString().equals("ITEM")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMItem fromplmItem = itemRepository.findOne(fAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMItem fromplmItem = itemRepository.findOne(fAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMItem plmItem = itemRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getItemNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMItem plmItem = itemRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getItemNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("ITEMREVISION")) {
                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                            PLMItem fromplmItem = itemRepository.findOne(plmItemRevision.getItemMaster());

                            PLMItemRevision plmItemRevisionOld = itemRevisionRepository.findOne(tAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(plmItemRevisionOld.getItemMaster());

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                            PLMItem fromplmItem = itemRepository.findOne(plmItemRevision.getItemMaster());

                            PLMItemRevision plmItemRevisionOld = itemRevisionRepository.findOne(tAttribute.getRefValue());
                            PLMItem toplmItemOld = itemRepository.findOne(plmItemRevisionOld.getItemMaster());

                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toplmItemOld.getItemNumber().toString());
                            dto.setNewValue(fromplmItem.getItemNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(fAttribute.getRefValue());
                        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getItemNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(tAttribute.getRefValue());
                        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getItemNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("CHANGE")) {
                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMECO fromPlmItem = ecoRepository.findOne(fAttribute.getRefValue());
                            PLMECO toPlmItemOld = ecoRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toPlmItemOld.getEcoNumber().toString());
                            dto.setNewValue(fromPlmItem.getEcoNumber().toString());
                            dto.setChangesMade(true);
                        } else {
                            PLMECO fromPlmItem = ecoRepository.findOne(fAttribute.getRefValue());
                            PLMECO toPlmItemOld = ecoRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toPlmItemOld.getEcoNumber().toString());
                            dto.setNewValue(fromPlmItem.getEcoNumber().toString());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMECO plmItem = ecoRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(plmItem.getEcoNumber().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMECO plmItem = ecoRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(plmItem.getEcoNumber().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }
            if (itemTypeAttribute.getRefType().toString().equals("WORKFLOW")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                            PLMWorkflowDefinition toworkflowOldValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toworkflowOldValue.getName());
                            dto.setNewValue(fromworkflownewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMWorkflowDefinition fromworkflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                            PLMWorkflowDefinition toworkflowOldValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(toworkflowOldValue.getName());
                            dto.setNewValue(fromworkflownewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMWorkflowDefinition workflownewValue = workFlowDefinitionRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(workflownewValue.getName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMWorkflowDefinition workflownewValue = workFlowDefinitionRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(workflownewValue.getName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURER")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getName());
                            dto.setNewValue(manufacturernewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getName());
                            dto.setNewValue(manufacturernewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMManufacturer manufacturernewValue = manufacturerRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturernewValue.getName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMManufacturer manufacturerOldValue = manufacturerRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturerOldValue.getName().toString());
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("MANUFACTURERPART")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getPartName());
                            dto.setNewValue(manufacturernewValue.getPartName());
                            dto.setChangesMade(true);
                        } else {
                            PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                            PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(manufacturerOldValue.getPartName());
                            dto.setNewValue(manufacturernewValue.getPartName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMManufacturerPart manufacturernewValue = manufacturerPartRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(manufacturernewValue.getPartName().toString());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMManufacturerPart manufacturerOldValue = manufacturerPartRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(manufacturerOldValue.getPartName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }

            }
            if (itemTypeAttribute.getRefType().toString().equals("PERSON")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                            Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getFullName());
                            dto.setNewValue(personNewValue.getFullName());
                            dto.setChangesMade(true);
                        } else {
                            Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                            Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getFullName());
                            dto.setNewValue(personNewValue.getFullName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        Person personNewValue = personRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(personNewValue.getFullName());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        Person personOldValue = personRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(personOldValue.getFullName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }

            if (itemTypeAttribute.getRefType().toString().equals("PROJECT")) {

                if (fAttribute.getRefValue() != null) {
                    if (tAttribute.getRefValue() != null) {
                        if (!fAttribute.getRefValue().toString().equals(tAttribute.getRefValue().toString())) {
                            PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                            PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getName());
                            dto.setNewValue(personNewValue.getName());
                            dto.setChangesMade(true);
                        } else {
                            PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                            PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                            dto.setPropertyName(itemTypeAttribute.getName());
                            dto.setOldValue(personOldValue.getName());
                            dto.setNewValue(personNewValue.getName());
                            dto.setChangesMade(false);
                        }

                    } else {
                        PLMProject personNewValue = projectRepository.findOne(fAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue(personNewValue.getName());
                        dto.setChangesMade(true);
                    }
                } else {
                    if (tAttribute.getRefValue() != null) {
                        PLMProject personOldValue = projectRepository.findOne(tAttribute.getRefValue());
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue(personOldValue.getName().toString());
                        dto.setNewValue("");
                        dto.setChangesMade(true);

                    } else {
                        dto.setPropertyName(itemTypeAttribute.getName());
                        dto.setOldValue("");
                        dto.setNewValue("");
                        dto.setChangesMade(false);
                    }
                }
            }

        }
        return dto;
    }

    public ItemToItemCompareDTO getIntegerValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getIntegerValue() != null) {
            if (tAttribute.getIntegerValue() != null) {
                if (!fAttribute.getIntegerValue().toString().equals(tAttribute.getIntegerValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getIntegerValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);
            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Double Atribute Comparision
     */
    public ItemToItemCompareDTO getDoubleValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getDoubleValue() != null) {
            if (tAttribute.getDoubleValue() != null) {
                if (!fAttribute.getDoubleValue().toString().equals(tAttribute.getDoubleValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getDoubleValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Date Atribute Comparision
     */
    public ItemToItemCompareDTO getDateValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getDateValue() != null) {
            if (tAttribute.getDateValue() != null) {
                if (!fAttribute.getDateValue().toString().equals(tAttribute.getDateValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getDateValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Time Atribute Comparision
     */
    public ItemToItemCompareDTO getTimeValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getTimeValue() != null) {
            if (tAttribute.getTimeValue() != null) {
                if (!fAttribute.getTimeValue().toString().equals(tAttribute.getTimeValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getTimeValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item TimeStamp Atribute Comparision
     */
    public ItemToItemCompareDTO getTimeStampValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getTimestampValue() != null) {
            if (tAttribute.getTimeValue() != null) {
                if (!fAttribute.getTimestampValue().toString().equals(tAttribute.getTimestampValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getTimestampValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Boolean Atribute Comparision
     */
    public ItemToItemCompareDTO getBooleanValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();
        String fBool = String.valueOf(fAttribute.getBooleanValue());
        String tBool = String.valueOf(tAttribute.getBooleanValue());
        if (!fBool.equals(tBool)) {
            itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
            itemToItemCompareDTO.setOldValue(tBool);
            itemToItemCompareDTO.setNewValue(fBool);
            itemToItemCompareDTO.setChangesMade(true);
        } else {
            itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
            itemToItemCompareDTO.setOldValue(tBool);
            itemToItemCompareDTO.setNewValue(fBool);
            itemToItemCompareDTO.setChangesMade(false);
        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Currency Atribute Comparision
     */
    public ItemToItemCompareDTO getCurrencyValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fAttribute, PLMItemAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getCurrencyValue() != null) {
            if (tAttribute.getCurrencyValue() != null) {
                if (!fAttribute.getCurrencyValue().toString().equals(tAttribute.getCurrencyValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getCurrencyValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /*
   * Text Attribute Compare method
   * */
    public ItemToItemCompareDTO compareTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem, PLMItemAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getStringValue() != null && !fromItem.getStringValue().isEmpty()) {
            if (toItem.getStringValue() != null && !toItem.getStringValue().isEmpty()) {
                if (!fromItem.getStringValue().equals(toItem.getStringValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getStringValue());
                    dto.setOldValue(toItem.getStringValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getStringValue());
                    dto.setOldValue(toItem.getStringValue());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getStringValue());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getStringValue() != null && !toItem.getStringValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getStringValue());
                dto.setChangesMade(false);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
   * Long Text Attribute Compare method
   * */
    public ItemToItemCompareDTO compareLongTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem, PLMItemAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getLongTextValue() != null && !fromItem.getLongTextValue().isEmpty()) {
            if (toItem.getLongTextValue() != null && !toItem.getLongTextValue().isEmpty()) {
                if (!fromItem.getLongTextValue().equals(toItem.getLongTextValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getLongTextValue());
                    dto.setOldValue(toItem.getLongTextValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getLongTextValue());
                    dto.setOldValue(toItem.getLongTextValue());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getLongTextValue());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getLongTextValue() != null && !toItem.getLongTextValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getLongTextValue());
                dto.setChangesMade(false);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
      * Rich Text Attribute Compare method
      * */
    public ItemToItemCompareDTO compareRichTextValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem, PLMItemAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getRichTextValue() != null && !fromItem.getRichTextValue().isEmpty()) {
            if (toItem.getRichTextValue() != null && !toItem.getRichTextValue().isEmpty()) {
                if (!fromItem.getRichTextValue().equals(toItem.getRichTextValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getRichTextValue());
                    dto.setOldValue(toItem.getRichTextValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getRichTextValue());
                    dto.setOldValue(toItem.getRichTextValue());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getRichTextValue());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getRichTextValue() != null && !toItem.getRichTextValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getRichTextValue());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }

    /*
     * HyperLink Attribute Compare method
     * */
    public ItemToItemCompareDTO compareHyperLinkValues(PLMItemTypeAttribute typeAttribute, PLMItemAttribute fromItem, PLMItemAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getHyperLinkValue() != null && !fromItem.getHyperLinkValue().isEmpty()) {
            if (toItem.getHyperLinkValue() != null && !toItem.getHyperLinkValue().isEmpty()) {
                if (!fromItem.getHyperLinkValue().equals(toItem.getHyperLinkValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getHyperLinkValue());
                    dto.setOldValue(toItem.getHyperLinkValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getHyperLinkValue());
                    dto.setOldValue(toItem.getHyperLinkValue());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getHyperLinkValue());
                dto.setOldValue("");
                dto.setChangesMade(true);

            }

        } else {
            if (toItem.getHyperLinkValue() != null && !toItem.getHyperLinkValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getHyperLinkValue());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }

        }
        return dto;
    }


    /**
     * Item To Item Integer Atribute Comparision
     */

    public ItemToItemCompareDTO getIntegerValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getIntegerValue() != null) {
            if (tAttribute.getIntegerValue() != null) {
                if (!fAttribute.getIntegerValue().toString().equals(tAttribute.getIntegerValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getIntegerValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getIntegerValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getIntegerValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);
            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Double Atribute Comparision
     */
    public ItemToItemCompareDTO getDoubleValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getDoubleValue() != null) {
            if (tAttribute.getDoubleValue() != null) {
                if (!fAttribute.getDoubleValue().toString().equals(tAttribute.getDoubleValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setNewValue(fAttribute.getDoubleValue().toString());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getDoubleValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getDoubleValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Date Atribute Comparision
     */
    public ItemToItemCompareDTO getDateValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getDateValue() != null) {
            if (tAttribute.getDateValue() != null) {
                if (!fAttribute.getDateValue().toString().equals(tAttribute.getDateValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getDateValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getDateValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getDateValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Time Atribute Comparision
     */
    public ItemToItemCompareDTO getTimeValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getTimeValue() != null) {
            if (tAttribute.getTimeValue() != null) {
                if (!fAttribute.getTimeValue().toString().equals(tAttribute.getTimeValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getTimeValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getTimeValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getTimeValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item TimeStamp Atribute Comparision
     */
    public ItemToItemCompareDTO getTimeStampValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getTimestampValue() != null) {
            if (tAttribute.getTimeValue() != null) {
                if (!fAttribute.getTimestampValue().toString().equals(tAttribute.getTimestampValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getTimestampValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getTimestampValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getTimestampValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Boolean Atribute Comparision
     */
    public ItemToItemCompareDTO getBooleanValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();
        String fBool = String.valueOf(fAttribute.getBooleanValue());
        String tBool = String.valueOf(tAttribute.getBooleanValue());
        if (!fBool.equals(tBool)) {
            itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
            itemToItemCompareDTO.setOldValue(tBool);
            itemToItemCompareDTO.setNewValue(fBool);
            itemToItemCompareDTO.setChangesMade(true);
        } else {
            itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
            itemToItemCompareDTO.setOldValue(tBool);
            itemToItemCompareDTO.setNewValue(fBool);
            itemToItemCompareDTO.setChangesMade(false);
        }

        return itemToItemCompareDTO;
    }

    /**
     * Item To Item Currency Atribute Comparision
     */
    public ItemToItemCompareDTO getCurrencyValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fAttribute, PLMItemRevisionAttribute tAttribute) {

        ItemToItemCompareDTO itemToItemCompareDTO = new ItemToItemCompareDTO();

        if (fAttribute.getCurrencyValue() != null) {
            if (tAttribute.getCurrencyValue() != null) {
                if (!fAttribute.getCurrencyValue().toString().equals(tAttribute.getCurrencyValue().toString())) {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setChangesMade(true);
                } else {
                    itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                    itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                    itemToItemCompareDTO.setChangesMade(false);
                }

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue(fAttribute.getCurrencyValue().toString());
                itemToItemCompareDTO.setChangesMade(true);
            }

        } else {
            if (tAttribute.getCurrencyValue() != null) {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue(tAttribute.getCurrencyValue().toString());
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(true);

            } else {
                itemToItemCompareDTO.setPropertyName(typeAttribute.getName());
                itemToItemCompareDTO.setOldValue("");
                itemToItemCompareDTO.setNewValue("");
                itemToItemCompareDTO.setChangesMade(false);
            }

        }

        return itemToItemCompareDTO;
    }

    /*
   * Text Attribute Compare method
   * */
    public ItemToItemCompareDTO compareTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem, PLMItemRevisionAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getStringValue() != null && !fromItem.getStringValue().isEmpty()) {
            if (toItem.getStringValue() != null && !toItem.getStringValue().isEmpty()) {
                if (!fromItem.getStringValue().equals(toItem.getStringValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getStringValue());
                    dto.setOldValue(toItem.getStringValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getStringValue());
                    dto.setOldValue(toItem.getStringValue());
                    dto.setChangesMade(false);
                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getStringValue());
                dto.setOldValue("");
                dto.setChangesMade(true);
            }
        } else {
            if (toItem.getStringValue() != null && !toItem.getStringValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getStringValue());
                dto.setChangesMade(false);
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);
            }
        }
        return dto;
    }
    /*
    * Item Number method
    * */

    /*
   * Long Text Attribute Compare method
   * */
    public ItemToItemCompareDTO compareLongTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem, PLMItemRevisionAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getLongTextValue() != null && !fromItem.getLongTextValue().isEmpty()) {
            if (toItem.getLongTextValue() != null && !toItem.getLongTextValue().isEmpty()) {
                if (!fromItem.getLongTextValue().equals(toItem.getLongTextValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getLongTextValue());
                    dto.setOldValue(toItem.getLongTextValue());
                    dto.setChangesMade(true);
                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getLongTextValue());
                    dto.setOldValue(toItem.getLongTextValue());
                    dto.setChangesMade(false);
                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getLongTextValue());
                dto.setOldValue("");
                dto.setChangesMade(true);
            }
        } else {
            if (toItem.getLongTextValue() != null && !toItem.getLongTextValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getLongTextValue());
                dto.setChangesMade(false);
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);
            }
        }
        return dto;
    }

    /*
      * Rich Text Attribute Compare method
      * */
    public ItemToItemCompareDTO compareRichTextValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem, PLMItemRevisionAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getRichTextValue() != null && !fromItem.getRichTextValue().isEmpty()) {
            if (toItem.getRichTextValue() != null && !toItem.getRichTextValue().isEmpty()) {
                if (!fromItem.getRichTextValue().equals(toItem.getRichTextValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getRichTextValue());
                    dto.setOldValue(toItem.getRichTextValue());
                    dto.setChangesMade(true);
                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getRichTextValue());
                    dto.setOldValue(toItem.getRichTextValue());
                    dto.setChangesMade(false);
                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getRichTextValue());
                dto.setOldValue("");
                dto.setChangesMade(true);
            }
        } else {
            if (toItem.getRichTextValue() != null && !toItem.getRichTextValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getRichTextValue());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);
            }
        }
        return dto;
    }

    /*
     * HyperLink Attribute Compare method
     * */
    public ItemToItemCompareDTO compareHyperLinkValues(PLMItemTypeAttribute typeAttribute, PLMItemRevisionAttribute fromItem, PLMItemRevisionAttribute toItem) {
        ItemToItemCompareDTO dto = new ItemToItemCompareDTO();
        if (fromItem.getHyperLinkValue() != null && !fromItem.getHyperLinkValue().isEmpty()) {
            if (toItem.getHyperLinkValue() != null && !toItem.getHyperLinkValue().isEmpty()) {
                if (!fromItem.getHyperLinkValue().equals(toItem.getHyperLinkValue())) {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getHyperLinkValue());
                    dto.setOldValue(toItem.getHyperLinkValue());
                    dto.setChangesMade(true);

                } else {
                    dto.setPropertyName(typeAttribute.getName());
                    dto.setNewValue(fromItem.getHyperLinkValue());
                    dto.setOldValue(toItem.getHyperLinkValue());
                    dto.setChangesMade(false);

                }
            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue(fromItem.getHyperLinkValue());
                dto.setOldValue("");
                dto.setChangesMade(true);
            }
        } else {
            if (toItem.getHyperLinkValue() != null && !toItem.getHyperLinkValue().isEmpty()) {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue(toItem.getHyperLinkValue());
                dto.setChangesMade(true);

            } else {
                dto.setPropertyName(typeAttribute.getName());
                dto.setNewValue("");
                dto.setOldValue("");
                dto.setChangesMade(false);

            }
        }
        return dto;
    }
}
