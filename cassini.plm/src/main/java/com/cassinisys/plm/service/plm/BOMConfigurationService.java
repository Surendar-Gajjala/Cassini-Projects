package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.model.dto.BomModalDto;
import com.cassinisys.plm.model.dto.ItemAttributeDto;
import com.cassinisys.plm.model.dto.ItemInclusionMap;
import com.cassinisys.plm.model.dto.MapperDTO;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.plm.dto.BomDto;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.service.activitystream.dto.ASBomConfigValues;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

;

/**
 * Created by Nageshreddy on 16-03-2020.
 */
@Service
public class BOMConfigurationService implements CrudService<BOMConfiguration, Integer>,
        PageableService<BOMConfiguration, Integer> {

    PLMItemRevision plmItemRevision = null;
    @Autowired
    private BOMConfigurationRepository bomConfigurationRepository;
    @Autowired
    private ItemTypeAttributeRepository itemTypeAttributeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private BomService bomService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemRevisionStatusHistoryRepository revisionStatusHistoryRepository;
    @Autowired
    private ItemConfigurableAttributesRepository itemConfigurableAttributesRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ItemTypeRepository itemTypeRepository;

    private BOMConfiguration bomConfiguration = null;

    @Autowired
    private SessionWrapper sessionWrapper;

    /**
     * Get Attribute values possible combinations to create Instances.
     */
    public static List<List<String>> getCombinations(List<List<String>> lists) {
        List<List<String>> combinations = new ArrayList<>();
        ArrayList<List<String>> newCombinations;
        int index = 0;
        // extract each of the integers in the first list
        // and add each to ints as a new list
        for (String i : lists.get(0)) {
            List<String> newList = new ArrayList<>();
            newList.add(i);
            combinations.add(newList);
        }
        index++;
        while (index < lists.size()) {
            List<String> nextList = lists.get(index);
            newCombinations = new ArrayList();
            for (List<String> first : combinations) {
                for (String second : nextList) {
                    List<String> newList = new LinkedList();
                    newList.addAll(first);
                    newList.add(second);
                    newCombinations.add(newList);
                }
            }
            combinations = newCombinations;
            index++;
        }
        return combinations;
    }

    @Override
    @Transactional
    public BOMConfiguration create(BOMConfiguration bomConfiguration) {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(bomConfiguration.getItem());
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        return bomConfigurationRepository.save(bomConfiguration);
    }

    @Override
    public BOMConfiguration update(BOMConfiguration bomConfiguration) {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(bomConfiguration.getItem());
        PLMItem plmItem = itemRepository.findOne(plmItemRevision.getItemMaster());
        return bomConfigurationRepository.save(bomConfiguration);
    }

    @Override
    public void delete(Integer integer) {
        bomConfigurationRepository.delete(integer);
    }

    @Override
    public BOMConfiguration get(Integer integer) {
        return bomConfigurationRepository.findOne(integer);
    }

    @Override
    public List<BOMConfiguration> getAll() {
        return bomConfigurationRepository.findAll();
    }

    @Override
    public Page<BOMConfiguration> findAll(Pageable pageable) {
        return bomConfigurationRepository.findAll(pageable);
    }

    public List<BOMConfiguration> getBomConfigsByItem(Integer id) {
        return bomConfigurationRepository.findByItem(id);
    }

    public List<BomDto> resolveSelectedBOMConfigChildren(Integer id1) {
        PLMBom plmBom = bomRepository.findOne(id1);
        PLMItem item = plmBom.getItem();
        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemType(item.getItemType().getId());
        List<PLMBom> plmBoms = bomRepository.findByParentOrderBySequenceAsc(itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision()));
        if (item.getConfigured()) {
            PLMItem plmItem = itemRepository.findOne(item.getInstance());
            Integer revisionId = itemRevisionRepository.findOne(plmItem.getLatestRevision()).getId();
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(item.getId());
            List<String> values = objectAttributes.stream().map(sc -> sc.getListValue()).collect(Collectors.toList());
            List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(revisionId);
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
                List<String> listValues = new ArrayList();
                addListValues(listValues, map);
                if (CollectionUtils.isEqualCollection(values, listValues)) {
                    BomModalDto bomModalDto = bomService.getBomConfigurationModal(bomConfiguration.getItem(), bomConfiguration.getId());
                    plmBoms = getInstanceBasedOnBomModalDto(bomConfiguration, bomModalDto);
                }
            }
        } else if (item.getConfigurable()) {
            Integer revisionId = item.getLatestRevision();
            List<PLMItemRevision> instances = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(revisionId);
            List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(revisionId);
            for (BOMConfiguration bomConfiguration : bomConfigurations) {
                for (PLMItemRevision plmItemRevision : instances) {
                    List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(plmItemRevision.getItemMaster());
                    List<String> values = objectAttributes.stream().map(sc -> sc.getListValue()).collect(Collectors.toList());

                    String json = bomConfiguration.getRules();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> map = null;
                    try {
                        map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                        });
                    } catch (Exception e) {
                        new CassiniException(e.getMessage());
                    }
                    List<String> listValues = new ArrayList();
                    addListValues(listValues, map);

                    if (CollectionUtils.isEqualCollection(values, listValues)) {
                        BomModalDto bomModalDto = bomService.getBomConfigurationModal(bomConfiguration.getItem(), bomConfiguration.getId());
                        plmBoms = getInstanceBasedOnBomModalDto(bomConfiguration, bomModalDto);
                    }
                }
            }
        }
        List<BomDto> dtoList = new LinkedList<>();
        plmBoms.forEach(bom -> {
            BomDto bomDto = bomService.convertToBomDto(bom, "bom.latest", null);
            dtoList.add(bomDto);

        });
        return dtoList;
    }

    private void addListValues(List<String> listValues, Map<String, Object> map) {
        for (String key : map.keySet()) {
            if (key != "id" && key != "children") {
                Object object = map.get(key);
                String value = (String) object;
                listValues.add(value);
            }
        }
    }

    public List<BomDto> resolveSelectedBOMConfig(Integer id1) {
        List<BomDto> dtoList = new LinkedList<>();
        BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(id1);
        BomModalDto bomModalDto = bomService.getBomConfigurationModal(bomConfiguration.getItem(), id1);
        List<PLMBom> plmBoms = getInstanceBasedOnBomModalDto(bomConfiguration, bomModalDto);
        plmBoms.forEach(bom -> {
            BomDto bomDto = bomService.convertToBomDto(bom, "bom.latest", null);
            dtoList.add(bomDto);
        });
        return dtoList;
    }

    private List<PLMBom> getInstanceBasedOnBomModalDto(BOMConfiguration bomConfiguration, BomModalDto bomModalDto) {
        PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(bomConfiguration.getItem());
        List<PLMBom> plmBoms = bomRepository.findByParentOrderBySequenceAsc(itemRevisionRepository.findOne(bomModalDto.getItem().getLatestRevision()));

        List<PLMBom> bomList = new LinkedList<>();
        Map<String, List<ItemInclusionMap>> parentItemExclusionMap = new HashMap<>();
        HashMap<Integer, Integer> itemExclusionMap = new HashMap<>();

        List<String> keyValues = new ArrayList<>();
        String valuesKey = "";
        List<ObjectAttribute> itemAttributes = objectAttributeRepository.findByObjectId(plmItemRevision.getItemMaster());

        for (ItemAttributeDto objectAttribute : bomModalDto.getAttributes()) {
            valuesKey = "";
            valuesKey = bomModalDto.getItem().getItemName() + "_" + objectAttribute.getItemAttribute().getAttribute().getName() + "_" + objectAttribute.getListValue();
            keyValues.add(valuesKey);
        }

        Map<String, List<ItemInclusionMap>> inclusionsMap = new Gson().fromJson(
                plmItemRevision.getItemExclusions(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                }.getType()
        );

        if (inclusionsMap != null && inclusionsMap.size() > 0) {
            inclusionsMap.values().forEach(itemInclusionMap -> {
                ItemInclusionMap value1 = itemInclusionMap.get(0);
                ItemInclusionMap value2 = itemInclusionMap.get(1);
                if (value1 != null && value1.getItemId().equals(plmItemRevision.getId())) {
                    List<ItemInclusionMap> values = parentItemExclusionMap.containsKey(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) ? parentItemExclusionMap.get(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) : new ArrayList<>();
                    values.add(value2);
                    parentItemExclusionMap.put(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue(), values);
                }
            });

            for (String key : keyValues) {
                List<ItemInclusionMap> values = parentItemExclusionMap.get(key);
                if (values != null && values.size() > 0) {
                    values.forEach(value1 -> {
                        itemExclusionMap.put(value1.getItemId(), value1.getItemId());
                    });
                }
            }
        }

        plmBoms.forEach(bom -> {
            if (bom.getItem().getConfigurable()) {
                bomList.add(bom);
            } else {
                Integer bomItemId = itemExclusionMap.get(bom.getItem().getId());
                if (bomItemId == null) {
                    bomList.add(bom);
                }
            }
        });

        if (bomConfiguration != null && bomModalDto != null) {
            List<BomModalDto> bomModalDtos = bomModalDto.getChildren();
            PLMItemRevision parent = null;
            Integer revisionId1 = bomModalDto.getItem().getLatestRevision();
            List<ItemAttributeDto> attributes1 = bomModalDto.getAttributes();
            List<PLMItemRevision> revisions1 = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(revisionId1);
            List<Integer> integers1 = revisions1.stream().map(sc -> sc.getItemMaster()).collect(Collectors.toList());
            List<Integer> attIds1 = attributes1.stream().map(sc -> sc.getId().getAttributeDef()).collect(Collectors.toList());
            Integer[] attriIds1 = attIds1.toArray(new Integer[attIds1.size()]);
            List<String> values1 = attributes1.stream().map(sc -> sc.getListValue()).collect(Collectors.toList());
            String[] vals1 = values1.toArray(new String[values1.size()]);
            Boolean value = true;
            for (Integer id : integers1) {
                List<ObjectAttribute> objectAttributes1 = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(id, attriIds1, vals1);
                if (objectAttributes1.size() == attIds1.size()) {
                    PLMItem item = itemRepository.findOne(id);
                    parent = itemRevisionRepository.findOne(item.getLatestRevision());
                    parent.setItem(item);
                    value = false;
                }
            }
            if (value) {
                parent = itemRevisionRepository.findOne(bomModalDto.getItem().getLatestRevision());
            }

            for (int m = 0; m < bomList.size(); m++) {
                PLMBom plmBom1 = bomList.get(m);
                for (int n = 0; n < bomModalDtos.size(); n++) {
                    BomModalDto bomModalDto1 = bomModalDtos.get(n);
                    if (plmBom1.getItem().getId().equals(bomModalDto1.getItem().getId())) {
                        Integer revisionId = bomModalDto1.getItem().getLatestRevision();
                        List<ItemAttributeDto> attributes = bomModalDto1.getAttributes();
                        List<PLMItemRevision> revisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(revisionId);
                        List<Integer> integers = revisions.stream().map(sc -> sc.getItemMaster()).collect(Collectors.toList());
                        List<Integer> attIds = attributes.stream().map(sc -> sc.getId().getAttributeDef()).collect(Collectors.toList());
                        Integer[] attriIds = attIds.toArray(new Integer[attIds.size()]);
                        List<String> values = attributes.stream().map(sc -> sc.getListValue()).collect(Collectors.toList());
                        String[] vals = values.toArray(new String[values.size()]);
                        for (Integer id : integers) {
                            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(id, attriIds, vals);
                            if (objectAttributes.size() == attIds.size()) {
                                PLMBom plmBom2 = bomRepository.findByParentAndItem(parent, itemRepository.findOne(id));
                                if (plmBom2 != null) {
                                    plmBom1.setItem(itemRepository.findOne(id));
                                    plmBoms.set(m, plmBom1);
                                } else {
                                    plmBom1.setItem(itemRepository.findOne(id));
                                }

                            }
                        }
                    }
                }
            }
        }
        /*bomList.forEach(plmBom -> {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
            plmBom.getItem().getItemFiles().addAll(itemFileService.findByItem(itemRevision));
            PLMItemRevision plmItemRevision3 = itemRevisionRepository.findOne(plmBom.getItem().getLatestRevision());
            plmBom.setCount(bomRepository.findByParentOrderBySequenceAsc(plmItemRevision3).size());
        });*/
        return bomList;
    }

    private void getRequiredInstance(PLMItemRevision plmItemRevision, PLMItemRevision instance, PLMItemRevision plmBOMRevision) {
        List<PLMItemRevision> plmItemRevisions = itemRevisionRepository.findByInstanceOrderByCreatedDateDesc(plmItemRevision.getId());
        List<Integer> integers = plmItemRevisions.stream().map(sc -> sc.getItemMaster()).collect(Collectors.toList());
        List<PLMItemTypeAttribute> plmItemTypeAttributes = plmItemRevision.getPlmItemTypeAttributes();
        List<Integer> attIds = plmItemTypeAttributes.stream().map(sc -> sc.getId()).collect(Collectors.toList());
        Integer[] attriIds = attIds.toArray(new Integer[attIds.size()]);
        List<String> values = plmItemTypeAttributes.stream().map(sc -> sc.getValue()).collect(Collectors.toList());
        String[] vals = values.toArray(new String[values.size()]);
        PLMItemRevision instance1 = null;
        Boolean value = true;
        for (Integer id : integers) {
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(id, attriIds, vals);
            if (objectAttributes.size() == attIds.size()) {
                PLMItem item = itemRepository.findOne(id);
                instance1 = itemRevisionRepository.findOne(item.getLatestRevision());
                instance1.setItem(itemRepository.findOne(id));
                instance1.setPlmItemTypeAttributes(plmItemTypeAttributes);
                plmBOMRevision = instance1;
                value = false;
            }
        }
        if (value) {
            instance1 = plmItemRevision;
            instance1.setPlmItemRevisions(new ArrayList());
            plmBOMRevision = plmItemRevision;
        }
        instance.getPlmItemRevisions().add(plmBOMRevision);
    }

    public PLMItemRevision getBomConfigItemWithValues(Integer id) {
        plmItemRevision = null;
        bomConfiguration = bomConfigurationRepository.findOne(id);
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
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            plmItemRevision = setAttributeValues(entries);
        }
        return plmItemRevision;
    }

    private PLMItemRevision setAttributeValues(Set<Map.Entry<String, Object>> entries1) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : entries1) {
            map.put(entry.getKey(), entry.getValue());
        }
        Integer itemId = (Integer) map.get("id");
        PLMItem item = itemRepository.findOne(itemId);
        plmItemRevision = itemRevisionRepository.findOne(item.getLatestRevision());
        plmItemRevision.setItem(item);
        Integer typeId = plmItemRevision.getItem().getItemType().getId();
        List<PLMItemTypeAttribute> plmItemTypeAttributes = new ArrayList();
        if (entries1.size() > 0) {
            for (Map.Entry entry1 : entries1) {
                String key = (String) entry1.getKey();
                if (!key.equalsIgnoreCase("children") && !key.equals("id")) {
                    String attributeName = (String) entry1.getKey();
                    String attributeValue = (String) entry1.getValue();
                    PLMItemTypeAttribute plmItemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(typeId, attributeName);
                    plmItemTypeAttribute.setValue(attributeValue);
                    plmItemTypeAttributes.add(plmItemTypeAttribute);
                } else if (!key.equals("id")) {
                    List<LinkedHashMap<Object, Object>> entry4 = (List<LinkedHashMap<Object, Object>>) entry1.getValue();
                    if (entry4.size() > 0) {
                        setAttributeValuesForChildren(entry4, plmItemRevision);
                    }
                }
                plmItemRevision.setPlmItemTypeAttributes(plmItemTypeAttributes);
            }
        }
        return plmItemRevision;
    }

    private void setAttributeValuesForChildren(List<LinkedHashMap<Object, Object>> entries2, PLMItemRevision plmItemRevision1) {
        for (LinkedHashMap<Object, Object> objectLinkedHashMap : entries2) {
            PLMItemRevision plmItemRevision2 = null;
            List<PLMItemTypeAttribute> plmItemTypeAttributes = new ArrayList();
            Map<Object, Object> map = new HashMap<>();
            for (Map.Entry entry : objectLinkedHashMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            Integer itemId = (Integer) map.get("id");
            PLMItem item = itemRepository.findOne(itemId);
            plmItemRevision2 = itemRevisionRepository.findOne(item.getLatestRevision());
            plmItemRevision2.setItem(item);
            Integer typeId = plmItemRevision2.getItem().getItemType().getId();
            if (objectLinkedHashMap.size() > 0) {
                for (Map.Entry entry1 : objectLinkedHashMap.entrySet()) {
                    String key = (String) entry1.getKey();
                    if (!key.equalsIgnoreCase("children") && !key.equals("id")) {
                        String attributeName = (String) entry1.getKey();
                        String attributeValue = (String) entry1.getValue();
                        PLMItemTypeAttribute plmItemTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(typeId, attributeName);
                        plmItemTypeAttribute.setValue(attributeValue);
                        plmItemTypeAttributes.add(plmItemTypeAttribute);
                    } else if (!key.equals("id")) {
                        List<LinkedHashMap<Object, Object>> entry44 = (List<LinkedHashMap<Object, Object>>) entry1.getValue();
                        if (entry44.size() > 0) {
                            setAttributeValuesForChildren(entry44, plmItemRevision2);
                        }
                    }
                    plmItemRevision2.setPlmItemTypeAttributes(plmItemTypeAttributes);
                }
            }
            plmItemRevision1.getPlmItemRevisions().add(plmItemRevision2);
        }
    }

    /**
     * Get Attribute values Exclusions of ItemType
     */
    public Map<String, List<MapperDTO>> getExclusions(PLMItemType itemType) {
        Map<String, List<MapperDTO>> exclusionsMap = new Gson().fromJson(
                itemType.getExcluRules(), new TypeToken<LinkedHashMap<String, List<MapperDTO>>>() {
                }.getType()
        );
        return exclusionsMap;
    }

    /**
     * Get All Possible combinations from Exclusion Rules
     */
    public List<List<String>> getAllPossibleCombsAfterRemoveExclRules(PLMItemType itemType) {
        Map<String, String[]> lovsForList = new LinkedHashMap<>();
        List<PLMItemTypeAttribute> types = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(itemType.getId());
        for (PLMItemTypeAttribute type : types) {
            if (type.getConfigurable() == true && type.getDataType().toString().equals("LIST")) {
                lovsForList.put(type.getName(), type.getLov().getValues());
            }
        }
        List<List<String>> listOfValues = new LinkedList<List<String>>();
        for (Map.Entry<String, String[]> entry1 : lovsForList.entrySet()) {
            String[] arrOfStr = entry1.getKey().split("_");
            String key = entry1.getKey();
            String[] value1 = entry1.getValue();
            listOfValues.add(Arrays.asList(value1));
        }
        List<List<String>> combsList = new ArrayList<>();
        if (listOfValues.size() > 0) {
            combsList = getCombinations(listOfValues);
            Map<String, List<MapperDTO>> exclusionsMap = getExclusions(itemType);
            if (exclusionsMap != null && exclusionsMap.size() > 0) {
                Set<String> keysFor = exclusionsMap.keySet();
                synchronized (keysFor) {
                    for (String key : keysFor) {
                        String[] arrayOfKeys = key.split("_");
                        String firstKey = arrayOfKeys[0];
                        String secondKey = arrayOfKeys[1];
                        for (int j = 0; j < combsList.size(); j++) {
                            if (combsList.get(j).contains(firstKey) && combsList.get(j).contains(secondKey)) {  // if string not in master list
                                combsList.remove(combsList.get(j));
                                j--;
                            }
                        }
                    }
                }
            }
        }
        return combsList;

    }

    /**
     * Get All Possible combinations from Exclusion Rules
     */
    public List<List<String>> getAllPossibleCombsAfterRemoveItemsExclRules(PLMItemRevision itemRevision) {
        Map<String, String[]> lovsForList = new LinkedHashMap<>();
//        List<PLMItemTypeAttribute> types = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(itemType.getId());
        List<ItemConfigurableAttributes> configurableAttributes = itemConfigurableAttributesRepository.findByItem(itemRevision.getId());
        for (ItemConfigurableAttributes type : configurableAttributes) {
            if (type.getValues().length > 0) {
                lovsForList.put(type.getAttribute().getName(), type.getValues());
            }
        }
        List<List<String>> listOfValues = new LinkedList<List<String>>();
        for (Map.Entry<String, String[]> entry1 : lovsForList.entrySet()) {
            String[] arrOfStr = entry1.getKey().split("_");
            String key = entry1.getKey();
            String[] value1 = entry1.getValue();
            listOfValues.add(Arrays.asList(value1));
        }
        List<List<String>> combsList = new ArrayList<>();
        if (listOfValues.size() > 0) {
            combsList = getCombinations(listOfValues);
            Map<String, List<MapperDTO>> exclusionsMap = new Gson().fromJson(
                    itemRevision.getAttributeExclusionRules(), new TypeToken<LinkedHashMap<String, List<MapperDTO>>>() {
                    }.getType()
            );
            if (exclusionsMap != null && exclusionsMap.size() > 0) {
                Set<String> keysFor = exclusionsMap.keySet();
                synchronized (keysFor) {
                    for (String key : keysFor) {
                        String[] arrayOfKeys = key.split("_");
                        String firstKey = arrayOfKeys[0];
                        String secondKey = arrayOfKeys[1];
                        for (int j = 0; j < combsList.size(); j++) {
                            if (combsList.get(j).contains(firstKey) && combsList.get(j).contains(secondKey)) {  // if string not in master list
                                combsList.remove(combsList.get(j));
                                j--;
                            }
                        }
                    }
                }
            }
        }
        return combsList;

    }

    public List<List<String>> getExistingCombinationsOfInstances(Integer itemId) {
        PLMItem plmItem = itemRepository.findOne(itemId);
        List<PLMItem> plmItems = itemRepository.findByInstanceOrderByCreatedDateDesc(itemId);
        List<List<String>> lists = new ArrayList();
        List<PLMItemTypeAttribute> types = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(plmItem.getItemType().getId());
        Integer[] integers1 = new Integer[types.size()];
        for (int i = 0; i < types.size(); i++) {
            integers1[i] = types.get(i).getId();
        }
        for (PLMItem item : plmItems) {
            List<String> strings = new LinkedList();
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsIn(item.getId(), integers1);
            for (ObjectAttribute objectAttribute : objectAttributes) {
                strings.add(objectAttribute.getListValue());
            }
            lists.add(strings);
        }
        return lists;
    }

    /**
     * Get All Possible combinations from Exclusion Rules and Existing Instances
     */
    public List<List<String>> getPossibleCombinationsAfterRemoveExclutionsAndExistingInstances(Integer itemId) {
        PLMItem plmItem = itemRepository.findOne(itemId);
        List<List<String>> existingCombis = getExistingCombinationsOfInstances(itemId);
        List<List<String>> possible = getAllPossibleCombsAfterRemoveExclRules(plmItem.getItemType());
        List<List<String>> possible1 = new ArrayList();
        for (List l : possible) {
            boolean val = false;
            for (List l1 : existingCombis) {
                if (l.containsAll(l1)) {
//                    System.out.println(l + "does contain " + l1);
                    val = true;
                }
            }
            if (!val)
                possible1.add(l);
        }
        return possible1;
    }

    public List<String> getBomConfigurationInclusions(Integer id) {
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(id);
        List<String> values = new ArrayList();
        if (bomConfigurations.size() > 0) {
            for (BOMConfiguration config : bomConfigurations) {
                PLMItemRevision revision = itemRevisionRepository.findOne(config.getItem());
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                StringBuilder val2 = new StringBuilder(item.getItemName());
                val2.append(" ");
                String json = config.getRules();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });
                } catch (Exception e) {
                    new CassiniException(e.getMessage());
                }
                for (String key : map.keySet()) {
                    if (key != "id" && key != "children") {
                        Object object = map.get(key);
                        StringBuilder val = new StringBuilder(val2);
                        val.append(key);
                        String value = (String) object;
                        val.append("(" + value + ") - ");
                        setChildrenValues(map, val, values);
                    }
                }
            }
        }
        return values;
    }

    private void setChildrenValues(Map<String, Object> map, StringBuilder val, List<String> values) {
        for (String key : map.keySet()) {
            if (key == "children") {
                Object o = map.get(key);
                List<HashMap<String, Object>> childrenMap = (List<HashMap<String, Object>>) o;
                if (childrenMap.size() > 0) {
                    for (HashMap<String, Object> children : childrenMap) {
                        Set<Map.Entry<String, Object>> entries1 = children.entrySet();
                        StringBuilder val2 = null;
                        for (Map.Entry<String, Object> entry : entries1) {
                            String key1 = entry.getKey();
                            if (key1.equals("id")) {
                                PLMItem plmItem1 = itemRepository.findOne((Integer) entry.getValue());
                                val2 = new StringBuilder(val);
                                val2.append(plmItem1.getItemName() + " ");
                            }
                        }
                        for (Map.Entry<String, Object> entry : entries1) {
                            String key1 = entry.getKey();
                            if (!key1.equals("id") && !key1.equals("children")) {
                                StringBuilder val3 = new StringBuilder(val2);
                                Object object = entry.getValue();
                                val3.append(key1);
                                String value = (String) object;
                                val3.append("(" + value + ")");
                                values.add(new String(val3));
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAttributeInclusions(Map<String, Object> map, List<String> values) {
        for (String firstKey : map.keySet()) {
            if (firstKey != "id" && firstKey != "children") {
                Object firstKeyObject = map.get(firstKey);
                String firstKeyValue = (String) firstKeyObject;

                for (String secondKey : map.keySet()) {
                    if (secondKey != "id" && secondKey != "children") {
                        Object object = map.get(secondKey);
                        String value = (String) object;

                        if (!firstKey.equals(secondKey)) {
                            String val = secondKey + "(" + firstKeyValue + "-" + value + ")";
                            values.add(val);

                            val = firstKey + "(" + value + "-" + firstKeyValue + ")";
                            values.add(val);
                        }

                    }
                }
            }
        }
    }


    public List<String> getBomConfigurationAttributeInclusions(Integer id) {
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(id);
        List<String> values = new ArrayList();
        if (bomConfigurations.size() > 0) {
            for (BOMConfiguration config : bomConfigurations) {
                PLMItemRevision revision = itemRevisionRepository.findOne(config.getItem());
                PLMItem item = itemRepository.findOne(revision.getItemMaster());
                String json = config.getRules();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = null;
                try {
                    map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });
                } catch (Exception e) {
                    new CassiniException(e.getMessage());
                }

                setAttributeInclusions(map, values);
            }
        }

        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
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
                                Object key = children.get("id");
                                Integer itemId = (Integer) key;

                                if (itemId.equals(plmBom.getItem().getId())) {
                                    setAttributeInclusions(children, values);

                                }
                            });
                        }
                    }
                }
            }
        }

        List<PLMItem> instances = itemRepository.findByInstanceOrderByCreatedDateDesc(item.getId());
        for (PLMItem instance : instances) {
            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectId(instance.getId());

            for (ObjectAttribute objectAttribute : objectAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());

                for (ObjectAttribute attribute : objectAttributes) {
                    ObjectTypeAttribute typeAttribute = objectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

                    if (!objectTypeAttribute.getId().equals(typeAttribute.getId())) {
                        String val = typeAttribute.getName() + "(" + objectAttribute.getListValue() + "-" + attribute.getListValue() + ")";
                        values.add(val);

                        val = objectTypeAttribute.getName() + "(" + attribute.getListValue() + "-" + objectAttribute.getListValue() + ")";
                        values.add(val);
                    }
                }

            }
        }
        return values;
    }

    @Transactional
    public List<PLMItem> createItemAllInstances(Integer id, List<PLMItem> items) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        List<PLMItem> instances = new ArrayList<>();
        for (PLMItem item : items) {
            item = createItemInstances(id, item, false);
            instances.add(item);
            applicationEventPublisher.publishEvent(new ItemEvents.ItemInstanceAddedEvent(itemRevision, instances));
        }
        return null;
    }

    @Transactional
    public PLMItem createItemInstances(Integer id, PLMItem item, Boolean single) {
        PLMItemRevision itemRevision = itemRevisionRepository.findOne(id);
        PLMItem item2 = itemRepository.findOne(itemRevision.getItemMaster());
        checkNotNull(item);
        item.setId(null);
        List<PLMItem> existItems = itemRepository.findByInstanceOrderByCreatedDateDesc(item.getInstance());

        List<List<String>> combsList = getAllPossibleCombsAfterRemoveItemsExclRules(itemRevision);
        List<String> strings = new ArrayList<>();
        for (PLMItemAttribute itemAttribute : item.getItemAttributes()) {
            strings.add(itemAttribute.getListValue());
        }
        Boolean existInstance = false;
        for (List<String> list : combsList) {
            if (list.containsAll(strings)) {
                existInstance = true;
            }
        }
        if (!existInstance) {
            throw new CassiniException(messageSource.getMessage("with_selected_combination_item_not_possible" + " [ " + strings + " ]",
                    null, "Instance not possible with values" + " [ " + strings + " ]", LocaleContextHolder.getLocale()));
        } else {
            existInstance = checkCombinationExitWithItem(existItems, item.getItemAttributes());
            if (existInstance) {
                throw new CassiniException(messageSource.getMessage("item_instance_already_exist_with_selected_combination" + " [ " + strings + " ]",
                        null, "Instance already exist with values" + " [ " + strings + " ]", LocaleContextHolder.getLocale()));
            } else {
                if (item.getTypeId() != null) {
                    PLMItemType itemType = itemTypeRepository.findOne(item.getTypeId());
                    item.setItemType(itemType);
                }
                PLMItem item1 = itemRepository.findByItemNumberEqualsIgnoreCase(item.getItemNumber());
                if (item1 != null) {
                    throw new CassiniException(messageSource.getMessage(item1.getItemNumber() + " : " + "itemNumber_already_exists", null, item1.getItemNumber() + " : " + "Item Number already exist", LocaleContextHolder.getLocale()));
                }
                item.setMakeOrBuy(item2.getMakeOrBuy());
                item = itemRepository.save(item);
                PLMItemRevision revision = new PLMItemRevision();
                revision.setItemMaster(item.getId());
                revision.setObjectType(PLMObjectType.ITEMREVISION);
                PLMItemType plmItemType = itemTypeRepository.findOne(item.getItemType().getId());
                Lov lov = plmItemType.getRevisionSequence();
                PLMLifeCyclePhase lifeCyclePhase = plmItemType.getLifecycle().getPhaseByType(LifeCyclePhaseType.PRELIMINARY);
                revision.setLifeCyclePhase(lifeCyclePhase);
                revision.setRevision(itemRevision.getRevision());
                revision.setHasBom(false);
                revision.setHasFiles(false);
                revision = itemRevisionRepository.save(revision);
                item.setLatestRevision(revision.getId());

                if (item.getThumbnail() == null) {
                    item.setThumbnail(item2.getThumbnail());
                }

                item = itemRepository.save(item);

                revision.setInstance(item2.getLatestRevision());
                revision = itemRevisionRepository.save(revision);

                PLMItemRevisionStatusHistory statusHistory = new PLMItemRevisionStatusHistory();
                statusHistory.setItem(revision.getId());
                statusHistory.setOldStatus(revision.getLifeCyclePhase());
                statusHistory.setNewStatus(revision.getLifeCyclePhase());
                statusHistory.setUpdatedBy(item.getCreatedBy());
                statusHistory = revisionStatusHistoryRepository.save(statusHistory);

                List<String> keyValues = new ArrayList<>();
                String valuesKey = "";
                for (PLMItemAttribute itemAttribute : item.getItemAttributes()) {
                    ObjectAttributeId objectAttributeId = new ObjectAttributeId();
                    objectAttributeId.setObjectId(item.getId());
                    objectAttributeId.setAttributeDef(itemAttribute.getId().getAttributeDef());
                    itemAttribute.setId(objectAttributeId);
                    itemService.createItemAttribute(itemAttribute);

                    valuesKey = "";
                    ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(itemAttribute.getId().getAttributeDef());
                    valuesKey = item2.getItemName() + "_" + objectTypeAttribute.getName() + "_" + itemAttribute.getListValue();
                    keyValues.add(valuesKey);
                }

                if (single) {
                    List<PLMItem> items = new ArrayList<>();
                    items.add(item);
                    applicationEventPublisher.publishEvent(new ItemEvents.ItemInstanceAddedEvent(itemRevision, items));
                }

                if (item.getConfigured()) {
                    PLMItem parentItem = itemRepository.findOne(item.getInstance());
                    PLMItemRevision parentRevision = itemRevisionRepository.findOne(parentItem.getLatestRevision());

                    if (item.getBomConfiguration() != null) {
                        BOMConfiguration bomConfiguration = bomConfigurationRepository.findOne(item.getBomConfiguration());
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

                        HashMap<Integer, List<String>> itemValuesMap = new HashMap<>();
                        HashMap<Integer, List<Integer>> attributeIdsMap = new HashMap<>();

                        setBomConfigurationForItem(map, itemIdMap, childrenMap, itemValuesMap, attributeIdsMap);

                        List<PLMBom> plmBoms = bomRepository.findByParentAndConfigurableTrueOrderBySequenceAsc(parentRevision.getId());
                        List<PLMBom> copyBoms = new ArrayList<>();
                        for (PLMBom bom : plmBoms) {

                            if (bom.getItem().getConfigurable() != null && bom.getItem().getConfigurable()) {

                                List<PLMItem> items = itemRepository.findByInstanceOrderByCreatedDateDesc(bom.getItem().getId());

                                if (items.size() > 0) {
                                    Boolean instanceExit = false;
                                    for (PLMItem instanceItem : items) {
                                        List<String> valueList = itemValuesMap.get(bom.getItem().getId());
                                        List<Integer> attrIds = attributeIdsMap.get(bom.getItem().getId());
                                        Integer[] attriIds = attrIds.toArray(new Integer[attrIds.size()]);
                                        String[] vals = valueList.toArray(new String[valueList.size()]);
                                        List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(instanceItem.getId(), attriIds, vals);
                                        if (objectAttributes.size() == valueList.size()) {
                                            PLMBom bom1 = new PLMBom();
                                            bom1.setItem(instanceItem);
                                            bom1.setSequence(bom.getSequence());
                                            bom1.setParent(revision);
                                            bom1.setQuantity(bom.getQuantity());
                                            bom1.setRefdes(bom.getRefdes());
                                            bom1.setNotes(bom.getNotes());
                                            copyBoms.add(bom1);
                                            instanceExit = true;
                                        }
                                    }

                                    if (!instanceExit) {
                                        copyBomItem(bom, revision, copyBoms);
                                    }
                                } else {
                                    copyBomItem(bom, revision, copyBoms);
                                }
                            }
                        }

                        List<PLMBom> normalBomItems = bomRepository.findByParentAndConfigurableFalseOrderBySequenceAsc(parentRevision.getId());

                        Map<String, List<ItemInclusionMap>> inclusionsMap = new Gson().fromJson(
                                parentRevision.getItemExclusions(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                                }.getType()
                        );

                        Map<String, List<ItemInclusionMap>> parentItemExclusionMap = new HashMap<>();

                        if (inclusionsMap != null && inclusionsMap.size() > 0) {
                            inclusionsMap.values().forEach(itemInclusionMap -> {
                                ItemInclusionMap value1 = itemInclusionMap.get(0);
                                ItemInclusionMap value2 = itemInclusionMap.get(1);
                                if (value1 != null && value1.getItemId().equals(parentRevision.getId())) {
                                    List<ItemInclusionMap> values = parentItemExclusionMap.containsKey(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) ? parentItemExclusionMap.get(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) : new ArrayList<>();
                                    values.add(value2);
                                    parentItemExclusionMap.put(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue(), values);
                                }
                            });

                            HashMap<Integer, Integer> itemExclusionMap = new HashMap<>();
                            for (String key : keyValues) {
                                List<ItemInclusionMap> values = parentItemExclusionMap.get(key);
                                if (values != null && values.size() > 0) {
                                    values.forEach(value -> {
                                        itemExclusionMap.put(value.getItemId(), value.getItemId());
                                    });
                                }
                            }

                            for (PLMBom bom : normalBomItems) {
                                Integer bomItemId = itemExclusionMap.get(bom.getItem().getId());
                                if (bomItemId == null) {
                                    copyBomItem(bom, revision, copyBoms);
                                }
                            }
                        } else {
                            copyBomItems(normalBomItems, revision, copyBoms);
                        }

                        bomRepository.save(copyBoms);
                        if (copyBoms.size() > 0) {
                            revision.setHasBom(true);
                            revision.setBomConfiguration(bomConfiguration.getId());
                            revision = itemRevisionRepository.save(revision);
                        }
                    }
                }
            }
        }
        return item;
    }

    public void copyBomItems(List<PLMBom> boms1, PLMItemRevision itemRevision, List<PLMBom> copyBoms) {
        for (PLMBom bom : boms1) {
            copyBomItem(bom, itemRevision, copyBoms);
        }
    }

    public void copyBomItem(PLMBom bom, PLMItemRevision itemRevision, List<PLMBom> copyBoms) {
        PLMBom bom1 = new PLMBom();
        bom1.setItem(bom.getItem());
        bom1.setSequence(bom.getSequence());
        bom1.setParent(itemRevision);
        bom1.setQuantity(bom.getQuantity());
        bom1.setRefdes(bom.getRefdes());
        bom1.setNotes(bom.getNotes());
        copyBoms.add(bom1);
    }

    private Boolean checkCombinationExitWithItem(List<PLMItem> existItemCombinations, List<PLMItemAttribute> itemAttributes) {
        Boolean combinationExist = false;
        for (PLMItem exitCombination : existItemCombinations) {
            Integer combinationCount = 0;
            for (PLMItemAttribute itemAttribute : itemAttributes) {
                ObjectAttribute objectAttribute = objectAttributeRepository.findByObjectIdAndAttributeDefIdAndListValue(exitCombination.getId(), itemAttribute.getId().getAttributeDef(), itemAttribute.getListValue());
                if (objectAttribute != null) {
                    combinationCount++;
                }
            }
            if (combinationCount.equals(itemAttributes.size())) {
                combinationExist = true;
            }
        }
        return combinationExist;
    }

    @Transactional(readOnly = true)
    public List<PLMItem> getAllCombinations(Integer id) {
        List<List<String>> combsList = new ArrayList<>();
        PLMItem item = itemRepository.findOne(id);
        List<PLMItemTypeAttribute> itemTypeAttributes = itemTypeAttributeRepository.findByItemTypeAndConfigurableTrueOrderBySeq(item.getItemType().getId());
        combsList = getPossibleCombinationsAfterRemoveExclutionsAndExistingInstances(item.getId());
        List<PLMItem> combinationItems = new ArrayList<>();
        if (combsList.size() > 0) {
            for (List<String> combination : combsList) {
                PLMItem combinationItem = new PLMItem();
                combinationItem.setItemName(item.getItemName());
                combinationItem.setItemNumber(item.getItemNumber() + " - ");
                combinationItem.setDescription(item.getDescription());
                combinationItem.setItemType(item.getItemType());
                combinationItem.setUnits(item.getUnits());
                combinationItem.setInstance(item.getId());
                combinationItem.setConfigured(true);
                combinationItem.setConfigurable(false);
                for (String combinationValue : combination) {
                    Integer index = combination.indexOf(combinationValue);
                    PLMItemTypeAttribute itemTypeAttribute = itemTypeAttributes.get(index);
                    PLMItemAttribute itemAttribute = new PLMItemAttribute();
                    ObjectAttributeId attributeId = new ObjectAttributeId();
                    attributeId.setAttributeDef(itemTypeAttribute.getId());
                    itemAttribute.setId(attributeId);
                    itemAttribute.setListValue(combinationValue);
                    combinationItem.getItemAttributes().add(itemAttribute);
                }
                combinationItems.add(combinationItem);

            }
        }
        return combinationItems;
    }

    @Transactional
    public BOMConfiguration createBomConfiguration(Integer itemId, BOMConfiguration bomConfiguration) {
        PLMItemRevision revision = itemRevisionRepository.findOne(bomConfiguration.getItem());
        PLMItem item = itemRepository.findOne(revision.getItemMaster());
        List<BOMConfiguration> bomConfigurations = bomConfigurationRepository.findByItem(bomConfiguration.getItem());
        List<List<String>> existBomConfigValues = new ArrayList<>();
        List<String> configurationValues = new ArrayList<>();
        if (bomConfigurations.size() > 0) {
            List<String> values = new ArrayList<>();
            String json = bomConfiguration.getRules();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = null;
            try {
                map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                });
            } catch (Exception e) {
                new CassiniException(e.getMessage());
            }
            if (map != null && map.size() > 0) {
                Set<Map.Entry<String, Object>> entries1 = map.entrySet();
                for (Map.Entry<String, Object> entry : entries1) {
                    if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                        configurationValues.add((String) entry.getValue());
                    }
                }
            }
            for (BOMConfiguration configuration : bomConfigurations) {
                if (bomConfiguration.getId() != null && bomConfiguration.getId().equals(configuration.getId())) {
                } else {
                    values = new ArrayList<>();
                    json = configuration.getRules();
                    objectMapper = new ObjectMapper();
                    map = null;
                    try {
                        map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                        });
                    } catch (Exception e) {
                        new CassiniException(e.getMessage());
                    }
                    if (map != null && map.size() > 0) {
                        Set<Map.Entry<String, Object>> entries1 = map.entrySet();
                        for (Map.Entry<String, Object> entry : entries1) {
                            if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                                values.add((String) entry.getValue());
                            }
                        }
                        existBomConfigValues.add(values);
                    }
                }
            }
            Boolean configurationExist = false;
            for (List<String> value : existBomConfigValues) {
                if (value.containsAll(configurationValues)) {
                    configurationExist = true;
                    break;
                }
            }
            if (configurationExist) {
                throw new CassiniException(messageSource.getMessage("bom_configuration_already_exist_with" + configurationValues,
                        null, "Configuration with same values already exists" + configurationValues, LocaleContextHolder.getLocale()));
            }

        }
        BOMConfiguration exist = bomConfigurationRepository.findByItemAndName(itemId, bomConfiguration.getName());
        if (exist != null && !exist.getId().equals(bomConfiguration.getId())) {
            throw new CassiniException(messageSource.getMessage("name_already_exists",
                    null, "Name already exist" + configurationValues, LocaleContextHolder.getLocale()));
        }
        BomModalDto modalDto = bomConfiguration.getBomModalDto();
