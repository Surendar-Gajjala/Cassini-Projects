package com.cassinisys.plm.service.pqm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.DefaultValueDto;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.model.core.Lov;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.PPAPEvents;
import com.cassinisys.plm.filtering.PPAPCriteria;
import com.cassinisys.plm.filtering.PPAPPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.pdm.PDMConstants;
import com.cassinisys.plm.model.plm.LifeCyclePhaseType;
import com.cassinisys.plm.model.plm.PLMLifeCycle;
import com.cassinisys.plm.model.plm.PLMLifeCyclePhase;
import com.cassinisys.plm.model.pqm.PQMPPAP;
import com.cassinisys.plm.model.pqm.PQMPPAPAttribute;
import com.cassinisys.plm.model.pqm.PQMPPAPChecklist;
import com.cassinisys.plm.model.pqm.QPQMPPAP;
import com.cassinisys.plm.repo.mfr.SupplierPartRepository;
import com.cassinisys.plm.repo.mfr.SupplierRepository;
import com.cassinisys.plm.repo.plm.LifeCyclePhaseRepository;
import com.cassinisys.plm.repo.plm.LifeCycleRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.pqm.PPAPAttributeRepository;
import com.cassinisys.plm.repo.pqm.PPAPChecklistRepository;
import com.cassinisys.plm.repo.pqm.PPAPRepository;
import com.cassinisys.plm.service.plm.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.File;
import java.util.List;

@Service
public class PPAPService {

    @Autowired
    private PPAPRepository ppapRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private MessageSource messageSource;
    @Autowired(required = true)
    private PPAPPredicateBuilder ppapPredicateBuilder;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierPartRepository supplierPartRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;
    @Autowired
    private LifeCycleRepository lifeCycleRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private PPAPAttributeRepository ppapAttributeRepository;
    @Autowired
    private DocumentService documentService;


    @Transactional
    @PreAuthorize("hasPermission(#ppap,'create')")
    public PQMPPAP create(PQMPPAP ppap) {
        PQMPPAP existPpapNumber = ppapRepository.findByNumber(ppap.getNumber());
        if (existPpapNumber != null) {
            throw new CassiniException(messageSource.getMessage(ppap.getNumber() + " : " + "ppap_number_already_exists", null, "PPAP Number already exist", LocaleContextHolder.getLocale()));
        }
        autoNumberService.saveNextNumber(ppap.getType().getNumberSource().getId(), ppap.getNumber());
        List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleOrderByIdAsc(ppap.getType().getLifecycle().getId());
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhases.get(0);
        ppap.setStatus(lifeCyclePhase);
        ppap = ppapRepository.save(ppap);

        applicationEventPublisher.publishEvent(new PPAPEvents.PPAPCreatedEvent(ppap));
        updatePPAPChecklist(ppap);

        return ppap;
    }


