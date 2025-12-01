package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.JigsFixtureEvents;
import com.cassinisys.plm.filtering.JigsFixtureCriteria;
import com.cassinisys.plm.filtering.JigsFixturePredicateBuilder;
import com.cassinisys.plm.model.mes.*;
import com.cassinisys.plm.model.mes.dto.JigsFixtureDto;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;
import com.cassinisys.plm.repo.mes.JigsFixtureTypeRepository;
import com.cassinisys.plm.repo.mes.MESJigsFixtureRepository;
import com.cassinisys.plm.repo.mes.MESObjectAttributeRepository;
import com.cassinisys.plm.repo.mes.ManufacturerDataRepository;
import com.cassinisys.plm.repo.mro.MROAssetRepository;
import com.cassinisys.plm.service.mro.AssetService;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by CassiniSystems on 09-06-2020.
 */
@Service
public class JigsFixtureService implements CrudService<MESJigsFixture, Integer> {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MESJigsFixtureRepository jigsFixtureRepository;
    @Autowired
    private JigsFixtureTypeRepository jigsFixtureTypeRepository;
    @Autowired
    private JigsFixturePredicateBuilder jigsFixturePredicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MESObjectAttributeRepository mesObjectAttributeRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ManufacturerDataRepository manufacturerDataRepository;
    @Autowired
    private AssetService assetService;
    @Autowired
    private MROAssetRepository mroAssetRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#jigsFixture,'create')")
    public MESJigsFixture create(MESJigsFixture jigsFixture) {
        MROAsset mroAsset = jigsFixture.getAsset();
        MESManufacturerData manufacturerData = jigsFixture.getManufacturerData();
        List<MESObjectAttribute> mesObjectAttributes = jigsFixture.getMesObjectAttributes();
        MESJigsFixture existJigsFixture = jigsFixtureRepository.findByNumber(jigsFixture.getNumber());
        MESJigsFixture existJigsFixtureName = jigsFixtureRepository.findByJigTypeAndName(jigsFixture.getJigType(), jigsFixture.getName());
        if (existJigsFixture != null) {
            String message = messageSource.getMessage("jig_number_already_exists", null, "{0} JigFixture number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existJigsFixture.getNumber());
            throw new CassiniException(result);
        }
        if (existJigsFixtureName != null) {
            String message = messageSource.getMessage("jig_name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existJigsFixtureName.getName());
            throw new CassiniException(result);
        }
        autoNumberService.saveNextNumber(jigsFixture.getType().getAutoNumberSource().getId(), jigsFixture.getNumber());
        jigsFixture = jigsFixtureRepository.save(jigsFixture);

        if (jigsFixture.getRequiresMaintenance() && mroAsset != null) {
            mroAsset.setResource(jigsFixture.getId());
            assetService.create(mroAsset);
        }
        if (manufacturerData == null) {
            manufacturerData = new MESManufacturerData();
        }
        manufacturerData.setObject(jigsFixture.getId());
        manufacturerData = manufacturerDataRepository.save(manufacturerData);
        for (MESObjectAttribute attribute : mesObjectAttributes) {
            if (attribute.getTimeValue() != null || attribute.getTimestampValue() != null || attribute.getRefValue() != null || attribute.getListValue() != null || attribute.getMListValue().length > 0 ||
                    attribute.getImageValue() != null || attribute.getAttachmentValues().length > 0 || (attribute.getCurrencyType() != null &&
                    attribute.getCurrencyValue() != null) || attribute.getDateValue() != null || attribute.getDoubleValue() != null || attribute.getHyperLinkValue() != null ||
                    attribute.getStringValue() != null || attribute.getLongTextValue() != null || attribute.getRichTextValue() != null || attribute.getIntegerValue() != null || attribute.getBooleanValue()) {

                attribute.setId(new ObjectAttributeId(jigsFixture.getId(), attribute.getId().getAttributeDef()));
                mesObjectAttributeRepository.save(attribute);
            }
        }
        applicationEventPublisher.publishEvent(new JigsFixtureEvents.JigFixtureCreatedEvent(jigsFixture));
        return jigsFixture;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#jigsFixture.id ,'edit')")
    public MESJigsFixture update(MESJigsFixture jigsFixture) {
        MESManufacturerData manufacturerData = jigsFixture.getManufacturerData();

        MESManufacturerData oldManufacturerData = manufacturerDataRepository.findOne(jigsFixture.getId());

        MESJigsFixture existJigsFixtureName = jigsFixtureRepository.findByJigTypeAndName(jigsFixture.getJigType(), jigsFixture.getName());
        if (existJigsFixtureName != null && !existJigsFixtureName.getId().equals(jigsFixture.getId())) {
            String message = messageSource.getMessage("jig_name_already_exists", null, "{0} name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existJigsFixtureName.getName());
            throw new CassiniException(result);
        }
        MESJigsFixture oldJigsFixture = JsonUtils.cloneEntity(jigsFixtureRepository.findOne(jigsFixture.getId()), MESJigsFixture.class);
        oldJigsFixture.setManufacturerData(oldManufacturerData);

        jigsFixture = jigsFixtureRepository.save(jigsFixture);
         /* App events */
         jigsFixture.setManufacturerData(manufacturerData);
         applicationEventPublisher.publishEvent(new JigsFixtureEvents.JigFixtureBasicInfoUpdatedEvent(oldJigsFixture, jigsFixture));
 
        if (manufacturerData == null) {
            manufacturerData = new MESManufacturerData();
        }
        manufacturerData.setObject(jigsFixture.getId());
        manufacturerData = manufacturerDataRepository.save(manufacturerData);
       
        return jigsFixture;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        List<MROAsset> assets = mroAssetRepository.findByResource(id);
        if (assets.size() > 0) {
            String message = messageSource.getMessage("jigfixture_already_used_in_asset", null, "Jig and fixture already in use", LocaleContextHolder.getLocale());
            throw new CassiniException(message);
        }
        jigsFixtureRepository.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESJigsFixture get(Integer id) {
        MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(id);
        jigsFixture.setManufacturerData(manufacturerDataRepository.findOne(id));
        return jigsFixture;
    }

    @Override
    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESJigsFixture> getAll() {
        return jigsFixtureRepository.findAll();
    }


    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESJigsFixture> findMultiple(List<Integer> ids) {
        return jigsFixtureRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<JigsFixtureDto> getAllJigsFixtures(Pageable pageable, JigsFixtureCriteria jigsFixtureCriteria) {
        Predicate predicate = jigsFixturePredicateBuilder.build(jigsFixtureCriteria, QMESJigsFixture.mESJigsFixture);
        Page<MESJigsFixture> jigsFixtures = jigsFixtureRepository.findAll(predicate, pageable);

        List<JigsFixtureDto> jigsFixtureDtos = new LinkedList<>();
        jigsFixtures.getContent().forEach(jigsFixture -> {
            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(jigsFixture.getId(), PLMObjectType.MESOBJECT,false);
            JigsFixtureDto jigsFixtureDto = new JigsFixtureDto();
            jigsFixtureDto.setJigsFixtureFiles(objectFileDto.getObjectFiles());
            jigsFixtureDto.setId(jigsFixture.getId());
            jigsFixtureDto.setName(jigsFixture.getName());
            jigsFixtureDto.setNumber(jigsFixture.getNumber());
            jigsFixtureDto.setObjectType(jigsFixture.getObjectType().name());
            jigsFixtureDto.setSubType(jigsFixture.getType().getName());
            jigsFixtureDto.setDescription(jigsFixture.getDescription());
            jigsFixtureDto.setType(jigsFixture.getType().getName());
            jigsFixtureDto.setCreatedBy(personRepository.findOne(jigsFixture.getCreatedBy()).getFullName());
            jigsFixtureDto.setCreatedDate(jigsFixture.getCreatedDate());
            jigsFixtureDto.setModifiedBy(personRepository.findOne(jigsFixture.getModifiedBy()).getFullName());
            jigsFixtureDto.setModifiedDate(jigsFixture.getModifiedDate());
            jigsFixtureDto.setActive(jigsFixture.getActive());
            jigsFixtureDto.setRequiresMaintenance(jigsFixture.getRequiresMaintenance());
            if (jigsFixture.getImage() != null) {
                jigsFixtureDto.setHasImage(true);
            }
            jigsFixtureDtos.add(jigsFixtureDto);
        });

        return new PageImpl<JigsFixtureDto>(jigsFixtureDtos, pageable, jigsFixtures.getTotalElements());
    }

    @Transactional
    public MESJigsFixture uploadImage(Integer id, MultipartHttpServletRequest request) {
        MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(id);
        if (jigsFixture != null) {
            Map<String, MultipartFile> filesMap = request.getFileMap();
            List<MultipartFile> files = new ArrayList<>(filesMap.values());
            if (files.size() > 0) {
                MultipartFile file = files.get(0);
                try {
                    jigsFixture.setImage(file.getBytes());
                    jigsFixture = jigsFixtureRepository.save(jigsFixture);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jigsFixture;
    }

    public void downloadImage(Integer toolId, HttpServletResponse response) {
        MESJigsFixture jigsFixture = jigsFixtureRepository.findOne(toolId);
        if (jigsFixture != null) {
            InputStream is = new ByteArrayInputStream(jigsFixture.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESJigsFixture> getJigsFixturesByType(Integer typeId, Pageable pageable) {
        List<Integer> ids = new ArrayList<>();
        MESJigsFixtureType type = jigsFixtureTypeRepository.findOne(typeId);
        collectHierarchyTypeIds(ids, type);
        return jigsFixtureRepository.getByTypeIds(ids, pageable);
    }

    private void collectHierarchyTypeIds(List<Integer> collector, MESJigsFixtureType type) {
        if (type != null) {
            collector.add(type.getId());
            List<MESJigsFixtureType> children = jigsFixtureTypeRepository.findByParentTypeOrderByIdAsc(type.getId());
            for (MESJigsFixtureType child : children) {
                collectHierarchyTypeIds(collector, child);
            }
        }
    }

}
