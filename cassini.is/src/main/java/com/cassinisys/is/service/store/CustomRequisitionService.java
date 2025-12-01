package com.cassinisys.is.service.store;

import com.cassinisys.is.filtering.CustomRequisitionCriteria;
import com.cassinisys.is.filtering.CustomRequisitionPredicateBuilder;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.is.model.store.*;
import com.cassinisys.is.repo.store.CustomRequisitionItemRepository;
import com.cassinisys.is.repo.store.CustomRequisitionRepository;
import com.cassinisys.is.repo.store.ISTopStoreRepository;
import com.cassinisys.is.service.procm.ItemService;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Rajabrahmachary on 14-09-2018.
 */
@Service
public class CustomRequisitionService implements CrudService<CustomRequisition, Integer> {

    /*  adding dependencies */

    public static Map fileMap = new HashMap();
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private CustomRequisitionItemRepository customRequisitionItemRepository;
    @Autowired
    private CustomRequisitionRepository customRequisitionRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ISTopStoreRepository topStoreRepository;
    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomRequisitionPredicateBuilder requisitionPredicateBuilder;
    
    /*  methods for CustomRequisition */

    @Transactional(readOnly = false)
    @Override
    public CustomRequisition create(CustomRequisition customRequisition) {
        if (customRequisition.getRequisitionNumber() == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Requisition Number Source");
            String number = autoNumberService.getNextNumber(autoNumber.getId());
            customRequisition.setRequisitionNumber(number);
        }
        customRequisition.setRequestedDate(new Date());
        CustomRequisition requisition = customRequisitionRepository.save(customRequisition);
        for (CustomRequisitionItem requisitionItem : customRequisition.getCustomRequisitionItems()) {
            requisitionItem.setRequisition(requisition.getId());
        }
        customRequisitionItemRepository.save(customRequisition.getCustomRequisitionItems());
        return requisition;
    }

