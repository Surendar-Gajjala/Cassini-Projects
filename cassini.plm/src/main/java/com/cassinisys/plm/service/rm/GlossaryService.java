package com.cassinisys.plm.service.rm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.Preference;
import com.cassinisys.platform.model.core.*;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.col.CommentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PreferenceRepository;
import com.cassinisys.platform.repo.core.AutoNumberRepository;
import com.cassinisys.platform.repo.core.LovRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.common.ForgeService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.filtering.GlossaryCriteria;
import com.cassinisys.plm.filtering.GlossaryEntryPredicateBuilder;
import com.cassinisys.plm.filtering.GlossaryPredicateBuilder;
import com.cassinisys.plm.model.dto.DetailsCount;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.*;
import com.cassinisys.plm.repo.pm.GlossaryDeliverableRepository;
import com.cassinisys.plm.repo.pm.PLMObjectDocumentRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.FileHelpers;
import com.cassinisys.plm.service.plm.ItemFileService;
import com.cassinisys.plm.service.plm.ItemServiceException;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by GSR on 19-06-2018.
 */
@Service
@Transactional
public class GlossaryService implements CrudService<PLMGlossary, Integer>, PageableService<PLMGlossary, Integer> {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Map fileMap = new HashMap();
    static SecureRandom rnd = new SecureRandom();
    @Autowired
    Configuration fmConfiguration;

    @Autowired
    private GlossaryRepository glossaryRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private GlossaryRevisionHistoryRepository glossaryRevisionHistoryRepository;

    @Autowired
    private GlossaryRevisionStatusHistoryRepository glossaryRevisionStatusHistoryRepository;

    @Autowired
    private LifeCyclePhaseRepository lifeCyclePhaseRepository;

    @Autowired
    private GlossaryPredicateBuilder predicateBuilder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private GlossaryFileRepository glossaryFileRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FileDownloadHistoryRepository fileDownloadHistoryRepository;

    @Autowired
    private GlossaryEntryItemRepository glossaryEntryItemRepository;

    @Autowired
    private LifeCycleRepository lifeCycleRepository;

    @Autowired
    private GlossaryEntryRepository glossaryEntryRepository;

    @Autowired
    private LovRepository lovRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private ExportService exportService;

    @Autowired
    private GlossaryDeliverableRepository glossaryDeliverableRepository;

    @Autowired
    private AutoNumberService autoNumberService;

    @Autowired
    private GlossaryLanguagesRepository glossaryLanguagesRepository;

    @Autowired
    private GlossaryDetailsRepository glossaryDetailsRepository;

    @Autowired
    private GlossaryEntryDetailsRepository glossaryEntryDetailsRepository;

    @Autowired
    private GlossaryEntryPredicateBuilder glossaryEntryPredicateBuilder;

    @Autowired
    private RecentlyVisitedRepository recentlyVisitedRepository;

    @Autowired
    private GlossaryEntryEditRepository glossaryEntryEditRepository;

    @Autowired
    private GlossaryEntryPermissionRepository glossaryEntryPermissionRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private AttributeAttachmentService attributeAttachmentService;

    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;

    @Autowired
    private AutoNumberRepository autoNumberRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private PLMWorkflowDefinitionRepository plmWorkflowDefinitionRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private ManufacturerPartRepository manufacturerPartRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ForgeService forgeService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileHelpers fileHelpers;

    @Autowired
    private ItemFileService itemFileService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;