//        PLMItemType itemType = itemTypeRepository.findOne(modalDto.getItem().getItemType());
        List<List<String>> combsList = getAllPossibleCombsAfterRemoveItemsExclRules(revision);
        List<String> strings = new ArrayList<>();
        for (ItemAttributeDto attributeDto : modalDto.getAttributes()) {
            strings.add(attributeDto.getListValue());
        }
        Boolean existInstance = false;
        for (List<String> list : combsList) {
            if (list.containsAll(strings)) {
                existInstance = true;
            }
        }
        if (!existInstance) {
            throw new CassiniException(messageSource.getMessage("with_selected_combination_values_instance_not_possible " + " [ " + modalDto.getItem().getItemNumber() + " - " + modalDto.getItem().getItemName() + " ]",
                    null, "With selected combination values Instance no possible for [ " + modalDto.getItem().getItemNumber() + " - " + modalDto.getItem().getItemName() + " ]", LocaleContextHolder.getLocale()));
        }
        for (BomModalDto bomModalDto : modalDto.getChildren()) {
//            itemType = itemTypeRepository.findOne(bomModalDto.getItem().getItemType());
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(bomModalDto.getItem().getLatestRevision());
            combsList = getAllPossibleCombsAfterRemoveItemsExclRules(itemRevision);
            strings = new ArrayList<>();
            for (ItemAttributeDto attributeDto : bomModalDto.getAttributes()) {
                strings.add(attributeDto.getListValue());
            }
            existInstance = false;
            for (List<String> list : combsList) {
                if (list.containsAll(strings)) {
                    existInstance = true;
                }
            }
            if (!existInstance) {
                throw new CassiniException(messageSource.getMessage("with_selected_combination_values_instance_not_possible " + " [ " + modalDto.getItem().getItemNumber() + " - " + modalDto.getItem().getItemName() + " ]",
                        null, "With selected combination values Instance no possible for [ " + modalDto.getItem().getItemNumber() + " - " + modalDto.getItem().getItemName() + " ]", LocaleContextHolder.getLocale()));
            }
        }
        Boolean creation = true;
        List<ASBomConfigValues> bomConfigValues = new ArrayList<>();
        BOMConfiguration existBomConfig = null;
        if (bomConfiguration.getId() == null) {
            creation = true;
        } else {
            creation = false;
            existBomConfig = (BOMConfiguration) Utils.cloneObject(bomConfigurationRepository.findOne(bomConfiguration.getId()), BOMConfiguration.class);
            bomConfigValues = checkAttributeValuesChanges(bomConfiguration, existBomConfig);
        }
        bomConfiguration = bomConfigurationRepository.save(bomConfiguration);
        if (creation) {
            applicationEventPublisher.publishEvent(new ItemEvents.BomConfigurationAddedEvent(revision, bomConfiguration, existBomConfig, bomConfigValues));
        } else {
            applicationEventPublisher.publishEvent(new ItemEvents.BomConfigurationUpdatedEvent(revision, bomConfiguration, existBomConfig, bomConfigValues));
        }
        PLMItemRevision itemRevision = itemRevisionRepository.findByInstanceAndBomConfiguration(itemId, bomConfiguration.getId());
        if (itemRevision != null) {
            PLMItem itemRevisionMaster = itemRepository.findOne(itemRevision.getItemMaster());
            String json = bomConfiguration.getRules();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = null;
            try {
                map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
                });
            } catch (Exception e) {
                new CassiniException(e.getMessage());
            }
            List<String> keyValues = new ArrayList<>();
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                    PLMItemTypeAttribute objectTypeAttribute = itemTypeAttributeRepository.findByItemTypeAndName(itemRevisionMaster.getItemType().getId(), (String) entry.getKey());
                    if (objectTypeAttribute != null) {
                        ObjectAttribute attribute = objectAttributeRepository.findByObjectIdAndAttributeDefId(itemRevisionMaster.getId(), objectTypeAttribute.getId());
                        if (attribute != null) {
                            attribute.setListValue((String) entry.getValue());
                            attribute = objectAttributeRepository.save(attribute);
                            String valuesKey = itemRevisionMaster.getItemName() + "_" + objectTypeAttribute.getName() + "_" + attribute.getListValue();
                            keyValues.add(valuesKey);
                        }
                    }
                }
            }
            Map<String, Object> itemIdMap = new HashMap<>();
            List<HashMap<String, Object>> childrenMap = null;
            HashMap<Integer, List<String>> itemValuesMap = new HashMap<>();
            HashMap<Integer, List<Integer>> attributeIdsMap = new HashMap<>();
            setBomConfigurationForItem(map, itemIdMap, childrenMap, itemValuesMap, attributeIdsMap);

            PLMItemRevision parentRevision = itemRevisionRepository.findOne(itemId);
            List<PLMBom> bomList = bomRepository.findByParentOrderBySequenceAsc(itemRevision);
            if (bomList.size() > 0) {
                bomRepository.delete(bomList);
            }
            List<PLMBom> plmBoms = bomRepository.findByParentAndConfigurableTrueOrderBySequenceAsc(parentRevision.getId());
            List<PLMBom> copyBoms = new ArrayList<>();
            for (PLMBom bom : plmBoms) {

                if (bom.getItem().getConfigurable() != null && bom.getItem().getConfigurable()) {

                    List<PLMItem> items = itemRepository.findByInstanceOrderByCreatedDateDesc(bom.getItem().getId());

                    if (items.size() > 0) {
                        Boolean instanceExit = false;
                        for (PLMItem instanceItem : items) {
                            List<String> valueList = itemValuesMap.get(bom.getItem().getId());
                            List<Integer> attrIds = attributeIdsMap.get(bom.getItem().getId());
                            Integer[] attriIds = attrIds.toArray(new Integer[attrIds.size()]);
                            String[] vals = valueList.toArray(new String[valueList.size()]);
                            List<ObjectAttribute> objectAttributes = objectAttributeRepository.findByObjectIdAndAttributeDefIdsAndListValues(instanceItem.getId(), attriIds, vals);
                            if (objectAttributes.size() == valueList.size()) {
                                copyBomItem(bom, itemRevision, copyBoms);
                                instanceExit = true;
                            }
                        }

                        if (!instanceExit) {
                            copyBomItem(bom, itemRevision, copyBoms);
                        }
                    } else {
                        copyBomItem(bom, itemRevision, copyBoms);
                    }
                }
            }

            List<PLMBom> normalBomItems = bomRepository.findByParentAndConfigurableFalseOrderBySequenceAsc(parentRevision.getId());

            Map<String, List<ItemInclusionMap>> inclusionsMap = new Gson().fromJson(
                    parentRevision.getItemExclusions(), new TypeToken<LinkedHashMap<String, List<ItemInclusionMap>>>() {
                    }.getType()
            );

            Map<String, List<ItemInclusionMap>> parentItemExclusionMap = new HashMap<>();

            if (inclusionsMap != null && inclusionsMap.size() > 0) {
                inclusionsMap.values().forEach(itemInclusionMap -> {
                    ItemInclusionMap value1 = itemInclusionMap.get(0);
                    ItemInclusionMap value2 = itemInclusionMap.get(1);
                    if (value1 != null && value1.getItemId().equals(parentRevision.getId())) {
                        List<ItemInclusionMap> values = parentItemExclusionMap.containsKey(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) ? parentItemExclusionMap.get(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue()) : new ArrayList<>();
                        values.add(value2);
                        parentItemExclusionMap.put(value1.getItemName() + "_" + value1.getKey() + "_" + value1.getValue(), values);
                    }
                });

                HashMap<Integer, Integer> itemExclusionMap = new HashMap<>();
                for (String key : keyValues) {
                    List<ItemInclusionMap> values = parentItemExclusionMap.get(key);
                    if (values != null && values.size() > 0) {
                        values.forEach(value -> {
                            itemExclusionMap.put(value.getItemId(), value.getItemId());
                        });
                    }
                }

                for (PLMBom bom : normalBomItems) {
                    Integer bomItemId = itemExclusionMap.get(bom.getItem().getId());
                    if (bomItemId == null) {
                        copyBomItem(bom, itemRevision, copyBoms);
                    }
                }
            } else {
                copyBomItems(normalBomItems, itemRevision, copyBoms);
            }
            bomRepository.save(copyBoms);
            if (copyBoms.size() > 0) {
                itemRevision.setHasBom(true);
                itemRevision.setBomConfiguration(bomConfiguration.getId());
                itemRevision = itemRevisionRepository.save(itemRevision);
            }
        }
        return bomConfiguration;
    }


    public void setBomConfigurationForItem(Map<String, Object> map, Map<String, Object> itemIdMap, List<HashMap<String, Object>> childrenMap, HashMap<Integer, List<String>> itemValuesMap, HashMap<Integer, List<Integer>> attributeIdsMap) {
        if (map != null && map.size() > 0) {
            Object o = map.get("children");
            childrenMap = (List<HashMap<String, Object>>) o;
            childrenMap.forEach(children -> {
                Set<Map.Entry<String, Object>> entries1 = children.entrySet();
                for (Map.Entry<String, Object> entry : entries1) {
                    itemIdMap.put(entry.getKey(), entry.getValue());
                }
                Integer revisionItemId = (Integer) itemIdMap.get("id");
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
                itemValuesMap.put(revisionItemId, values);
                attributeIdsMap.put(revisionItemId, attributeIds);
            });
        }
    }

    private List<ASBomConfigValues> checkAttributeValuesChanges(BOMConfiguration bomConfiguration, BOMConfiguration existBomConfig) {
        List<ASBomConfigValues> bomConfigValues = new ArrayList<>();
        String json = bomConfiguration.getRules();
        String oldValues = existBomConfig.getRules();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        Map<String, Object> oldMap = null;
        try {
            map = objectMapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
            oldMap = objectMapper.readValue(oldValues, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (Exception e) {
            new CassiniException(e.getMessage());
        }

        if (map != null && oldMap != null) {
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            Set<Map.Entry<String, Object>> oldEntries = oldMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                if (!entry.getKey().equals("id") && !entry.getKey().equals("children")) {
                    for (Map.Entry<String, Object> oldEntry : oldEntries) {
                        if (!oldEntry.getKey().equals("id") && !oldEntry.getKey().equals("children") && entry.getKey().equals(oldEntry.getKey())) {
                            String oldValue = (String) oldEntry.getValue();
                            String newValue = (String) entry.getValue();
                            if (!oldValue.equals(newValue)) {
                                ASBomConfigValues configValue = new ASBomConfigValues();
                                configValue.setAttribute(oldEntry.getKey());
                                configValue.setOldValue(oldValue);
                                configValue.setNewValue(newValue);

                                bomConfigValues.add(configValue);
                            }
                        }
                    }
                }
            }

        }

        return bomConfigValues;
    }
}
