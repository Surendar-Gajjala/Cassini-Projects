package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.MeasurementUnit;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.core.MeasurementUnitRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ToolEvents;
import com.cassinisys.plm.filtering.ToolCriteria;
import com.cassinisys.plm.filtering.ToolPredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.plm.PLMItemAttribute;
import com.cassinisys.plm.repo.mes.*;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.service.cm.DCOService;
import com.cassinisys.plm.service.mro.AssetService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.service.pqm.ObjectFileService;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ToolService implements CrudService<MESTool, Integer> {
    @Autowired
    private MESToolRepository toolRepo;
    @Autowired
    private ToolPredicateBuilder toolPredicateBuilder;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private DCOService dcoService;
    @Autowired
    private ToolTypeRepository toolTypeRepository;
    @Autowired
    private MESObjectTypeAttributeRepository mesObjectTypeAttributeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesTool,'create')")
    public MESTool create(MESTool mesTool) {
        MROAsset mroAsset = mesTool.getAsset();
        MESTool existingTool = toolRepo.findByName(mesTool.getName());
        MESManufacturerData manufacturerData = mesTool.getManufacturerData();
        MESTool existToolNumber = toolRepo.findByNumber(mesTool.getNumber());
        if (existToolNumber != null) {
            String message = messageSource.getMessage("tool_number_already_exists", null, "{0} Tool number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existToolNumber.getNumber());
            throw new CassiniException(result);
        }
        if (existingTool != null) {
            String message = messageSource.getMessage("tool_name_already_exists", null, "{0} Tool name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existingTool.getName());
            throw new CassiniException(result);
        } else {
            autoNumberService.saveNextNumber(mesTool.getType().getAutoNumberSource().getId(), mesTool.getNumber());
            mesTool = toolRepo.save(mesTool);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesTool.getId());
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        if (mesTool.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(mesTool.getId());
            assetService.create(mroAsset);
        }
        applicationEventPublisher.publishEvent(new ToolEvents.ToolCreatedEvent(mesTool));
        return mesTool;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesTool.id ,'edit')")
    public MESTool update(MESTool mesTool) {
        MESTool existingTool = toolRepo.findByName(mesTool.getName());
        MESTool oldTool = JsonUtils.cloneEntity(toolRepo.findOne(mesTool.getId()), MESTool.class);

        MESManufacturerData oldManufacturerData = manufacturerDataRepository.findOne(mesTool.getId());
        oldTool.setManufacturerData(oldManufacturerData);

        MESManufacturerData manufacturerData = mesTool.getManufacturerData();
        if (existingTool != null && !existingTool.getId().equals(mesTool.getId())) {
            throw new CassiniException("Tool name already exist");
        } else {
            mesTool = toolRepo.save(mesTool);
            if (manufacturerData == null) {
                manufacturerData = new MESManufacturerData();
            }
            manufacturerData.setObject(mesTool.getId());
            mesTool.setManufacturerData(manufacturerData);
            applicationEventPublisher.publishEvent(new ToolEvents.ToolBasicInfoUpdatedEvent(oldTool, mesTool));
            manufacturerData = manufacturerDataRepository.save(manufacturerData);
        }
        
        return mesTool;
    }

    @Override
    @PreAuthorize("hasPermission(#tooId,'delete')")
    public void delete(Integer toolId) {
        List<MROAsset> assets = mroAssetRepository.findByResource(toolId);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("tool_already_used_in_asset", null, "Tool already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        toolRepo.delete(toolId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESTool get(Integer toolId) {
        MESTool tool = toolRepo.findOne(toolId);
        tool.setManufacturerData(manufacturerDataRepository.findOne(toolId));
        return tool;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESTool> getAll() {
        return toolRepo.findAll();
    }


    public void saveToolAttributes(List<MESObjectAttribute> attributes) {
        for (MESObjectAttribute attribute : attributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {
                mesObjectAttributeRepository.save(attribute);
            }
        }
    }

    @Transactional
    public MESTool saveImageAttributeValue(Integer objectId, Integer attributeId, Map<String, MultipartFile> fileMap) {
        MESTool tool = toolRepo.findOne(objectId);
        if (tool != null) {
            PLMItemAttribute itemAttribute = new PLMItemAttribute();
            itemAttribute.setId(new ObjectAttributeId(objectId, attributeId));
            List<MultipartFile> files = new ArrayList<>(fileMap.values());
            dcoService.setImage(files, itemAttribute);

        }

        return tool;
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public MESObjectAttribute updateToolAttribute(MESObjectAttribute attribute) {
        MESObjectAttribute oldValue = mesObjectAttributeRepository.findByObjectAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, MESObjectAttribute.class);
        MESObjectTypeAttribute mesObjectTypeAttribute = mesObjectTypeAttributeRepository.findOne(attribute.getId().getAttributeDef());

        if (mesObjectTypeAttribute.getMeasurement() != null) {
            List<MeasurementUnit> measurementUnits = measurementUnitRepository.findByMeasurementOrderByIdAsc(mesObjectTypeAttribute.getMeasurement().getId());
            MeasurementUnit measurementUnit = measurementUnitRepository.findOne(attribute.getMeasurementUnit().getId());
            MeasurementUnit baseUnit = measurementUnitRepository.findByMeasurementAndBaseUnitTrue(mesObjectTypeAttribute.getMeasurement().getId());

            Integer attributeUnitIndex = measurementUnits.indexOf(measurementUnit);
            Integer baseUnitIndex = measurementUnits.indexOf(baseUnit);

            if (!attributeUnitIndex.equals(baseUnitIndex)) {
                attribute.setDoubleValue(attribute.getDoubleValue() / measurementUnit.getConversionFactor());
            } else {
                attribute.setDoubleValue(attribute.getDoubleValue());
            }
        } else {
            attribute.setDoubleValue(attribute.getDoubleValue());
        }
        attribute = mesObjectAttributeRepository.save(attribute);

        MESTool tool = toolRepo.findOne(attribute.getId().getObjectId());
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESTool> getAllToolsByPageable(Pageable pageable, ToolCriteria criteria) {
        Predicate predicate = toolPredicateBuilder.build(criteria, QMESTool.mESTool);
        Page<MESTool> tools = toolRepo.findAll(predicate, pageable);
        tools .getContent().forEach(tool -> {

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(tool.getId(), PLMObjectType.MESOBJECT,false);
            tool.setToolFiles(objectFileDto.getObjectFiles());
        });
        return tools;
    }

    @Transactional
    public MESTool uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESTool tool = toolRepo.findOne(id);
        if (tool != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    tool.setImage(file.getBytes());
                    tool = toolRepo.save(tool);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return tool;
    }

    public void downloadImage(Integer toolId, HttpServletResponse response) {
        MESTool tool = toolRepo.findOne(toolId);
        if (tool != null) {
            InputStream is = new ByteArrayInputStream(tool.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESTool> getToolsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESToolType type = toolTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return toolRepo.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESToolType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESToolType> children = toolTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESToolType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }
}

