package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.MeasurementRepository;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.filtering.BomItemSearchCriteria;
import com.cassinisys.plm.filtering.BomItemSearchPredicateBuilder;
import com.cassinisys.plm.model.dto.*;
import com.cassinisys.plm.model.mfr.ManufacturerPartStatus;
import com.cassinisys.plm.model.mfr.PLMItemManufacturerPart;
import com.cassinisys.plm.model.pgc.*;
import com.cassinisys.plm.model.pgc.dto.BosItemDto;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.BomDataDto;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.repo.mfr.ItemManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.pgc.*;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.service.activitystream.dto.ASBOMConfigItemInclusion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by reddy on 22/12/15.
 */
@Service
public class BomService implements CrudService<PLMBom, Integer>,
        PageableService<PLMBom, Integer> {

    List<PLMBom> individualList = new LinkedList<>();
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailService mailService;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private EMailTemplateConfigRepository eMailTemplateConfigRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private BOMConfigurationRepository bomConfigurationRepository;
    @Autowired
    private BOMConfigurationService bomConfigurationService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private BomItemSearchPredicateBuilder bomItemSearchPredicateBuilder;
    @Autowired
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemManufacturerPartRepository itemManufacturerPartRepository;
    @Autowired
    private PGCDeclarationPartRepository pgcDeclarationPartRepository;
    @Autowired
    private PGCItemSpecificationRepository pgcItemSpecificationRepository;
    @Autowired
    private PGCDeclarationPartComplianceRepository pgcDeclarationPartComplianceRepository;
    @Autowired
    private PGCDeclarationSpecificationRepository pgcDeclarationSpecificationRepository;
    @Autowired
    private PGCBosItemRepository pgcBosItemRepository;
    @Autowired
    private PGCSubstanceRepository pgcSubstanceRepository;
    @Autowired
    private PGCSpecificationSubstanceRepository pgcSpecificationSubstanceRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;


    @Override
    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','itembom')")
    public PLMBom create(PLMBom bom) {
        return bomRepository.save(bom);
    }

    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','itembom')")
    public PLMBom create(Integer itemId, PLMBom bomItem, Boolean single) {
        PLMItemRevision parent = bomItem.getParent();
        checkForCyclicalUpwards(parent, bomItem);

        /*--- Reference Designator validation -----------*/

        HashMap<String, String> refDesMap = new HashMap<>();

        List<PLMBom> children = bomRepository.findByParentOrderByCreatedDateAsc(parent);
        for (PLMBom bom : children) {
            if (bomItem.getId() == null || (bomItem.getId() != null && !bomItem.getId().equals(bom.getId()))) {
                if (bom.getRefdes() != null && !bom.getRefdes().equals("")) {
                    String[] refDes = bom.getRefdes().split(",");
                    if (refDes.length == bom.getQuantity()) {
                        for (String refDe : refDes) {
                            String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                            if (existRefDes == null) {
                                refDesMap.put(refDe.toUpperCase().trim(), refDe);
                            }
                        }
                    }
                }
            }
        }

        if (bomItem.getRefdes() != null && !bomItem.getRefdes().equals("")) {
            String[] refDes = bomItem.getRefdes().split(",");
            if (refDes.length == bomItem.getQuantity()) {
                for (String refDe : refDes) {
                    String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                    if (existRefDes == null) {
                        refDesMap.put(refDe.toUpperCase().trim(), refDe);
                    } else {
                        throw new CassiniException(messageSource.getMessage(refDe + "ref_desig_already_in_use",
                                null, "Reference designator already in use", LocaleContextHolder.getLocale()));
                    }
                }
            } else {
                throw new CassiniException(messageSource.getMessage("bom_item_ref_desig_should_same",
                        null, "BOM item quantity and reference designators should be same", LocaleContextHolder.getLocale()));
            }
        }

        bomItem.setSequence(children.size() + 1);
        parent.setHasBom(Boolean.TRUE);
        itemRevisionRepository.save(parent);
        PLMBom existBom = bomRepository.findByParentAndItem(bomItem.getParent(), bomItem.getItem());
        if (existBom == null) {
            bomItem = bomRepository.save(bomItem);
        }/* else {
            throw new CassiniException(messageSource.getMessage(bomItem.getItem().getItemNumber() + " :" + "bom_item_already_exist",
                    null, bomItem.getItem().getItemNumber() + " :" + " BOM item already exist", LocaleContextHolder.getLocale()));
        }*/

        List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(parent.getId());
        for (PLMItemRevision instance : instances) {
            PLMBom plmBom = new PLMBom();
            plmBom.setItem(bomItem.getItem());
            plmBom.setParent(instance);
            plmBom.setSequence(bomItem.getSequence());
            plmBom.setNotes(bomItem.getNotes());
            plmBom.setRefdes(bomItem.getRefdes());
            plmBom.setQuantity(bomItem.getQuantity());
            plmBom = bomRepository.save(plmBom);

            instance.setHasBom(true);
            instance = itemRevisionRepository.save(instance);
        }

        itemService.updateItem(itemId);
        String addedItem = null;
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        addedItem = bomItem.getItem().getItemNumber() + " - " + bomItem.getItem().getItemName() + " - Rev " + itemRevision.getRevision()
                + " Item added by " + person.getFullName() + "as Bom item to ( " + item.getItemNumber() + " - " + item.getItemName()
                + " : Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        itemService.sendItemSubscribeNotification(item, addedItem, mailSubject);


        /* App Events */
        if (single) {
            applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemAddedEvent(parent, bomItem));
        }
        return bomItem;
    }

    @Transactional
    public BomDto createBomItem(Integer itemId, BomDto bomDto, Boolean single) {
        PLMBom bom = new PLMBom();
        bom.setItem(itemRepository.findOne(bomDto.getItem()));
        bom.setParent(itemRevisionRepository.findOne(bomDto.getParent()));
        bom.setQuantity(bomDto.getQuantity());
        bom.setRefdes(bomDto.getRefdes());
        bom.setNotes(bomDto.getNotes());
        bom.setEffectiveFrom(bomDto.getEffectiveFrom());
        bom.setEffectiveTo(bomDto.getEffectiveTo());
        bom = create(itemId, bom, single);
        bomDto = convertToBomDto(bom, "bom.latest", null);
        return bomDto;
    }

    private void checkForCyclicalUpwards(PLMItemRevision parent, PLMBom bomItem) {
        if (parent.getItemMaster().equals(bomItem.getItem().getId())) {
            throw new CassiniException(messageSource.getMessage("cannot_add_item_to_BOM_as_it_creates_a_recursive_infinite_loop",
                    null, "Cannot add item to BOM as it creates a recursive infinite loop", LocaleContextHolder.getLocale()));
        }
        PLMItem item = itemRepository.findOne(parent.getItemMaster());
        List<PLMBom> boms = bomRepository.findByItem(item);
        boms.forEach(bom -> checkForCyclicalUpwards(bom.getParent(), bomItem));
    }

    private void checkForCyclicalDownwards(List<PLMItemRevision> revs, PLMItem parentItem) {
        revs.forEach(rev -> {
            if (rev.getHasBom()) {
                List<PLMBom> bomItems = bomRepository.findByParent(rev);
                bomItems.forEach(b -> {
                    if (b.getItem().getId().equals(parentItem.getId())) {
                        throw new CassiniException(messageSource.getMessage("cannot_add_bom_item_as_of_the_parent",
                                null, "Cannot add item as one of the parent in the hierarchy above also appears as a bom item in the hierarchy below", LocaleContextHolder.getLocale()));
                    }
                    List<PLMItemRevision> childRevs = itemRevisionRepository.findByItemMasterOrderByIdAsc(b.getItem().getId());
                    checkForCyclicalDownwards(childRevs, itemRepository.findOne(parentItem.getId()));
                });
            }
        });
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','itembom')")
    public List<PLMBom> create(List<PLMBom> bomItems) {
        bomItems = bomRepository.save(bomItems);

        /* App Events */
        if (bomItems.size() > 0) {
            PLMBom bomItem = bomItems.get(0);

            applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemsAddedEvent(bomItem.getParent(), bomItems));
        }

        return bomItems;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'create','itembom')")
    public List<PLMBom> create(Integer itemId, List<PLMBom> bomItems) {
        PLMItemRevision parent = itemRevisionRepository.findOne(itemId);
        bomItems.forEach(bomItem -> {
            bomItem = create(itemId, bomItem, false);
        });
        PLMItemRevision itemRevision = bomItems.get(0).getParent();
        applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemsAddedEvent(itemRevision, bomItems));
        return bomItems;
    }

    public List<BomDto> createMultipleBomItems(Integer itemId, List<BomDto> bomDtos) {
        PLMItemRevision parent = itemRevisionRepository.findOne(itemId);
        List<PLMBom> bomItems = new ArrayList<>();
        bomDtos.forEach(bomDto -> {
            PLMBom bom = new PLMBom();
            bom.setItem(itemRepository.findOne(bomDto.getItem()));
            bom.setParent(itemRevisionRepository.findOne(bomDto.getParent()));
            bom.setQuantity(bomDto.getQuantity());
            bom.setRefdes(bomDto.getRefdes());
            bom.setNotes(bomDto.getNotes());
            bom.setEffectiveFrom(bomDto.getEffectiveFrom());
            bom.setEffectiveTo(bomDto.getEffectiveTo());
            bom.setSequence(bomDto.getSequence());
            if (bomDto.getId() == null) {
                bom = create(itemId, bom, false);
                bomItems.add(bom);
                bomDto = convertToBomDto(bom, "bom.latest", null);
            } else {
                bom.setId(bomDto.getId());
                bom = update(itemId, bom);
                bomDto = convertToBomDto(bom, "bom.latest", null);
            }
        });
        if (bomItems.size() > 0) {
            PLMItemRevision itemRevision = bomItems.get(0).getParent();
            applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemsAddedEvent(itemRevision, bomItems));
        }
        return bomDtos;
    }

    @Override
    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','itembom')")
    public PLMBom update(PLMBom plmBom) {
        PLMBom oldBomItem = JsonUtils.cloneEntity(bomRepository.findOne(plmBom.getId()), PLMBom.class);
        plmBom = bomRepository.save(plmBom);

        /* App Events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemUpdatedEvent(plmBom.getParent(), oldBomItem, plmBom));

        return plmBom;

    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','itembom')")
    public PLMBom update(Integer itemId, PLMBom plmBom) {
        PLMBom oldBomItem = JsonUtils.cloneEntity(bomRepository.findOne(plmBom.getId()), PLMBom.class);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        plmBom.setParent(itemRevision);

        /*--- Reference Designator validation -----------*/

        HashMap<String, String> refDesMap = new HashMap<>();

        List<PLMBom> children = bomRepository.findByParentOrderByCreatedDateAsc(itemRevision);
        for (PLMBom bom : children) {
            if (plmBom.getId() == null || (plmBom.getId() != null && !plmBom.getId().equals(bom.getId()))) {
                if (bom.getRefdes() != null && !bom.getRefdes().equals("")) {
                    String[] refDes = bom.getRefdes().split(",");
                    if (refDes.length == bom.getQuantity()) {
                        for (String refDe : refDes) {
                            String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                            if (existRefDes == null) {
                                refDesMap.put(refDe.toUpperCase().trim(), refDe);
                            }
                        }
                    }
                }
            }
        }

        if (plmBom.getRefdes() != null && !plmBom.getRefdes().equals("")) {
            String[] refDes = plmBom.getRefdes().split(",");
            if (refDes.length == plmBom.getQuantity()) {
                for (String refDe : refDes) {
                    String existRefDes = refDesMap.get(refDe.toUpperCase().trim());
                    if (existRefDes == null) {
                        refDesMap.put(refDe.toUpperCase().trim(), refDe);
                    } else {
                        throw new CassiniException(messageSource.getMessage(refDe + " : " + "reference_designator_already_in_use",
                                null, refDe + " : " + "Reference Designator already in use", LocaleContextHolder.getLocale()));
                    }
                }
            } else {
                throw new CassiniException(messageSource.getMessage("bom_item_quantity_and_reference_designators_should_be_same",
                        null, "BOM Item Quantity and Reference Designators should be same", LocaleContextHolder.getLocale()));
            }
        }

        PLMBom bom = bomRepository.save(plmBom);

        List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
        for (PLMItemRevision instance : instances) {
            PLMBom instanceBomItem = bomRepository.findByParentAndItem(instance, bom.getItem());
            if (instanceBomItem != null) {
                instanceBomItem.setQuantity(bom.getQuantity());
                instanceBomItem.setNotes(bom.getNotes());
                instanceBomItem.setRefdes(bom.getRefdes());
                instanceBomItem = bomRepository.save(instanceBomItem);
            } else {
                List<PLMItem> instanceItems = itemRepository.findByInstanceOrderByCreatedDateDesc(bom.getItem().getId());
                for (PLMItem instanceItem : instanceItems) {
                    instanceBomItem = bomRepository.findByParentAndItem(instance, instanceItem);
                    if (instanceBomItem != null) {
                        instanceBomItem.setQuantity(bom.getQuantity());
                        instanceBomItem.setNotes(bom.getNotes());
                        instanceBomItem.setRefdes(bom.getRefdes());
                        instanceBomItem = bomRepository.save(instanceBomItem);
                    }
                }
            }
        }
        itemService.updateItem(itemId);

        /* App Events */
        applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemUpdatedEvent(bom.getParent(), oldBomItem, bom));

        return bom;
    }

    @Transactional
    public BomDto updateItemBom(Integer itemId, BomDto bomDto) {
        PLMBom bom = JsonUtils.cloneEntity(bomRepository.findOne(bomDto.getId()), PLMBom.class);
        bom.setQuantity(bomDto.getQuantity());
        bom.setRefdes(bomDto.getRefdes());
        bom.setNotes(bomDto.getNotes());
        bom.setEffectiveFrom(bomDto.getEffectiveFrom());
        bom.setEffectiveTo(bomDto.getEffectiveTo());
        bom = update(itemId, bom);
        bomDto = convertToBomDto(bom, "bom.latest", null);
        return bomDto;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','itembom')")
    public List<PLMBom> update(List<PLMBom> bomItems) {
        bomItems = bomRepository.save(bomItems);

        /* App Events */
        if (bomItems.size() > 0) {
            PLMBom bomItem = bomItems.get(0);

            applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemsUpdatedEvent(bomItem.getParent(), bomItems));
        }

        return bomItems;
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'edit','itembom')")
    public List<PLMBom> update(Integer itemId, List<PLMBom> bomItems) {
        return update(bomItems);
    }

    @Override
    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','itembom')")
    public void delete(Integer id) {
        PLMBom bomItem = bomRepository.findOne(id);
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(bomItem.getParent());
        for (PLMBom plmBom : bomList) {
            if (plmBom.getSequence() != null && bomItem.getSequence() != null && plmBom.getSequence() > bomItem.getSequence()) {
                plmBom.setSequence(plmBom.getSequence() - 1);
                plmBom = bomRepository.save(plmBom);
            }
        }
        applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemDeletedEvent(bomItem.getParent(), bomItem));
        bomRepository.delete(id);
    }

    @Transactional
    @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'delete','itembom')")
    public void delete(Integer itemId, Integer bomItemId) {
        PLMBom bomItem = bomRepository.findOne(bomItemId);

        delete(bomItemId);

        String addedItem = null;
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomItem.getParent().getId());
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(itemRevision.getId());
        for (PLMItemRevision instance : instances) {
            PLMBom instanceBomItem = bomRepository.findByParentAndItem(instance, bomItem.getItem());
            if (instanceBomItem != null) {
                bomRepository.delete(instanceBomItem.getId());
            } else {
                List<PLMItem> itemInstances = itemRepository.findByInstanceOrderByCreatedDateDesc(bomItem.getItem().getId());
                for (PLMItem itemInstance : itemInstances) {
                    PLMBom instanceItem = bomRepository.findByParentAndItem(instance, itemInstance);
                    if (instanceItem != null) {
                        bomRepository.delete(instanceItem.getId());
                    }
                }
            }
        }
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(itemRevision.getId());
        for (BOMConfiguration bomConfiguration : bomConfigurations) {
            String json = bomConfiguration.getRules();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = null;
            try {
                map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                });
            } catch (Exception e) {
                new CassiniException(e.getMessage());
            }
            List<HashMap<String, Object>> childrenMap = null;
            if (map != null && map.size() > 0) {
                Object o = map.get("children");
                childrenMap = (List<HashMap<String, Object>>) o;
                HashMap<String, Object> removeItem = null;
                for (HashMap<String, Object> children : childrenMap) {
                    Set<Map.Entry<String, Object>> entries1 = children.entrySet();
                    for (Map.Entry<String, Object> entry : entries1) {
                        if (entry.getKey().equals("id") && entry.getValue().equals(bomItem.getItem().getId())) {
                            removeItem = children;
                        }
                    }
                }
                try {
                    childrenMap.remove(removeItem);
                    map.put("children", childrenMap);
                    String mapValue = new ObjectMapper().writeValueAsString(map);
                    bomConfiguration.setRules(mapValue);
                    bomConfiguration = bomConfigurationRepository.save(bomConfiguration);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        List<PLMBom> boms = bomRepository.findByParent(itemRevision);
        if (boms.size() == 0) {
            itemRevision.setHasBom(false);
            itemRevision = itemRevisionRepository.save(itemRevision);
        }
        addedItem = bomItem.getItem().getItemNumber() + " - " + bomItem.getItem().getItemName() + " : Rev " + itemRevision.getRevision()
                + " Bom Item Deleted by " + person.getFullName() + "  from ( " + item.getItemNumber()
                + " - " + item.getItemName() + " - " + item.getItemType().getName() + " : Rev " + itemRevision.getRevision()
                + " : " + itemRevision.getLifeCyclePhase().getPhase() + " ) Item";
        String mailSubject = item.getItemNumber() + " : " + " Rev " + itemRevision.getRevision() + " : " + itemRevision.getLifeCyclePhase().getPhase() + " Notification";
        itemService.sendItemSubscribeNotification(item, addedItem, mailSubject);
    }

    @Override
    public PLMBom get(Integer id) {
        return bomRepository.findOne(id);
    }

    public PLMBom getItemBom(Integer bomId) {
        PLMItemRevision item = itemRevisionRepository.findOne(bomId);
        return bomRepository.findByItem(item);
    }

    public List<PLMBom> getItemBomList(List<Integer> ids) {
        return bomRepository.findByItemIn(ids);
    }

    @Override
    public List<PLMBom> getAll() {
        return bomRepository.findAll();
    }

    @Override
    public Page<PLMBom> findAll(Pageable pageable) {
        return bomRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<PLMBom> getBom(PLMItemRevision item, Boolean hierarchy) {
        List<PLMBom> plmBoms = bomRepository.findByParentIdOrderBySequenceAsc(item.getId());
        plmBoms.forEach(plmBom -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
//            plmBom.getItem().getItemFiles().addAll(itemFileService.findByItem(itemRevision));
            plmBom.getItem().setHasBom(itemRevision.getHasBom());
            plmBom.setCount(bomRepository.getItemBomCount(itemRevision.getId()));
            /*PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(plmBom.getItem().getLatestRevision(), ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = itemManufacturerPartRepository.findByStatusAndItem(ManufacturerPartStatus.ALTERNATE, plmBom.getItem().getLatestRevision());
                if (manufacturerParts.size() > 0) {
                    plmBom.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                    plmBom.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    plmBom.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                    plmBom.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                }
            } else {
                plmBom.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
                plmBom.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
                plmBom.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
                plmBom.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
            }*/
            if (hierarchy) {
                plmBom.getItem().setLatestRevisionObject(itemRevision);
                plmBom = visitItemChildren(plmBom);
            }
        });
        return plmBoms;
    }

    @Transactional(readOnly = true)
    public List<BomDto> getBomItemsByFreeTextSearch(Integer itemId, BomItemSearchCriteria bomItemSearchCriteria, Boolean hierarchy) {
        List<BomDto> bomList = new ArrayList<>();

        Pageable pageable = new PageRequest(0, 1000,
                new Sort(new Sort.Order(Sort.Direction.ASC, "sequence")));

        bomItemSearchCriteria.setItem(itemId);
        Predicate predicate = bomItemSearchPredicateBuilder.build(bomItemSearchCriteria, QPLMBom.pLMBom);
        if (predicate != null) {
            Page<PLMBom> plmItem = bomRepository.findAll(predicate, pageable);
            for (PLMBom item : plmItem.getContent()) {
                BomDto bomDto = convertToBomDto(item, "bom.latest", null);
                if (hierarchy) {
                    bomDto = visitItemChildrenWithQuery(bomDto, pageable, bomItemSearchCriteria);
                }
                bomList.add(bomDto);
            }
        }

        return bomList;
    }

    private PLMBom visitItemChildren(PLMBom bom) {
        PLMItemRevision parentItem = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
        List<PLMBom> children = bomRepository.findByParentIdOrderBySequenceAsc(parentItem.getId());
        children.forEach(plmBom -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
//            plmBom.getItem().getItemFiles().addAll(itemFileService.findByItem(itemRevision));
            plmBom.getItem().setLatestRevisionObject(itemRevision);
            plmBom.setCount(bomRepository.getItemBomCount(itemRevision.getId()));
            /*PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(plmBom.getItem().getLatestRevision(), ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = itemManufacturerPartRepository.findByStatusAndItem(ManufacturerPartStatus.ALTERNATE, plmBom.getItem().getLatestRevision());
                if (manufacturerParts.size() > 0) {
                    plmBom.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                    plmBom.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    plmBom.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                    plmBom.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                }
            } else {
                plmBom.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
                plmBom.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
                plmBom.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
                plmBom.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
            }*/
            plmBom = visitItemChildren(plmBom);
        });
        bom.getChildrens().addAll(children);

        return bom;
    }

    private BomDto visitItemChildrenWithQuery(BomDto bom, Pageable pageable, BomItemSearchCriteria bomItemSearchCriteria) {
        BomItemSearchCriteria itemSearchCriteria = new BomItemSearchCriteria();
        itemSearchCriteria.setItem(bom.getLatestRevision());
        itemSearchCriteria.setFromDate(bomItemSearchCriteria.getFromDate());
        itemSearchCriteria.setToDate(bomItemSearchCriteria.getToDate());
        Predicate predicate = bomItemSearchPredicateBuilder.build(itemSearchCriteria, QPLMBom.pLMBom);
        if (predicate != null) {
            Page<PLMBom> children = bomRepository.findAll(predicate, pageable);
            List<PLMBom> bomList = children.getContent();
            for (PLMBom plmBom : bomList) {
                BomDto bomDto = convertToBomDto(plmBom, "bom.latest", null);
                bomDto = visitItemChildrenWithQuery(bomDto, pageable, itemSearchCriteria);
                bom.getChildren().add(bomDto);
            }
        }

        return bom;
    }

    @Transactional(readOnly = true)
    public List<PLMBom> getWhereUsed(PLMItem item, Boolean hierarchy) {
        List<PLMBom> boms2 = bomRepository.getByItemOrderByCreatedDateAsc(item.getId());
        List<PLMBom> boms1 = new ArrayList<>();
        for (PLMBom plmBom : boms2) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getParent().getId());
            PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
            Integer bomCount = bomRepository.getItemsCountByParent(plmBom.getParent().getItemMaster());
            plmBom.setChildren(bomCount);
            plmBom.getParent().setItem(item1);
            if (hierarchy) {
                visitItemWhereUsedParent(plmBom);
            }
            boms1.add(plmBom);
        }
        return boms1;
    }

    private void visitItemWhereUsedParent(PLMBom plmBom) {
        List<PLMBom> list = bomRepository.getByItemOrderByCreatedDateAsc(plmBom.getParent().getItemMaster());
        list.forEach(bom -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getParent().getId());
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            Integer bomCount = bomRepository.getItemsCountByParent(plmBom.getParent().getItemMaster());
            plmBom.setChildren(bomCount);
            plmBom.getParent().setItem(item);
            visitItemWhereUsedParent(bom);
            plmBom.getChildrens().add(bom);
        });
    }

    @Transactional(readOnly = true)
    public List<PLMBom> getWhereUsedCounts(PLMItem item) {
        List<PLMBom> boms2 = bomRepository.findByItemOrderByCreatedDateAsc(item);
        List<PLMBom> boms1 = new ArrayList<>();
        for (PLMBom plmBom : boms2) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getParent().getId());
            PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
            if (itemRevision.getId().equals(item1.getLatestRevision())) {
                boms1.add(plmBom);
            }
        }
        return boms1;
    }

    public List<PLMBom> getWhereUsedItems(Integer itemId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item1 = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMBom> boms = bomRepository.findByItemOrderByCreatedDateAsc(item1);
        List<PLMBom> boms1 = new ArrayList<>();
        for (PLMBom plmBom : boms) {
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmBom.getParent().getId());
            PLMItem item2 = itemRepository.findOne(plmItemRevision.getItemMaster());
            List<PLMBom> boms2 = bomRepository.findByItemOrderByCreatedDateAsc(item2);
            plmBom.setChildren(boms2.size());
            boms1.add(plmBom);
        }
        return boms1;
    }

    private void sendSubscribeNotification(PLMItem item, String message, String mailSubject) {
        List<PLMSubscribe> subscribes = subscribeRepository.getByObjectIdAndSubscribeTrue(item.getId());
        String[] recipientAddress = new String[subscribes.size()];
        String email = "";
        if (subscribes.size() > 0) {
            for (int i = 0; i < subscribes.size(); i++) {
                PLMSubscribe subscribe = subscribes.get(i);
                if (email == "") {
                    email = subscribe.getPerson().getEmail();
                } else {
                    email = email + "," + subscribe.getPerson().getEmail();
                }
            }
            String[] recipientList = email.split(",");
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String host = url.substring(0, url.indexOf(uri));
            final String messageContent = message;
            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("host", host);
                model.put("message", messageContent);
                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);
                mail.setMailSubject(mailSubject);
                EmailTemplateConfiguration emailTemplateConfiguration = eMailTemplateConfigRepository.findByTemplateName("subscribeNotification.html");
                Preference preference = preferenceRepository.findByPreferenceKey("SYSTEM.LOGO");
                if (preference != null) {
                    if (preference.getCustomLogo() != null) {
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "dummy_logo.png");
                        File file = new File(url1.getPath());
                        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            outputStream.write(preference.getCustomLogo());
                            model.put("companyLogo", ItemService.class.getClassLoader().getResource("templates/email/share/" + "dummy_logo.png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                if (emailTemplateConfiguration != null) {
                    if (emailTemplateConfiguration.getTemplateSourceCode() != null && emailTemplateConfiguration.getTemplateSourceCode() != "") {
                        byte[] data = DatatypeConverter.parseBase64Binary(emailTemplateConfiguration.getTemplateSourceCode());
                        URL url1 = ItemService.class
                                .getClassLoader().getResource("templates/email/share/" + "customTemplate.html");
                        File file = new File(url1.getPath());
                        try {
                            OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                            Writer writer = new OutputStreamWriter(outputStream);
                            writer.write(emailTemplateConfiguration.getTemplateSourceCode());
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mail.setTemplatePath("email/share/customTemplate.html");
                    } else {
                        mail.setTemplatePath("email/subscribeNotification.html");
                    }
                } else {
                    mail.setTemplatePath("email/subscribeNotification.html");
                }
                mail.setModel(model);
                mailService.sendEmail(mail);
            }).start();
        }
    }

    @Transactional
    public List<ItemBomExport> getBomExport(PLMItemRevision item) {
        List<ItemBomExport> itemBomExports = new ArrayList<>();
        List<PLMBom> plmBoms = bomRepository.findByParentOrderBySequenceAsc(item);
        plmBoms.forEach(plmBom -> {
            ItemBomExport itemBomExport = new ItemBomExport();
            itemBomExport.setLevel(1);
            plmBom.setLevel(1);
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemRevision.getId());
            PLMItem item1 = itemRepository.findOne(plmItemRevision.getItemMaster());
            itemBomExport.setItemNumber(addspace(plmBom.getLevel(), item1.getItemNumber()));
            itemBomExport.setItemType(item1.getItemType().getName());
            itemBomExport.setItemName(item1.getItemName());
            itemBomExport.setQuantity(plmBom.getQuantity().toString());
            itemBomExport.setParent(itemRepository.findOne(item.getItemMaster()).getItemNumber());
            itemBomExport.setRevision(itemRevision.getRevision());
            itemBomExport.setLifeCyclePhase(itemRevision.getLifeCyclePhase().getPhase());
            itemBomExport.setReferenceDesignators(plmBom.getRefdes());
            itemBomExport.setNotes(plmBom.getNotes());
            itemBomExports.add(itemBomExport);
            visitBomItemChildren(plmBom, itemBomExports);
        });
        return itemBomExports;
    }

    private List<ItemBomExport> visitBomItemChildren(PLMBom plmBom, List<ItemBomExport> itemBomExports) {
        PLMItemRevision itemRevision1 = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
        List<PLMBom> plmBoms = bomRepository.findByParentOrderBySequenceAsc(itemRevision1);
        plmBoms.forEach(bom -> {
            bom.setLevel(plmBom.getLevel() + 1);
            ItemBomExport itemBomExport = new ItemBomExport();
            itemBomExport.setLevel(plmBom.getLevel() + 1);
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemRevision.getId());
            PLMItem item1 = itemRepository.findOne(plmItemRevision.getItemMaster());
            itemBomExport.setItemNumber(addspace(bom.getLevel(), item1.getItemNumber()));
            itemBomExport.setItemType(item1.getItemType().getName());
            itemBomExport.setItemName(item1.getItemName());
            itemBomExport.setQuantity(bom.getQuantity().toString());
            itemBomExport.setReferenceDesignators(bom.getRefdes());
            itemBomExport.setParent(itemRepository.findOne(bom.getParent().getItemMaster()).getItemNumber());
            itemBomExport.setRevision(itemRevision.getRevision());
            itemBomExport.setLifeCyclePhase(itemRevision.getLifeCyclePhase().getPhase());
            itemBomExport.setNotes(bom.getNotes());
            itemBomExports.add(itemBomExport);
            visitBomItemChildren(bom, itemBomExports);
        });
        return itemBomExports;
    }

    private String addspace(int i, String str) {
        StringBuilder str1 = new StringBuilder();
        for (int j = 0; j < i; j++) {
            str1.append("   ");
        }
        str1.append(str);
        return str1.toString();

    }

    @Transactional
    public PLMItem updateBomSequence() {
        List<PLMItem> items = itemRepository.findAll();
        List<PLMItemFile> itemFiles = itemFileRepository.findAll();
        if (items.size() > 0) {
            items.forEach(plmItem -> {
                PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                List<PLMBom> bomItems = bomRepository.findByParentAndSequenceIsNullOrderByCreatedDateAsc(itemRevision);
                if (bomItems.size() > 0) {
                    Integer sequence = bomRepository.findByParentAndSequenceIsNotNullOrderByCreatedDateAsc(itemRevision).size() + 1;
                    for (PLMBom bomItem : bomItems) {
                        bomItem.setSequence(sequence);
                        bomItem = bomRepository.save(bomItem);
                        sequence++;
                    }
                }
            });
        }
        if (itemFiles.size() > 0) {
            itemFiles.forEach(plmItemFile -> {
                plmItemFile.setFileType(plmItemFile.getType());
                plmItemFile.setParentFile(plmItemFile.getParent());
                plmItemFile = itemFileRepository.save(plmItemFile);
            });
        }
        return items.get(0);
    }

    @Transactional
    public PLMItemRevision updateBomItemSequence(Integer itemId) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        List<PLMBom> bomItems = bomRepository.findByParentAndSequenceIsNullOrderByCreatedDateAsc(itemRevision);
        if (bomItems.size() > 0) {
            Integer sequence = bomRepository.findByParentAndSequenceIsNotNullOrderByCreatedDateAsc(itemRevision).size() + 1;
            for (PLMBom bomItem : bomItems) {
                bomItem.setSequence(sequence);
                bomItem = bomRepository.save(bomItem);
                sequence++;
            }
        }
        return itemRevision;
    }

    @Transactional
    public void updateBomItemSeq(Integer actualId, Integer targetId) {
        PLMBom actualBomItem = bomRepository.findOne(actualId);
        PLMBom targetBomItem = bomRepository.findOne(targetId);
        List<PLMBom> bomItems = bomRepository.findByParent(actualBomItem.getParent());
        if ((actualBomItem.getSequence() > targetBomItem.getSequence())) {
            for (PLMBom bomItem : bomItems) {
                if (targetBomItem.getId().equals(bomItem.getId()) || actualBomItem.getId().equals(bomItem.getId())) {
                } else {
                    if ((targetBomItem.getSequence() < bomItem.getSequence()) && (actualBomItem.getSequence() > bomItem.getSequence())) {
                        bomItem.setSequence(bomItem.getSequence() + 1);
                        bomItem = bomRepository.save(bomItem);
                    }
                }
            }
            actualBomItem.setSequence(targetBomItem.getSequence());
            actualBomItem = bomRepository.save(actualBomItem);
            targetBomItem.setSequence(targetBomItem.getSequence() + 1);
            targetBomItem = bomRepository.save(targetBomItem);
        } else {
            for (PLMBom bomItem : bomItems) {
                if (targetBomItem.getId().equals(bomItem.getId()) || actualBomItem.getId().equals(bomItem.getId())) {
                } else {
                    if ((targetBomItem.getSequence() > bomItem.getSequence()) && (actualBomItem.getSequence() < bomItem.getSequence())) {
                        bomItem.setSequence(bomItem.getSequence() - 1);
                        bomItem = bomRepository.save(bomItem);
                    }
                }
            }
            actualBomItem.setSequence(targetBomItem.getSequence());
            actualBomItem = bomRepository.save(actualBomItem);
            targetBomItem.setSequence(targetBomItem.getSequence() - 1);
            targetBomItem = bomRepository.save(targetBomItem);
        }
    }

    @Transactional
    public List<BomDto> pasteCopiedItemsToBomItem(Integer itemId, List<PLMItem> items) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem itemMaster = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMBom> bomList = new ArrayList<>();
        for (PLMItem item : items) {
            Boolean itemExist = false;
            if (item.getId().equals(itemRevision.getItemMaster())) {
                itemExist = true;
            }
            List<PLMBom> bomItemChildren = bomRepository.findByParent(itemRevision);
            if (bomItemChildren.size() > 0) {
                for (PLMBom plmBom : bomItemChildren) {
                    if (plmBom.getItem().getId().equals(item.getId())) {
                        itemExist = true;
                    }
                }
            }
            if (!itemExist) {
                List<PLMBom> parentBoms = bomRepository.findByItem(itemMaster);
                if (parentBoms.size() > 0) {
                    for (PLMBom plmBom : parentBoms) {
                        if (plmBom.getItem().getId().equals(item.getId())) {
                            itemExist = true;
                        }
                        itemExist = getParentItems(plmBom, item, itemExist);
                    }
                }
            }
            if (!itemExist) {
                PLMBom plmBom = new PLMBom();
                plmBom.setItem(itemRepository.findOne(item.getId()));
                plmBom.setParent(itemRevision);
                plmBom.setQuantity(1);
                List<PLMBom> children = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
                plmBom.setSequence(children.size() + 1);
                plmBom = bomRepository.save(plmBom);
                bomList.add(plmBom);
                itemRevision.setHasBom(true);
                itemRevision = itemRevisionRepository.save(itemRevision);
            }
        }

        if (bomList.size() > 0) {
            applicationEventPublisher.publishEvent(new ItemEvents.ItemBomItemsAddedEvent(bomList.get(0).getParent(), bomList));
        }
        List<BomDto> dtoList = new LinkedList<>();
        bomList.forEach(bom -> {
            BomDto bomDto = convertToBomDto(bom, "bom.latest", null);
            dtoList.add(bomDto);
        });
        return dtoList;
    }

    private Boolean getParentItems(PLMBom bom, PLMItem item, Boolean itemExist) {
        PLMItem plmItem = itemRepository.findOne(bom.getParent().getItemMaster());
        List<PLMBom> parentBoms = bomRepository.findByItem(plmItem);
        if (parentBoms.size() > 0) {
            for (PLMBom plmBom : parentBoms) {
                if (plmBom.getItem().getId().equals(item.getId())) {
                    itemExist = true;
                }
            }

        }
        return itemExist;
    }

    @Transactional
    public void undoCopiedBomItems(Integer itemId, List<BomDto> bomItems) {
        bomItems.forEach(plmBom -> {
            bomRepository.delete(plmBom.getId());
        });
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomItems.get(0).getParent());
        List<PLMBom> boms = bomRepository.findByParent(itemRevision);
        if (boms.size() == 0) {
            itemRevision.setHasBom(false);
            itemRevision = itemRevisionRepository.save(itemRevision);
        }
    }

    public List<PLMBom> getIndividualItemList(List<PLMBom> fromplmBoms, List<PLMBom> toplmBoms) {
        individualList = new LinkedList();
        String color = "";
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            PLMItemRevision fromRev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
            for (int k = 0; k < toplmBoms.size(); k++) {
                PLMBom plmToBom = toplmBoms.get(k);
                PLMItemRevision toRev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                if (plmFromBom.getItem().getItemNumber().equals(plmToBom.getItem().getItemNumber())) {
                    Boolean flag = Boolean.TRUE;
                    plmFromBom.setLifeCycle(fromRev.getLifeCyclePhase().getPhase());
                    plmFromBom.setRev(fromRev.getRevision());
                    if (plmFromBom.getQuantity() != plmToBom.getQuantity()) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedQty(plmToBom.getQuantity());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (!fromRev.getRevision().equals(toRev.getRevision())) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedRevision(toRev.getRevision());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (!fromRev.getLifeCyclePhase().getPhase().equals(toRev.getLifeCyclePhase().getPhase())) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedLifeCycle(toRev.getLifeCyclePhase().getPhase());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (plmFromBom.getRefdes() != null && plmFromBom.getRefdes() != "") {
                        if (!plmFromBom.getRefdes().equals(plmToBom.getRefdes())) {
                            flag = Boolean.FALSE;
                            plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                        }
                    } else if (plmToBom.getRefdes() != null && plmToBom.getRefdes() != "") {
                        if (plmFromBom.getRefdes() != null && plmFromBom.getRefdes() != "") {
                            if (!plmFromBom.getRefdes().equals(plmToBom.getRefdes())) {
                                flag = Boolean.FALSE;
                                plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                            }
                        } else {
                            flag = Boolean.FALSE;
                            plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                        }
                    }
                    if (flag == Boolean.FALSE) {
                        plmFromBom.setColor("orange_color");//Updated Orange
                        plmFromBom.setLevel(0);
                        push(plmFromBom, this.individualList);
                        visitItemChildrenBomCompare(plmFromBom);
                        //visitItemChildrenBomCompareItemSame(plmFromBom, plmToBom);
                        toplmBoms.remove(plmToBom);
                    } else {
                        plmFromBom.setColor("white_color");
                        plmFromBom.setLevel(0);
                        push(plmFromBom, this.individualList);
                        visitItemChildrenBomCompare(plmFromBom);
                        //visitItemChildrenBomCompareItemSame(plmFromBom, plmToBom);
                        toplmBoms.remove(plmToBom);
                    }
                } else {
                    if (plmToBom.getColor() == null) {
                        if (!this.individualList.contains(plmFromBom)) {
                            plmFromBom.setColor("red_color");//Deleted Red
                            PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
                            plmFromBom.setRev(rev.getRevision());
                            plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                            plmFromBom.setColor("red_color");//Deleted Red
                        }
                    }
                }
//                        toItems.add(plmToBom);
            }
        }
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            if (!this.individualList.contains(plmFromBom)) {
                for (int k = 0; k < toplmBoms.size(); k++) {
                    PLMBom plmToBom = toplmBoms.get(k);
                    /*if (plmFromBom.getSequence().equals(plmToBom.getSequence())) {*/
                    plmFromBom.setLevel(0);
                    if (!this.individualList.contains(plmFromBom)) {
                        push(plmFromBom, this.individualList);
                    }
                    visitItemChildrenBomCompare(plmFromBom);
                    PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                    plmToBom.setRev(rev.getRevision());
                    plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                    plmToBom.setColor("green_color");//Added Red
                    plmToBom.setLevel(0);
                    push(plmToBom, this.individualList);
                    visitItemChildrenBomCompare(plmToBom);
                    toplmBoms.remove(plmToBom);
                   /* }*/

                }
            } else {
                Integer index = this.individualList.indexOf(plmFromBom);
                for (int k = 0; k < toplmBoms.size(); k++) {
                    PLMBom plmToBom = toplmBoms.get(k);
                   /* if (plmFromBom.getSequence().equals(plmToBom.getSequence())) {*/
                    plmToBom.setColor("green_color");//Added Red
                    PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                    plmToBom.setRev(rev.getRevision());
                    plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                    plmToBom.setLevel(0);
                    push(plmToBom, this.individualList);
                    //this.individualList.add(index, plmToBom);
                    visitItemChildrenBomCompare(plmToBom);
                    //itemList.add(index + 1, plmToBom);
                    toplmBoms.remove(plmToBom);
                   /* }*/
//                        toItems.add(plmToBom);
                }
            }
        }
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            if (!this.individualList.contains(plmFromBom)) {
                PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
                plmFromBom.setRev(rev.getRevision());
                plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                plmFromBom.setColor("red_color");//Green
                plmFromBom.setLevel(0);
                push(plmFromBom, this.individualList);
                visitItemChildrenBomCompare(plmFromBom);

            }
        }
        for (PLMBom plmToBom : toplmBoms) {
            PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
            plmToBom.setRev(rev.getRevision());
            plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            plmToBom.setColor("green_color");//Red
            plmToBom.setLevel(0);
            push(plmToBom, this.individualList);
            visitItemChildrenBomCompare(plmToBom);

        }
        List<String> newList = new ArrayList<>();
        Stream.concat(fromplmBoms.stream(), toplmBoms.stream()).forEachOrdered(str -> {
            newList.add(str.getItem().getItemNumber());
        });
        return this.individualList;
    }

    private PLMBom visitItemChildrenBomCompare(PLMBom bom) {
        PLMItemRevision parentItem = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
        List<PLMBom> children = bomRepository.findByParentOrderBySequenceAsc(parentItem);
        children.forEach(plmBom -> {
            plmBom.setLevel(bom.getLevel() + 1);
            plmBom.setColor(bom.getColor());
            //plmBom.setColor("white_color");
            PLMItemRevision rev = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
            plmBom.setRev(rev.getRevision());
            plmBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            push(plmBom, this.individualList);
            plmBom = visitItemChildrenBomCompare(plmBom);

        });

        bom.getChildrens().addAll(children);
        return bom;
    }


    private PLMBom visitItemChildrenBomCompareItemSame(PLMBom fromBom, PLMBom toBom) {
        PLMItemRevision parentFromItem = itemRevisionRepository.findOne(fromBom.getItem().getLatestRevision());
        List<PLMBom> fromChildren = bomRepository.findByParentOrderBySequenceAsc(parentFromItem);
        PLMItemRevision parentToItem = itemRevisionRepository.findOne(toBom.getItem().getLatestRevision());
        List<PLMBom> toChildren = bomRepository.findByParentOrderBySequenceAsc(parentToItem);
        fromChildren.forEach(plmFromBom -> {
            plmFromBom.setLevel(fromBom.getLevel() + 1);
            /*plmFromBom.setColor(fromBom.getColor());
            PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
            plmFromBom.setRev(rev.getRevision());
            plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            plmFromBom.setSpace(true);
            push(plmFromBom, this.individualList);*/
            plmFromBom = visitItemChildrenBomCompare(plmFromBom);

        });

        toChildren.forEach(plmToBom -> {
            plmToBom.setLevel(fromBom.getLevel() + 1);
            /*plmFromBom.setColor(fromBom.getColor());
            PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
            plmFromBom.setRev(rev.getRevision());
            plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            plmFromBom.setSpace(true);
            push(plmFromBom, this.individualList);*/
            plmToBom = visitItemChildrenBomCompare(plmToBom);

        });

        fromBom.getChildrens().addAll(fromChildren);
        toBom.getChildrens().addAll(toChildren);
        //getIndividualItemListRecursive(fromBom.getChildrens(), toBom.getChildrens());
        return fromBom;
    }


    /**
     * Individual BomItem Comparison
     */
    public BomItemComparisionDTO getCompareBomItemsByIndividualItems(PLMItemRevision fromRev, PLMItemRevision toRev, Boolean latest) {
        PLMItemRevision fromItem = fromRev;
        PLMItemRevision toItem = toRev;
        PLMItem fromItem1 = itemRepository.findOne(fromItem.getItemMaster());
        PLMItem toItem1 = itemRepository.findOne(toItem.getItemMaster());
        BomItemComparisionDTO dto = new BomItemComparisionDTO();
        dto.setFromItemName(fromItem1.getItemName());
        dto.setToItemName(toItem1.getItemName());
        dto.setFromItemNumber(fromItem1.getItemNumber());
        dto.setToItemNumber(toItem1.getItemNumber());
        dto.setFromItemRev(fromItem.getRevision());
        dto.setToItemRev(toItem.getRevision());
        List<PLMBom> fromplmBoms = bomRepository.findByParentOrderBySequenceAsc(fromItem);
        List<PLMBom> toplmBoms = bomRepository.findByParentOrderBySequenceAsc(toItem);
        List<PLMBom> itemList = getIndividualItemList(fromplmBoms, toplmBoms);
        dto.setItemList(itemList);
        return dto;
    }


    /*
    *
    * Same Items with different Revisions
    * */
    public List<PLMBom> getRevisionItemList(PLMItemRevision fromItem, PLMItemRevision toItem, List<PLMBom> fromplmBoms, List<PLMBom> toplmBoms) {
        individualList = new LinkedList();
        String color = "";
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            PLMItemRevision fromRev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
            for (int k = 0; k < toplmBoms.size(); k++) {
                PLMBom plmToBom = toplmBoms.get(k);
                PLMItemRevision toRev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                if (plmFromBom.getItem().getItemNumber().equals(plmToBom.getItem().getItemNumber())) {
                    Boolean flag = Boolean.TRUE;
                    plmFromBom.setLifeCycle(fromRev.getLifeCyclePhase().getPhase());
                    plmFromBom.setRev(fromRev.getRevision());
                    if (plmFromBom.getQuantity() != plmToBom.getQuantity()) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedQty(plmToBom.getQuantity());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (!fromRev.getRevision().equals(toRev.getRevision())) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedRevision(toRev.getRevision());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (!fromRev.getLifeCyclePhase().getPhase().equals(toRev.getLifeCyclePhase().getPhase())) {
                        flag = Boolean.FALSE;
                        plmFromBom.setUpdatedLifeCycle(toRev.getLifeCyclePhase().getPhase());
//                                    plmToBom.setIsChanged(true);
                    }
                    if (plmFromBom.getRefdes() != null && plmFromBom.getRefdes() != "") {
                        if (!plmFromBom.getRefdes().equals(plmToBom.getRefdes())) {
                            flag = Boolean.FALSE;
                            plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                        }
                    } else if (plmToBom.getRefdes() != null && plmToBom.getRefdes() != "") {
                        if (plmFromBom.getRefdes() != null && plmFromBom.getRefdes() != "") {
                            if (!plmFromBom.getRefdes().equals(plmToBom.getRefdes())) {
                                flag = Boolean.FALSE;
                                plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                            }
                        } else {
                            flag = Boolean.FALSE;
                            plmFromBom.setUpdatedDefdes(plmToBom.getRefdes());
                        }
                    }
                    if (flag == Boolean.FALSE) {
                        plmFromBom.setColor("orange_color");//Updated Orange
                        plmFromBom.setLevel(0);
                        push(plmFromBom, this.individualList);
                        visitItemChildrenBomCompare(plmFromBom);
                        //visitItemChildrenBomCompareItemSame(plmFromBom, plmToBom);
                        toplmBoms.remove(plmToBom);
                    } else {
                        plmFromBom.setColor("white_color");
                        plmFromBom.setLevel(0);
                        push(plmFromBom, this.individualList);
                        visitItemChildrenBomCompare(plmFromBom);
                        //visitItemChildrenBomCompareItemSame(plmFromBom, plmToBom);
                        toplmBoms.remove(plmToBom);
                    }
                } else {
                    if (plmToBom.getColor() == null) {
                        if (!this.individualList.contains(plmFromBom)) {
                            plmFromBom.setColor("red_color");//Deleted Red
                            PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
                            plmFromBom.setRev(rev.getRevision());
                            plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                            plmFromBom.setColor("red_color");//Deleted Red
                        }
                    }
                }
//                        toItems.add(plmToBom);
            }
        }
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            if (!this.individualList.contains(plmFromBom)) {
                for (int k = 0; k < toplmBoms.size(); k++) {
                    PLMBom plmToBom = toplmBoms.get(k);
                    /*if (plmFromBom.getSequence().equals(plmToBom.getSequence())) {*/
                    plmFromBom.setLevel(0);
                    push(plmFromBom, this.individualList);
                    visitItemChildrenBomCompare(plmFromBom);
                    PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                    plmToBom.setRev(rev.getRevision());
                    plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                    plmToBom.setColor("green_color");//Added Red
                    plmToBom.setLevel(0);
                    push(plmToBom, this.individualList);
                    visitItemChildrenBomCompare(plmToBom);
                    toplmBoms.remove(plmToBom);
                   /* }*/

                }
            } else {
                Integer index = this.individualList.indexOf(plmFromBom);
                for (int k = 0; k < toplmBoms.size(); k++) {
                    PLMBom plmToBom = toplmBoms.get(k);
                   /* if (plmFromBom.getSequence().equals(plmToBom.getSequence())) {*/
                    plmToBom.setColor("green_color");//Added Red
                    PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
                    plmToBom.setRev(rev.getRevision());
                    plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                    plmToBom.setLevel(0);
                    push(plmToBom, this.individualList);
                    //this.individualList.add(index, plmToBom);
                    visitItemChildrenBomCompare(plmToBom);
                    //itemList.add(index + 1, plmToBom);
                    toplmBoms.remove(plmToBom);
                   /* }*/
//                        toItems.add(plmToBom);
                }
            }
        }
        for (int i = 0; i < fromplmBoms.size(); i++) {
            PLMBom plmFromBom = fromplmBoms.get(i);
            if (!this.individualList.contains(plmFromBom)) {
                PLMItemRevision rev = itemRevisionRepository.findOne(plmFromBom.getItem().getLatestRevision());
                plmFromBom.setRev(rev.getRevision());
                plmFromBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
                plmFromBom.setColor("red_color");//Green
                plmFromBom.setLevel(0);
                push(plmFromBom, this.individualList);
                visitItemChildrenBomCompare(plmFromBom);

            }
        }
        for (PLMBom plmToBom : toplmBoms) {
            PLMItemRevision rev = itemRevisionRepository.findOne(plmToBom.getItem().getLatestRevision());
            plmToBom.setRev(rev.getRevision());
            plmToBom.setLifeCycle(rev.getLifeCyclePhase().getPhase());
            plmToBom.setColor("green_color");//Red
            plmToBom.setLevel(0);
            push(plmToBom, this.individualList);
            //visitItemChildrenBomCompare(plmToBom);

        }
        List<String> newList = new ArrayList<>();
        Stream.concat(fromplmBoms.stream(), toplmBoms.stream()).forEachOrdered(str -> {
            newList.add(str.getItem().getItemNumber());
        });
        return this.individualList;
    }

    /**
     * Individual Revision Items
     */
    public BomItemComparisionDTO getCompareBomItemsByIndividualRevisions(PLMItemRevision fromRev, PLMItemRevision toRev, Boolean latest) {
        BomItemComparisionDTO dto = new BomItemComparisionDTO();
        PLMItemRevision fromItem = null;
        PLMItemRevision toItem = null;
        if (fromRev.getCreatedDate().after(toRev.getCreatedDate())) {
            fromItem = toRev;
            toItem = fromRev;
        }
        if (fromRev.getCreatedDate().before(toRev.getCreatedDate())) {
            fromItem = fromRev;
            toItem = toRev;
        }
        PLMItem fromItem1 = itemRepository.findOne(fromItem.getItemMaster());
        PLMItem toItem1 = itemRepository.findOne(toItem.getItemMaster());
        dto.setFromItemName(fromItem1.getItemName());
        dto.setToItemName(toItem1.getItemName());
        dto.setFromItemNumber(fromItem1.getItemNumber());
        dto.setToItemNumber(toItem1.getItemNumber());
        dto.setFromItemRev(fromItem.getRevision());
        dto.setToItemRev(toItem.getRevision());
        List<PLMBom> fromplmBoms = bomRepository.findByParentOrderBySequenceAsc(fromItem);
        List<PLMBom> toplmBoms = bomRepository.findByParentOrderBySequenceAsc(toItem);
        List<PLMBom> itemList = getRevisionItemList(fromItem, toItem, fromplmBoms, toplmBoms);
        dto.setItemList(itemList);
        return dto;
    }

    private void push(Object object, List list) {
        list.add(list.size(), object);
    }

    @Transactional(readOnly = true)
    public BomModalDto getBomModal(Integer itemId) {
        BomModalDto modalDto = new BomModalDto();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        modalDto.setItem(new ItemDto());
        modalDto.getItem().setId(item.getId());
        modalDto.getItem().setItemName(item.getItemName());
        modalDto.getItem().setItemNumber(item.getItemNumber());
        modalDto.getItem().setConfigurable(item.getConfigurable());
        modalDto.getItem().setItemType(item.getItemType().getId());
        modalDto.getItem().setDescription(item.getDescription());
        modalDto.getItem().setLatestRevision(itemRevision.getId());
        List<ItemConfigurableAttributes> itemConfigurableAttributes = itemConfigurableAttributesRepository.findByItem(itemId);

        for (ItemConfigurableAttributes configurableAttribute : itemConfigurableAttributes) {
            ItemAttributeDto attributeDto = new ItemAttributeDto();
            ObjectAttributeId attributeId = new ObjectAttributeId();
            attributeId.setObjectId(item.getId());
            attributeId.setAttributeDef(configurableAttribute.getAttribute().getId());
            attributeDto.setId(attributeId);
            attributeDto.setItemAttribute(configurableAttribute);
            modalDto.getAttributes().add(attributeDto);
        }
        List<PLMBom> bomList = bomRepository.findByParentAndConfigurableTrueOrderBySequenceAsc(itemRevision.getId());
        for (PLMBom plmBom : bomList) {
            BomModalDto childModalDto = new BomModalDto();
            childModalDto.setItem(new ItemDto());
            childModalDto.getItem().setId(plmBom.getItem().getId());
            childModalDto.getItem().setItemName(plmBom.getItem().getItemName());
            childModalDto.getItem().setItemNumber(plmBom.getItem().getItemNumber());
            childModalDto.getItem().setConfigurable(plmBom.getItem().getConfigurable());
            childModalDto.getItem().setItemType(plmBom.getItem().getItemType().getId());
            childModalDto.getItem().setDescription(plmBom.getItem().getDescription());
            childModalDto.getItem().setLatestRevision(plmBom.getItem().getLatestRevision());
            childModalDto.setParent(new RevisionDto());
            childModalDto.getParent().setId(plmBom.getParent().getId());
            childModalDto.getParent().setItemMaster(plmBom.getParent().getItemMaster());
            childModalDto.getParent().setHasBom(plmBom.getParent().getHasBom());
            childModalDto.getParent().setRevision(plmBom.getParent().getRevision());
            childModalDto.getParent().setInstance(plmBom.getParent().getInstance());
            itemConfigurableAttributes = itemConfigurableAttributesRepository.findByItem(plmBom.getItem().getLatestRevision());
            for (ItemConfigurableAttributes configurableAttribute : itemConfigurableAttributes) {
                ItemAttributeDto attributeDto = new ItemAttributeDto();
                ObjectAttributeId attributeId = new ObjectAttributeId();
                attributeId.setObjectId(plmBom.getItem().getId());
                attributeId.setAttributeDef(configurableAttribute.getAttribute().getId());
                attributeDto.setId(attributeId);
                attributeDto.setItemAttribute(configurableAttribute);
                childModalDto.getAttributes().add(attributeDto);
            }
            modalDto.getChildren().add(childModalDto);
        }
        return modalDto;
    }

    public BomModalDto getBomInclusionRules(Integer itemId, BomModalDto modalDto) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        Map<String, Map<String, List<String>>> itemValueMap = new HashMap<>();
        List<ItemConfigurableAttributes> configurableAttributes = itemConfigurableAttributesRepository.findByItem(itemId);
        for (ItemConfigurableAttributes configurableAttribute : configurableAttributes) {
            for (ItemAttributeDto attributeDto : modalDto.getAttributes()) {
                if (attributeDto.getItemAttribute().getId().equals(configurableAttribute.getId())) {
                    attributeDto.getItemAttribute().setValues(configurableAttribute.getValues());
                }
            }
        }
        modalDto = getItemAttributesExcludedValues(itemRevision, modalDto);
        if (modalDto.getAttributes().size() > 0) {
            Map<String, List<ItemInclusionMap>> inclusionsMap = new Gson().fromJson(
                    itemRevision.getInclusionRules(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                    }.getType()
            );
            Integer allValuesCount = 0;
            for (ItemAttributeDto attributeDto : modalDto.getAttributes()) {
                if (attributeDto.getListValue() != null && !attributeDto.getListValue().equals("")) {
                    allValuesCount++;
                    if (inclusionsMap != null && inclusionsMap.size() > 0) {
                        inclusionsMap.values().forEach(itemInclusionMap -> {
                            ItemInclusionMap value1 = itemInclusionMap.get(0);
                            ItemInclusionMap value2 = itemInclusionMap.get(1);
                            if (value1.getItemId().equals(itemId) && value1.getKey().equals(attributeDto.getItemAttribute().getAttribute().getName()) && value1.getValue().equals(attributeDto.getListValue())) {
                                Map<String, List<String>> attributeMap = itemValueMap.containsKey(value1.getItemId() + "_" + value2.getItemId()) ? itemValueMap.get(value1.getItemId() + "_" + value2.getItemId()) : new HashMap<String, List<String>>();
                                List<String> values = attributeMap.containsKey(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey()) ? attributeMap.get(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey()) : new ArrayList<String>();
                                Integer index = values.indexOf(value2.getValue());
                                if (index == -1) {
                                    values.add(value2.getValue());
                                }
                                attributeMap.put(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey(), values);
                                itemValueMap.put(value1.getItemId() + "_" + value2.getItemId(), attributeMap);
                            }
                        });
                    }
                }
            }
            if (allValuesCount.equals(modalDto.getAttributes().size())) {
                for (BomModalDto childBomModalDto : modalDto.getChildren()) {
                    PLMItem childItem = itemRepository.findOne(childBomModalDto.getItem().getId());
                    PLMItemRevision childItemRevision = itemRevisionRepository.findOne(childItem.getLatestRevision());
//                    List<PLMItemTypeAttribute> childItemTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(childItem.getItemType().getId());
                    List<ItemConfigurableAttributes> itemConfigurableAttributes = itemConfigurableAttributesRepository.findByItem(childItem.getLatestRevision());
                    for (ItemConfigurableAttributes configurableAttribute : itemConfigurableAttributes) {
                        childBomModalDto.getAttributes().forEach(attributeDto -> {
                            if (attributeDto.getItemAttribute().getId().equals(configurableAttribute.getId())) {
                                attributeDto.getItemAttribute().setValues(configurableAttribute.getValues());
                            }
                        });
                    }
                    childBomModalDto = getItemAttributesExcludedValues(childItemRevision, childBomModalDto);
                    for (ItemAttributeDto attributeDto : childBomModalDto.getAttributes()) {
                        String[] str = new String[0];
                        List<String> commonStrings = new ArrayList<>();
                        for (ItemAttributeDto modalAttribute : modalDto.getAttributes()) {
                            Map<String, List<String>> attributeValueMap = itemValueMap.get(modalDto.getItem().getLatestRevision() + "_" + childBomModalDto.getItem().getId());
                            if (attributeValueMap != null && attributeValueMap.size() > 0) {
                                List<String> values = attributeValueMap.get(childBomModalDto.getItem().getId() + "_" + modalAttribute.getItemAttribute().getAttribute().getName() + "_" + attributeDto.getItemAttribute().getAttribute().getName());
                                if (values != null) {
                                    Object[] objArray = values.toArray();
                                    String[] strings = Arrays.copyOf(objArray, objArray.length, String[].class);
                                    Integer index = modalDto.getAttributes().indexOf(modalAttribute);
                                    if (index == 0) {
                                        str = Arrays.copyOf(objArray, objArray.length, String[].class);
                                    }
                                    Boolean valueExist = false;
                                    List<String> existValues = new ArrayList<>();
                                    for (int i = 0; i < strings.length; i++) {
                                        for (int j = 0; j < str.length; j++) {
                                            if (strings[i].equals(str[j])) {
                                                valueExist = true;
                                                existValues.add(strings[i]);
                                                if (commonStrings.indexOf(strings[i]) == -1) {
                                                    commonStrings.add(strings[i]);
                                                }
                                            }
                                        }
                                    }
                                    if (!valueExist) {
                                        commonStrings = new ArrayList<>();
                                        break;
                                    } else {
                                        Object[] objects = existValues.toArray();
                                        str = Arrays.copyOf(objects, objects.length, String[].class);
                                        commonStrings = existValues;
                                    }
                                } else {
                                    commonStrings = new ArrayList<>();
                                    break;
                                }
                            }
                        }
                        Object[] commonArray = commonStrings.toArray();
                        String[] valueList = Arrays.copyOf(commonArray, commonArray.length, String[].class);
                        attributeDto.getItemAttribute().setValues(valueList);
                        if (attributeDto.getListValue() != null && !attributeDto.getListValue().equals("")) {
                            List<String> list = Arrays.asList(attributeDto.getItemAttribute().getValues());
                            if (list.indexOf(attributeDto.getListValue()) == -1) {
                                attributeDto.setListValue(null);
                            }
                        }
                        if (attributeDto.getItemAttribute().getValues().length == 1) {
                            attributeDto.setListValue(attributeDto.getItemAttribute().getValues()[0]);
                        }

                    }

                }
            }
        }
        return modalDto;
    }

    private BomModalDto getItemAttributesExcludedValues(PLMItemRevision itemRevision, BomModalDto modalDto) {
        Map<String, Map<String, List<String>>> attributeExclusionValueMap = new HashMap<>();
        Map<String, List<MapperDTO>> itemAttributeExclusionMap = new Gson().fromJson(
                itemRevision.getAttributeExclusionRules(), new TypeToken<LinkedHashMap<String, List<MapperDTO>>>() {
                }.getType()
        );
        if (itemAttributeExclusionMap != null && itemAttributeExclusionMap.size() > 0) {
            for (ItemAttributeDto attributeDto : modalDto.getAttributes()) {
                if (attributeDto.getListValue() != null) {
                    itemAttributeExclusionMap.values().forEach(attributeExclusionMap -> {
                        MapperDTO value1 = attributeExclusionMap.get(0);
                        MapperDTO value2 = attributeExclusionMap.get(1);
                        if (value1.getKey().equals(attributeDto.getItemAttribute().getAttribute().getName()) && value1.getValue().equals(attributeDto.getListValue())) {
                            Map<String, List<String>> attributeMap = attributeExclusionValueMap.containsKey(value1.getKey() + "") ? attributeExclusionValueMap.get(value1.getKey() + "") : new HashMap<String, List<String>>();
                            List<String> values = attributeMap.containsKey(value2.getKey() + "_" + value1.getKey()) ? attributeMap.get(value2.getKey() + "_" + value1.getKey()) : new ArrayList<String>();
                            Integer index = values.indexOf(value2.getValue());
                            if (index == -1) {
                                values.add(value2.getValue());
                            }
                            attributeMap.put(value2.getKey() + "_" + value1.getKey(), values);
                            attributeExclusionValueMap.put(value1.getKey(), attributeMap);
                        } else if (value2.getKey().equals(attributeDto.getItemAttribute().getAttribute().getName()) && value2.getValue().equals(attributeDto.getListValue())) {
                            Map<String, List<String>> attributeMap = attributeExclusionValueMap.containsKey(value2.getKey() + "") ? attributeExclusionValueMap.get(value2.getKey() + "") : new HashMap<String, List<String>>();
                            List<String> values = attributeMap.containsKey(value1.getKey() + "_" + value2.getKey()) ? attributeMap.get(value1.getKey() + "_" + value2.getKey()) : new ArrayList<String>();
                            Integer index = values.indexOf(value1.getValue());
                            if (index == -1) {
                                values.add(value1.getValue());
                            }
                            attributeMap.put(value1.getKey() + "_" + value2.getKey(), values);
                            attributeExclusionValueMap.put(value2.getKey(), attributeMap);
                        }
                    });
                    if (attributeExclusionValueMap.size() > 0) {
                        Map<String, List<String>> attributeMap = attributeExclusionValueMap.get(attributeDto.getItemAttribute().getAttribute().getName() + "");
                        if (attributeMap != null && attributeMap.size() > 0) {
                            for (ItemAttributeDto attribute : modalDto.getAttributes()) {
                                if (!attribute.getItemAttribute().getAttribute().getName().equals(attributeDto.getItemAttribute().getAttribute().getName())) {
                                    for (ItemAttributeDto sideAttribute : modalDto.getAttributes()) {
                                        List<String> attributeValues = attributeMap.get(sideAttribute.getItemAttribute().getAttribute().getName() + "_" + attributeDto.getItemAttribute().getAttribute().getName());
                                        if (attributeValues != null && attributeValues.size() > 0) {
                                            List<String> strings = Arrays.asList(sideAttribute.getItemAttribute().getValues());
                                            List<String> values = new ArrayList<>();
                                            for (String value : strings) {
                                                Boolean valueExist = false;
                                                for (String attributeValue : attributeValues) {
                                                    if (value.equals(attributeValue)) {
                                                        valueExist = true;
                                                    }
                                                }
                                                if (!valueExist) {
                                                    values.add(value);
                                                }
                                            }
                                            Object[] objects = values.toArray();
                                            sideAttribute.getItemAttribute().setValues(Arrays.copyOf(objects, objects.length, String[].class));
                                            if (sideAttribute.getListValue() != null && !sideAttribute.getListValue().equals("")) {
                                                List<String> list = Arrays.asList(sideAttribute.getItemAttribute().getValues());
                                                if (list.indexOf(sideAttribute.getListValue()) == -1) {
                                                    attributeDto.setListValue(null);
                                                }
                                            }
                                            if (sideAttribute.getItemAttribute().getValues().length == 1) {
                                                sideAttribute.setListValue(sideAttribute.getItemAttribute().getValues()[0]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (attributeDto.getListValue() != null && !attributeDto.getListValue().equals("")) {
                        List<String> list = Arrays.asList(attributeDto.getItemAttribute().getValues());
                        if (list.indexOf(attributeDto.getListValue()) == -1) {
                            attributeDto.setListValue(null);
                        }
                    }
                }
            }
        }
        return modalDto;
    }

    @Transactional(readOnly = true)
    public BomModalDto getBomItemToItemExclusionRules(Integer itemId, BomModalDto modalDto) {
        modalDto = getBomInclusionRules(itemId, modalDto);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        Map<String, Map<String, List<String>>> itemValueMap = new HashMap<>();
        Map<String, List<ItemInclusionMap>> exclusionsMap = new Gson().fromJson(
                itemRevision.getItemExclusionRules(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                }.getType()
        );
        for (BomModalDto dto : modalDto.getChildren()) {
            PLMItem plmItem = itemRepository.findOne(dto.getItem().getId());
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
            dto = getItemAttributesExcludedValues(plmItemRevision, dto);
        }
        if (exclusionsMap != null && exclusionsMap.size() > 0) {
            for (BomModalDto bomModalDto : modalDto.getChildren()) {
                for (ItemAttributeDto attributeDto : bomModalDto.getAttributes()) {
                    exclusionsMap.values().forEach(itemInclusionMap -> {
                        ItemInclusionMap value1 = itemInclusionMap.get(0);
                        ItemInclusionMap value2 = itemInclusionMap.get(1);
                        if (value1.getItemId().equals(bomModalDto.getItem().getId()) && value1.getKey().equals(attributeDto.getItemAttribute().getAttribute().getName()) && value1.getValue().equals(attributeDto.getListValue())) {
                            Map<String, List<String>> attributeMap = itemValueMap.containsKey(value1.getItemId() + "") ? itemValueMap.get(value1.getItemId() + "") : new HashMap<String, List<String>>();
                            List<String> values = attributeMap.containsKey(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey()) ? attributeMap.get(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey()) : new ArrayList<String>();
                            Integer index = values.indexOf(value2.getValue());
                            if (index == -1) {
                                values.add(value2.getValue());
                            }
                            attributeMap.put(value2.getItemId() + "_" + value1.getKey() + "_" + value2.getKey(), values);
                            itemValueMap.put(value1.getItemId() + "", attributeMap);
                        } else if (value2.getItemId().equals(bomModalDto.getItem().getId()) && value2.getKey().equals(attributeDto.getItemAttribute().getAttribute().getName()) && value2.getValue().equals(attributeDto.getListValue())) {
                            Map<String, List<String>> attributeMap = itemValueMap.containsKey(value2.getItemId() + "") ? itemValueMap.get(value2.getItemId() + "") : new HashMap<String, List<String>>();
                            List<String> values = attributeMap.containsKey(value1.getItemId() + "_" + value2.getKey() + "_" + value1.getKey()) ? attributeMap.get(value1.getItemId() + "_" + value2.getKey() + "_" + value1.getKey()) : new ArrayList<String>();
                            Integer index = values.indexOf(value1.getValue());
                            if (index == -1) {
                                values.add(value1.getValue());
                            }
                            attributeMap.put(value1.getItemId() + "_" + value2.getKey() + "_" + value1.getKey(), values);
                            itemValueMap.put(value2.getItemId() + "", attributeMap);
                        }
                    });
                    if (itemValueMap.size() > 0) {
                        Map<String, List<String>> attributeMap = itemValueMap.get(bomModalDto.getItem().getId() + "");
                        if (attributeMap != null && attributeMap.size() > 0) {
                            for (BomModalDto bomItem : modalDto.getChildren()) {
                                if (!bomItem.getItem().getId().equals(bomModalDto.getItem().getId())) {
                                    for (ItemAttributeDto bomItemAttribute : bomItem.getAttributes()) {
                                        List<String> attributeValues = attributeMap.get(bomItem.getItem().getId() + "_" + attributeDto.getItemAttribute().getAttribute().getName() + "_" + bomItemAttribute.getItemAttribute().getAttribute().getName());
                                        if (attributeValues != null && attributeValues.size() > 0) {
                                            List<String> strings = Arrays.asList(bomItemAttribute.getItemAttribute().getValues());
                                            List<String> values = new ArrayList<>();
                                            for (String value : strings) {
                                                Boolean valueExist = false;
                                                for (String attributeValue : attributeValues) {
                                                    if (value.equals(attributeValue)) {
                                                        valueExist = true;
                                                    }
                                                }
                                                if (!valueExist) {
                                                    values.add(value);
                                                }
                                            }
                                            Object[] objects = values.toArray();
                                            bomItemAttribute.getItemAttribute().setValues(Arrays.copyOf(objects, objects.length, String[].class));
                                            if (bomItemAttribute.getListValue() != null && !bomItemAttribute.getListValue().equals("")) {
                                                List<String> list = Arrays.asList(bomItemAttribute.getItemAttribute().getValues());
                                                if (list.indexOf(bomItemAttribute.getListValue()) == -1) {
                                                    attributeDto.setListValue(null);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (attributeDto.getListValue() != null && !attributeDto.getListValue().equals("")) {
                        List<String> list = Arrays.asList(attributeDto.getItemAttribute().getValues());
                        if (list.indexOf(attributeDto.getListValue()) == -1) {
                            attributeDto.setListValue(null);
                        }
                    }
                }
            }
        }
        return modalDto;
    }

    @Transactional(readOnly = true)
    public List<BOMConfiguration> getItemBomConfigurations(Integer itemId) {
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(itemId);
        bomConfigurations.forEach(bomConfiguration -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findByInstanceAndBomConfiguration(itemId, bomConfiguration.getId());
            if (itemRevision != null) {
                bomConfiguration.setHasInstance(true);
            }
        });
        return bomConfigurations;
    }

    @Transactional(readOnly = true)
    public List<BOMConfiguration> getAvailableItemBomConfigurations(Integer itemId) {
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(itemId);
        List<BOMConfiguration> configurationList = new LinkedList<>();
        bomConfigurations.forEach(bomConfiguration -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findByInstanceAndBomConfiguration(itemId, bomConfiguration.getId());
            if (itemRevision == null) {
                configurationList.add(bomConfiguration);
            }
        });
        return configurationList;
    }

    @Transactional(readOnly = true)
    public BomModalDto getBomConfigurationModal(Integer itemId, Integer configId) {
        BomModalDto modalDto = new BomModalDto();
        BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(configId);
        String json = bomConfiguration.getRules();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            new CassiniException(e.getMessage());
        }
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        Map<Integer, ItemDto> itemMap = new HashMap<>();
        Map<Integer, ItemAttributeDto> itemAttributeMap = new HashMap<>();
        for (String key : map.keySet()) {
            if (key == "id") {
                Object o = map.get(key);
                Integer id = (Integer) o;
                modalDto.setItem(new ItemDto());
                modalDto.getItem().setId(item.getId());
                modalDto.getItem().setItemName(item.getItemName());
                modalDto.getItem().setItemNumber(item.getItemNumber());
                modalDto.getItem().setConfigurable(item.getConfigurable());
                modalDto.getItem().setItemType(item.getItemType().getId());
                modalDto.getItem().setDescription(item.getDescription());
                modalDto.getItem().setLatestRevision(itemRevision.getId());

            } else if (key == "children") {
                Object o = map.get(key);
                List<HashMap<String, Object>> childrenMap = (List<HashMap<String, Object>>) o;
                if (childrenMap.size() > 0) {
                    for (HashMap<String, Object> children : childrenMap) {
                        PLMItemRevision revision = null;
                        PLMItem plmItem = null;
                        Map<Integer, ItemAttributeDto> childAttributeMap = new HashMap<>();
                        BomModalDto childModalDto = new BomModalDto();
                        Set<Map.Entry<String, Object>> entries1 = children.entrySet();
                        for (Map.Entry<String, Object> entry : entries1) {
                            if (entry.getKey().equals("id")) {
                                plmItem = itemRepository.findOne((Integer) entry.getValue());
                                revision = itemRevisionRepository.findOne(plmItem.getLatestRevision());
                            }
                        }
                        for (String childKey : children.keySet()) {
                            if (childKey == "id") {
                                Object object = children.get(childKey);
                                Integer id = (Integer) object;
                                childModalDto.setItem(new ItemDto());
                                if (plmItem != null) {
                                    childModalDto.getItem().setId(plmItem.getId());
                                    childModalDto.getItem().setItemName(plmItem.getItemName());
                                    childModalDto.getItem().setItemNumber(plmItem.getItemNumber());
                                    childModalDto.getItem().setConfigurable(plmItem.getConfigurable());
                                    childModalDto.getItem().setItemType(plmItem.getItemType().getId());
                                    childModalDto.getItem().setDescription(plmItem.getDescription());
                                }
                                if (revision != null) {
                                    childModalDto.getItem().setLatestRevision(revision.getId());
                                }
                                childModalDto.setParent(new RevisionDto());
                                childModalDto.getParent().setId(itemRevision.getId());
                                childModalDto.getParent().setItemMaster(itemRevision.getItemMaster());
                                childModalDto.getParent().setHasBom(itemRevision.getHasBom());
                                childModalDto.getParent().setRevision(itemRevision.getRevision());
                                childModalDto.getParent().setInstance(itemRevision.getInstance());

                            } else if (childKey == "children") {
                            } else {
                                if (plmItem != null) {
                                    Object object = children.get(childKey);
                                    String value = (String) object;
                                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(plmItem.getItemType().getId(), childKey);
                                    ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(plmItem.getLatestRevision(), itemTypeAttribute);
                                    ItemAttributeDto attributeDto = new ItemAttributeDto();
                                    ObjectAttributeId attributeId = new ObjectAttributeId();
                                    attributeId.setObjectId(plmItem.getId());
                                    attributeId.setAttributeDef(itemTypeAttribute.getId());
                                    attributeDto.setId(attributeId);
                                    attributeDto.setListValue(value);
                                    attributeDto.setItemAttribute(configurableAttribute);
                                    childModalDto.getAttributes().add(attributeDto);
                                    childAttributeMap.put(itemTypeAttribute.getId(), attributeDto);
                                }
                            }
                        }
                        if (plmItem != null) {
                            List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(plmItem.getItemType().getId());
                            for (PLMItemTypeAttribute plmItemTypeAttribute : itemTypeAttributes) {
                                ItemAttributeDto existAttribute = childAttributeMap.get(plmItemTypeAttribute.getId());
                                if (existAttribute == null) {
                                    ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(plmItem.getLatestRevision(), plmItemTypeAttribute);
                                    ItemAttributeDto attributeDto = new ItemAttributeDto();
                                    ObjectAttributeId attributeId = new ObjectAttributeId();
                                    attributeId.setObjectId(item.getId());
                                    attributeId.setAttributeDef(plmItemTypeAttribute.getId());
                                    attributeDto.setId(attributeId);
                                    attributeDto.setListValue(null);
                                    attributeDto.setItemAttribute(configurableAttribute);
                                    childModalDto.getAttributes().add(attributeDto);
                                }
                            }
                        }
                        PLMBom plmBom = bomRepository.findByParentAndItem(itemRevision, plmItem);
                        if (plmBom != null) {
                            childModalDto.setSequenceNumber(plmBom.getSequence());
                        }
                        modalDto.getChildren().add(childModalDto);
                        itemMap.put(childModalDto.getItem().getId(), childModalDto.getItem());
                    }
                }
            } else {
                Object object = map.get(key);
                String value = (String) object;
                PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(item.getItemType().getId(), key);
                ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemId, itemTypeAttribute);
                ItemAttributeDto attributeDto = new ItemAttributeDto();
                ObjectAttributeId attributeId = new ObjectAttributeId();
                attributeId.setObjectId(item.getId());
                attributeId.setAttributeDef(itemTypeAttribute.getId());
                attributeDto.setId(attributeId);
                attributeDto.setListValue(value);
                attributeDto.setItemAttribute(configurableAttribute);
                modalDto.getAttributes().add(attributeDto);
                itemAttributeMap.put(itemTypeAttribute.getId(), attributeDto);
            }
        }
        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
        for (PLMItemTypeAttribute plmItemTypeAttribute : itemTypeAttributes) {
            ItemAttributeDto existAttribute = itemAttributeMap.get(plmItemTypeAttribute.getId());
            ItemConfigurableAttributes configurableAttribute = itemConfigurableAttributesRepository.findByItemAndAttribute(itemId, plmItemTypeAttribute);
            if (existAttribute == null) {
                ItemAttributeDto attributeDto = new ItemAttributeDto();
                ObjectAttributeId attributeId = new ObjectAttributeId();
                attributeId.setObjectId(item.getId());
                attributeId.setAttributeDef(plmItemTypeAttribute.getId());
                attributeDto.setId(attributeId);
                attributeDto.setListValue(null);
                attributeDto.setItemAttribute(configurableAttribute);
                modalDto.getAttributes().add(attributeDto);
            }
        }
        for (PLMBom bom : bomList) {
            ItemDto itemDto = itemMap.get(bom.getItem().getId());
            if (itemDto == null && bom.getItem().getConfigurable()) {
                BomModalDto childModalDto = new BomModalDto();
                childModalDto.setItem(new ItemDto());
                childModalDto.getItem().setId(bom.getItem().getId());
                childModalDto.getItem().setItemName(bom.getItem().getItemName());
                childModalDto.getItem().setItemNumber(bom.getItem().getItemNumber());
                childModalDto.getItem().setConfigurable(bom.getItem().getConfigurable());
                childModalDto.getItem().setItemType(bom.getItem().getItemType().getId());
                childModalDto.getItem().setDescription(bom.getItem().getDescription());
                childModalDto.getItem().setLatestRevision(bom.getItem().getLatestRevision());
                childModalDto.setParent(new RevisionDto());
                childModalDto.getParent().setId(itemRevision.getId());
                childModalDto.getParent().setItemMaster(itemRevision.getItemMaster());
                childModalDto.getParent().setHasBom(itemRevision.getHasBom());
                childModalDto.getParent().setRevision(itemRevision.getRevision());
                childModalDto.getParent().setInstance(itemRevision.getInstance());
//                itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(bom.getItem().getItemType().getId());
                List<ItemConfigurableAttributes> itemConfigurableAttributes = itemConfigurableAttributesRepository.findByItem(bom.getItem().getLatestRevision());
                for (ItemConfigurableAttributes configurableAttribute : itemConfigurableAttributes) {
                    ItemAttributeDto attributeDto = new ItemAttributeDto();
                    ObjectAttributeId attributeId = new ObjectAttributeId();
                    attributeId.setObjectId(item.getId());
                    attributeId.setAttributeDef(configurableAttribute.getAttribute().getId());
                    attributeDto.setId(attributeId);
                    attributeDto.setListValue(null);
                    attributeDto.setItemAttribute(configurableAttribute);
                    childModalDto.getAttributes().add(attributeDto);
                }
                childModalDto.setSequenceNumber(bom.getSequence());
                modalDto.getChildren().add(childModalDto);
            }
        }
        Collections.sort(modalDto.getChildren(), new Comparator<BomModalDto>() {
            public int compare(final BomModalDto object1, final BomModalDto object2) {
                if (object1.getSequenceNumber() != null && object2.getSequenceNumber() != null) {
                    return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                } else {
                    return 0;
                }

            }
        });
        return modalDto;
    }

    @Transactional(readOnly = true)
    public List<PLMItemTypeAttribute> getConfiguredAttributeValues(Integer id, Integer configId) {
        List<PLMItemTypeAttribute> itemTypeAttributes = new ArrayList<>();
        BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(configId);
        String json = bomConfiguration.getRules();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            new CassiniException(e.getMessage());
        }
        if (map.size() > 0) {
            Object object = map.get("id");
            Integer itemId = (Integer) object;
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
            PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
            for (String key : map.keySet()) {
                if (key != "id" && key != "children") {
                    object = map.get(key);
                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndNameAndConfigurableTrue(item.getItemType().getId(), key);
                    itemTypeAttribute.setValue((String) object);
                    itemTypeAttributes.add(itemTypeAttribute);
                }
            }
            List<PLMItem> existItems = itemRepository.findByInstanceOrderByCreatedDateDesc(item.getId());
            Boolean existInstance = false;
            for (PLMItem exitCombination : existItems) {
                Integer combinationCount = 0;
                for (PLMItemTypeAttribute itemAttribute : itemTypeAttributes) {
                    ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefIdAndListValue(exitCombination.getId(), itemAttribute.getId(), itemAttribute.getValue());
                    if (objectAttribute != null) {
                        combinationCount++;
                    }
                }
                if (combinationCount.equals(itemTypeAttributes.size())) {
                    existInstance = true;
                }
            }
            if (existInstance) {
                throw new CassiniException(messageSource.getMessage("item_instance_already_exist_with_selected_bom_configuration",
                        null, "Item Instance already exist with selected BOM Configuration", LocaleContextHolder.getLocale()));
            }
            Collections.sort(itemTypeAttributes, new Comparator<PLMItemTypeAttribute>() {
                public int compare(final PLMItemTypeAttribute object1, final PLMItemTypeAttribute object2) {
                    if (object1.getSeq() != null && object2.getSeq() != null) {
                        return object1.getSeq().compareTo(object2.getSeq());
                    } else {
                        return 0;
                    }

                }
            });
        }
        return itemTypeAttributes;
    }

    @Transactional
    public List<PLMBom> getResolvedItemBom(Integer itemId) {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(itemId);
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(plmItemRevision);
        for (PLMBom plmBom : bomList) {
            plmBom = getResolvedBomItemInstance(itemId, plmBom.getId());
        }
        return bomList;
    }

    @Transactional
    public PLMBom getResolvedBomItemInstance(Integer itemId, Integer id) {
        PLMBom bom = bomRepository.findOne(id);
        PLMItemRevision parentRevision = itemRevisionRepository.findOne(bom.getParent().getId());
        PLMItem parentItem = itemRepository.findOne(parentRevision.getItemMaster());
        if (parentItem.getConfigurable()) {
            throw new CassiniException(messageSource.getMessage("resolve_bom_item" + parentItem.getItemNumber() + " " + parentItem.getItemName(),
                    null, "Please resolve BOM item [ " + parentItem.getItemNumber() + " " + parentItem.getItemName() + " ]", LocaleContextHolder.getLocale()));
        }
        if (parentRevision.getBomConfiguration() != null) {
            BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(parentRevision.getBomConfiguration());
            PLMItem item = bom.getItem();
            PLMItemRevision revision = itemRevisionRepository.findOne(item.getLatestRevision());
            List<PLMItem> instances = itemRepository.findByInstanceOrderByCreatedDateDesc(item.getId());
            List<String> resolveItemValues = new ArrayList<>();
            if (instances.size() > 0) {
                String json = bomConfiguration.getRules();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });
                } catch (Exception e) {
                    new CassiniException(e.getMessage());
                }
                Map<String, Object> itemIdMap = new HashMap<>();
                List<HashMap<String, Object>> childrenMap = null;
                if (map != null && map.size() > 0) {
                    Object o = map.get("children");
                    childrenMap = (List<HashMap<String, Object>>) o;
                    for (HashMap<String, Object> children : childrenMap) {
                        Set<Map.Entry<String, Object>> entries1 = children.entrySet();
                        for (Map.Entry<String, Object> entry : entries1) {
                            itemIdMap.put(entry.getKey(), entry.getValue());
                        }
                        Integer revisionItemId = (Integer) itemIdMap.get("id");
                        if (revisionItemId.equals(item.getId())) {
                            PLMItem plmItem = itemRepository.findOne(revisionItemId);
                            List<String> values = new ArrayList<String>();
                            List<Integer> attributeIds = new ArrayList<Integer>();
                            for (Map.Entry entry1 : entries1) {
                                String key = (String) entry1.getKey();
                                if (!key.equalsIgnoreCase("children") && !key.equals("id")) {
                                    String attributeName = (String) entry1.getKey();
                                    String attributeValue = (String) entry1.getValue();
                                    PLMItemTypeAttribute plmItemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(plmItem.getItemType().getId(), attributeName);
                                    attributeIds.add(plmItemTypeAttribute.getId());
                                    values.add(attributeValue);
                                }
                            }
                            Integer[] attriIds = attributeIds.toArray(new Integer[attributeIds.size()]);
                            String[] vals = values.toArray(new String[values.size()]);
                            for (PLMItem instance : instances) {
                                List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(instance.getId(), attriIds, vals);
                                if (objectAttributes.size() == values.size()) {
                                    applicationEventPublisher.publishEvent(new ItemEvents.BomItemResolvedEvent(parentItem, parentRevision, bom.getItem(), instance));
                                    bom.setItem(instance);
                                    bom = bomRepository.save(bom);
                                    resolveItemValues.addAll(values);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (revision.getHasBom() && bom.getItem().getConfigured()) {
                if (revision.getBomConfiguration() == null) {
                    List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(revision.getId());
                    if (bomConfigurations.size() > 0) {
                        for (BOMConfiguration configuration : bomConfigurations) {
                            String json = configuration.getRules();
                            ObjectMapper objectMapper = new ObjectMapper();
                            Map<String, Object> map = null;
                            try {
                                map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                                });
                            } catch (Exception e) {
                                new CassiniException(e.getMessage());
                            }
                            if (map != null && map.size() > 0) {
                                List<String> configValues = new ArrayList<>();
                                Set<Map.Entry<String, Object>> entries1 = map.entrySet();
                                for (Map.Entry<String, Object> entry : entries1) {
                                    if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                                        configValues.add((String) entry.getValue());
                                    }
                                }
                                if (configValues.containsAll(resolveItemValues)) {
                                    PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
                                    plmItemRevision.setBomConfiguration(configuration.getId());
                                    plmItemRevision = itemRevisionRepository.save(plmItemRevision);
                                    resolveBomItemChildren(revision, bom.getItem(), map);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    resolveBomConfigChildren(revision, resolveItemValues, bom);
                }
            }
        }
        return bom;
    }

    public void resolveBomConfigChildren(PLMItemRevision revision, List<String> resolveItemValues, PLMBom bom) {
        BOMConfiguration configuration = bomConfigurationRepository.findOne(revision.getBomConfiguration());
        String json = configuration.getRules();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            new CassiniException(e.getMessage());
        }
        if (map != null && map.size() > 0) {
            List<String> configValues = new ArrayList<>();
            Set<Map.Entry<String, Object>> entries1 = map.entrySet();
            for (Map.Entry<String, Object> entry : entries1) {
                if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                    configValues.add((String) entry.getValue());
                }
            }
            if (configValues.containsAll(resolveItemValues)) {
                resolveBomItemChildren(revision, bom.getItem(), map);
            }
        }
    }

    private void resolveBomItemChildren(PLMItemRevision itemRevision, PLMItem parentItem, Map<String, Object> map) {
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        Map<String, Object> itemIdMap = new HashMap<>();
        List<HashMap<String, Object>> childrenMap = null;
        HashMap<Integer, List<String>> itemValuesMap = new HashMap<>();
        HashMap<Integer, List<Integer>> attributeIdsMap = new HashMap<>();

        bomConfigurationService.setBomConfigurationForItem(map, itemIdMap, childrenMap, itemValuesMap, attributeIdsMap);

        for (PLMBom bom : bomList) {
            if (bom.getItem().getConfigurable() != null && bom.getItem().getConfigurable()) {
                PLMItem bomItem = bom.getItem();
                List<PLMItem> items = itemRepository.findByInstanceOrderByCreatedDateDesc(bom.getItem().getId());
                if (items.size() > 0) {
                    List<String> resolvedItemValues = new ArrayList<>();
                    for (PLMItem instanceItem : items) {
                        List<String> valueList = itemValuesMap.get(bom.getItem().getId());
                        List<Integer> attrIds = attributeIdsMap.get(bom.getItem().getId());
                        Integer[] attriIds = attrIds.toArray(new Integer[attrIds.size()]);
                        String[] vals = valueList.toArray(new String[valueList.size()]);
                        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(instanceItem.getId(), attriIds, vals);
                        if (objectAttributes.size() == valueList.size()) {
                            PLMItemRevision instanceRevision = itemRevisionRepository.findOne(parentItem.getLatestRevision());
                            PLMBom plmBom = bomRepository.findByParentAndItem(instanceRevision, bom.getItem());
                            if (plmBom != null) {
                                plmBom.setItem(instanceItem);
                                plmBom = bomRepository.save(plmBom);
                                applicationEventPublisher.publishEvent(new ItemEvents.BomItemResolvedEvent(parentItem, itemRevision, bom.getItem(), instanceItem));
                                resolvedItemValues.addAll(valueList);
                                if (bomItem.getConfigurable() && plmBom.getItem().getConfigured()) {
                                    PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(bomItem.getLatestRevision());
                                    if (plmItemRevision.getHasBom()) {
                                        if (plmItemRevision.getBomConfiguration() == null) {
                                            List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(plmItemRevision.getId());
                                            if (bomConfigurations.size() > 0) {
                                                for (BOMConfiguration configuration : bomConfigurations) {
                                                    String json = configuration.getRules();
                                                    ObjectMapper objectMapper = new ObjectMapper();
                                                    Map<String, Object> childMap = null;
                                                    try {
                                                        childMap = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                                                        });
                                                    } catch (Exception e) {
                                                        new CassiniException(e.getMessage());
                                                    }
                                                    if (childMap != null && childMap.size() > 0) {
                                                        List<String> configValues = new ArrayList<>();
                                                        Set<Map.Entry<String, Object>> entries1 = childMap.entrySet();
                                                        for (Map.Entry<String, Object> entry : entries1) {
                                                            if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                                                                configValues.add((String) entry.getValue());
                                                            }
                                                        }
                                                        if (configValues.containsAll(resolvedItemValues)) {
                                                            PLMItemRevision revision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
                                                            revision.setBomConfiguration(configuration.getId());
                                                            revision = itemRevisionRepository.save(revision);
                                                            resolveBomItemChildren(plmItemRevision, plmBom.getItem(), childMap);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            resolveBomConfigChildren(plmItemRevision, resolvedItemValues, plmBom);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public BomRollUpReportDto getItemBomRollUpReport(Integer itemId, List<PLMItemTypeAttribute> attributes) {
        BomRollUpReportDto bomRollUpReportDto = new BomRollUpReportDto();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
//        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByIdIn(attributeIds);
        HashMap<Integer, PLMItemTypeAttribute> itemTypeAttributeHashMap = new LinkedHashMap<>();
        List<Integer> measurementIds = new ArrayList<>();
        List<String> itemTypeNames = new ArrayList<>();
        attributes.forEach(plmItemTypeAttribute -> {
            if (plmItemTypeAttribute.getMeasurement() != null) {
                measurementIds.add(plmItemTypeAttribute.getMeasurement().getId());
            }
            if (itemTypeNames.indexOf(plmItemTypeAttribute.getName()) == -1) {
                itemTypeNames.add(plmItemTypeAttribute.getName());
            }
            itemTypeAttributeHashMap.put(plmItemTypeAttribute.getId(), plmItemTypeAttribute);
        });

        Map<Integer, PLMItemRevision> revisionMap = new LinkedHashMap();
        Map<Integer, List<PLMBom>> allBomItemMap = new LinkedHashMap();
        Map<Integer, List<MeasurementUnit>> measurementUnitsMap = new LinkedHashMap<>();
        Map<Integer, MeasurementUnit> baseUnitMap = new LinkedHashMap<>();
        Map<Integer, MeasurementUnit> measurementUnitMap = new LinkedHashMap<>();
        Map<String, List<PLMItemTypeAttribute>> attributeNameMap = new LinkedHashMap<>();

        List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementIn(measurementIds);
        List<MeasurementUnit> measurementBaseUnits = measurementUnitRepository.findByMeasurementInAndBaseUnitTrue(measurementIds);

        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByNameIn(itemTypeNames);

        attributeNameMap = itemTypeAttributes.stream().collect(Collectors.groupingBy(d -> d.getName()));
        measurementUnitsMap = measurementUnits.stream().collect(Collectors.groupingBy(d -> d.getMeasurement()));
        measurementUnitMap = measurementUnits.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        baseUnitMap = measurementBaseUnits.stream().collect(Collectors.toMap(x -> x.getMeasurement(), x -> x));

        List<Integer> latestRevisionIds = bomRepository.getBomItemLatestRevisionIds(itemId);
        if (latestRevisionIds.size() > 0) {
            List<Integer> revisions = new LinkedList<>();
            revisions.add(itemId);
            revisions.addAll(latestRevisionIds);
            latestRevisionIds.forEach(latestRevisionId -> {
                collectBomChildrenIds(latestRevisionId, "bom.latest", revisions);
            });
            List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
            allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
            List<PLMItemRevision> latestRevisions = itemRevisionRepository.findByIdIn(revisions);
            revisionMap = latestRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
        }

        PLMBom plmBom = new PLMBom();
        plmBom.setItem(item);
        BomRollUpReport itemRollUpReport = new BomRollUpReport();
        itemRollUpReport.setBomItem(convertToBomDto(plmBom, "bom.latest", revisionMap));
        itemRollUpReport.getBomItem().setLevel(0);
        for (PLMItemTypeAttribute typeAttribute : attributes) {
            PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributeHashMap.get(typeAttribute.getId());
            BomRollUpAttribute bomRollUpAttribute = new BomRollUpAttribute();
            bomRollUpAttribute.setName(itemTypeAttribute.getName());
            bomRollUpAttribute.setDataType(itemTypeAttribute.getDataType());
            if (itemTypeAttribute.getMeasurement() != null) {
                bomRollUpAttribute.setMeasurement(itemTypeAttribute.getMeasurement().getId());
            }
            if (itemTypeAttribute.getMeasurementUnit() != null) {
                bomRollUpAttribute.setMeasurementUnit(itemTypeAttribute.getMeasurementUnit().getId());
                bomRollUpAttribute.setUnitSymbol(itemTypeAttribute.getMeasurementUnit().getSymbol());
            }
            itemRollUpReport.getBomRollUpAttributes().add(bomRollUpAttribute);
            itemRollUpReport.getBomRollUpAttributes().add(bomRollUpAttribute);
        }

        bomRollUpReportDto.getBomItems().add(itemRollUpReport);

        attributes.forEach(attribute -> {
            bomRollUpReportDto.getBomAttributes().add(attribute);
            bomRollUpReportDto.getBomAttributes().add(attribute);
        });

        itemRollUpReport = visitChildrenForRollup(itemRollUpReport, attributes, revisionMap, allBomItemMap, measurementUnitsMap, measurementUnitMap, baseUnitMap, attributeNameMap);

        return bomRollUpReportDto;
    }

    private BomRollUpReport visitChildrenForRollup(BomRollUpReport bomRollUpReport, List<PLMItemTypeAttribute> itemTypeAttributes, Map<Integer, PLMItemRevision> revisionMap,
                                                   Map<Integer, List<PLMBom>> allBomMap, Map<Integer, List<MeasurementUnit>> measurementUnitsMap, Map<Integer, MeasurementUnit> measurementUnitMap,
                                                   Map<Integer, MeasurementUnit> baseUnitMap, Map<String, List<PLMItemTypeAttribute>> attributeNameMap) {

        List<PLMBom> bomList = allBomMap.containsKey(bomRollUpReport.getBomItem().getLatestRevision()) ? allBomMap.get(bomRollUpReport.getBomItem().getLatestRevision()) : new ArrayList<>();
        bomList.forEach(bom -> {
            BomRollUpReport rollUpReport = new BomRollUpReport();
            rollUpReport.setBomItem(convertToBomDto(bom, "bom.latest", revisionMap));
            rollUpReport.getBomItem().setLevel(bomRollUpReport.getBomItem().getLevel() + 1);
            for (PLMItemTypeAttribute itemTypeAttribute : itemTypeAttributes) {
                BomRollUpAttribute bomRollUpAttribute = new BomRollUpAttribute();
                bomRollUpAttribute.setName(itemTypeAttribute.getName());
                bomRollUpAttribute.setDataType(itemTypeAttribute.getDataType());
                if (itemTypeAttribute.getMeasurement() != null) {
                    bomRollUpAttribute.setMeasurement(itemTypeAttribute.getMeasurement().getId());
                }
                if (itemTypeAttribute.getMeasurementUnit() != null) {
                    bomRollUpAttribute.setMeasurementUnit(itemTypeAttribute.getMeasurementUnit().getId());
                    bomRollUpAttribute.setUnitSymbol(itemTypeAttribute.getMeasurementUnit().getSymbol());
                }
                rollUpReport.getBomRollUpAttributes().add(bomRollUpAttribute);
                BomRollUpAttribute cloneAttribute = (BomRollUpAttribute) Utils.cloneObject(bomRollUpAttribute, BomRollUpAttribute.class);
                cloneAttribute.setUnitValue(Boolean.FALSE);
                rollUpReport.getBomRollUpAttributes().add(cloneAttribute);
            }

            if (!rollUpReport.getBomItem().getHasBom()) {
                for (PLMItemTypeAttribute typeAttribute : itemTypeAttributes) {
                    List<PLMItemTypeAttribute> nameAttributes = attributeNameMap.containsKey(typeAttribute.getName()) ? attributeNameMap.get(typeAttribute.getName()) : new ArrayList<PLMItemTypeAttribute>();
                    ObjectAttribute objectAttribute = null;
                    for (PLMItemTypeAttribute nameAttribute : nameAttributes) {
                        objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(bom.getItem().getId(), nameAttribute.getId());
                        if (objectAttribute != null) {
                            break;
                        }
                    }
                    if (objectAttribute != null) {
                        for (BomRollUpAttribute upAttribute : rollUpReport.getBomRollUpAttributes()) {
                            if (upAttribute.getName().equals(typeAttribute.getName()) && (upAttribute.getUnitValue() || !upAttribute.getUnitValue())) {
                                upAttribute.setObjectAttribute(objectAttribute);
                                if (upAttribute.getDataType().equals(DataType.INTEGER)) {
                                    upAttribute.setRollUpValue(objectAttribute.getIntegerValue().doubleValue());
                                    upAttribute.setMultipliedValue(bom.getQuantity().doubleValue() * upAttribute.getRollUpValue());
                                    upAttribute.setActualValue(objectAttribute.getIntegerValue().doubleValue());
                                    upAttribute.setActualMultipliedValue(bom.getQuantity().doubleValue() * objectAttribute.getIntegerValue());
                                } else {

                                    if (upAttribute.getMeasurementUnit() != null) {
                                        if (objectAttribute.getMeasurementUnit() != null) {
                                            List<MeasurementUnit> measurementUnits = measurementUnitsMap.containsKey(upAttribute.getMeasurement()) ? measurementUnitsMap.get(upAttribute.getMeasurement()) : new ArrayList<MeasurementUnit>();
                                            MeasurementUnit baseUnit = baseUnitMap.get(upAttribute.getMeasurement());
                                            MeasurementUnit selectedMeasurementUnit = measurementUnitMap.get(upAttribute.getMeasurementUnit());
                                            MeasurementUnit measurementUnit = measurementUnitMap.get(objectAttribute.getMeasurementUnit().getId());

                                            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);
                                            Integer selectedUnitIndex = measurementUnits.indexOf(selectedMeasurementUnit);
                                            Integer attributeIndex = measurementUnits.indexOf(measurementUnit);

                                            Double attributeValue = 0.0;
                                            Double actualValue = 0.0;

                                            if (!selectedUnitIndex.equals(baseUnitIndex)) {
                                                attributeValue = objectAttribute.getDoubleValue() * selectedMeasurementUnit.getConversionFactor();
                                            } else {
                                                attributeValue = objectAttribute.getDoubleValue();
                                            }

                                            if (!attributeIndex.equals(baseUnitIndex)) {
                                                actualValue = objectAttribute.getDoubleValue() * measurementUnit.getConversionFactor();
                                            } else {
                                                actualValue = objectAttribute.getDoubleValue();
                                            }
                                            String stringValue = new BigDecimal(attributeValue).toString();
                                            String[] strings = stringValue.split("\\.");
                                            if (strings.length > 1) {
                                                if (strings[1].length() > 4) {
                                                    upAttribute.setApproximated(true);
                                                }
                                            }
                                            upAttribute.setRollUpValue(Double.parseDouble(String.format("%.4f", attributeValue)));
                                            upAttribute.setMultipliedValue(bom.getQuantity().doubleValue() * Double.parseDouble(String.format("%.4f", attributeValue)));

                                            upAttribute.setActualValue(actualValue);
                                            upAttribute.setActualMultipliedValue(bom.getQuantity().doubleValue() * actualValue);
                                            upAttribute.setUnitSymbol(measurementUnit.getSymbol());
                                        }
                                    } else {
                                        upAttribute.setRollUpValue(objectAttribute.getDoubleValue());
                                        String stringValue = new BigDecimal(upAttribute.getRollUpValue()).toString();
                                        String[] strings = stringValue.split("\\.");
                                        if (strings.length > 1) {
                                            if (strings[1].length() > 4) {
                                                upAttribute.setApproximated(true);
                                            }
                                        }
                                        upAttribute.setMultipliedValue(bom.getQuantity().doubleValue() * Double.parseDouble(String.format("%.4f", upAttribute.getRollUpValue())));

                                        upAttribute.setActualValue(objectAttribute.getDoubleValue());
                                        upAttribute.setActualMultipliedValue(bom.getQuantity().doubleValue() * objectAttribute.getDoubleValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            bomRollUpReport.getChildren().add(rollUpReport);
            rollUpReport = visitChildrenForRollup(rollUpReport, itemTypeAttributes, revisionMap, allBomMap, measurementUnitsMap, measurementUnitMap, baseUnitMap, attributeNameMap);

            for (BomRollUpAttribute bomRollUpAttribute : rollUpReport.getBomRollUpAttributes()) {
                for (BomRollUpAttribute upAttribute : bomRollUpReport.getBomRollUpAttributes()) {
                    if (upAttribute.getName().equals(bomRollUpAttribute.getName()) && bomRollUpAttribute.getUnitValue()) {
                        if (bomRollUpReport.getBomItem().getId() != null) {
                            if (upAttribute.getDataType().equals(DataType.INTEGER)) {
                                upAttribute.setRollUpValue(upAttribute.getRollUpValue() + (bomRollUpAttribute.getMultipliedValue()));
                            } else if (upAttribute.getDataType().equals(DataType.DOUBLE)) {
                                String stringValue = bomRollUpAttribute.getMultipliedValue().toString();
                                String[] strings = stringValue.split("\\.");
                                if (strings.length > 1) {
                                    if (strings[1].length() > 4) {
                                        upAttribute.setApproximated(true);
                                    }
                                }
                                if (bomRollUpAttribute.getApproximated()) {
                                    upAttribute.setApproximated(true);
                                }
                                upAttribute.setRollUpValue(upAttribute.getRollUpValue() + (Double.parseDouble(String.format("%.4f", bomRollUpAttribute.getMultipliedValue()))));
                            }
                        }
                    }
                }
            }
        });

        for (BomRollUpAttribute bomRollUpAttribute : bomRollUpReport.getBomRollUpAttributes()) {
            if (bomRollUpReport.getChildren().size() > 0) {
                Double multipliedValue = 0.0;
                for (BomRollUpReport rollUpReport : bomRollUpReport.getChildren()) {
                    for (BomRollUpAttribute attribute : rollUpReport.getBomRollUpAttributes()) {
                        if (bomRollUpAttribute.getName().equals(attribute.getName()) && attribute.getUnitValue()) {
                            multipliedValue = multipliedValue + attribute.getMultipliedValue();

                            if (attribute.getApproximated()) {
                                bomRollUpAttribute.setApproximated(true);
                            }
                        }
                    }
                }
                if (bomRollUpReport.getBomItem().getId() != null) {
                    String stringValue = multipliedValue.toString();
                    String[] strings = stringValue.split("\\.");
                    if (strings.length > 1) {
                        if (strings[1].length() > 4) {
                            bomRollUpAttribute.setApproximated(true);
                        }
                    }
                    bomRollUpAttribute.setMultipliedValue(bomRollUpReport.getBomItem().getQuantity() * Double.parseDouble(String.format("%.4f", multipliedValue)));
                } else {
                    String stringValue = multipliedValue.toString();
                    String[] strings = stringValue.split("\\.");
                    if (strings.length > 1) {
                        if (strings[1].length() > 4) {
                            bomRollUpAttribute.setApproximated(true);
                        }
                    }
                    bomRollUpAttribute.setMultipliedValue(Double.parseDouble(String.format("%.4f", multipliedValue)));
                }
            }
        }

        return bomRollUpReport;
    }


    @Transactional
    public BomComplianceReport getBomComplianceReport(Integer itemId) {
        BomComplianceReport bomComplianceReport = new BomComplianceReport();

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        List<PGCItemSpecification> itemSpecifications = pgcItemSpecificationRepository.getItemSpecifications(itemId);

        BomComplianceData bomComplianceData = new BomComplianceData();
        bomComplianceData.setItemName(item.getItemName());
        bomComplianceData.setItemNumber(item.getItemNumber());
        bomComplianceData.setItemId(item.getId());
        bomComplianceData.setItemRevision(itemRevision.getId());
        for (PGCItemSpecification itemSpecification : itemSpecifications) {
            ComplianceSpecification specification = new ComplianceSpecification();
            specification.setId(itemSpecification.getSpecification().getId());
            specification.setName(itemSpecification.getSpecification().getName());

            ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);

            bomComplianceData.getSpecifications().add(complianceSpecification);
        }
        bomComplianceData = visitChildrenForComplianceReport(bomComplianceData, bomComplianceData.getSpecifications());

        bomComplianceReport.setBomComplianceData(bomComplianceData);
        itemSpecifications.forEach(pgcItemSpecification -> {
            ComplianceSpecification specification = new ComplianceSpecification();
            specification.setId(pgcItemSpecification.getSpecification().getId());
            specification.setName(pgcItemSpecification.getSpecification().getName());
            ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);
            bomComplianceReport.getSpecifications().add(complianceSpecification);
        });

        for (ComplianceSpecification specification : bomComplianceData.getSpecifications()) {
            Integer nonComplaintCount = 0;
            for (BomComplianceData childItem : bomComplianceData.getChildren()) {
                for (ComplianceSpecification childSpec : childItem.getSpecifications()) {
                    if (childSpec.getId().equals(specification.getId())) {
                        if (!childSpec.getCompliant() && !childSpec.getExempt()) {
                            nonComplaintCount++;
                        }
                    }
                }
            }
            if (nonComplaintCount == 0) {
                specification.setCompliant(true);
            }
        }

        return bomComplianceReport;
    }

    private BomComplianceData visitChildrenForComplianceReport(BomComplianceData bomComplianceData, List<ComplianceSpecification> itemSpecifications) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomComplianceData.getItemRevision());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        bomList.forEach(bom -> {
            BomComplianceData complianceData = new BomComplianceData();
            PLMItemRevision revision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            PLMItem item = itemRepository.findOne(revision.getItemMaster());

            complianceData.setItemName(item.getItemName());
            complianceData.setItemNumber(item.getItemNumber());
            complianceData.setItemId(item.getId());
            complianceData.setItemRevision(revision.getId());
            complianceData.setParent(bom.getParent().getId());
            complianceData.setRequireCompliance(bom.getItem().getRequireCompliance());
            for (ComplianceSpecification itemSpecification : itemSpecifications) {
                ComplianceSpecification specification = new ComplianceSpecification();
                specification.setId(itemSpecification.getId());
                specification.setName(itemSpecification.getName());
                ComplianceSpecification complianceSpecification = JsonUtils.cloneEntity(specification, ComplianceSpecification.class);
                complianceData.getSpecifications().add(complianceSpecification);
            }
            PLMItemManufacturerPart itemManufacturerPart = itemManufacturerPartRepository.findByItemAndStatus(bom.getItem().getLatestRevision(), ManufacturerPartStatus.PREFERRED);
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = itemManufacturerPartRepository.findByStatusAndItemOrderByIdAsc(ManufacturerPartStatus.ALTERNATE, bom.getItem().getLatestRevision());
                if (manufacturerParts.size() > 0) {
                    complianceData.setPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    complianceData.setPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                    complianceData.setPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                    complianceData.setMfrName(manufacturerRepository.findOne(manufacturerParts.get(0).getManufacturerPart().getManufacturer()).getName());
                }
            } else {
                complianceData.setPartId(itemManufacturerPart.getManufacturerPart().getId());
                complianceData.setPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
                complianceData.setPartName(itemManufacturerPart.getManufacturerPart().getPartName());
                complianceData.setMfrName(manufacturerRepository.findOne(itemManufacturerPart.getManufacturerPart().getManufacturer()).getName());
            }
            if (bom.getItem().getMakeOrBuy().equals(MakeOrBuy.MAKE)) {
                complianceData.getSpecifications().forEach(complianceSpecification -> {
                    complianceSpecification.setExempt(true);
                });
            } else {
                if (complianceData.getRequireCompliance()) {
                    if (complianceData.getPartId() != null) {
                        List<PGCDeclarationPart> declarationParts = pgcDeclarationPartRepository.getDeclarationPartByPart(complianceData.getPartId());
                        if (declarationParts.size() > 0) {
                            for (PGCDeclarationPart declarationPart : declarationParts) {
                                List<PGCBosItem> bosItems = new ArrayList<PGCBosItem>();
                                if (declarationPart.getBos() != null) {
                                    bosItems = pgcBosItemRepository.findByBos(declarationPart.getBos());
                                }
                                if (bosItems.size() > 0) {
                                    List<PGCDeclarationPartCompliance> declarationPartCompliances = pgcDeclarationPartComplianceRepository.findByDeclarationPart(declarationPart.getId());
                                    if (declarationPartCompliances.size() > 0) {
                                        for (ComplianceSpecification itemSpecification : complianceData.getSpecifications()) {
                                            if (!complianceData.getRequireCompliance()) {
                                                itemSpecification.setCompliant(false);
                                            } else {
                                                for (PGCDeclarationPartCompliance declarationPartCompliance : declarationPartCompliances) {
                                                    PGCDeclarationSpecification declarationSpecification = pgcDeclarationSpecificationRepository.findOne(declarationPartCompliance.getDeclarationSpec());
                                                    if (declarationSpecification != null && declarationSpecification.getSpecification().getId().equals(itemSpecification.getId())) {
                                                        itemSpecification.setCompliant(declarationPartCompliance.getCompliant());

                                                        for (PGCBosItem bosItem : bosItems) {
                                                            PGCSubstance substance = pgcSubstanceRepository.findOne(bosItem.getSubstance());
                                                            PGCSpecificationSubstance specificationSubstance = pgcSpecificationSubstanceRepository.findBySpecificationAndSubstance(itemSpecification.getId(), substance);
                                                            if (specificationSubstance != null) {
                                                                PGCBosItem pgcBosItem = JsonUtils.cloneEntity(bosItem, PGCBosItem.class);
                                                                BosItemDto bosItemDto = new BosItemDto();
                                                                bosItemDto.setId(pgcBosItem.getId());
                                                                bosItemDto.setBos(pgcBosItem.getBos());
                                                                bosItemDto.setBosItemType(pgcBosItem.getBosItemType());
                                                                if (pgcBosItem.getBosItemType().equals(BosItemType.SUBSTANCE)) {

                                                                    bosItemDto.setSubstance(substance.getId());
                                                                    bosItemDto.setCasNumber(substance.getCasNumber());
                                                                    bosItemDto.setSubstanceName(substance.getName());
                                                                    bosItemDto.setSubstanceType(substance.getType().getName());
                                                                }
                                                                if (pgcBosItem.getUom() != null) {
                                                                    MeasurementUnit measurementUnit = measurementUnitRepository.findOne(pgcBosItem.getUom());
                                                                    bosItemDto.setUnitName(measurementUnit.getName());
                                                                    bosItemDto.setUnitSymbol(measurementUnit.getSymbol());
                                                                    Measurement measurement = measurementRepository.findOne(measurementUnit.getMeasurement());
                                                                    MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurement.getId());
                                                                    if (!baseUnit.getId().equals(measurementUnit.getId())) {
                                                                        bosItemDto.setMass(pgcBosItem.getMass() * measurementUnit.getConversionFactor());
                                                                    } else {
                                                                        bosItemDto.setMass(pgcBosItem.getMass());
                                                                    }
                                                                }
                                                                if (specificationSubstance.getUom() != null) {
                                                                    MeasurementUnit measurementUnit = measurementUnitRepository.findOne(specificationSubstance.getUom());
                                                                    bosItemDto.setSpecMassUnitName(measurementUnit.getName());
                                                                    bosItemDto.setSpecMassUnitSymbol(measurementUnit.getSymbol());
                                                                    Measurement measurement = measurementRepository.findOne(measurementUnit.getMeasurement());
                                                                    MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(measurement.getId());
                                                                    if (!baseUnit.getId().equals(measurementUnit.getId())) {
                                                                        bosItemDto.setSpecMass(specificationSubstance.getThresholdMass() * measurementUnit.getConversionFactor());
                                                                    } else {
                                                                        bosItemDto.setSpecMass(specificationSubstance.getThresholdMass());
                                                                    }
                                                                }
                                                                itemSpecification.getBillOfSubstances().add(bosItemDto);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    complianceData.getSpecifications().forEach(complianceSpecification -> {
                        complianceSpecification.setExempt(true);
                    });
                }
            }
            bomComplianceData.getChildren().add(complianceData);
            complianceData = visitChildrenForComplianceReport(complianceData, itemSpecifications);


            for (ComplianceSpecification specification : complianceData.getSpecifications()) {
                if (complianceData.getChildren().size() > 0) {
                    Integer nonComplaintCount = 0;
                    for (BomComplianceData childItem : complianceData.getChildren()) {
                        for (ComplianceSpecification childSpec : childItem.getSpecifications()) {
                            if (childSpec.getId().equals(specification.getId())) {
                                if (!childSpec.getCompliant() && !childSpec.getExempt()) {
                                    nonComplaintCount++;
                                }
                            }
                        }
                    }
                    if (nonComplaintCount == 0) {
                        specification.setCompliant(true);
                    }
                }
            }

        });

        return bomComplianceData;
    }


    @Transactional
    public BomWhereUsedReport getBomWhereUsedReport(Integer itemId) {
        BomWhereUsedReport bomWhereUsedReport = new BomWhereUsedReport();
        HashMap<Integer, PLMItem> itemMap = new LinkedHashMap<>();
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem itemRevisionMaster = itemRepository.findOne(itemRevision.getItemMaster());

        BomDataDto bomDataDto = new BomDataDto();
        bomDataDto.setName(itemRevisionMaster.getItemName());
        bomDataDto.setItemId(itemRevisionMaster.getId());
        PLMBom plmBom = new PLMBom();
        plmBom.setItem(itemRevisionMaster);
        BomDto bomDto = convertToBomItemDto(plmBom, "bom.latest");
        List<PLMBom> plmBoms = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        plmBoms.forEach(bom -> {
            if (bomDataDto.getChildren() == null) {
                bomDataDto.setChildren(new ArrayList<>());
            }
            BomDataDto dataDto = new BomDataDto();
            dataDto.setName(bom.getItem().getItemName());
            dataDto.setItemId(bom.getItem().getId());
            BomDto dto = convertToBomItemDto(bom, "bom.latest");
           /* PLMItemRevision bomItemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            PLMItem item = itemRepository.findOne(bomItemRevision.getItemMaster());
            if (!bomItemRevision.getHasBom()) {
                PLMItem plmItem = itemMap.get(item.getId());
                if (plmItem == null) {
                    item.setCount(1);
                } else {
                    item.setCount(plmItem.getCount() + 1);
                }
                itemMap.put(item.getId(), item);
            }*/
            if (bomDto.getChildren().indexOf(dto) == -1) {
                bomDto.getChildren().add(dto);
            }
            bomDataDto.getChildren().add(dataDto);
            dto = visitChildrenForWhereUsedReport(dto, dataDto);
        });

//        plmBom = visitChildrenForWhereUsedReport(plmBom, itemMap, bomDataDto);

        List<PLMItem> items = new ArrayList<>();
        for (PLMItem plmItem : itemMap.values()) {
            items.add(plmItem);
        }

        bomWhereUsedReport.getBomItems().add(bomDto);
        bomWhereUsedReport.getItems().addAll(items);
        bomWhereUsedReport.setBomDataDto(bomDataDto);

        return bomWhereUsedReport;
    }

    private BomDto visitChildrenForWhereUsedReport(BomDto bomDto, BomDataDto bomDataDto) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomDto.getLatestRevision());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        bomList.forEach(bom -> {
            if (bomDataDto.getChildren() == null) {
                bomDataDto.setChildren(new ArrayList<>());
            }
            BomDataDto dataDto = new BomDataDto();
            dataDto.setName(bom.getItem().getItemName());
            dataDto.setItemId(bom.getItem().getId());
            BomDto dto = convertToBomItemDto(bom, "bom.latest");
            /*PLMItemRevision bomItemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            PLMItem item = itemRepository.findOne(bomItemRevision.getItemMaster());
            if (!bomItemRevision.getHasBom()) {
                PLMItem plmItem = itemMap.get(item.getId());
                if (plmItem == null) {
                    item.setCount(1);
                } else {
                    item.setCount(plmItem.getCount() + 1);
                }
                itemMap.put(item.getId(), item);
            }*/

            if (bomDto.getChildren().indexOf(dto) == -1) {
                bomDto.getChildren().add(dto);
            }
            bomDataDto.getChildren().add(dataDto);
            dto = visitChildrenForWhereUsedReport(dto, dataDto);
        });

        return bomDto;
    }

    @Transactional(readOnly = true)
    public void collectBomChildrenIds(Integer itemId, String bomRule, List<Integer> revisionIds) {
        switch (bomRule) {
            case "bom.latest":
                List<Integer> ids = bomRepository.getBomItemLatestRevisionIds(itemId);
                revisionIds.addAll(ids);
                ids.forEach(id -> {
                    collectBomChildrenIds(id, bomRule, revisionIds);
                });
                break;
            case "bom.latest.released":
                List<Integer> latestReleasedIds = bomRepository.getBomItemLatestReleasedRevisionIds(itemId);
                revisionIds.addAll(latestReleasedIds);
                latestReleasedIds.forEach(id -> {
                    collectBomChildrenIds(id, bomRule, revisionIds);
                });
                break;
            case "bom.as.released":
                List<Integer> asReleasedIds = bomRepository.getBomItemAsReleasedRevisionIds(itemId);
                revisionIds.addAll(asReleasedIds);
                asReleasedIds.forEach(id -> {
                    collectBomChildrenIds(id, bomRule, revisionIds);
                });
            default:
                break;
        }
    }


    @Transactional(readOnly = true)
    public List<BomDto> getTotalBom(Integer itemId, Boolean hierarchy, String bomRule) {
        List<BomDto> bomList = new LinkedList<>();
        Map<Integer, PLMItemRevision> revisionMap = new LinkedHashMap();
        Map<Integer, PLMItemManufacturerPart> preferredMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMItemManufacturerPart>> alternateMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMBom>> allBomItemMap = new LinkedHashMap();
        List<PLMBom> plmBoms = bomRepository.findByParentIdOrderBySequenceAsc(itemId);

        switch (bomRule) {
            case "bom.latest":
                List<Integer> latestRevisionIds = bomRepository.getBomItemLatestRevisionIds(itemId);
                if (latestRevisionIds.size() > 0) {
                    List<Integer> revisions = new LinkedList<>();
                    revisions.addAll(latestRevisionIds);
                    if (hierarchy) {
                        latestRevisionIds.forEach(latestRevisionId -> {
                            collectBomChildrenIds(latestRevisionId, bomRule, revisions);
                        });
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    } else {
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    }
                    List<PLMItemRevision> latestRevisions = itemRevisionRepository.findByIdIn(revisions);
                    List<PLMItemManufacturerPart> latestRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
                    List<PLMItemManufacturerPart> latestRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
                    revisionMap = latestRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
                    preferredMfrPartMap = latestRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
                    alternateMfrPartMap = latestRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
                }
                break;
            case "bom.latest.released":
                List<Integer> latestReleasedRevisionIds = bomRepository.getBomItemLatestReleasedRevisionIds(itemId);
                if (latestReleasedRevisionIds.size() > 0) {
                    List<Integer> revisions = new LinkedList<>();
                    revisions.addAll(latestReleasedRevisionIds);
                    if (hierarchy) {
                        latestReleasedRevisionIds.forEach(latestRevisionId -> {
                            collectBomChildrenIds(latestRevisionId, bomRule, revisions);
                        });
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    } else {
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    }
                    List<PLMItemRevision> latestReleasedRevisions = itemRevisionRepository.findByIdIn(revisions);
                    List<PLMItemManufacturerPart> latestReleasedRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
                    List<PLMItemManufacturerPart> latestReleasedRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
                    revisionMap = latestReleasedRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
                    preferredMfrPartMap = latestReleasedRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
                    alternateMfrPartMap = latestReleasedRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
                }
                break;
            case "bom.as.released":
                List<Integer> asReleasedRevisionIds = bomRepository.getBomItemAsReleasedRevisionIds(itemId);
                if (asReleasedRevisionIds.size() > 0) {
                    List<Integer> revisions = new LinkedList<>();
                    revisions.addAll(asReleasedRevisionIds);
                    if (hierarchy) {
                        asReleasedRevisionIds.forEach(latestRevisionId -> {
                            collectBomChildrenIds(latestRevisionId, bomRule, revisions);
                        });
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    } else {
                        List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
                        allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
                    }
                    List<PLMItemRevision> asReleasedRevisions = itemRevisionRepository.findByIdIn(revisions);
                    List<PLMItemManufacturerPart> asReleasedRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
                    List<PLMItemManufacturerPart> asReleasedRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
                    revisionMap = asReleasedRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
                    preferredMfrPartMap = asReleasedRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
                    alternateMfrPartMap = asReleasedRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
                }
                break;
            default:
                break;
        }
        Map<Integer, PLMItemRevision> map = revisionMap;
        Map<Integer, PLMItemManufacturerPart> preferredPartMap = preferredMfrPartMap;
        Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap = alternateMfrPartMap;
        Map<Integer, List<PLMBom>> allBomMap = allBomItemMap;

        plmBoms.forEach(bom -> {
            BomDto bomDto = convertToBomDto(bom, bomRule, map);
            bomDto.setLevel(0);
            bomDto.setExpanded(true);
            if (hierarchy && bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildren(bomDto, bomRule, map, preferredPartMap, alternatePartMap, allBomMap);
            }
            List<PLMBom> children = allBomMap.containsKey(bomDto.getLatestRevision()) ? allBomMap.get(bomDto.getLatestRevision()) : new ArrayList<>();
            bomDto.setCount(children.size());
            PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
                if (manufacturerParts != null && manufacturerParts.size() > 0) {
                    bomDto.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                    bomDto.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                    bomDto.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                }
            } else {
                bomDto.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
                bomDto.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
                bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
                bomDto.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
            }
            bomList.add(bomDto);
        });
        return bomList;
    }

    private String getThumbnail(byte[] thumbnail) {
        String baseString = "";
        byte[] imgBytesAsBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(thumbnail);
        String imgDataAsBase64 = new String(imgBytesAsBase64);
        baseString = "data:image/png;base64," + imgDataAsBase64;
        return baseString;
    }

    @Transactional(readOnly = true)
    public List<BomDto> getTotalBomForPrint(Integer itemId) {
        List<BomDto> bomList = new LinkedList<>();
        Map<Integer, PLMItemRevision> revisionMap = new LinkedHashMap();
        Map<Integer, PLMItemManufacturerPart> preferredMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMItemManufacturerPart>> alternateMfrPartMap = new LinkedHashMap();
        Map<Integer, List<PLMBom>> allBomItemMap = new LinkedHashMap();
        List<PLMBom> plmBoms = bomRepository.findByParentIdOrderBySequenceAsc(itemId);

        List<Integer> latestRevisionIds = bomRepository.getBomItemLatestRevisionIds(itemId);
        if (latestRevisionIds.size() > 0) {
            List<Integer> revisions = new LinkedList<>();
            revisions.addAll(latestRevisionIds);
            latestRevisionIds.forEach(latestRevisionId -> {
                collectBomChildrenIds(latestRevisionId, "bom.latest", revisions);
            });
            List<PLMBom> allBomList = bomRepository.findByParentIdInOrderBySequenceAsc(revisions);
            allBomItemMap = allBomList.stream().collect(Collectors.groupingBy(d -> d.getParent().getId()));
            List<PLMItemRevision> latestRevisions = itemRevisionRepository.findByIdIn(revisions);
            List<PLMItemManufacturerPart> latestRevisionPreferredParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.PREFERRED);
            List<PLMItemManufacturerPart> latestRevisionAlternateParts = itemManufacturerPartRepository.findByItemInAndStatus(revisions, ManufacturerPartStatus.ALTERNATE);
            revisionMap = latestRevisions.stream().collect(Collectors.toMap(x -> x.getId(), x -> x));
            preferredMfrPartMap = latestRevisionPreferredParts.stream().collect(Collectors.toMap(x -> x.getItem(), x -> x));
            alternateMfrPartMap = latestRevisionAlternateParts.stream().collect(Collectors.groupingBy(PLMItemManufacturerPart::getItem));
        }
        Map<Integer, PLMItemRevision> map = revisionMap;
        Map<Integer, PLMItemManufacturerPart> preferredPartMap = preferredMfrPartMap;
        Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap = alternateMfrPartMap;
        Map<Integer, List<PLMBom>> allBomMap = allBomItemMap;

        plmBoms.forEach(bom -> {
            BomDto bomDto = convertToBomDto(bom, "bom.latest", map);
            bomDto.setLevel(0);
            if (bom.getItem().getThumbnail() != null) {
                bomDto.setThumbnailString(getThumbnail(bom.getItem().getThumbnail()));
            } else {
                bomDto.setThumbnailString(null);
            }
            bomList.add(bomDto);
            if (bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildrenPrint(bomDto, "bom.latest", map, preferredPartMap, alternatePartMap, allBomMap, bomList);
            }

            PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
                if (manufacturerParts != null && manufacturerParts.size() > 0) {
                    bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                }
            } else {
                bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
            }
        });
        return bomList;
    }


    private BomDto visitChildrenForItemBomChildrenPrint(BomDto dto, String bomRule, Map<Integer, PLMItemRevision> revisionMap, Map<Integer,
            PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap, Map<Integer, List<PLMBom>> allBomMap, List<BomDto> dtoList) {
        List<PLMBom> bomList = allBomMap.containsKey(dto.getLatestRevision()) ? allBomMap.get(dto.getLatestRevision()) : new ArrayList<>();
        bomList.forEach(bom -> {
            BomDto bomDto = convertToBomDto(bom, bomRule, revisionMap);
            bomDto.setLevel(dto.getLevel() + 1);
            if (bom.getItem().getThumbnail() != null) {
                bomDto.setThumbnailString(getThumbnail(bom.getItem().getThumbnail()));
            } else {
                bomDto.setThumbnailString(null);
            }
            PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
                if (manufacturerParts != null && manufacturerParts.size() > 0) {
                    bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                }
            } else {
                bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
            }
            dtoList.add(bomDto);
            if (bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildrenPrint(bomDto, bomRule, revisionMap, preferredPartMap, alternatePartMap, allBomMap, dtoList);
            }
        });
        return dto;
    }


    @Transactional(readOnly = true)
    public BomDto convertToBomDto(PLMBom bom, String bomRule, Map<Integer, PLMItemRevision> revisionMap) {
        PLMItemRevision itemRevision = null;
        if (revisionMap == null) {
            if (bomRule.equals("bom.latest")) {
                itemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            } else if (bomRule.equals("bom.latest.released")) {
                if (bom.getItem().getLatestReleasedRevision() != null) {
                    itemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestReleasedRevision());
                }
            } else if (bom.getAsReleasedRevision() != null) {
                itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
            }
        } else {
            if (bomRule.equals("bom.latest")) {
                itemRevision = revisionMap.get(bom.getItem().getLatestRevision());
            } else if (bomRule.equals("bom.latest.released")) {
                if (bom.getItem().getLatestReleasedRevision() != null) {
                    itemRevision = revisionMap.get(bom.getItem().getLatestReleasedRevision());
                }
            } else if (bom.getAsReleasedRevision() != null) {
                itemRevision = revisionMap.get(bom.getAsReleasedRevision());
            }
        }
        BomDto bomDto = new BomDto();
        bomDto.setId(bom.getId());
        bomDto.setItem(bom.getItem().getId());
        if (bom.getParent() != null) {
            bomDto.setParent(bom.getParent().getId());
            bomDto.setParentItemMaster(bom.getParent().getItemMaster());
            bomDto.setParentLifecyclePhaseType(bom.getParent().getLifeCyclePhase().getPhaseType());
        }
        if (bom.getSubstituteItem() != null) {
            bomDto.setSubstituteItem(bom.getSubstituteItem().getId());
            bomDto.setSubstituteItemNumber(bom.getSubstituteItem().getItemNumber());
        }
        bomDto.setQuantity(bom.getQuantity());
        bomDto.setRefdes(bom.getRefdes());
        bomDto.setNotes(bom.getNotes());
        bomDto.setAsReleasedRevision(bom.getAsReleasedRevision());
        bomDto.setSequence(bom.getSequence());
        bomDto.setEffectiveFrom(bom.getEffectiveFrom());
        bomDto.setEffectiveTo(bom.getEffectiveTo());
        bomDto.setHasSubstitutes(bom.getHasSubstitutes());
        bomDto.setItemNumber(bom.getItem().getItemNumber());
        bomDto.setItemName(bom.getItem().getItemName());
        bomDto.setItemTypeName(bom.getItem().getItemType().getName());
        bomDto.setDescription(bom.getItem().getDescription());
        if (itemRevision != null) {
            bomDto.setRevision(itemRevision.getRevision());
            bomDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            bomDto.setHasBom(itemRevision.getHasBom());
            bomDto.setLatestRevision(itemRevision.getId());
            bomDto.setItemEffectiveFrom(itemRevision.getEffectiveFrom());
            bomDto.setItemEffectiveTo(itemRevision.getEffectiveTo());
        }
        bomDto.setMakeOrBuy(bom.getItem().getMakeOrBuy());
        if (bom.getItem().getThumbnail() != null) {
            bomDto.setHasThumbnail(true);
        }
        bomDto.setConfigurable(bom.getItem().getConfigurable());
        bomDto.setConfigured(bom.getItem().getConfigured());
        bomDto.setItemClass(bom.getItem().getItemType().getItemClass());
        bomDto.setUnits(bom.getItem().getUnits());
        bomDto.setItemCreatedDate(bom.getItem().getCreatedDate());
        return bomDto;
    }

    @Transactional(readOnly = true)
    public BomDto convertToBomItemDto(PLMBom bom, String bomRule) {
        PLMItemRevision itemRevision = null;
        if (bomRule.equals("bom.latest")) {
            itemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
        } else if (bomRule.equals("bom.latest.released")) {
            if (bom.getItem().getLatestReleasedRevision() != null) {
                itemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestReleasedRevision());
            }
        } else if (bom.getAsReleasedRevision() != null) {
            itemRevision = itemRevisionRepository.findOne(bom.getAsReleasedRevision());
        }
        BomDto bomDto = new BomDto();
        bomDto.setId(bom.getId());
        bomDto.setItem(bom.getItem().getId());
        if (bom.getParent() != null) {
            bomDto.setParent(bom.getParent().getId());
            bomDto.setParentLifecyclePhaseType(bom.getParent().getLifeCyclePhase().getPhaseType());
        }
        bomDto.setQuantity(bom.getQuantity());
        bomDto.setRefdes(bom.getRefdes());
        bomDto.setNotes(bom.getNotes());
        bomDto.setSequence(bom.getSequence());
        bomDto.setHasSubstitutes(bom.getHasSubstitutes());
        bomDto.setItemNumber(bom.getItem().getItemNumber());
        bomDto.setItemName(bom.getItem().getItemName());
        bomDto.setItemTypeName(bom.getItem().getItemType().getName());
        bomDto.setDescription(bom.getItem().getDescription());
        if (itemRevision != null) {
            bomDto.setRevision(itemRevision.getRevision());
            bomDto.setLifeCyclePhase(itemRevision.getLifeCyclePhase());
            bomDto.setHasBom(itemRevision.getHasBom());
            bomDto.setLatestRevision(itemRevision.getId());
            bomDto.setCount(bomRepository.getItemBomCount(itemRevision.getId()));
        }
        bomDto.setMakeOrBuy(bom.getItem().getMakeOrBuy());
        bomDto.setConfigurable(bom.getItem().getConfigurable());
        bomDto.setConfigured(bom.getItem().getConfigured());
        bomDto.setItemClass(bom.getItem().getItemType().getItemClass());
        bomDto.setUnits(bom.getItem().getUnits());
        return bomDto;
    }

    private BomDto visitChildrenForItemBomChildren(BomDto dto, String bomRule, Map<Integer, PLMItemRevision> revisionMap, Map<Integer,
            PLMItemManufacturerPart> preferredPartMap, Map<Integer, List<PLMItemManufacturerPart>> alternatePartMap, Map<Integer, List<PLMBom>> allBomMap) {
        List<PLMBom> bomList = allBomMap.containsKey(dto.getLatestRevision()) ? allBomMap.get(dto.getLatestRevision()) : new ArrayList<>();
        bomList.forEach(bom -> {
            BomDto bomDto = convertToBomDto(bom, bomRule, revisionMap);
            bomDto.setLevel(dto.getLevel() + 1);
            bomDto.setExpanded(true);
            if (bom.getItem().getThumbnail() != null) {
                bomDto.setThumbnailString(getThumbnail(bom.getItem().getThumbnail()));
            } else {
                bomDto.setThumbnailString(null);
            }
            PLMItemManufacturerPart itemManufacturerPart = preferredPartMap.get(bom.getItem().getLatestRevision());
            if (itemManufacturerPart == null) {
                List<PLMItemManufacturerPart> manufacturerParts = alternatePartMap.get(bom.getItem().getLatestRevision());
                if (manufacturerParts != null && manufacturerParts.size() > 0) {
                    bomDto.setMfrId(manufacturerParts.get(0).getManufacturerPart().getManufacturer());
                    bomDto.setMfrPartId(manufacturerParts.get(0).getManufacturerPart().getId());
                    bomDto.setMfrPartNumber(manufacturerParts.get(0).getManufacturerPart().getPartNumber());
                    bomDto.setMfrPartName(manufacturerParts.get(0).getManufacturerPart().getPartName());
                }
            } else {
                bomDto.setMfrId(itemManufacturerPart.getManufacturerPart().getManufacturer());
                bomDto.setMfrPartId(itemManufacturerPart.getManufacturerPart().getId());
                bomDto.setMfrPartNumber(itemManufacturerPart.getManufacturerPart().getPartNumber());
                bomDto.setMfrPartName(itemManufacturerPart.getManufacturerPart().getPartName());
            }
            dto.getChildren().add(bomDto);
            if (bomDto.getLatestRevision() != null) {
                bomDto = visitChildrenForItemBomChildren(bomDto, bomRule, revisionMap, preferredPartMap, alternatePartMap, allBomMap);
            }
        });
        return dto;
    }


    @Transactional
    public BomWhereUsedReport getBomWhereUsedReportByIds(Integer itemId, List<Integer> ids) {
        BomWhereUsedReport bomWhereUsedReport = new BomWhereUsedReport();

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());
        PLMBom plmBom = new PLMBom();
        plmBom.setItem(item);
        plmBom.setBomItemRevision(itemRevision);
        plmBom = visitChildrenForWhereUsedReportByIds(plmBom, ids);

        bomWhereUsedReport.getBomItems().add(convertToBomItemDto(plmBom, "bom.latest"));

        return bomWhereUsedReport;
    }

    private PLMBom visitChildrenForWhereUsedReportByIds(PLMBom plmBom, List<Integer> ids) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
        List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
        bomList.forEach(bom -> {
            PLMItemRevision bomItemRevision = itemRevisionRepository.findOne(bom.getItem().getLatestRevision());
            if (bomItemRevision.getHasBom()) {
                bom.setBomItemRevision(bomItemRevision);
                plmBom.getChildrens().add(bom);
            } else {
                Boolean itemExist = false;
                for (Integer id : ids) {
                    if (bom.getItem().getId().equals(id)) {
                        plmBom.setItemExist(true);
                        itemExist = true;
                    }
                }
                if (itemExist) {
                    Boolean itemNotExist = true;
                    for (PLMBom child : plmBom.getChildrens()) {
                        if (child.getId().equals(bom.getId())) {
                            itemNotExist = false;
                        }
                    }

                    if (itemNotExist) {
                        bom.setBomItemRevision(bomItemRevision);
                        plmBom.getChildrens().add(bom);
                    }
                }
            }

            bom = visitChildrenForWhereUsedReportByIds(bom, ids);
        });

        if (plmBom.getChildrens().size() > 0) {
            for (PLMBom bom : plmBom.getChildrens()) {
                if (bom.getItemExist()) {
                    plmBom.setItemExist(true);
                }
            }
        }

        return plmBom;
    }

    @Transactional(readOnly = true)
    public List<String> getAttributeValueUsedInConfigurations(Integer itemId, Integer attributeId, String value) {

        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(itemId);
        PLMItemTypeAttribute configurableAttribute = itemTypeAttributeRepository.findOne(attributeId);
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);

        Map<String, List<ItemInclusionMap>> inclusionsMap = new Gson().fromJson(
                itemRevision.getInclusionRules(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                }.getType()
        );
        List<String> stringList = new ArrayList<>();
        if (inclusionsMap != null && inclusionsMap.size() > 0) {
            inclusionsMap.values().forEach(itemInclusionMap -> {
                ItemInclusionMap value1 = itemInclusionMap.get(0);
                ItemInclusionMap value2 = itemInclusionMap.get(1);
                if (value1.getItemId().equals(itemId)) {
                    if (value1.getKey().equals(configurableAttribute.getName()) && value1.getValue().equals(value)) {
                        stringList.add(value);
                    }
                } else {
                    if (value2.getKey().equals(configurableAttribute.getName()) && value2.getValue().equals(value)) {
                        stringList.add(value);
                    }
                }
            });
        }

        Map<String, List<MapperDTO>> itemAttributeExclusionMap = new Gson().fromJson(
                itemRevision.getAttributeExclusionRules(), new TypeToken<LinkedHashMap<String, List<MapperDTO>>>() {
                }.getType()
        );

        if (itemAttributeExclusionMap != null && itemAttributeExclusionMap.size() > 0 && stringList.size() == 0) {
            itemAttributeExclusionMap.values().forEach(attributeExclusionMap -> {
                MapperDTO value1 = attributeExclusionMap.get(0);
                MapperDTO value2 = attributeExclusionMap.get(1);
                if (value1.getId().equals(configurableAttribute.getId()) && value1.getValue().equals(value)) {
                    stringList.add(value);
                } else if (value2.getId().equals(configurableAttribute.getId()) && value2.getValue().equals(value)) {
                    stringList.add(value);
                }
            });
        }

        PLMItem item = itemRepository.findOne(itemRevision.getItemMaster());

        List<PLMBom> bomList = bomRepository.findByItem(item);

        if (bomList.size() > 0) {
            for (PLMBom plmBom : bomList) {
                bomConfigurations = bomConfigurationRepository.findByItem(plmBom.getParent().getId());
                if (bomConfigurations.size() > 0) {
                    for (BOMConfiguration config : bomConfigurations) {
                        String json = config.getRules();
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> map = null;
                        try {
                            map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                            });
                        } catch (Exception e) {
                            new CassiniException(e.getMessage());
                        }

                        if (map != null && map.size() > 0) {
                            List<HashMap<String, Object>> childrenMap = null;
                            Object o = map.get("children");
                            childrenMap = (List<HashMap<String, Object>>) o;
                            childrenMap.forEach(children -> {

                                for (String firstKey : children.keySet()) {
                                    Object key = children.get("id");
                                    Integer keyId = (Integer) key;
                                    if (keyId.equals(plmBom.getItem().getId())) {
                                        if (firstKey != "id" && firstKey != "children") {
                                            Object firstKeyObject = children.get(firstKey);
                                            String firstKeyValue = (String) firstKeyObject;

                                            if (firstKey.equals(configurableAttribute.getName()) && firstKeyValue.equals(value)) {
                                                stringList.add(value);
                                            }
                                        }
                                    }
                                }

                            });
                        }
                    }
                }
            }
        }

        return stringList;
    }

    @Transactional
    public List<ASBOMConfigItemInclusion> createBomConfigItemInclusions(Integer itemId, List<ASBOMConfigItemInclusion> configItemInclusions) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.BomConfigItemInclusionsEvent(itemRevision, configItemInclusions));

        return configItemInclusions;
    }

    @Transactional
    public List<ASBOMConfigItemInclusion> createBomNonConfigItemInclusions(Integer itemId, List<ASBOMConfigItemInclusion> configItemInclusions) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.BomNonConfigItemInclusionsEvent(itemRevision, configItemInclusions));

        return configItemInclusions;
    }

    @Transactional
    public List<ASBOMConfigItemInclusion> createBomConfigAttributeExclusions(Integer itemId, List<ASBOMConfigItemInclusion> configItemInclusions) {

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(itemId);
        applicationEventPublisher.publishEvent(new ItemEvents.BomConfigAttributeExclusionsEvent(itemRevision, configItemInclusions));

        return configItemInclusions;
    }

    @Transactional
    public PLMBom substituteBomItem(Integer itemId, PLMBom substituteBomItem) {
        return bomRepository.save(substituteBomItem);
    }
}
