package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.AssemblyLineEvents;
import com.cassinisys.plm.filtering.AssemblyLineCriteria;
import com.cassinisys.plm.filtering.AssemblyLinePredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.repo.mes.*;
import com.mysema.query.types.Predicate;
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
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.cassinisys.plm.model.plm.PLMObjectType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class AssemblyLineService implements CrudService<MESAssemblyLine, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESAssemblyLineRepository assemblyLineRepository;
    @Autowired
    private AssemblyLineTypeRepository assemblyLineTypeRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private AssemblyLinePredicateBuilder assemblyLinePredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESPlantRepository mesPlantRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MESWorkCenterRepository workCenterRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#assemblyLine,'create')")
    public MESAssemblyLine create(MESAssemblyLine assemblyLine) {
        List<MESObjectAttribute> mesObjectAttributes = assemblyLine.getMesObjectAttributes();
        MESAssemblyLine existAssemblyLine = assemblyLineRepository.findByNumber(assemblyLine.getNumber());
        MESAssemblyLine existAssemblyLineName = assemblyLineRepository.findByPlantAndName(assemblyLine.getPlant(), assemblyLine.getName());
        if (existAssemblyLine != null) {
            String message = messageSource.getMessage("assemblyLine_number_already_exists", null, "{0} Assembly line number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existAssemblyLine.getNumber());
            throw new CassiniException(result);
        }
        if (existAssemblyLineName != null) {
            String message = messageSource.getMessage("assemblyLine_name_already_exists", null, "{0} Assembly line name already exist in plant", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existAssemblyLineName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(assemblyLine.getType().getAutoNumberSource().getId(), assemblyLine.getNumber());
        assemblyLine = assemblyLineRepository.save(assemblyLine);
        for (MESObjectAttribute attribute : mesObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(assemblyLine.getId(), attribute.getId().getAttributeDef()));
                mesObjectAttributeRepository.save(attribute);
            }
        }
        applicationEventPublisher.publishEvent(new AssemblyLineEvents.AssemblyLineCreatedEvent(assemblyLine));
        return assemblyLine;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#assemblyLine.id ,'edit')") 
    public MESAssemblyLine update(MESAssemblyLine assemblyLine) {
        MESAssemblyLine existAssemblyLineName = assemblyLineRepository.findByPlantAndName(assemblyLine.getPlant(), assemblyLine.getName());
        if (existAssemblyLineName != null && !existAssemblyLineName.getId().equals(assemblyLine.getId()) ) {
            String message = messageSource.getMessage("assemblyLine_name_already_exists", null, "{0} Assembly line name already exist in plant", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existAssemblyLineName.getName());
            throw new CassiniException(result);
        }
        MESAssemblyLine oldAssemblyLine = JsonUtils.cloneEntity(assemblyLineRepository.findOne(assemblyLine.getId()), MESAssemblyLine.class);
        assemblyLine = assemblyLineRepository.save(assemblyLine);
        applicationEventPublisher.publishEvent(new AssemblyLineEvents.AssemblyLineBasicInfoUpdatedEvent(oldAssemblyLine, assemblyLine));
        return assemblyLine;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        assemblyLineRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESAssemblyLine get(Integer id) {
        MESAssemblyLine assemblyLine = assemblyLineRepository.findOne(id);
        if (assemblyLine.getPlant() != null) {
            assemblyLine.setPlantName(mesPlantRepository.findOne(assemblyLine.getPlant()).getName());
        }
        return assemblyLine;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESAssemblyLine> getAll() {
        return assemblyLineRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESAssemblyLine> findMultiple(List<Integer> ids) {
        return assemblyLineRepository.findByIdIn(ids);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESAssemblyLine> getAllAssemblyLines(Pageable pageable, AssemblyLineCriteria assemblyLineCriteria) {
        Predicate predicate = assemblyLinePredicateBuilder.build(assemblyLineCriteria, QMESAssemblyLine.mESAssemblyLine);
        Page<MESAssemblyLine> assemblyLines = assemblyLineRepository.findAll(predicate, pageable);
        assemblyLines.getContent().forEach(assemblyLine -> {
            if (assemblyLine.getPlant() != null) {
                assemblyLine.setPlantName(mesPlantRepository.findOne(assemblyLine.getPlant()).getName());
            }

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(assemblyLine.getId(), PLMObjectType.MESOBJECT,false);
            assemblyLine.setAssemblyLineFiles(objectFileDto.getObjectFiles());
        });
        return assemblyLines;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESAssemblyLine> getObjectsByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESAssemblyLineType type = assemblyLineTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return assemblyLineRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESAssemblyLineType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESAssemblyLineType> children = assemblyLineTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESAssemblyLineType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MESWorkCenter> getAssemblyLineWorkCenters(Integer assemblyLine) {
        List<MESWorkCenter> workCenters = workCenterRepository.findByAssemblyLine(assemblyLine);
        workCenters.forEach(workCenter -> {
            if (workCenter.getPlant() != null) {
                workCenter.setPlantName(mesPlantRepository.findOne(workCenter.getPlant()).getName());
            }
        });
        return workCenters;
    }


    @Transactional(readOnly = true)
    public ItemDetailsDto getAssemblyLineTabCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setWorkCenters(workCenterRepository.findByAssemblyLine(id).size());
        return detailsDto;
    }

}