    @Override
    @Transactional(readOnly = false)
    public CustomRequisition update(CustomRequisition customRequisition) {
        List<CustomRequisitionItem> customRequisitionItems = new ArrayList<>();
        if (customRequisition.getCustomRequisitionItems().size() > 0) {
            for (CustomRequisitionItem customRequisitionItem : customRequisition.getCustomRequisitionItems()) {
                customRequisitionItem.setRequisition(customRequisition.getId());
                customRequisitionItems.add(customRequisitionItem);
            }
        }
        if (customRequisitionItems.size() > 0) {
            createCustomRequisitionItem(customRequisitionItems);
        }
        return customRequisitionRepository.save(customRequisition);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer customRequisitionId) {
        customRequisitionRepository.delete(customRequisitionId);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomRequisition get(Integer customRequisitionId) {
        return customRequisitionRepository.findOne(customRequisitionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomRequisition> getAll() {
        List<CustomRequisition> approvedRequisitions = new ArrayList<>();
        List<CustomRequisition> customRequisitions = customRequisitionRepository.findAll();
        for (CustomRequisition customRequisition : customRequisitions) {
            if (customRequisition.getStatus().equals(RequisitionStatus.APPROVED)) {
                customRequisition.setCustomRequisitionItems(getAllRequisitionItems(customRequisition.getId()));
                approvedRequisitions.add(customRequisition);
            }
        }
        return approvedRequisitions;
    }

    @Transactional(readOnly = true)
    public List<CustomRequisition> getAllStoreRequisitions(Integer storeId) {
        List<CustomRequisition> approvedRequisitions = new ArrayList<>();
        List<CustomRequisition> customRequisitions = customRequisitionRepository.findByStoreOrderByIdAsc(storeId);
        for (CustomRequisition customRequisition : customRequisitions) {
            if (customRequisition.getStatus().equals(RequisitionStatus.APPROVED)) {
                customRequisition.setCustomRequisitionItems(getAllRequisitionItems(customRequisition.getId()));
                approvedRequisitions.add(customRequisition);
            }
        }
        return approvedRequisitions;
    }

    public Page<CustomRequisition> requisitionFreeTextSearch(Pageable pageable, CustomRequisitionCriteria criteria) {
        Predicate predicate = requisitionPredicateBuilder.build(criteria, QCustomRequisition.customRequisition);
        return customRequisitionRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomRequisition> getPageableRequisitions(Integer storeId, Pageable pageable) {
        ISTopStore topStore = topStoreRepository.findOne(storeId);
        return customRequisitionRepository.findByStoreOrderByIdAsc(topStore, pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomRequisition> getAllPageableRequisitions(Pageable pageable) {
        return customRequisitionRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<CustomRequisition> findPageableRequisitionsByProject(Integer projectId, Pageable pageable) {
        RequisitionStatus requestStatus = RequisitionStatus.APPROVED;
        Page<CustomRequisition> customRequisitions = customRequisitionRepository.findByProjectOrderByIdAsc(projectId, requestStatus, pageable);
        for (CustomRequisition customRequisition : customRequisitions) {
            customRequisition.setCustomRequisitionItems(getAllRequisitionItems(customRequisition.getId()));
        }
        return customRequisitions;
    }

     /*  methods for CustomRequisition Attributes */

    @Transactional(readOnly = true)
    public List<ObjectTypeAttribute> getRequiredRequisitionAttributes(String objectType) {
        List<ObjectTypeAttribute> requisitionAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf(objectType));
        return requisitionAttributes;
    }

    @Transactional(readOnly = true)
    public Map<Integer, List<ObjectAttribute>> getObjectAttributesByRequisitionIdsAndAttributeIds(Integer[] requisitionIds, Integer[] objectAttributeIds) {
        Map<Integer, List<ObjectAttribute>> objectAttributesMap = new HashMap();
        List<ObjectAttribute> attributes = objectAttributeRepository.findByObjectIdsInAndAttributeDefIdsIn(requisitionIds, objectAttributeIds);
        for (ObjectAttribute attribute : attributes) {
            Integer id = attribute.getId().getObjectId();
            List<ObjectAttribute> objectAttributes = objectAttributesMap.get(id);
            if (objectAttributes == null) {
                objectAttributes = new ArrayList<>();
                objectAttributesMap.put(id, objectAttributes);
            }
            objectAttributes.add(attribute);
        }
        return objectAttributesMap;

    }



    /*  methods for CustomRequisitionItem */

    @Transactional(readOnly = false)
    public List<CustomRequisitionItem> createCustomRequisitionItem(List<CustomRequisitionItem> customRequisitionItems) {
        return customRequisitionItemRepository.save(customRequisitionItems);
    }

    @Transactional(readOnly = false)
    public CustomRequisitionItem updateCustomRequisitionItem(CustomRequisitionItem customRequisitionItem) {
        return customRequisitionItemRepository.save(customRequisitionItem);
    }

    @Transactional(readOnly = false)
    public void deleteCustomRequisitionItem(Integer customRequisitionItemId) {
        customRequisitionItemRepository.delete(customRequisitionItemId);
    }

    @Transactional(readOnly = true)
    public CustomRequisitionItem getCustomRequisitionItem(Integer customRequisitionItemId) {
        return customRequisitionItemRepository.findOne(customRequisitionItemId);
    }

    @Transactional(readOnly = true)
    public List<CustomRequisitionItem> getAllRequisitionItems(Integer requisitionId) {
        List<CustomRequisitionItem> customRequisitionItems = customRequisitionItemRepository.findByRequisitionOrderByIdAsc(requisitionId);
        for (CustomRequisitionItem customRequisitionItem : customRequisitionItems) {
            customRequisitionItem.setIndentQuantity(customRequisitionItem.getQuantity());
        }
        return customRequisitionItems;
    }

    @Transactional(readOnly = true)
    public Page<CustomRequisitionItem> getPagedCustomRequisitionItems(Integer requisitionId, Pageable pageable) {
        List<CustomRequisitionItem> customRequisitionItems = getAllRequisitionItems(requisitionId);
        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > customRequisitionItems.size() ? customRequisitionItems.size() : (start + pageable.getPageSize());
        return new PageImpl<CustomRequisitionItem>(customRequisitionItems.subList(start, end), pageable, customRequisitionItems.size());
    }

    public String getRvnlRailwaysLogoImageUrl() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String url = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + "/app/assets/images/rvnllogo.png";
        return url;
    }

    public String printRequisitionChallan(Integer requisitionId, HttpServletRequest request, HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        CustomRequisition customRequisition = customRequisitionRepository.findOne(requisitionId);
        fileId = customRequisition.getRequisitionNumber() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            List<CustomRequisitionItem> requisitionItems = customRequisitionItemRepository.findByRequisitionOrderByIdAsc(requisitionId);
            String htmlResponse = getRequisitionChallanExportHtml(requisitionItems, customRequisition, request, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileId = fileId.replace("/", ".");
        fileMap.put(fileId, is);
        return fileId;
    }

    @Transactional(readOnly = true)
    public String getRequisitionChallanExportHtml(List<CustomRequisitionItem> customRequisitionItems, CustomRequisition customRequisition, HttpServletRequest request, HttpServletResponse response) {
        List<ObjectAtrributeDTO> objectAtrributeDTOList = new ArrayList<>();
        List<ObjectTypeAttribute> objectTypeAttributes = objectTypeAttributeRepository.findByObjectTypeAndRequiredTrue(ObjectType.valueOf("CUSTOM_REQUISITION"));
        for (ObjectTypeAttribute objectTypeAttribute : objectTypeAttributes) {
            ObjectAtrributeDTO objectAtrributeDTO = new ObjectAtrributeDTO();
            objectAtrributeDTO.setObjectAttribute(objectAttributeRepository.findByObjectIdAndAttributeDefId(customRequisition.getId(), objectTypeAttribute.getId()));
            objectAtrributeDTO.setObjectTypeAttribute(objectTypeAttribute);
            objectAtrributeDTOList.add(objectAtrributeDTO);
        }
        String imgUrl = null;
        String title = null;
        ServletContext context = request.getServletContext();
        String path = context.getRealPath("/") + "application.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;
            title = (String) jsonObject.get("printTemplateTitle");
            String localImg = (String) jsonObject.get("templateLogo");
            imgUrl = getLocalHostUrl() + "/" + localImg;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<>();
        model.put("objectAtrributes", objectAtrributeDTOList);
        model.put("requisitionItems", customRequisitionItems);
        model.put("requisition", customRequisition);
        model.put("projectName", customRequisition.getProject().getName());
        model.put("storeName", customRequisition.getStore().getStoreName());
        model.put("logo", imgUrl);
        model.put("title", title);
        String exportHtmlData = getRequisitionChallanFromTemplate(model);
        return exportHtmlData;

    }

    @Transactional(readOnly = true)
    public String getRequisitionChallanFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/nfr");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate("requisitionChallan.html"), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void downloadExportFile(String fileId, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileId, "UTF-8"));
        } catch (UnsupportedEncodingException var51) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileId);
        }
        try {
            ServletOutputStream var5 = response.getOutputStream();
            IOUtils.copy((InputStream) fileMap.get(fileId), var5);
            var5.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    @Transactional(readOnly = true)
    public String getLocalHostUrl() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String url = request.getScheme()
                + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort();
        return url;
    }

    @Transactional(readOnly = true)
    public List<ISMaterialItem> findNonRequisitionItems(Integer requisitionId, Integer projectId, Pageable pageable) {
        List<ISMaterialItem> materialItems = new ArrayList<>();
        List<CustomRequisitionItem> customRequisitionItems = customRequisitionItemRepository.findByRequisitionOrderByIdAsc(requisitionId);
        List<ISMaterialItem> materialItemList = itemService.getProjectMaterials(projectId, pageable).getContent();
        Hashtable<String, String> hashtable = new Hashtable<>();
        for (CustomRequisitionItem customRequisitionItem : customRequisitionItems) {
            for (ISMaterialItem materialItem : materialItemList) {
                if (materialItem.getItemNumber() == customRequisitionItem.getMaterialItem().getItemNumber()) {
                    hashtable.put(materialItem.getItemNumber(), materialItem.getItemNumber());
                }
            }
        }
        for (CustomRequisitionItem customRequisitionItem : customRequisitionItems) {
            for (ISMaterialItem materialItem : materialItemList) {
                if (materialItem.getItemNumber() != customRequisitionItem.getMaterialItem().getItemNumber() && hashtable.get(materialItem.getItemNumber()) == null) {
                    materialItems.add(materialItem);
                }
            }
        }
        return materialItems;

    }

}