    private void updatePPAPChecklist(PQMPPAP pqmppap) {
        Integer lovId = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Preference preference = preferenceRepository.findByPreferenceKey("DEFAULT_PPAP_CHECKLIST");
        if (preference != null && preference.getJsonValue() != null) {
            try {
                DefaultValueDto valueDto = objectMapper.readValue(preference.getJsonValue(), new TypeReference<DefaultValueDto>() {
                });
                preference.setDefaultValue(valueDto);
                if (valueDto.getTypeId() != null) {
                    lovId = valueDto.getTypeId();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        if (lovId != null) {
            Lov lov = lovRepository.findOne(lovId);
            if (lov != null && lov.getValues().length > 0) {
                for (int i = 0; i < lov.getValues().length; i++) {
                    String name = lov.getValues()[i];
                    PQMPPAPChecklist existFolderName = null;
                    PQMPPAPChecklist pqmppapChecklist = new PQMPPAPChecklist();
                    pqmppapChecklist.setName(name);
                    existFolderName = ppapChecklistRepository.findByPpapAndNameAndParentFileIsNullAndLatestTrue(pqmppap.getId(), name);
                    if (existFolderName == null) {
                        String folderNumber = null;
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default Folder Number Source");
                        if (autoNumber != null) {
                            folderNumber = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                        pqmppapChecklist.setVersion(1);
                        pqmppapChecklist.setSize(0L);
                        pqmppapChecklist.setPpap(pqmppap.getId());
                        pqmppapChecklist.setFileNo(folderNumber);
                        pqmppapChecklist.setFileType("FOLDER");
                        pqmppapChecklist.setRevision(setRevision());
                        pqmppapChecklist.setLifeCyclePhase(setLifecyclePhase());
                        pqmppapChecklist = ppapChecklistRepository.save(pqmppapChecklist);
                    }

                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + pqmppap.getId();
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + pqmppapChecklist.getId();
                    fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                }
            }
        }
    }

    public String setRevision() {
        String[] values = new String[0];
        String revision = "";
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_DOCUMENT_REVISION_SEQUENCE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer typeId = jsonNode.get("typeId").asInt();
                    if (typeId != null) {
                        Lov lov = lovRepository.findOne(typeId);
                        if (lov != null) {
                            values = lov.getValues();
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        if (values.length == 0) {
            values = PDMConstants.RevisionSequence.stream().toArray(String[]::new);
        }
        if (values.length > 0) {
            revision = values[0];
        }
        return revision;
    }

    public PLMLifeCyclePhase setLifecyclePhase() {
        PLMLifeCyclePhase lifeCyclePhase = null;
        Preference pref = preferenceRepository.findByPreferenceKey("DEFAULT_PPAP_CHECKLIST_LIFECYCLE");
        if (pref != null) {
            String json = pref.getJsonValue();
            if (json != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode jsonNode = objectMapper.readTree(json);
                    Integer typeId = jsonNode.get("typeId").asInt();
                    if (typeId != null) {
                        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(typeId);
                        if (lifeCycle != null) {
                            List<PLMLifeCyclePhase> lifeCyclePhases = lifeCyclePhaseRepository.findByLifeCycleAndPhaseTypeOrderByIdAsc(lifeCycle.getId(), LifeCyclePhaseType.PRELIMINARY);
                            if (lifeCyclePhases.size() > 0) {
                                lifeCyclePhase = lifeCyclePhases.get(0);
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        return lifeCyclePhase;
    }


    @Transactional
    @PreAuthorize("hasPermission(#ppap.id,'edit')")
    public PQMPPAP update(PQMPPAP ppap) {
        PQMPPAP oldPpap = JsonUtils.cloneEntity(ppapRepository.findOne(ppap.getId()), PQMPPAP.class);
        applicationEventPublisher.publishEvent(new PPAPEvents.PPAPBasicInfoUpdatedEvent(oldPpap, ppap));
        return ppapRepository.save(ppap);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id,'delete')")
    public void delete(Integer id) {
        ppapRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public List<PQMPPAP> getAll() {
        return ppapRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("@customePrivilegeFilter.filterDTO(authentication, filterObject,'view')")
    public Page<PQMPPAP> getAllPPAPsByPageable(Pageable pageable, PPAPCriteria criteria) {
        Predicate predicate = ppapPredicateBuilder.build(criteria, QPQMPPAP.pQMPPAP);
        Page<PQMPPAP> ppaps = ppapRepository.findAll(predicate, pageable);
        ppaps.forEach(ppap -> {
            ppap.setSupplierName(supplierRepository.findOne(ppap.getSupplier()).getName());
            ppap.setMfrPart(supplierPartRepository.findOne(ppap.getSupplierPart()).getManufacturerPart());
        });
        return ppaps;
    }

    @Transactional
    public PQMPPAPAttribute createPPAPAttribute(PQMPPAPAttribute attribute) {
        return ppapAttributeRepository.save(attribute);
    }

    @Transactional
    @PreAuthorize("hasPermission(#attribute.id.objectId,'edit',#attribute.id.attributeDef)")
    public PQMPPAPAttribute updatePPAPAttribute(PQMPPAPAttribute attribute) {
        PQMPPAPAttribute oldValue = ppapAttributeRepository.findByPpapAndAttribute(attribute.getId().getObjectId(), attribute.getId().getAttributeDef());
        oldValue = JsonUtils.cloneEntity(oldValue, PQMPPAPAttribute.class);
        attribute = ppapAttributeRepository.save(attribute);
        PQMPPAP pqmppap = ppapRepository.findOne(attribute.getId().getObjectId());
        applicationEventPublisher.publishEvent(new PPAPEvents.PPAPAttributesUpdatedEvent(pqmppap, oldValue, attribute));
        return attribute;
    }

    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject,'view')")
    public PQMPPAP getById(Integer id) {
        PQMPPAP ppap = ppapRepository.findOne(id);
        ppap.setSupplierName(supplierRepository.findOne(ppap.getSupplier()).getName());
        ppap.setMfrPart(supplierPartRepository.findOne(ppap.getSupplierPart()).getManufacturerPart());
        return ppap;
    }

    @Transactional(readOnly = true)
    public DetailsCount getPPAPTabCounts(Integer id) {
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setChecklistCount(ppapChecklistRepository.getFilesCountByPpapAndFileTypeAndLatestTrue(id, "FILE"));
        detailsCount.setChecklistCount(detailsCount.getChecklistCount() + objectDocumentRepository.getDocumentsCountByObjectId(id));
        return detailsCount;
    }


    @Transactional
    //@PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'promote','mfrsupplier')")
    public PQMPPAP promotePpap(Integer id, PQMPPAP oldPpap) {
        PQMPPAP ppap = ppapRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(ppap.getType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository
                .findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = ppap.getStatus().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream()
                .filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index++;
        setLifeCyclePhases(index, ppap, oldPpap, plmLifeCyclePhases);
        return ppap;
    }

    public void setLifeCyclePhases(Integer index, PQMPPAP newPpap, PQMPPAP oldPpap,
                                   List<PLMLifeCyclePhase> plmLifeCyclePhases) {
        if (index != -1) {
            PLMLifeCyclePhase oldStatus = oldPpap.getStatus();
            PLMLifeCyclePhase lifeCyclePhase = plmLifeCyclePhases.get(index);
            if (lifeCyclePhase != null) {
                newPpap.setStatus(lifeCyclePhase);
                newPpap = ppapRepository.save(newPpap);
                if (newPpap.getStatus().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
                    documentService.sendPpapApprovedNotification(newPpap);
                }
            }
        }
    }

    @Transactional
    // @PreAuthorize("@customePrivilegeFilter.filterPrivilage(authentication,'demote','mfrsupplier')")
    public PQMPPAP demotePpap(Integer id, PQMPPAP oldPpap) {
        PQMPPAP newPpap = ppapRepository.findOne(id);
        PLMLifeCycle lifeCycle = lifeCycleRepository.findOne(newPpap.getType().getLifecycle().getId());
        List<PLMLifeCyclePhase> plmLifeCyclePhases = lifeCyclePhaseRepository
                .findByLifeCycleOrderByIdAsc(lifeCycle.getId());
        Integer lifeCyclePhase1 = newPpap.getStatus().getId();
        PLMLifeCyclePhase lifeCyclePhase2 = plmLifeCyclePhases.stream()
                .filter(p -> p.getId().toString().equals(lifeCyclePhase1.toString())).findFirst().get();
        Integer index = plmLifeCyclePhases.indexOf(lifeCyclePhase2);
        index--;
        setLifeCyclePhases(index, newPpap, oldPpap, plmLifeCyclePhases);

        return newPpap;
    }

}