    @Override
    public PLMGlossary create(PLMGlossary glossary) {
        List<PLMGlossaryDetails> glossaryDetails = glossary.getGlossaryDetails();
        PLMGlossary existGlossary = glossaryRepository.findByLatestTrueAndDefaultDetail(glossary.getDefaultGlossaryDetail().getName());
        if (existGlossary == null) {
            AutoNumber autoNumber = autoNumberService.getByName("Default Terminology Number Source");
            if (autoNumber != null) {
                glossary.setNumber(autoNumberService.getNextNumber(autoNumber.getId()));
            }
            glossary.setIsReleased(false);
            glossary.setRevision("A");
            PLMLifeCycle defaultTerminologyLifeCycle = lifeCycleRepository.findByName("Default Terminology Lifecycle");
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, defaultTerminologyLifeCycle.getId());
            glossary.setLifeCyclePhase(lifeCyclePhase);
            glossary.setLatest(true);
            glossary = glossaryRepository.save(glossary);
            for (PLMGlossaryDetails glossaryDetail : glossaryDetails) {
                glossaryDetail.setGlossary(glossary.getId());
                glossaryDetailsRepository.save(glossaryDetail);

            }
            PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
            PLMGlossaryDetails defaultDetails = glossaryDetailsRepository.findByGlossaryAndLanguage(glossary.getId(), glossaryLanguage);
            glossary.setDefaultDetail(defaultDetails);
            glossary.setName(defaultDetails.getName());
            glossary.setDescription(defaultDetails.getDescription());
            glossary = glossaryRepository.save(glossary);
            Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
            PLMGlossaryRevisionStatusHistory revisionStatusHistory = new PLMGlossaryRevisionStatusHistory();
            revisionStatusHistory.setGlossary(glossary.getId());
            revisionStatusHistory.setFromStatus(glossary.getLifeCyclePhase());
            revisionStatusHistory.setToStatus(glossary.getLifeCyclePhase());
            revisionStatusHistory.setRevision(glossary.getRevision());
            revisionStatusHistory.setTimeStamp(new Date());
            revisionStatusHistory.setUpdatedBy(personId);
            revisionStatusHistory = glossaryRevisionStatusHistoryRepository.save(revisionStatusHistory);
        } else {
            throw new CassiniException(glossary.getDefaultGlossaryDetail().getName() + " : " + messageSource.getMessage("glossary_name_already_exist", null, "Glossary name already exist", LocaleContextHolder.getLocale()));
        }
        return glossary;
    }

    @Override
    public PLMGlossary update(PLMGlossary glossary) {
        List<PLMGlossaryDetails> glossaryDetails = glossary.getGlossaryDetails();
        for (PLMGlossaryDetails glossaryDetail : glossaryDetails) {
            glossaryDetail.setGlossary(glossary.getId());
            glossaryDetail = glossaryDetailsRepository.save(glossaryDetail);
        }
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
        PLMGlossaryDetails defaultDetails = glossaryDetailsRepository.findByGlossaryAndLanguage(glossary.getId(), glossaryLanguage);
        glossary.setName(defaultDetails.getName());
        glossary.setDescription(defaultDetails.getDescription());
        glossary = glossaryRepository.save(glossary);
        return glossary;
    }

    @Override
    public void delete(Integer id) {
        List<RecentlyVisited> recentlyVisiteds = recentlyVisitedRepository.findByObjectId(id);
        for (RecentlyVisited recentlyVisited : recentlyVisiteds) {
            recentlyVisitedRepository.delete(recentlyVisited.getId());
        }
        PLMGlossary glossary = glossaryRepository.findOne(id);
        glossaryRepository.delete(id);
    }

    @Override
    public PLMGlossary get(Integer id) {
        PLMGlossary glossary = glossaryRepository.findOne(id);
        if (glossary != null) {
            GlossaryEntryPermission glossaryEntryPermission = glossaryEntryPermissionRepository.findByGlossaryAndGlossaryUser(glossary.getId(), sessionWrapper.getSession().getLogin().getPerson().getId());
            if (glossaryEntryPermission != null) {
                glossary.setGlossaryEntryPermission(glossaryEntryPermission);
            }
        }
        glossary.getGlossaryDetails().addAll(glossaryDetailsRepository.findByGlossaryOrderByLanguage(glossary.getId()));
        return glossary;
    }

    @Override
    public List<PLMGlossary> getAll() {
        return glossaryRepository.findAll();
    }

    public Page<PLMGlossary> findAll(Pageable pageable) {
        Page<PLMGlossary> glossaries = glossaryRepository.findByLatestTrueOrderByModifiedDateDesc(pageable);

        /*--------------  First checking All Glossaries have Default Details -------------*/
        for (PLMGlossary glossary : glossaries.getContent()) {
            if (glossary.getDefaultDetail() == null) {
                PLMGlossaryLanguages defaultLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
                AutoNumber autoNumber = autoNumberService.getByName("Default Terminology Number Source");
                if (autoNumber != null) {
                    String nextNumber = autoNumberService.getNextNumber(autoNumber.getId());
                    glossary.setNumber(nextNumber);
                }
                PLMGlossaryDetails glossaryDetails = new PLMGlossaryDetails();
                glossaryDetails.setLanguage(defaultLanguage);
                glossaryDetails.setName(glossary.getName());
                glossaryDetails.setDescription(glossary.getDescription());
                glossaryDetails.setGlossary(glossary.getId());
                glossaryDetails = glossaryDetailsRepository.save(glossaryDetails);
                glossary.setDefaultLanguage(defaultLanguage);
                glossary.setDefaultDetail(glossaryDetails);
                glossary = glossaryRepository.save(glossary);
                List<PLMGlossary> oldGlossaries = glossaryRepository.findByLatestFalseAndNameEqualsIgnoreCase(glossary.getName());
                for (PLMGlossary oldGlossary : oldGlossaries) {
                    PLMGlossaryDetails oldGlossaryDetails = new PLMGlossaryDetails();
                    oldGlossaryDetails.setLanguage(defaultLanguage);
                    oldGlossaryDetails.setName(oldGlossary.getName());
                    oldGlossaryDetails.setDescription(oldGlossary.getDescription());
                    oldGlossaryDetails.setGlossary(oldGlossary.getId());
                    oldGlossaryDetails = glossaryDetailsRepository.save(oldGlossaryDetails);
                    oldGlossary.setNumber(glossary.getNumber());
                    oldGlossary.setDefaultLanguage(defaultLanguage);
                    oldGlossary.setDefaultDetail(oldGlossaryDetails);
                    oldGlossary = glossaryRepository.save(oldGlossary);
                }
            }
        }


        /*-------------------- First Checking all Glossary Entries have Default Details -------------------------*/
        List<PLMGlossaryEntry> glossaryEntries = glossaryEntryRepository.getByLatestTrueOrderByCreatedDateAsc();
        for (PLMGlossaryEntry glossaryEntry : glossaryEntries) {
            if (glossaryEntry.getDefaultDetail() == null) {
                PLMGlossaryLanguages defaultLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
                PLMGlossaryEntryDetails existDetails = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), defaultLanguage);
                if (existDetails == null) {
                    PLMGlossaryEntryDetails glossaryEntryDetails = new PLMGlossaryEntryDetails();
                    glossaryEntryDetails.setName(glossaryEntry.getName());
                    glossaryEntryDetails.setDescription(glossaryEntry.getDescription());
                    glossaryEntryDetails.setLanguage(defaultLanguage);
                    glossaryEntryDetails.setGlossaryEntry(glossaryEntry.getId());
                    glossaryEntryDetails.setNotes(glossaryEntry.getNotes());
                    glossaryEntryDetails = glossaryEntryDetailsRepository.save(glossaryEntryDetails);
                    glossaryEntry.setDefaultDetail(glossaryEntryDetails);
                    glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
                    List<PLMGlossaryEntry> oldGlossaryEntries = glossaryEntryRepository.findByLatestFalseAndNameEqualsIgnoreCase(glossaryEntry.getName());
                    for (PLMGlossaryEntry oldGlossaryEntry : oldGlossaryEntries) {
                        PLMGlossaryEntryDetails oldGlossaryEntryDetails = new PLMGlossaryEntryDetails();
                        oldGlossaryEntryDetails.setName(oldGlossaryEntry.getName());
                        oldGlossaryEntryDetails.setDescription(oldGlossaryEntry.getDescription());
                        oldGlossaryEntryDetails.setLanguage(defaultLanguage);
                        oldGlossaryEntryDetails.setGlossaryEntry(oldGlossaryEntry.getId());
                        oldGlossaryEntryDetails.setNotes(oldGlossaryEntry.getNotes());
                        oldGlossaryEntryDetails = glossaryEntryDetailsRepository.save(oldGlossaryEntryDetails);
                        oldGlossaryEntry.setDefaultDetail(oldGlossaryEntryDetails);
                        oldGlossaryEntry = glossaryEntryRepository.save(oldGlossaryEntry);
                    }
                } else {
                    glossaryEntry.setDefaultDetail(existDetails);
                    glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
                }
            }
        }
        for (PLMGlossary glossary : glossaries) {
            glossary.setGlossaryDetails(glossaryDetailsRepository.findByGlossary(glossary.getId()));
        }
        return glossaries;
    }

    public PLMGlossary promoteGlossary(Integer glossaryId) {
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossaryId);
        PLMLifeCyclePhase presentStatus = plmGlossary.getLifeCyclePhase();
        if (plmGlossary.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.PRELIMINARY)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.REVIEW, plmGlossary.getLifeCyclePhase().getLifeCycle());
            plmGlossary.setLifeCyclePhase(lifeCyclePhase);
        } else if (plmGlossary.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.RELEASED, plmGlossary.getLifeCyclePhase().getLifeCycle());
            plmGlossary.setLifeCyclePhase(lifeCyclePhase);
            plmGlossary.setReleasedDate(new Date());
            plmGlossary.setIsReleased(true);
        }
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        plmGlossary.setModifiedBy(personId);
        plmGlossary.setModifiedDate(new Date());
        plmGlossary = glossaryRepository.save(plmGlossary);
        if (plmGlossary.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
            List<PLMGlossaryEntryItem> entryItems = glossaryEntryItemRepository.findByGlossary(plmGlossary.getId());
            if (entryItems.size() > 0) {
                for (PLMGlossaryEntryItem glossaryEntryItem : entryItems) {
                    PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findByLatestTrueAndName(glossaryEntryItem.getEntry().getDefaultDetail().getName());
                    if (!glossaryEntry.getId().equals(glossaryEntryItem.getEntry().getId())) {
                        glossaryEntryItem.setEntry(glossaryEntry);
                        glossaryEntryItem = glossaryEntryItemRepository.save(glossaryEntryItem);
                    }
                }
            }
        }
        PLMGlossaryRevisionStatusHistory revisionStatusHistory = new PLMGlossaryRevisionStatusHistory();
        revisionStatusHistory.setGlossary(plmGlossary.getId());
        revisionStatusHistory.setFromStatus(presentStatus);
        revisionStatusHistory.setToStatus(plmGlossary.getLifeCyclePhase());
        revisionStatusHistory.setRevision(plmGlossary.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = glossaryRevisionStatusHistoryRepository.save(revisionStatusHistory);
        return plmGlossary;
    }

    public PLMGlossary demoteGlossary(Integer glossaryId) {
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossaryId);
        PLMLifeCyclePhase presentStatus = plmGlossary.getLifeCyclePhase();
        if (plmGlossary.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.RELEASED)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.REVIEW, plmGlossary.getLifeCyclePhase().getLifeCycle());
            plmGlossary.setLifeCyclePhase(lifeCyclePhase);
            plmGlossary.setReleasedDate(null);
            plmGlossary.setIsReleased(false);

        } else if (plmGlossary.getLifeCyclePhase().getPhaseType().equals(LifeCyclePhaseType.REVIEW)) {
            PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, plmGlossary.getLifeCyclePhase().getLifeCycle());
            plmGlossary.setLifeCyclePhase(lifeCyclePhase);
        }
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        plmGlossary.setModifiedBy(personId);
        plmGlossary.setModifiedDate(new Date());
        plmGlossary = glossaryRepository.save(plmGlossary);
        PLMGlossaryRevisionStatusHistory revisionStatusHistory = new PLMGlossaryRevisionStatusHistory();
        revisionStatusHistory.setGlossary(plmGlossary.getId());
        revisionStatusHistory.setFromStatus(presentStatus);
        revisionStatusHistory.setToStatus(plmGlossary.getLifeCyclePhase());
        revisionStatusHistory.setRevision(plmGlossary.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = glossaryRevisionStatusHistoryRepository.save(revisionStatusHistory);
        return plmGlossary;
    }

    public PLMGlossary reviseGlossary(Integer glossary) {
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossary);
        if (plmGlossary != null) {
            return reviseNextGlossary(plmGlossary, null);
        } else {
            throw new ItemServiceException(messageSource.getMessage("revise_item_failed_item_not_found", null, Locale.getDefault()));
        }
    }

    private PLMGlossary reviseNextGlossary(PLMGlossary plmGlossary, String revision) {
        if (revision == null) {
            revision = getNextRevisionSequence(plmGlossary, revision);
        }
        if (revision != null) {
            PLMGlossary copy = createNextRevisionGlossary(plmGlossary, revision);
            plmGlossary.setLatest(false);
            glossaryRepository.save(plmGlossary);
            //Copy the related
            copyEntries(plmGlossary, copy);
            copyFolderStructure(plmGlossary, copy);
            copyAttributes(plmGlossary, copy);
            return copy;
        } else {
            throw new ItemServiceException(messageSource.getMessage("could_not_retrieve_next_revision_sequence",
                    null, "Could not retrieve next revision sequence", LocaleContextHolder.getLocale()));
        }
    }

    private String getNextRevisionSequence(PLMGlossary plmGlossary, String revision) {
        String nextRev = null;
        String lastRev = plmGlossary.getRevision();
        Lov lov = lovRepository.findByName("Default Revision Sequence");
        String[] values = lov.getValues();
        int lastIndex = -1;
        for (int i = 0; i < values.length; i++) {
            String rev = values[i];
            if (rev.equalsIgnoreCase(lastRev)) {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex != -1 && lastIndex < values.length) {
            nextRev = values[lastIndex + 1];
        }
        return nextRev;
    }

    private PLMGlossary createNextRevisionGlossary(PLMGlossary plmGlossary, String nextRev) {
        PLMGlossary copy = new PLMGlossary();
        PLMLifeCyclePhase lifeCyclePhase = lifeCyclePhaseRepository.findByPhaseTypeAndLifeCycle(LifeCyclePhaseType.PRELIMINARY, plmGlossary.getLifeCyclePhase().getLifeCycle());
        copy.setName(plmGlossary.getName());
        copy.setDescription(plmGlossary.getDescription());
        copy.setRevision(nextRev);
        copy.setLifeCyclePhase(lifeCyclePhase);
        copy.setLatest(true);
        copy.setIsReleased(false);
        copy.setNumber(plmGlossary.getNumber());
        copy = glossaryRepository.save(copy);
        List<PLMGlossaryDetails> glossaryDetails = glossaryDetailsRepository.findByGlossary(plmGlossary.getId());
        for (PLMGlossaryDetails glossaryDetail : glossaryDetails) {
            PLMGlossaryDetails copyGlossaryDetail = new PLMGlossaryDetails();
            copyGlossaryDetail.setName(glossaryDetail.getName());
            copyGlossaryDetail.setDescription(glossaryDetail.getDescription());
            copyGlossaryDetail.setLanguage(glossaryDetail.getLanguage());
            copyGlossaryDetail.setGlossary(copy.getId());
            copyGlossaryDetail = glossaryDetailsRepository.save(copyGlossaryDetail);
        }
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
        PLMGlossaryDetails defaultDetail = glossaryDetailsRepository.findByGlossaryAndLanguage(copy.getId(), glossaryLanguage);
        copy.setDefaultLanguage(glossaryLanguage);
        copy.setDefaultDetail(defaultDetail);
        copy = glossaryRepository.save(copy);
        Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
        PLMGlossaryRevisionStatusHistory revisionStatusHistory = new PLMGlossaryRevisionStatusHistory();
        revisionStatusHistory.setGlossary(copy.getId());
        revisionStatusHistory.setFromStatus(copy.getLifeCyclePhase());
        revisionStatusHistory.setToStatus(copy.getLifeCyclePhase());
        revisionStatusHistory.setRevision(copy.getRevision());
        revisionStatusHistory.setTimeStamp(new Date());
        revisionStatusHistory.setUpdatedBy(personId);
        revisionStatusHistory = glossaryRevisionStatusHistoryRepository.save(revisionStatusHistory);
        PLMGlossaryRevisionHistory glossaryRevisionHistory = new PLMGlossaryRevisionHistory();
        glossaryRevisionHistory.setGlossary(plmGlossary.getId());
        glossaryRevisionHistory.setFromRevision(plmGlossary.getRevision());
        glossaryRevisionHistory.setToRevision(copy.getRevision());
        glossaryRevisionHistory.setTimeStamp(new Date());
        glossaryRevisionHistory.setUpdatedBy(personId);
        glossaryRevisionHistory = glossaryRevisionHistoryRepository.save(glossaryRevisionHistory);
        return copy;
    }

    private void copyFiles(PLMGlossary oldGlossary, PLMGlossary newGlossary) {
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByGlossary(oldGlossary.getId());
        for (PLMGlossaryFile glossaryFile : glossaryFiles) {
            PLMGlossaryFile newGlossaryFile = null;
            File file = getFileByGlossary(oldGlossary.getId(), glossaryFile.getId());
            if (file != null) {
                newGlossaryFile = new PLMGlossaryFile();
                Login login = sessionWrapper.getSession().getLogin();
                newGlossaryFile.setName(glossaryFile.getName());
                newGlossaryFile.setFileNo(glossaryFile.getFileNo());
                newGlossaryFile.setCreatedBy(login.getPerson().getId());
                newGlossaryFile.setModifiedBy(login.getPerson().getId());
                newGlossaryFile.setGlossary(newGlossary.getId());
                newGlossaryFile.setVersion(glossaryFile.getVersion());
                newGlossaryFile.setSize(glossaryFile.getSize());
                newGlossaryFile.setLatest(glossaryFile.getLatest());
                newGlossaryFile.setFileType(glossaryFile.getFileType());
                newGlossaryFile.setParentFile(glossaryFile.getParentFile());
                newGlossaryFile = glossaryFileRepository.save(newGlossaryFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + newGlossary.getId();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + newGlossaryFile.getId();
                saveFileToDisk(file, path);
            }
        }
    }

    private void copyFolderStructure(PLMGlossary oldGlossary, PLMGlossary newGlossary) {
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByGlossaryAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(oldGlossary.getId());
        for (PLMGlossaryFile glossaryFile : glossaryFiles) {
            PLMGlossaryFile newGlossaryFile = null;
            File file = getGlossaryFile(oldGlossary.getId(), glossaryFile.getId(), "LATEST");
            if (file != null) {
                newGlossaryFile = new PLMGlossaryFile();
                Login login = sessionWrapper.getSession().getLogin();
                newGlossaryFile.setName(glossaryFile.getName());
                newGlossaryFile.setFileNo(glossaryFile.getFileNo());
                newGlossaryFile.setCreatedBy(login.getPerson().getId());
                newGlossaryFile.setModifiedBy(login.getPerson().getId());
                newGlossaryFile.setGlossary(newGlossary.getId());
                newGlossaryFile.setVersion(glossaryFile.getVersion());
                newGlossaryFile.setSize(glossaryFile.getSize());
                newGlossaryFile.setLatest(glossaryFile.getLatest());
                newGlossaryFile.setFileType(glossaryFile.getFileType());
                newGlossaryFile = glossaryFileRepository.save(newGlossaryFile);
                if (newGlossaryFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newGlossaryFile.getId();
                    saveFileToDisk(file, dir);
                    saveOldVersionItemFiles(oldGlossary, newGlossary, glossaryFile);
                }
            }
            saveGlossaryFileChildren(oldGlossary, newGlossary, glossaryFile, newGlossaryFile);

        }
    }

    @Transactional
    private void saveGlossaryFileChildren(PLMGlossary oldGlossary, PLMGlossary newGlossary, PLMGlossaryFile glossaryFile, PLMGlossaryFile plmGlossaryFile) {
        List<PLMGlossaryFile> childrenFiles = glossaryFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(glossaryFile.getId());
        for (PLMGlossaryFile childrenFile : childrenFiles) {
            PLMGlossaryFile newGlossaryFile = null;
            File file = getGlossaryFile(oldGlossary.getId(), childrenFile.getId(), "LATEST");
            if (file != null) {
                newGlossaryFile = new PLMGlossaryFile();
                Login login = sessionWrapper.getSession().getLogin();
                newGlossaryFile.setName(childrenFile.getName());
                newGlossaryFile.setFileNo(childrenFile.getFileNo());
                newGlossaryFile.setFileType(childrenFile.getFileType());
                newGlossaryFile.setCreatedBy(login.getPerson().getId());
                newGlossaryFile.setModifiedBy(login.getPerson().getId());
                newGlossaryFile.setGlossary(newGlossary.getId());
                newGlossaryFile.setVersion(childrenFile.getVersion());
                newGlossaryFile.setSize(childrenFile.getSize());
                newGlossaryFile.setLatest(childrenFile.getLatest());
                newGlossaryFile.setParentFile(plmGlossaryFile.getId());
                newGlossaryFile = glossaryFileRepository.save(newGlossaryFile);
                if (newGlossaryFile.getFileType().equals("FOLDER")) {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getParentFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                } else {
                    String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + getReplaceFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    dir = dir + File.separator + newGlossaryFile.getId();
                    saveFileToDisk(file, dir);
                    saveChildrenOldVersionItemFiles(oldGlossary, newGlossary, childrenFile, plmGlossaryFile);
                }
            }
            saveGlossaryFileChildren(oldGlossary, newGlossary, childrenFile, newGlossaryFile);
        }

    }

    private void saveOldVersionItemFiles(PLMGlossary oldGlossary, PLMGlossary newGlossary, PLMGlossaryFile glossaryFile) {
        List<PLMGlossaryFile> oldVersionFiles = glossaryFileRepository.findByGlossaryAndFileNoAndLatestFalseAndParentFileIsNullOrderByCreatedDateDesc(oldGlossary.getId(), glossaryFile.getFileNo());
        for (PLMGlossaryFile oldVersionFile : oldVersionFiles) {
            PLMGlossaryFile newGlossaryFile = null;
            File file = getGlossaryFile(oldGlossary.getId(), oldVersionFile.getId(), "COMMON");
            if (file != null) {
                newGlossaryFile = new PLMGlossaryFile();
                Login login = sessionWrapper.getSession().getLogin();
                newGlossaryFile.setName(oldVersionFile.getName());
                newGlossaryFile.setFileNo(oldVersionFile.getFileNo());
                newGlossaryFile.setFileType(oldVersionFile.getFileType());
                newGlossaryFile.setCreatedBy(login.getPerson().getId());
                newGlossaryFile.setModifiedBy(login.getPerson().getId());
                newGlossaryFile.setGlossary(newGlossary.getId());
                newGlossaryFile.setVersion(oldVersionFile.getVersion());
                newGlossaryFile.setSize(oldVersionFile.getSize());
                newGlossaryFile.setLatest(oldVersionFile.getLatest());
                newGlossaryFile = glossaryFileRepository.save(newGlossaryFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newGlossaryFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    private void saveChildrenOldVersionItemFiles(PLMGlossary oldGlossary, PLMGlossary newGlossary, PLMGlossaryFile glossaryFile, PLMGlossaryFile plmGlossaryFile) {
        List<PLMGlossaryFile> oldVersionFiles = glossaryFileRepository.findByGlossaryAndFileNoAndLatestFalseOrderByCreatedDateDesc(oldGlossary.getId(), glossaryFile.getFileNo());
        for (PLMGlossaryFile oldVersionFile : oldVersionFiles) {
            PLMGlossaryFile newGlossaryFile = null;
            File file = getGlossaryFile(oldGlossary.getId(), oldVersionFile.getId(), "COMMON");
            if (file != null) {
                newGlossaryFile = new PLMGlossaryFile();
                Login login = sessionWrapper.getSession().getLogin();
                newGlossaryFile.setName(oldVersionFile.getName());
                newGlossaryFile.setFileNo(oldVersionFile.getFileNo());
                newGlossaryFile.setFileType(oldVersionFile.getFileType());
                newGlossaryFile.setCreatedBy(login.getPerson().getId());
                newGlossaryFile.setModifiedBy(login.getPerson().getId());
                newGlossaryFile.setGlossary(newGlossary.getId());
                newGlossaryFile.setVersion(oldVersionFile.getVersion());
                newGlossaryFile.setSize(oldVersionFile.getSize());
                newGlossaryFile.setLatest(oldVersionFile.getLatest());
                newGlossaryFile.setParentFile(plmGlossaryFile.getId());
                newGlossaryFile = glossaryFileRepository.save(newGlossaryFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getReplaceFileSystemPath(newGlossary.getId(), newGlossaryFile.getId());
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                dir = dir + File.separator + newGlossaryFile.getId();
                saveFileToDisk(file, dir);
            }
        }
    }

    private void copyEntries(PLMGlossary oldGlossary, PLMGlossary newGlossary) {
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(oldGlossary.getId());
        if (glossaryEntryItems.size() > 0) {
            for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
                PLMGlossaryEntry entry = glossaryEntryRepository.findByLatestTrueAndName(glossaryEntryItem.getEntry().getDefaultDetail().getName());
                PLMGlossaryEntryItem newEntryItem = new PLMGlossaryEntryItem();
                newEntryItem.setEntry(entry);
                newEntryItem.setGlossary(newGlossary.getId());
                newEntryItem.setNotes(glossaryEntryItem.getNotes());
                newEntryItem = glossaryEntryItemRepository.save(newEntryItem);
            }
        }
    }

/*	private void copyAttributes(PLMGlossary oldGlossary, PLMGlossary newGlossary) {
        List<ObjectAttribute> oldGlossaryAttributes = objectAttributeRepository.findByObjectId(oldGlossary.getId());
		if (oldGlossaryAttributes.size() > 0) {
			for (ObjectAttribute attr : oldGlossaryAttributes) {
				ObjectAttribute objectAttribute1 = new ObjectAttribute();
				objectAttribute1.setId(new ObjectAttributeId(newGlossary.getId(), attr.getId().getAttributeDef()));
				objectAttribute1.setStringValue(attr.getStringValue());
				objectAttribute1.setLongTextValue(attr.getLongTextValue());
				objectAttribute1.setRichTextValue(attr.getRichTextValue());
				objectAttribute1.setIntegerValue(attr.getIntegerValue());
				objectAttribute1.setBooleanValue(attr.getBooleanValue());
				objectAttribute1.setDateValue(attr.getDateValue());
				objectAttribute1.setDoubleValue(attr.getDoubleValue());
				objectAttribute1.setListValue(attr.getListValue());
				objectAttribute1.setmListValue(attr.getmListValue());
				objectAttribute1.setTimeValue(attr.getTimeValue());
				objectAttribute1.setTimestampValue(attr.getTimestampValue());
				objectAttribute1.setImageValue(attr.getImageValue());
				objectAttribute1.setAttachmentValues(attr.getAttachmentValues());
				objectAttribute1.setRefValue(attr.getRefValue());
				objectAttribute1.setCurrencyType(attr.getCurrencyType());
				objectAttribute1.setCurrencyValue(attr.getCurrencyValue());

				objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
			}
		}
	}*/

    private void copyAttributes(PLMGlossary oldGlossary, PLMGlossary newGlossary) {
        List<ObjectAttribute> oldRequirementAttributes = objectAttributeRepository.findByObjectId(oldGlossary.getId());
        if (oldRequirementAttributes.size() > 0) {
            for (ObjectAttribute objectAttribute : oldRequirementAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
                ObjectAttribute objectAttribute1 = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                objectAttribute1.setId(new ObjectAttributeId(newGlossary.getId(), objectAttribute.getId().getAttributeDef()));
                if (objectAttribute1.getAttachmentValues().length > 0) {
                    List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute1.getAttachmentValues()));
                    List<Integer> integers = new ArrayList<>();
                    for (AttributeAttachment attachment : attachments) {
                        AttributeAttachment attachment1 = new AttributeAttachment();
                        attachment1.setObjectId(newGlossary.getId());
                        attachment1.setAttributeDef(attachment.getAttributeDef());
                        attachment1.setExtension(attachment.getExtension());
                        attachment1.setAddedBy(attachment.getAddedBy());
                        attachment1.setObjectType(attachment.getObjectType());
                        attachment1.setAddedOn(new Date());
                        attachment1.setName(attachment.getName());
                        attachment1.setSize(attachment.getSize());
                        attachment1 = attributeAttachmentRepository.save(attachment1);
                        integers.add(attachment1.getId());
                    }
                    objectAttribute1.setAttachmentValues(integers.stream().filter(Objects::nonNull).toArray(Integer[]::new));
                }
                objectAttribute1 = objectAttributeRepository.save(objectAttribute1);
            }
        }
    }

    private File getFileByGlossary(Integer glossaryId, Integer fileId) {
        checkNotNull(glossaryId);
        checkNotNull(fileId);
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        if (glossary == null) {
            throw new ResourceNotFoundException();
        }
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
        if (glossaryFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + glossaryId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    protected void saveFileToDisk(File fileToSave, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(new FileInputStream(fileToSave), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public PLMGlossaryRevisionHistory createGlossaryRevisionHistory(PLMGlossaryRevisionHistory glossaryRevisionHistory) {
        return glossaryRevisionHistoryRepository.save(glossaryRevisionHistory);
    }

    public PLMGlossaryRevisionHistory updateGlossaryRevisionHistory(PLMGlossaryRevisionHistory glossaryRevisionHistory) {
        return glossaryRevisionHistoryRepository.save(glossaryRevisionHistory);
    }

    public PLMGlossaryRevisionHistory getGlossaryRevisionHistory(Integer id) {
        return glossaryRevisionHistoryRepository.findOne(id);
    }

    public List<PLMGlossaryRevisionHistory> getAllGlossaryRevisionHistory() {
        return glossaryRevisionHistoryRepository.findAll();
    }

    public List<GlossaryRevisionHistoryDto> getGlossaryRevisionHistories(Integer glossaryId) {
        List<GlossaryRevisionHistoryDto> revisionHistoryDtos = new ArrayList<>();
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossaryId);
        List<PLMGlossary> plmGlossaryList = glossaryRepository.findByNumberOrderByCreatedDateDesc(plmGlossary.getNumber());
        for (PLMGlossary glossary : plmGlossaryList) {
            GlossaryRevisionHistoryDto revisionHistoryDto = new GlossaryRevisionHistoryDto();
            List<PLMGlossaryRevisionStatusHistory> revisionStatusHistories = glossaryRevisionStatusHistoryRepository.findByGlossaryOrderByTimeStampDesc(glossary.getId());
            if (revisionStatusHistories.size() > 0) {
                revisionHistoryDto.setPlmGlossary(glossary);
                revisionHistoryDto.setRevisionStatusHistories(revisionStatusHistories);
                revisionHistoryDtos.add(revisionHistoryDto);

            }
        }
        return revisionHistoryDtos;
    }

    public PLMGlossaryRevisionStatusHistory createGlossaryRevisionStatusHistory(PLMGlossaryRevisionStatusHistory statusHistory) {
        return glossaryRevisionStatusHistoryRepository.save(statusHistory);
    }

    public PLMGlossaryRevisionStatusHistory updateGlossaryRevisionStatusHistory(PLMGlossaryRevisionStatusHistory statusHistory) {
        return glossaryRevisionStatusHistoryRepository.save(statusHistory);
    }

    public PLMGlossaryRevisionStatusHistory getGlossaryRevisionStatusHistory(Integer id) {
        return glossaryRevisionStatusHistoryRepository.findOne(id);
    }

    public List<PLMGlossaryRevisionStatusHistory> getAllGlossaryRevisionStatusHistory() {
        return glossaryRevisionStatusHistoryRepository.findAll();
    }

    public Page<PLMGlossary> freeTextSearch(Pageable pageable, GlossaryCriteria criteria) {
        Predicate predicate = predicateBuilder.build(criteria, QPLMGlossary.pLMGlossary);
        return glossaryRepository.findAll(predicate, pageable);
    }

    public List<PLMGlossaryFile> uploadGlossaryFiles(Integer glossaryId, Integer folderId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMGlossaryFile> uploadedFiles = new ArrayList<>();
        PLMGlossaryFile glossaryFile = null;
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    if (folderId == 0) {
                        glossaryFile = glossaryFileRepository.findByGlossaryAndNameAndParentFileIsNullAndLatestTrue(glossaryId, name);
                    } else {
                        glossaryFile = glossaryFileRepository.findByParentFileAndNameAndLatestTrue(folderId, name);
                    }
                    Integer version = 1;
                    String autoNumber1 = null;
                    if (glossaryFile != null) {
                        glossaryFile.setLatest(false);
                        Integer oldVersion = glossaryFile.getVersion();
                        version = oldVersion + 1;
                        autoNumber1 = glossaryFile.getFileNo();
                        glossaryFileRepository.save(glossaryFile);

                    }
                    if (glossaryFile == null) {
                        AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                        if (autoNumber != null) {
                            autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                        }
                    }
                    glossaryFile = new PLMGlossaryFile();
                    glossaryFile.setName(name);
                    glossaryFile.setFileNo(autoNumber1);
                    glossaryFile.setCreatedBy(login.getPerson().getId());
                    glossaryFile.setModifiedBy(login.getPerson().getId());
                    glossaryFile.setVersion(version);
                    glossaryFile.setGlossary(glossaryId);
                    glossaryFile.setSize(file.getSize());
                    glossaryFile.setLatest(true);
                    glossaryFile.setFileType("FILE");
                    if (folderId != 0) {
                        glossaryFile.setParentFile(folderId);
                    }
                    glossaryFile = glossaryFileRepository.save(glossaryFile);
                    if (glossaryFile.getParentFile() != null) {
                        PLMGlossaryFile parent = glossaryFileRepository.findOne(glossaryFile.getParentFile());
                        parent.setModifiedDate(glossaryFile.getModifiedDate());
                        parent = glossaryFileRepository.save(parent);
                    }

                    String dir = "";
                    if (folderId == 0) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + glossaryId;
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + getParentFileSystemPath(glossaryId, folderId);
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + glossaryFile.getId();
                    saveDocumentToDisk(file, path);
                    uploadedFiles.add(glossaryFile);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploadedFiles;
    }

    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public List<PLMGlossaryFile> getGlossaryFiles(Integer glossary) {
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByGlossaryAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(glossary);
        glossaryFiles.forEach(glossaryFile -> {
            glossaryFile.setParentObject(PLMObjectType.GLOSSARY);
            if (glossaryFile.getFileType().equals("FOLDER")) {
                glossaryFile.setCount(glossaryFileRepository.getChildrenCountByParentFileAndLatestTrue(glossaryFile.getId()));
                glossaryFile.setCount(glossaryFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(glossaryFile.getGlossary(), glossaryFile.getId()));
            }
        });
        return glossaryFiles;
    }

    public void deleteGlossaryFile(Integer glossaryId, Integer fileId) {
        checkNotNull(fileId);
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
        String fNames = null;
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByGlossaryAndNameOrderByCreatedDateDesc(glossaryId, glossaryFile.getName());
        for (PLMGlossaryFile file : glossaryFiles) {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(glossaryId, fileId);
            fileSystemService.deleteDocumentFromDiskFolder(file.getId(), dir);
            if (fNames == null) {
                fNames = file.getName();

            } else {
                fNames = fNames + " , " + file.getName();
            }
            glossaryFileRepository.delete(file.getId());
        }
        if (glossaryFile.getParentFile() != null) {
            PLMGlossaryFile parent = glossaryFileRepository.findOne(glossaryFile.getParentFile());
            parent.setModifiedDate(new Date());
            parent = glossaryFileRepository.save(parent);
        }
    }

    public PLMGlossaryFile getFileById(Integer id) {
        checkNotNull(id);
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(id);
        if (glossaryFile == null) {
            throw new ResourceNotFoundException();
        }
        return glossaryFile;
    }

    public File getGlossaryFile(Integer glossaryId, Integer fileId, String type) {
        checkNotNull(glossaryId);
        checkNotNull(fileId);
        PLMGlossaryFile glossaryFile = null;
        if (type.equals("COMMON")) {
            glossaryFile = glossaryFileRepository.findByGlossaryAndId(glossaryId, fileId);
        } else {
            glossaryFile = glossaryFileRepository.findByGlossaryAndIdAndLatestTrue(glossaryId, fileId);
        }
        if (glossaryFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(glossaryId, fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    public PLMFileDownloadHistory fileDownloadHistory(Integer glossaryId, Integer fileId) {
        Person person = sessionWrapper.getSession().getLogin().getPerson();
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
        PLMFileDownloadHistory plmFileDownloadHistory = new PLMFileDownloadHistory();
        plmFileDownloadHistory.setFileId(fileId);
        plmFileDownloadHistory.setPerson(person);
        plmFileDownloadHistory.setDownloadDate(new Date());
        plmFileDownloadHistory = fileDownloadHistoryRepository.save(plmFileDownloadHistory);
        return plmFileDownloadHistory;
    }

    public List<PLMGlossaryFile> getGlossaryFileVersions(Integer glossaryId, Integer fileId) {
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findByGlossaryAndIdAndLatestTrue(glossaryId, fileId);
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByGlossaryAndName(glossaryId, glossaryFile.getName());
        return glossaryFiles;
    }

    public List<PLMGlossaryEntryItem> getGlossaryEntryItems(Integer glossaryId) {
        List<PLMGlossaryEntryItem> entryItems = glossaryEntryItemRepository.findByGlossary(glossaryId);
        for (PLMGlossaryEntryItem glossaryEntryItem : entryItems) {
            glossaryEntryItem.getEntry().setGlossaryEntryDetails(glossaryEntryDetailsRepository.findByGlossaryEntry(glossaryEntryItem.getEntry().getId()));
        }
        return entryItems;
    }

    public List<PLMGlossaryEntryItem> saveGlossaryEntryItems(Integer glossary, List<PLMGlossaryEntryItem> entryItems) {
        String entryNames = null;
        entryItems = glossaryEntryItemRepository.save(entryItems);
        for (PLMGlossaryEntryItem entryItem : entryItems) {
            if (entryNames == null) {
                entryNames = entryItem.getEntry().getName();
            } else {
                entryNames = entryNames + " , " + entryItem.getEntry().getName();
            }
        }
        return entryItems;
    }

    public void deleteGlossaryEntryItem(Integer glossaryId, Integer entryId) {
        glossaryEntryItemRepository.delete(entryId);
    }

    public ImportMessageDto importGlossaryEntryItems(Integer glossaryId, String importComment, Map<String, MultipartFile> fileMap) throws Exception {
        ImportMessageDto importMessageDto = new ImportMessageDto();
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        for (MultipartFile file1 : fileMap.values()) {
            if (file1.getOriginalFilename().trim().endsWith(".xlsx")) {
                Workbook workbook = WorkbookFactory.create(file1.getInputStream());
                Sheet worksheet = workbook.getSheetAt(0);
                // Reads the data in excel file until last row is encountered
                Row headerRow = worksheet.getRow(0);
                int rowColumns = headerRow.getLastCellNum();

                Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
                List<PLMGlossaryDetails> glossaryDetails = new ArrayList<>();
                glossaryDetails.addAll(glossaryDetailsRepository.findByGlossaryByDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
                glossaryDetails.addAll(glossaryDetailsRepository.findByGlossaryByNotDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));

                setGlossaryObject(rowColumns, glossaryDetails, worksheet, importMessageDto, glossary, headerRow, glossaryId, personId, importComment);

                workbook.close();
            } else if (file1.getOriginalFilename().trim().endsWith(".xls")) {
                HSSFWorkbook workbook = new HSSFWorkbook(file1.getInputStream());
                HSSFSheet worksheet = workbook.getSheetAt(0);
                HSSFRow headerRow = worksheet.getRow(0);
                int rowColumns = headerRow.getLastCellNum();

                Integer personId = sessionWrapper.getSession().getLogin().getPerson().getId();
                List<PLMGlossaryDetails> glossaryDetails = new ArrayList<>();
                glossaryDetails.addAll(glossaryDetailsRepository.findByGlossaryByDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
                glossaryDetails.addAll(glossaryDetailsRepository.findByGlossaryByNotDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));

                setGlossaryObject(rowColumns, glossaryDetails, worksheet, importMessageDto, glossary, headerRow, glossaryId, personId, importComment);
                workbook.close();

            }
            /*else {
                throw new CassiniException(messageSource.getMessage("upload_correct_excel_file", null, "Please upload correct excel sheet file", LocaleContextHolder.getLocale()));
			}*/
        }
        if (importMessageDto.getIgnoredRows().size() > 0) {
            importMessageDto.setIgnoredRowsMessage(null);
            for (Integer row : importMessageDto.getIgnoredRows()) {
                if (importMessageDto.getIgnoredRowsMessage() == null) {
                    importMessageDto.setIgnoredRowsMessage(row.toString());
                } else {
                    importMessageDto.setIgnoredRowsMessage(importMessageDto.getIgnoredRowsMessage() + "," + row);
                }
            }
        }
        return importMessageDto;
    }

    private void setGlossaryObject(int rowColumns, List<PLMGlossaryDetails> glossaryDetails, Sheet worksheet, ImportMessageDto importMessageDto, PLMGlossary glossary, Row headerRow, Integer glossaryId, Integer personId, String importComment) {
       /*-----------  Updating Entry Details with Description  ------------------------------*/
        if (rowColumns == (glossaryDetails.size() * 4)) {
            for (int i = 0; i <= worksheet.getLastRowNum(); i++) {
                if (i == 0) continue;
                Row entryDetailRow = worksheet.getRow(i);
                Cell firstDefaultNameCell = entryDetailRow.getCell(0);
                if (firstDefaultNameCell == null || firstDefaultNameCell.toString().equals("")) {
                    importMessageDto.getIgnoredRows().add(i);
                    continue;
                }
                importMessageDto.setExecutedRows(importMessageDto.getExecutedRows() + 1);
                PLMGlossaryLanguages glossaryDefaultLanguage = glossaryLanguagesRepository.findOne(glossary.getDefaultLanguage().getId());
                PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findByLatestTrueAndName(firstDefaultNameCell.toString());
                PLMGlossaryEntry newGlossaryEntry = new PLMGlossaryEntry();
                Boolean glossaryDefaultLanguageEntryEditExist = false;
                for (PLMGlossaryDetails detail : glossaryDetails) {
                    Boolean notDefaultLanguageNameExist = false;
                    PLMGlossaryEntryDetails newGlossaryEntryDetails = new PLMGlossaryEntryDetails();
                    Boolean glossaryEntryEditExist = false;
                    PLMGlossaryEntryEdit glossaryEntryEdit = new PLMGlossaryEntryEdit();
                    for (int j = 0; j < entryDetailRow.getLastCellNum(); j++) {
                        Cell headerNameCell = headerRow.getCell(j);
                        String[] headerNameSplit = headerNameCell.toString().split("_", 2);
                        PLMGlossaryLanguages headerNameLanguage = glossaryLanguagesRepository.findByLanguage(headerNameSplit[0]);
                        if (headerNameLanguage != null && detail.getLanguage().getLanguage().equals(headerNameLanguage.getLanguage())) {
                            if (headerNameSplit[1].equals("Name")) {
                                if (headerNameLanguage.getDefaultLanguage()) {
                                    if (glossaryEntry == null) {
                                        Cell nameCell = entryDetailRow.getCell(j);
                                        newGlossaryEntry.setName(nameCell.toString());
                                        newGlossaryEntry.setDefaultName(nameCell.toString());
                                        newGlossaryEntry.setVersion(0);
                                        newGlossaryEntry.setLatest(true);
                                        newGlossaryEntry.setCreatedDate(new Date());
                                        newGlossaryEntry.setCreatedBy(personId);
                                        newGlossaryEntry = glossaryEntryRepository.save(newGlossaryEntry);

                                                /*--  Adding Entry to Glossary Item ----*/
                                        PLMGlossaryEntryItem glossaryEntryItem = new PLMGlossaryEntryItem();
                                        glossaryEntryItem.setEntry(newGlossaryEntry);
                                        glossaryEntryItem.setGlossary(glossary.getId());
                                        glossaryEntryItem = glossaryEntryItemRepository.save(glossaryEntryItem);

                                                /*---- Adding details to GlossaryEntryDetails*/
                                        newGlossaryEntryDetails.setName(nameCell.toString());
                                        newGlossaryEntryDetails.setGlossaryEntry(newGlossaryEntry.getId());
                                        newGlossaryEntryDetails.setLanguage(headerNameLanguage);
                                        if (importComment != null && !importComment.equals("")) {
                                            newGlossaryEntryDetails.setNotes(importComment);
                                        } else {
                                            newGlossaryEntryDetails.setNotes("");
                                        }
                                        newGlossaryEntryDetails = glossaryEntryDetailsRepository.save(newGlossaryEntryDetails);
                                        newGlossaryEntry.setDefaultDetail(newGlossaryEntryDetails);
                                        newGlossaryEntry = glossaryEntryRepository.save(newGlossaryEntry);

                                    } else {
                                        if (headerNameLanguage.getDefaultLanguage()) {
                                            PLMGlossaryEntryItem entryItem = glossaryEntryItemRepository.findByGlossaryAndEntry(glossaryId, glossaryEntry);
                                            if (entryItem == null) {
                                                PLMGlossaryEntryItem entryItem1 = new PLMGlossaryEntryItem();
                                                entryItem1.setEntry(glossaryEntry);
                                                entryItem1.setGlossary(glossaryId);
                                                entryItem1 = glossaryEntryItemRepository.save(entryItem1);
                                            }
                                        }
                                    }
                                } else {
                                    Cell nameCell = entryDetailRow.getCell(j);
                                    if (glossaryEntry == null) {
                                        if (nameCell == null || nameCell.toString().equals("")) {
                                            notDefaultLanguageNameExist = false;
                                        } else {
                                            notDefaultLanguageNameExist = true;
                                            newGlossaryEntryDetails = new PLMGlossaryEntryDetails();
                                            newGlossaryEntryDetails.setName(nameCell.toString());
                                            newGlossaryEntryDetails.setGlossaryEntry(newGlossaryEntry.getId());
                                            newGlossaryEntryDetails.setLanguage(headerNameLanguage);
                                            newGlossaryEntryDetails = glossaryEntryDetailsRepository.save(newGlossaryEntryDetails);
                                        }
                                    } else {
                                        if (nameCell == null || nameCell.toString().equals("")) {
                                            notDefaultLanguageNameExist = false;
                                        } else {
                                            notDefaultLanguageNameExist = true;
                                        }
                                        if (notDefaultLanguageNameExist) {
                                            PLMGlossaryEntryDetails glossaryEntryDetails = glossaryEntryDetailsRepository.
                                                    findByGlossaryEntryAndLanguageAndNameEqualsIgnoreCase(glossaryEntry.getId(), headerNameLanguage, nameCell.toString());
                                            if (glossaryEntryDetails == null) {
                                                newGlossaryEntryDetails = new PLMGlossaryEntryDetails();
                                                newGlossaryEntryDetails.setName(nameCell.toString());
                                                newGlossaryEntryDetails.setGlossaryEntry(glossaryEntry.getId());
                                                newGlossaryEntryDetails.setLanguage(headerNameLanguage);
                                                newGlossaryEntryDetails = glossaryEntryDetailsRepository.save(newGlossaryEntryDetails);
                                            }
                                        }
                                    }
                                }
                            } else if (headerNameSplit[1].equals("Description")) {
                                Cell descriptionCell = entryDetailRow.getCell(j);
                                if (descriptionCell == null || descriptionCell.toString().equals("")) {
                                } else {
                                    if (headerNameLanguage.getDefaultLanguage()) {
                                        if (glossaryEntry == null) {
                                            newGlossaryEntry.setDescription(descriptionCell.toString());
                                            newGlossaryEntry.setDefaultDescription(descriptionCell.toString());
                                            newGlossaryEntry = glossaryEntryRepository.save(newGlossaryEntry);
                                            newGlossaryEntryDetails.setDescription(descriptionCell.toString());
                                            newGlossaryEntryDetails = glossaryEntryDetailsRepository.save(newGlossaryEntryDetails);
                                            PLMGlossaryEntryEdit newEntryEdit = new PLMGlossaryEntryEdit();
                                            newEntryEdit.setEditedDescription(descriptionCell.toString());
                                            newEntryEdit.setLanguage(headerNameLanguage);
                                            newEntryEdit.setEditVersion(0);
                                            newEntryEdit.setLatest(true);
                                            newEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                                            newEntryEdit.setUpdatedDate(new Date());
                                            newEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                                            newEntryEdit.setEntry(newGlossaryEntry.getId());
                                            if (importComment != null && !importComment.equals("")) {
                                                newEntryEdit.setEditedNotes(importComment);
                                            } else {
                                                newGlossaryEntryDetails.setNotes("");
                                            }
                                            newEntryEdit = glossaryEntryEditRepository.save(newEntryEdit);
                                        } else {
                                            PLMGlossaryEntryDetails languageEntry = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), headerNameLanguage);
                                            if (languageEntry != null && languageEntry.getDescription() != null && !languageEntry.getDescription().equals(descriptionCell.toString())) {

                                                        /*------- Adding to Entry Edit Table With glossaryEntry  ------------*/
                                                PLMGlossaryEntryEdit latestEntryEdit = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), headerNameLanguage);
                                                glossaryEntryEdit.setEditedDescription(descriptionCell.toString());
                                                glossaryEntryEdit.setLanguage(headerNameLanguage);
                                                glossaryEntryEdit.setLatest(true);
                                                glossaryEntryEdit.setEntry(glossaryEntry.getId());
                                                glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                                                glossaryEntryEdit.setUpdatedDate(new Date());
                                                if (latestEntryEdit != null && latestEntryEdit.getEditedDescription() != null) {
                                                    if (!latestEntryEdit.getEditedDescription().equals(descriptionCell.toString())) {
                                                        latestEntryEdit.setLatest(false);
                                                        latestEntryEdit = glossaryEntryEditRepository.save(latestEntryEdit);
                                                        glossaryEntryEdit.setEditVersion(latestEntryEdit.getEditVersion() + 1);
                                                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        glossaryDefaultLanguageEntryEditExist = true;
                                                    }

                                                } else {
                                                    if (latestEntryEdit != null) {
                                                        glossaryEntryEdit.setEditVersion(latestEntryEdit.getEditVersion() + 1);
                                                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        glossaryDefaultLanguageEntryEditExist = true;
                                                    } else {
                                                        glossaryEntryEdit.setEditVersion(0);
                                                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        glossaryDefaultLanguageEntryEditExist = true;
                                                    }

                                                }
                                            } else {
                                                glossaryEntryEdit = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), headerNameLanguage);
                                                if (glossaryEntryEdit != null) {
                                                    glossaryEntryEditExist = true;
                                                } else {
                                                    glossaryEntryEditExist = false;
                                                }
                                            }
                                        }
                                    } else {
                                        if (glossaryEntry == null) {
                                            if (notDefaultLanguageNameExist) {
                                                newGlossaryEntryDetails.setDescription(descriptionCell.toString());
                                                newGlossaryEntryDetails = glossaryEntryDetailsRepository.save(newGlossaryEntryDetails);
                                                PLMGlossaryEntryEdit newEntryEdit = new PLMGlossaryEntryEdit();
                                                newEntryEdit.setEditedDescription(descriptionCell.toString());
                                                newEntryEdit.setLanguage(headerNameLanguage);
                                                newEntryEdit.setEditVersion(0);
                                                newEntryEdit.setLatest(true);
                                                newEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                                                newEntryEdit.setUpdatedDate(new Date());
                                                newEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                                                newEntryEdit.setEntry(newGlossaryEntry.getId());
                                                newEntryEdit = glossaryEntryEditRepository.save(newEntryEdit);

                                            }
                                        } else {
                                            if (notDefaultLanguageNameExist) {
                                                PLMGlossaryEntryEdit latestEntryEdit = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), headerNameLanguage);
                                                if (latestEntryEdit == null) {
                                                    PLMGlossaryEntryDetails glossaryEntryDetails = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), headerNameLanguage);
                                                    if (glossaryEntryDetails != null) {
                                                        glossaryEntryDetails.setDescription(descriptionCell.toString());
                                                        glossaryEntryDetails = glossaryEntryDetailsRepository.save(glossaryEntryDetails);
                                                    }
                                                }
                                                if (glossaryDefaultLanguageEntryEditExist) {
                                                    glossaryEntryEdit = new PLMGlossaryEntryEdit();
                                                    glossaryEntryEdit.setEditedDescription(descriptionCell.toString());
                                                    glossaryEntryEdit.setLanguage(headerNameLanguage);
                                                    glossaryEntryEdit.setLatest(true);
                                                    glossaryEntryEdit.setEntry(glossaryEntry.getId());
                                                    glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                                                    glossaryEntryEdit.setUpdatedDate(new Date());
                                                    if (latestEntryEdit != null && latestEntryEdit.getEditedDescription() != null) {
                                                        if (!latestEntryEdit.getEditedDescription().equals(descriptionCell.toString())) {
                                                            latestEntryEdit.setLatest(false);
                                                            latestEntryEdit = glossaryEntryEditRepository.save(latestEntryEdit);
                                                            glossaryEntryEdit.setEditVersion(latestEntryEdit.getEditVersion() + 1);
                                                            glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        }

                                                    } else {
                                                        if (latestEntryEdit != null) {
                                                            glossaryEntryEdit.setEditVersion(latestEntryEdit.getEditVersion() + 1);
                                                            glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        } else {
                                                            glossaryEntryEdit.setEditVersion(0);
                                                            glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (headerNameSplit[1].equals("Notes")) {
                                Cell notesCell = entryDetailRow.getCell(j);
                                if (notesCell == null || notesCell.toString().equals("")) {
                                } else {
                                    if (headerNameLanguage.getDefaultLanguage()) {
                                        if (glossaryDefaultLanguageEntryEditExist) {
                                            if (glossaryEntry != null) {
                                                glossaryEntryEdit.setEditedNotes(notesCell.toString());
                                                glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                            }
                                        }

                                    } else {
                                        if (notDefaultLanguageNameExist) {
                                            if (glossaryEntry != null) {
                                                glossaryEntryEdit.setEditedNotes(notesCell.toString());
                                                glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
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
            throw new CassiniException(messageSource.getMessage("glossary_headers_not_match", null, "Terminology language headers did not match", LocaleContextHolder.getLocale()));
        }
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String printGlossary(Integer glossary, HttpServletResponse response) {
        InputStream is = null;
        String fileId = null;
        PLMGlossary glossary1 = glossaryRepository.findOne(glossary);
        fileId = glossary1.getDefaultDetail().getLanguage() + "_" + glossary1.getRevision() + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileId + "\"");
            response.setContentType("text/html");
            List<PLMGlossaryEntryItem> entryItems = glossaryEntryItemRepository.findByGlossary(glossary);
            List<PLMGlossaryEntry> glossaryEntries = new ArrayList<>();
            for (PLMGlossaryEntryItem glossaryEntryItem : entryItems) {
                glossaryEntries.add(glossaryEntryItem.getEntry());
            }

            /*if (entryItems.size() > 0) {
                Collections.sort(entryItems, new Comparator<PLMGlossaryEntryItem>() {
                    public int compare(final PLMGlossaryEntryItem object1, final PLMGlossaryEntryItem object2) {
                        return object1.getEntry().getName().compareTo(object2.getEntry().getName());
                    }
                });
            } else {
                throw new CassiniException("");
            }*/
            String htmlResponse = getExportHtml(glossaryEntries, glossary, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileMap.put(fileId, is);
        return fileId;
    }

    public String printGlossaryByLanguage(Integer glossary, String language, HttpServletResponse response) {
        InputStream is = null;
        String fileName = null;
        PLMGlossary glossary1 = glossaryRepository.findOne(glossary);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        PLMGlossaryDetails glossaryDetail = glossaryDetailsRepository.findByGlossaryAndLanguage(glossary1.getId(), glossaryLanguage);
        fileName = glossaryDetail.getName() + "_" + glossary1.getRevision() + "_" + language + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("text/html");
            List<PLMGlossaryEntry> glossaryEntries = new ArrayList<>();
            List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossary);
            List<PLMGlossaryEntryItem> languageEntryItems = new ArrayList<>();
            for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
                PLMGlossaryEntryDetails glossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage);
                if (glossaryEntryDetail != null) {
                    PLMGlossaryEntry glossaryEntry = glossaryEntryItem.getEntry();
                    glossaryEntry.setDefaultDetail(glossaryEntryDetail);
                    glossaryEntryItem.setEntry(glossaryEntry);
                    glossaryEntries.add(glossaryEntryItem.getEntry());
                }
            }
            if (glossaryEntries.size() > 0) {
                Collections.sort(glossaryEntries, new Comparator<PLMGlossaryEntry>() {
                    public int compare(final PLMGlossaryEntry object1, final PLMGlossaryEntry object2) {
                        return object1.getDefaultDetail().getName().compareTo(object2.getDefaultDetail().getName());
                    }
                });
            }
            String htmlResponse = getExportHtml(glossaryEntries, glossary, response);
            response.setContentLength(htmlResponse.getBytes().length);
            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileMap.put(fileName, is);
        return fileName;
    }

    public String getExportHtml(List<PLMGlossaryEntry> entryItems, Integer glossary, HttpServletResponse response) {
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossary);
        PLMGlossaryLanguages glossaryLanguage = entryItems.get(0).getDefaultDetail().getLanguage();
        PLMGlossaryDetails glossaryDetail = glossaryDetailsRepository.findByGlossaryAndLanguage(plmGlossary.getId(), glossaryLanguage);
        Map<String, Object> model = new HashMap<>();
        model.put("glossaryItem", entryItems);
        model.put("glossary", glossaryDetail);
        String exportHtmlData = getContentFromTemplate(model);
        return exportHtmlData;

    }

    public String getContentFromTemplate(Map<String, Object> model) {
        StringBuffer content = new StringBuffer();
        try {
           /* String templatePath = "/glossary.html";*/
            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/rm");
            // fmConfiguration.setClassForTemplateLoading(this.getClass(), "email/");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate("glossary.html"), model));
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

    public List<PLMGlossaryFile> getGlossaryFileVersionsAndCommentsAndDownloads(Integer glossaryId, Integer fileId, ObjectType objectType) {
        List<PLMGlossaryFile> glossaryFiles = new ArrayList<>();
        PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
        List<Comment> comments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(plmGlossaryFile.getId(), objectType);
        List<PLMFileDownloadHistory> fileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(plmGlossaryFile.getId());
        if (comments.size() > 0) {
            plmGlossaryFile.setComments(comments);
        }
        if (fileDownloadHistories.size() > 0) {
            plmGlossaryFile.setDownloadHistories(fileDownloadHistories);
        }
        glossaryFiles.add(plmGlossaryFile);
        List<PLMGlossaryFile> files = glossaryFileRepository.findByGlossaryAndFileNoAndLatestFalseOrderByCreatedDateDesc(plmGlossaryFile.getGlossary(), plmGlossaryFile.getFileNo());
        if (files.size() > 0) {
            for (PLMGlossaryFile file : files) {
                List<Comment> oldVersionComments = commentRepository.findByObjectIdAndObjectTypeOrderByCommentedDateDesc(file.getId(), objectType);
                if (oldVersionComments.size() > 0) {
                    file.setComments(oldVersionComments);
                }
                List<PLMFileDownloadHistory> oldFileDownloadHistories = fileDownloadHistoryRepository.findByFileIdOrderByDownloadDateDesc(file.getId());
                if (oldFileDownloadHistories.size() > 0) {
                    file.setDownloadHistories(oldFileDownloadHistories);
                }
                glossaryFiles.add(file);
            }
        }
        return glossaryFiles;
    }

    public PLMGlossaryLanguages createGlossaryLanguage(PLMGlossaryLanguages glossaryLanguage) {
        glossaryLanguage = glossaryLanguagesRepository.save(glossaryLanguage);
        return glossaryLanguage;
    }

    public PLMGlossaryLanguages updateGlossaryLanguage(Integer languageId, PLMGlossaryLanguages glossaryLanguage) {
        glossaryLanguage = glossaryLanguagesRepository.save(glossaryLanguage);
        return glossaryLanguage;
    }

    public PLMGlossaryLanguages setGlossaryLanguage(Integer languageId, PLMGlossaryLanguages glossaryLanguage) {
        glossaryLanguage = glossaryLanguagesRepository.save(glossaryLanguage);
        List<PLMGlossaryLanguages> glossaryLanguages = glossaryLanguagesRepository.findAll();
        for (PLMGlossaryLanguages language : glossaryLanguages) {
            if (!glossaryLanguage.getId().equals(language.getId())) {
                language.setDefaultLanguage(false);
                language = glossaryLanguagesRepository.save(language);
            }
        }
        return glossaryLanguage;
    }

    public void deleteGlossaryLanguage(Integer languageId) {
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findOne(languageId);
        glossaryLanguagesRepository.delete(languageId);
    }

    public List<PLMGlossaryLanguages> getGlossaryLanguages() {
        return glossaryLanguagesRepository.findAllByOrderByCreatedDateAsc();
    }

    public PLMGlossaryDetails updateGlossaryDetail(Integer detailId, PLMGlossaryDetails plmGlossaryDetail) {
        plmGlossaryDetail = glossaryDetailsRepository.save(plmGlossaryDetail);
        return plmGlossaryDetail;
    }

    public GlossaryDto getGlossaryDetails(Integer glossaryId, String language) {
        GlossaryDto glossaryDto = new GlossaryDto();
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        glossary.setDefaultDetail(glossaryDetailsRepository.findByGlossaryAndLanguage(glossary.getId(), glossaryLanguage));
        glossary.getGlossaryDetails().addAll(glossaryDetailsRepository.findByGlossaryByDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
        glossary.getGlossaryDetails().addAll(glossaryDetailsRepository.findByGlossaryByNotDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
        glossaryDto.setGlossary(glossary);

        /*------------  Setting Default Name and Default Description to Glossary Entry*/
        List<PLMGlossaryEntry> glossaryEntries = glossaryEntryRepository.findByDefaultNameIsNullAndDefaultDescriptionIsNullAndLatestTrueOrderByCreatedDateDesc();
        if (glossaryEntries.size() > 0) {
            for (PLMGlossaryEntry glossaryEntry : glossaryEntries) {
                PLMGlossaryEntryDetails glossaryEntryDetails = glossaryEntryDetailsRepository.findOne(glossaryEntry.getDefaultDetail().getId());
                if (glossaryEntryDetails != null) {
                    glossaryEntry.setDefaultName(glossaryEntryDetails.getName());
                    glossaryEntry.setDefaultDescription(glossaryEntryDetails.getDescription());
                    glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
                }

            }
        }
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossary.getId());
        List<PLMGlossaryEntryItem> laguageEntryItems = new ArrayList<>();
        for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
            PLMGlossaryEntryDetails glossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage);
            if (glossaryEntryDetail != null) {
                PLMGlossaryEntry glossaryEntry = glossaryEntryItem.getEntry();
                glossaryEntry.setDefaultDetail(glossaryEntryDetail);
                glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
                glossaryEntryItem.setEntry(glossaryEntry);
                glossaryEntryItem.getEntry().getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntryAndDefaultLanguage(glossaryEntryItem.getEntry().getId(), glossary.getDefaultLanguage().getId()));
                glossaryEntryItem.getEntry().getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntryAndNotDefaultLanguage(glossaryEntryItem.getEntry().getId(), glossary.getDefaultLanguage().getId()));
                glossaryEntryItem.getEntry().getEntryEdits().addAll(glossaryEntryEditRepository.findByEntryAndLanguageAndStatus(glossaryEntry.getId(), glossaryLanguage, GlossaryEntryEditStatus.NONE));
                laguageEntryItems.add(glossaryEntryItem);
            }
        }
        glossaryDto.setEntryItems(laguageEntryItems);
        if (glossaryDto.getEntryItems().size() > 0) {
            Collections.sort(glossaryDto.getEntryItems(), new Comparator<PLMGlossaryEntryItem>() {
                public int compare(final PLMGlossaryEntryItem object1, final PLMGlossaryEntryItem object2) {
                    return object1.getEntry().getDefaultDetail().getName().compareTo(object2.getEntry().getDefaultDetail().getName());
                }
            });
        }
        return glossaryDto;
    }

    public GlossaryDto getGlossaryEntrySearchDetails(Integer glossaryId, String language, String searchTerm) {
        GlossaryDto glossaryDto = new GlossaryDto();
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        glossary.setDefaultDetail(glossaryDetailsRepository.findByGlossaryAndLanguage(glossary.getId(), glossaryLanguage));
        glossary.setGlossaryDetails(glossaryDetailsRepository.findByGlossaryOrderByLanguage(glossary.getId()));
        glossaryDto.setGlossary(glossary);
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossary.getId());
        for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
            glossaryEntryItem.getEntry().setDefaultDetail(glossaryEntryDetailsRepository.
                    findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage));
            glossaryEntryItem.getEntry().getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntryOrderByLanguage(glossaryEntryItem.getEntry().getId()));
        }
        List<PLMGlossaryEntryItem> searchEntryItems = new ArrayList<>();
        for (PLMGlossaryEntryItem entryItem : glossaryEntryItems) {
            if (entryItem.getEntry().getDefaultDetail().getName().contains(searchTerm) || entryItem.getEntry().getDefaultDetail().getDescription().contains(searchTerm)) {
                searchEntryItems.add(entryItem);
            }
        }
        glossaryDto.setEntryItems(searchEntryItems);
        if (glossaryDto.getEntryItems().size() > 0) {
            Collections.sort(glossaryDto.getEntryItems(), new Comparator<PLMGlossaryEntryItem>() {
                public int compare(final PLMGlossaryEntryItem object1, final PLMGlossaryEntryItem object2) {
                    return object1.getEntry().getDefaultDetail().getName().compareTo(object2.getEntry().getDefaultDetail().getName());
                }
            });
        }
        return glossaryDto;
    }

    public GlossaryDto getGlossaryEntrySearch(Integer glossaryId, String search, String language) {
        GlossaryDto glossaryDto = new GlossaryDto();
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        glossary.setDefaultDetail(glossaryDetailsRepository.findByGlossaryAndLanguage(glossary.getId(), glossaryLanguage));
        glossary.getGlossaryDetails().addAll(glossaryDetailsRepository.findByGlossaryByDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
        glossary.getGlossaryDetails().addAll(glossaryDetailsRepository.findByGlossaryByNotDefaultLanguage(glossary.getId(), glossary.getDefaultLanguage().getId()));
        glossaryDto.setGlossary(glossary);
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.getGlossaryEntrySearchItems(glossaryId, search);
        List<PLMGlossaryEntryItem> laguageEntryItems = new ArrayList<>();
        for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
            PLMGlossaryEntryDetails glossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage);
            if (glossaryEntryDetail != null) {
                PLMGlossaryEntry glossaryEntry = glossaryEntryItem.getEntry();
                glossaryEntry.setDefaultDetail(glossaryEntryDetail);
                glossaryEntryItem.setEntry(glossaryEntry);
                glossaryEntryItem.getEntry().getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntryAndDefaultLanguage(glossaryEntryItem.getEntry().getId(), glossary.getDefaultLanguage().getId()));
                glossaryEntryItem.getEntry().getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntryAndNotDefaultLanguage(glossaryEntryItem.getEntry().getId(), glossary.getDefaultLanguage().getId()));
                glossaryEntryItem.getEntry().getEntryEdits().addAll(glossaryEntryEditRepository.findByEntryAndLanguageAndStatus(glossaryEntry.getId(), glossaryLanguage, GlossaryEntryEditStatus.NONE));
                laguageEntryItems.add(glossaryEntryItem);
            }
        }
        if (laguageEntryItems.size() > 0) {
            glossaryDto.setEntryItems(laguageEntryItems);
        }
        if (glossaryDto.getEntryItems().size() > 0) {
            Collections.sort(glossaryDto.getEntryItems(), new Comparator<PLMGlossaryEntryItem>() {
                public int compare(final PLMGlossaryEntryItem object1, final PLMGlossaryEntryItem object2) {
                    return object1.getEntry().getDefaultDetail().getName().compareTo(object2.getEntry().getDefaultDetail().getName());
                }
            });
        }
        return glossaryDto;

    }


    /*------------------------------- Glossary Permission -------------------*/

    public List<Person> createGlossaryPersons(Integer glossaryId, List<Person> persons) {
        String personNames = null;
        for (Person person : persons) {
            GlossaryEntryPermission glossaryEntryPermission = new GlossaryEntryPermission();
            glossaryEntryPermission.setGlossary(glossaryId);
            glossaryEntryPermission.setGlossaryUser(person.getId());
            if (personNames == null) {
                personNames = person.getFullName();
            } else {
                personNames = personNames + " , " + person.getFullName();
            }
            glossaryEntryPermission = glossaryEntryPermissionRepository.save(glossaryEntryPermission);
        }
        PLMGlossary plmGlossary = glossaryRepository.findOne(glossaryId);
        return persons;
    }

    public List<GlossaryEntryPermission> getAllGlossaryPersons(Integer specId) {
        List<GlossaryEntryPermission> glossaryEntryPermission = glossaryEntryPermissionRepository.findByGlossary(specId);
        return glossaryEntryPermission;
    }

    public List<Login> getAllPersons(Integer glossaryId) {
        List<Login> specPersons = new ArrayList<>();
        List<GlossaryEntryPermission> GlossaryEntryPermissions = glossaryEntryPermissionRepository.findByGlossary(glossaryId);
        List<Login> logins = loginRepository.findByExternalFalse();
        if (logins.size() != 0) {
            for (Login login : logins) {
                Boolean exist = false;
                for (GlossaryEntryPermission glossaryEntryPermission : GlossaryEntryPermissions) {
                    if (login.getPerson().getId().equals(glossaryEntryPermission.getGlossaryUser())) {
                        exist = true;
                    }
                }
                if (!exist) {
                    specPersons.add(login);
                }
            }
        } else {
            specPersons.addAll(logins);
        }
        return specPersons;
    }

    public GlossaryEntryPermission createGlossaryPermission(GlossaryEntryPermission glossaryEntryPermission) {
        GlossaryEntryPermission entryPermission = glossaryEntryPermissionRepository.save(glossaryEntryPermission);
        PLMGlossary plmGlossary = glossaryRepository.findOne(entryPermission.getGlossary());
        return entryPermission;
    }

    public void deleteGlossaryPerson(Integer id) {
        glossaryEntryPermissionRepository.delete(id);
    }

    public List<ObjectTypeAttribute> getAllTypeAttributes(String objectType) {
        List<ObjectTypeAttribute> typeAttributes =
                objectTypeAttributeRepository.findByObjectType(ObjectType.valueOf(objectType));
        return typeAttributes;
    }

    public PLMGlossaryFile updateFileName(Integer id, String newFileName) throws IOException {
        PLMGlossaryFile file1 = glossaryFileRepository.findOne(id);
        file1.setLatest(false);
        PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.save(file1);
        PLMGlossaryFile glossaryFile = (PLMGlossaryFile) Utils.cloneObject(plmGlossaryFile, PLMGlossaryFile.class);
        if (glossaryFile != null) {
            glossaryFile.setId(null);
            glossaryFile.setName(newFileName);
            glossaryFile.setVersion(file1.getVersion() + 1);
            glossaryFile.setLatest(true);
            glossaryFile.setReplaceFileName(file1.getName() + " ReName to " + newFileName);
            glossaryFile = glossaryFileRepository.save(glossaryFile);
            if (glossaryFile.getParentFile() != null) {
                PLMGlossaryFile parent = glossaryFileRepository.findOne(glossaryFile.getParentFile());
                parent.setModifiedDate(glossaryFile.getModifiedDate());
                parent = glossaryFileRepository.save(parent);
            }
            String dir = "";
            if (glossaryFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(glossaryFile.getGlossary(), id);
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + id;
            }
            dir = dir + File.separator + glossaryFile.getId();
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            String oldFileDir = "";
            if (plmGlossaryFile.getParentFile() != null) {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmGlossaryFile.getGlossary(), id);
            } else {
                oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmGlossaryFile.getGlossary() + File.separator + id;
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            try {
                File infile = new File(oldFileDir);
                File outfile = new File(dir);
                instream = new FileInputStream(infile);
                outstream = new FileOutputStream(outfile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, length);
                }
                instream.close();
                outstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return glossaryFile;
    }

    public List<PLMGlossaryFile> replaceGlossaryFiles(Integer glossaryId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMGlossaryFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMGlossaryFile glossaryFile1 = null;
        String fNames = null;
        String name = null;
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        Login login = sessionWrapper.getSession().getLogin();
        Preference preference = preferenceRepository.findByPreferenceKey("APPLICATION.FILETYPE");
        String[] fileExtension = null;
        boolean flag = true;
        if (preference != null) {
            if (preference.getStringValue() != null) fileExtension = preference.getStringValue().split("\\r?\\n");
        }
        String fileNames = null;
        PLMGlossaryFile plmGlossaryFile = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                if (fileExtension != null) {
                    for (String ext : fileExtension) {
                        if (name.matches("(?i:.*\\." + ext + "(:|$).*)") || name.equals(ext)) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    PLMGlossaryFile glossaryFile = null;
                    plmGlossaryFile = glossaryFileRepository.findOne(fileId);
                    if (plmGlossaryFile != null && plmGlossaryFile.getParentFile() != null) {
                        glossaryFile = glossaryFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                    } else {
                        glossaryFile = glossaryFileRepository.findByGlossaryAndNameAndParentFileIsNullAndLatestTrue(glossaryId, name);
                    }
                    if (glossaryFile != null) {
                        comments = commentRepository.findAllByObjectId(glossaryFile.getId());
                    }
                    if (plmGlossaryFile != null) {
                        plmGlossaryFile.setLatest(false);
                        plmGlossaryFile = glossaryFileRepository.save(plmGlossaryFile);
                    }
                    if (glossaryFile != null) {
                        comments = commentRepository.findAllByObjectId(glossaryFile.getId());
                    }
                    glossaryFile = new PLMGlossaryFile();
                    glossaryFile.setName(name);
                    if (plmGlossaryFile != null && plmGlossaryFile.getParentFile() != null) {
                        glossaryFile.setParentFile(plmGlossaryFile.getParentFile());
                    }
                    if (plmGlossaryFile != null) {
                        glossaryFile.setFileNo(plmGlossaryFile.getFileNo());
                        glossaryFile.setVersion(plmGlossaryFile.getVersion() + 1);
                        glossaryFile.setReplaceFileName(plmGlossaryFile.getName() + " Replaced to " + name);
                    }
                    glossaryFile.setCreatedBy(login.getPerson().getId());
                    glossaryFile.setModifiedBy(login.getPerson().getId());
                    glossaryFile.setGlossary(glossaryId);
                    glossaryFile.setSize(file.getSize());
                    glossaryFile.setFileType("FILE");
                    glossaryFile = glossaryFileRepository.save(glossaryFile);
                    if (glossaryFile.getParentFile() != null) {
                        PLMGlossaryFile parent = glossaryFileRepository.findOne(glossaryFile.getParentFile());
                        parent.setModifiedDate(glossaryFile.getModifiedDate());
                        parent = glossaryFileRepository.save(parent);
                    }
                    String dir = "";
                    if (plmGlossaryFile != null && plmGlossaryFile.getParentFile() != null) {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + getReplaceFileSystemPath(glossaryId, fileId);
                    } else {
                        dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                                "filesystem" + File.separator + glossaryId;
                    }
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.mkdirs();
                    }
                    String path = dir + File.separator + glossaryFile.getId();
                    fileSystemService.saveDocumentToDisk(file, path);
                    uploaded.add(glossaryFile);
                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    private String getReplaceFileSystemPath(Integer projectId, Integer fileId) {
        String path = "";
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
        if (glossaryFile.getParentFile() != null) {
            path = utilService.visitParentFolder(projectId, glossaryFile.getParentFile(), path);
        } else {
            path = File.separator + projectId;
        }
        return path;
    }

    public PLMGlossaryFile createGlossaryFolder(Integer glossaryId, PLMGlossaryFile plmGlossaryFile) {
        plmGlossaryFile.setId(null);
        String folderNumber = null;
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        folderNumber = autoNumberService.createAutoNumberIfNotExist("Default Folder Number Source", "Folder Number Source", 5, 1, 1, "0", "FLD-", 1);
        plmGlossaryFile.setGlossary(glossaryId);
        plmGlossaryFile.setFileNo(folderNumber);
        plmGlossaryFile.setFileType("FOLDER");
        plmGlossaryFile = glossaryFileRepository.save(plmGlossaryFile);
        if (plmGlossaryFile.getParentFile() != null) {
            PLMGlossaryFile parent = glossaryFileRepository.findOne(plmGlossaryFile.getParentFile());
            parent.setModifiedDate(plmGlossaryFile.getModifiedDate());
            parent = glossaryFileRepository.save(parent);
        }
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(glossaryId, plmGlossaryFile.getId());
        File fDir = new File(dir);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }
        return plmGlossaryFile;
    }

    private String getParentFileSystemPath(Integer itemId, Integer fileId) {
        String path = "";
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (glossaryFile.getParentFile() != null) {
            path = utilService.visitParentFolder(itemId, glossaryFile.getParentFile(), path);
        } else {
            path = File.separator + itemId + File.separator + glossaryFile.getId();
        }
        return path;
    }

    public List<PLMGlossaryFile> uploadGlossaryFolderFiles(Integer glossaryId, Integer fileId, Map<String, MultipartFile> fileMap) throws CassiniException {
        List<PLMGlossaryFile> uploaded = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        Login login = sessionWrapper.getSession().getLogin();
        String fileNames = null;
        PLMGlossaryFile glossaryFile = null;
        String fNames = null;
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                glossaryFile = glossaryFileRepository.findByParentFileAndNameAndLatestTrue(fileId, name);
                if (glossaryFile != null) {
                    comments = commentRepository.findAllByObjectId(glossaryFile.getId());
                }
                Integer version = 1;
                String autoNumber1 = null;
                if (glossaryFile != null) {
                    glossaryFile.setLatest(false);
                    Integer oldVersion = glossaryFile.getVersion();
                    version = oldVersion + 1;
                    autoNumber1 = glossaryFile.getFileNo();
                    glossaryFileRepository.save(glossaryFile);
                }
                if (glossaryFile == null) {
                    AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                    if (autoNumber != null) {
                        autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                    }
                }
                glossaryFile = new PLMGlossaryFile();
                glossaryFile.setName(name);
                glossaryFile.setFileNo(autoNumber1);
                glossaryFile.setFileType("FILE");
                glossaryFile.setParentFile(fileId);
                /*itemFile.setReplaceFileName(name);*/
                glossaryFile.setCreatedBy(login.getPerson().getId());
                glossaryFile.setModifiedBy(login.getPerson().getId());
                glossaryFile.setGlossary(glossary.getId());
                glossaryFile.setVersion(version);
                glossaryFile.setSize(file.getSize());
                glossaryFile.setFileType("FILE");
                glossaryFile = glossaryFileRepository.save(glossaryFile);
                if (fileNames == null) {
                    fNames = glossaryFile.getName();
                    fileNames = glossaryFile.getName() + " - Version : " + glossaryFile.getVersion();
                } else {
                    fNames = fNames + " , " + glossaryFile.getName();
                    fileNames = fileNames + " , " + glossaryFile.getName() + " - Version : " + glossaryFile.getVersion();
                }
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + getParentFileSystemPath(glossaryId, fileId);
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + glossaryFile.getId();
                fileSystemService.saveDocumentToDisk(file, path);
                /*Map<String, String> map = forgeService.uploadForgeFile(file.getOriginalFilename(), path);
                if (map != null) {
                    glossaryFile.setUrn(map.get("urn"));
                    glossaryFile.setThumbnail(map.get("thumbnail"));
                    glossaryFile = glossaryFileRepository.save(glossaryFile);
                }*/
                uploaded.add(glossaryFile);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uploaded;
    }

    @Transactional
    public PLMFile moveGlossaryFileToFolder(Integer id, PLMGlossaryFile plmGlossaryFile) throws Exception {
        PLMGlossaryFile file = glossaryFileRepository.findOne(plmGlossaryFile.getId());
        PLMGlossaryFile existFile = (PLMGlossaryFile) Utils.cloneObject(file, PLMGlossaryFile.class);
        String oldFileDir = "";
        if (existFile.getParentFile() != null) {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + getParentFileSystemPath(existFile.getGlossary(), existFile.getId());
        } else {
            oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + File.separator + existFile.getGlossary() + File.separator + existFile.getId();
        }
        if (plmGlossaryFile.getParentFile() != null) {
            PLMGlossaryFile existItemFile = glossaryFileRepository.findByParentFileAndNameAndLatestTrue(plmGlossaryFile.getParentFile(), plmGlossaryFile.getName());
            PLMGlossaryFile folder = glossaryFileRepository.findOne(plmGlossaryFile.getParentFile());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist_folder", null, "{0} file already exist in {1} folder", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName(), folder.getName());
                throw new CassiniException(result);
            } else {
                plmGlossaryFile = glossaryFileRepository.save(plmGlossaryFile);
            }
        } else {
            PLMGlossaryFile existItemFile = glossaryFileRepository.findByGlossaryAndNameAndParentFileIsNullAndLatestTrue(plmGlossaryFile.getGlossary(), plmGlossaryFile.getName());
            PLMGlossary glossary = glossaryRepository.findOne(plmGlossaryFile.getGlossary());
            if (existItemFile != null) {
                String message = messageSource.getMessage("file_already_exist", null, "{0} file already exist", LocaleContextHolder.getLocale());
                String result = MessageFormat.format(message + ".", existItemFile.getName());
                throw new CassiniException(result);
            } else {
                plmGlossaryFile = glossaryFileRepository.save(plmGlossaryFile);
            }
        }
        if (plmGlossaryFile != null) {
            String dir = "";
            if (plmGlossaryFile.getParentFile() != null) {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getParentFileSystemPath(plmGlossaryFile.getGlossary(), plmGlossaryFile.getId());
            } else {
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + plmGlossaryFile.getGlossary() + File.separator + plmGlossaryFile.getId();
            }
            File fDir = new File(dir);
            if (!fDir.exists()) {
                fDir.createNewFile();
            }
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            try {
                File infile = new File(oldFileDir);
                File outfile = new File(dir);
                instream = new FileInputStream(infile);
                outstream = new FileOutputStream(outfile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = instream.read(buffer)) > 0) {
                    outstream.write(buffer, 0, length);
                }
                instream.close();
                outstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File e = new File(oldFileDir);
            System.gc();
            Thread.sleep(1000L);
            FileUtils.deleteQuietly(e);
            List<PLMGlossaryFile> oldVersionFiles = glossaryFileRepository.findByGlossaryAndFileNoAndLatestFalseOrderByCreatedDateDesc(existFile.getGlossary(), existFile.getFileNo());
            for (PLMGlossaryFile oldVersionFile : oldVersionFiles) {
                oldFileDir = "";
                if (oldVersionFile.getParentFile() != null) {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(oldVersionFile.getGlossary(), oldVersionFile.getId());
                } else {
                    oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + oldVersionFile.getGlossary() + File.separator + oldVersionFile.getId();
                }
                dir = "";
                dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + getReplaceFileSystemPath(plmGlossaryFile.getGlossary(), plmGlossaryFile.getId());
                dir = dir + File.separator + oldVersionFile.getId();
                oldVersionFile.setParentFile(plmGlossaryFile.getParentFile());
                oldVersionFile = glossaryFileRepository.save(oldVersionFile);
                fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.createNewFile();
                }
                instream = null;
                outstream = null;
                try {
                    File infile = new File(oldFileDir);
                    File outfile = new File(dir);
                    instream = new FileInputStream(infile);
                    outstream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = instream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, length);
                    }
                    instream.close();
                    outstream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e = new File(oldFileDir);
                System.gc();
                Thread.sleep(1000L);
                FileUtils.deleteQuietly(e);
            }
        }
        return plmGlossaryFile;
    }

    public List<PLMGlossaryFile> getGlossaryFolderChildren(Integer folderId) {
        List<PLMGlossaryFile> glossaryFiles = glossaryFileRepository.findByParentFileAndLatestTrueOrderByCreatedDateDesc(folderId);
        glossaryFiles.forEach(glossaryFile -> {
            glossaryFile.setParentObject(PLMObjectType.GLOSSARY);
            if (glossaryFile.getFileType().equals("FOLDER")) {
                glossaryFile.setCount(glossaryFileRepository.getChildrenCountByParentFileAndLatestTrue(glossaryFile.getId()));
                glossaryFile.setCount(glossaryFile.getCount() + objectDocumentRepository.getDocumentsCountByObjectIdAndFolder(glossaryFile.getGlossary(), glossaryFile.getId()));
            }
        });
        return glossaryFiles;
    }

    public void deleteFolder(Integer glossaryId, Integer folderId) {
        String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + getParentFileSystemPath(glossaryId, folderId);
        List<PLMGlossaryFile> projectFiles = glossaryFileRepository.findByParentFileOrderByCreatedDateDesc(folderId);
        utilService.removeFileIfExist((List) projectFiles, dir);
        File fDir = new File(dir);
        FileUtils.deleteQuietly(fDir);
        PLMFile file = fileRepository.findOne(folderId);
        if (file.getParentFile() != null) {
            PLMFile parent = fileRepository.findOne(file.getParentFile());
            parent.setModifiedDate(new Date());
            parent = fileRepository.save(parent);
        }
        glossaryFileRepository.delete(folderId);
    }

    public DetailsCount getGlossaryCounts(Integer glossaryId, String language) {
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        DetailsCount detailsCount = new DetailsCount();
        detailsCount.setFiles(glossaryFileRepository.findByGlossaryAndFileTypeAndLatestTrueOrderByModifiedDateDesc(glossaryId, "FILE").size());
        detailsCount.setFiles(detailsCount.getFiles() + objectDocumentRepository.getDocumentsCountByObjectId(glossaryId));
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossary.getId());
        List<PLMGlossaryEntryItem> languageEntryItems = new ArrayList<>();
        for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
            PLMGlossaryEntryDetails glossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage);
            if (glossaryEntryDetail != null) {
                PLMGlossaryEntry glossaryEntry = glossaryEntryItem.getEntry();
                glossaryEntryItem.getEntry().getEntryEdits().addAll(glossaryEntryEditRepository.findByEntryAndLanguageAndStatus(glossaryEntry.getId(), glossaryLanguage, GlossaryEntryEditStatus.NONE));
                languageEntryItems.add(glossaryEntryItem);
            }
        }
        detailsCount.setEntries(languageEntryItems.size());
        return detailsCount;
    }

    public void generateZipFile(Integer glossaryId, HttpServletResponse response) throws FileNotFoundException, IOException {
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        List<PLMGlossaryFile> plmGlossaryFiles = glossaryFileRepository.findByGlossaryAndLatestTrueAndParentFileIsNullOrderByModifiedDateDesc(glossaryId);
        ArrayList<String> fileList = new ArrayList<>();
        plmGlossaryFiles.forEach(plmGlossaryFile -> {
            File file = getGlossaryFile(glossaryId, plmGlossaryFile.getId(), "LATEST");
            fileList.add(file.getAbsolutePath());
        });
        String zipName = glossary.getName() + "_Files.zip";
        File zipBox = new File(zipName);
        if (zipBox.exists())
            zipBox.delete();
        fileHelpers.Zip(zipBox.getAbsolutePath(), fileList.toArray(new String[0]), "GLOSSARY",glossaryId);
        InputStream inputStream = new FileInputStream(zipBox.getPath());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        response.getOutputStream().write(org.apache.commons.io.IOUtils.toByteArray(inputStream));
        response.getOutputStream().flush();
        inputStream.close();
    }

    @Transactional
    public PLMGlossaryFile getLatestUploadedFile(Integer itemId, Integer fileId) {
        PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
        PLMGlossaryFile itemFile = glossaryFileRepository.findByGlossaryAndFileNoAndLatestTrue(plmGlossaryFile.getGlossary(), plmGlossaryFile.getFileNo());
        return itemFile;
    }

    @Transactional
    public PLMFile updateFile(Integer id, PLMGlossaryFile plmGlossaryFile) {
        PLMFile file = fileRepository.findOne(id);
        PLMGlossaryFile glossaryFile = glossaryFileRepository.findOne(file.getId());
        if (file != null) {
            file.setDescription(plmGlossaryFile.getDescription());
            file = fileRepository.save(file);
        }
        return file;
    }

    @Transactional
    public List<PLMGlossaryFile> pasteFromClipboard(Integer glossaryId, Integer fileId, List<PLMFile> files) {
        List<PLMGlossaryFile> fileList = new ArrayList<>();
        for (PLMFile file : files) {
            PLMGlossaryFile glossaryFile = new PLMGlossaryFile();
            PLMGlossaryFile existFile = null;
            if (fileId != 0) {
                glossaryFile.setParentFile(fileId);
                existFile = glossaryFileRepository.findByNameEqualsIgnoreCaseAndParentFileAndGlossaryAndLatestTrue(file.getName(), fileId, glossaryId);
            } else {
                existFile = glossaryFileRepository.findByGlossaryAndNameAndParentFileIsNullAndLatestTrue(glossaryId, file.getName());
            }
            if (existFile == null) {
                glossaryFile.setName(file.getName());
                glossaryFile.setDescription(file.getDescription());
                glossaryFile.setGlossary(glossaryId);
                glossaryFile.setVersion(1);
                glossaryFile.setSize(file.getSize());
                glossaryFile.setLatest(file.getLatest());
                String autoNumber1 = null;
                AutoNumber autoNumber = autoNumberRepository.findByName("Default File Number Source");
                if (autoNumber != null) {
                    autoNumber1 = autoNumberService.getNextNumber(autoNumber.getId());
                }
                glossaryFile.setFileNo(autoNumber1);
                glossaryFile.setFileType("FILE");
                glossaryFile = glossaryFileRepository.save(glossaryFile);
                glossaryFile.setParentObject(PLMObjectType.GLOSSARY);
                fileList.add(glossaryFile);
                String oldFileDir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + itemFileService.getCopyFilePath(file);
                String directory = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + glossaryId;
                File fDirectory = new File(directory);
                if (!fDirectory.exists()) {
                    fDirectory.mkdirs();
                }
                String dir = "";
                if (glossaryFile.getParentFile() != null) {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + getParentFileSystemPath(glossaryId, glossaryFile.getId());
                } else {
                    dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                            "filesystem" + File.separator + glossaryId + File.separator + glossaryFile.getId();
                }
                try {
                    File fDir = new File(dir);
                    if (!fDir.exists()) {
                        fDir.createNewFile();
                    }
                    FileInputStream instream = null;
                    FileOutputStream outstream = null;
                    File infile = new File(oldFileDir);
                    File outfile = new File(dir);
                    instream = new FileInputStream(infile);
                    outstream = new FileOutputStream(outfile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = instream.read(buffer)) > 0) {
                        outstream.write(buffer, 0, length);
                    }
                    instream.close();
                    outstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileList;
    }

    public void undoCopiedGlossaryFiles(Integer itemId, List<PLMGlossaryFile> glossaryFiles) {
        glossaryFiles.forEach(glossaryFile -> {
            String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                    "filesystem" + getParentFileSystemPath(itemId, glossaryFile.getId());
            fileSystemService.deleteDocumentFromDiskFolder(glossaryFile.getId(), dir);
            glossaryFileRepository.delete(glossaryFile.getId());
        });
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryFile> findByGlossaryAndFileName(Integer id, String name) {
        return glossaryFileRepository.findByGlossaryAndNameContainingIgnoreCaseAndLatestTrue(id, name);
    }
}
