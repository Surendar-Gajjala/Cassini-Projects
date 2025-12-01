package com.cassinisys.plm.service.rm;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.col.AttributeAttachment;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.col.AttributeAttachmentRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.col.AttributeAttachmentService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Utils;
import com.cassinisys.plm.filtering.GlossaryEntryCriteria;
import com.cassinisys.plm.filtering.GlossaryEntryPredicateBuilder;
import com.cassinisys.plm.model.rm.*;
import com.cassinisys.plm.repo.cm.ECORepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.rm.*;
import com.cassinisys.plm.repo.wf.PLMWorkflowDefinitionRepository;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by GSR on 19-06-2018.
 */
@Service
public class GlossaryEntryService implements CrudService<PLMGlossaryEntry, Integer> {

    @Autowired
    private GlossaryEntryRepository glossaryEntryRepository;

    @Autowired
    private GlossaryEntryItemRepository glossaryEntryItemRepository;

    @Autowired
    private GlossaryEntryHistoryRepository glossaryEntryHistoryRepository;

    @Autowired
    private GlossaryRepository glossaryRepository;

    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;

    @Autowired
    private GlossaryEntryPredicateBuilder glossaryEntryPredicateBuilder;

    @Autowired
    private GlossaryEntryDetailsRepository glossaryEntryDetailsRepository;

    @Autowired
    private GlossaryLanguagesRepository glossaryLanguagesRepository;

    @Autowired
    private GlossaryEntryEditRepository glossaryEntryEditRepository;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private AttributeAttachmentService attributeAttachmentService;

    @Autowired
    private AttributeAttachmentRepository attributeAttachmentRepository;

    @Autowired
    private MessageSource messageSource;
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
    private ECORepository ecoRepository;

    @Override
    @Transactional
    public PLMGlossaryEntry create(PLMGlossaryEntry entry) {
        entry.setVersion(0);
        entry.setLatest(true);
        entry = glossaryEntryRepository.save(entry);
        return entry;
    }

    @Transactional
    public PLMGlossaryEntry createEntryAndAddToGlossary(Integer glossaryId, PLMGlossaryEntry entry) {
        if (entry.getDefaultGlossaryEntryDetail() != null) {
            PLMGlossaryEntry existEntry = glossaryEntryRepository.findByLatestTrueAndName(entry.getDefaultGlossaryEntryDetail().getName());
            if (existEntry == null) {
                List<PLMGlossaryEntryDetails> glossaryEntryDetails = entry.getGlossaryEntryDetails();
                entry.setDefaultDetail(null);
                entry.setVersion(0);
                entry.setLatest(true);
                entry = glossaryEntryRepository.save(entry);
                for (PLMGlossaryEntryDetails glossaryEntryDetail : glossaryEntryDetails) {
                    if (glossaryEntryDetail.getName() != null && !glossaryEntryDetail.getName().equals("") && glossaryEntryDetail.getDescription() != null && !glossaryEntryDetail.getDescription().equals("")) {
                        glossaryEntryDetail.setGlossaryEntry(entry.getId());
                        glossaryEntryDetailsRepository.save(glossaryEntryDetail);
                        PLMGlossaryEntryEdit glossaryEntryEdit = new PLMGlossaryEntryEdit();
                        glossaryEntryEdit.setEntry(entry.getId());
                        glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                        glossaryEntryEdit.setEditedDescription(glossaryEntryDetail.getDescription());
                        glossaryEntryEdit.setLanguage(glossaryEntryDetail.getLanguage());
                        glossaryEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                        glossaryEntryEdit.setUpdatedDate(new Date());
                        glossaryEntryEdit.setEditVersion(1);
                        glossaryEntryEdit.setLatest(true);
                        glossaryEntryEdit.setEditedNotes(glossaryEntryDetail.getNotes());
                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                    }
                }
                PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
                PLMGlossaryEntryDetails defaultEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(entry.getId(), glossaryLanguage);
                entry.setName(defaultEntryDetail.getName());
                entry.setDescription(defaultEntryDetail.getDescription());
                entry.setDefaultDetail(defaultEntryDetail);
                entry = glossaryEntryRepository.save(entry);
                PLMGlossaryEntryItem glossaryEntryItem = new PLMGlossaryEntryItem();
                glossaryEntryItem.setEntry(entry);
                glossaryEntryItem.setGlossary(glossaryId);
                glossaryEntryItem = glossaryEntryItemRepository.save(glossaryEntryItem);

            } else {
                throw new CassiniException(messageSource.getMessage("entry_already_exist", null, "Entry already exist", LocaleContextHolder.getLocale()));
            }
        }
        return entry;
    }

    @Override
    @Transactional
    public PLMGlossaryEntry update(PLMGlossaryEntry glossaryEntry) {
        List<PLMGlossaryEntryDetails> glossaryEntryDetails = glossaryEntry.getGlossaryEntryDetails();
        for (PLMGlossaryEntryDetails entryDetail : glossaryEntryDetails) {
            if (entryDetail.getId() == null) {
                entryDetail.setGlossaryEntry(glossaryEntry.getId());
                entryDetail = glossaryEntryDetailsRepository.save(entryDetail);
            }
        }
        List<PLMGlossaryEntryDetails> existGlossaryEntryDetails = glossaryEntryDetailsRepository.findByGlossaryEntry(glossaryEntry.getId());
//        List<PLMGlossaryEntryDetails> glossaryEntryDetails = glossaryEntry.getGlossaryEntryDetails();
        Boolean descriptionChanged = false;
        for (PLMGlossaryEntryDetails entryDetail : existGlossaryEntryDetails) {
            for (PLMGlossaryEntryDetails plmGlossaryEntryDetail : glossaryEntryDetails) {
                if (entryDetail.getLanguage().getLanguage().equals(plmGlossaryEntryDetail.getLanguage().getLanguage())
                        && !entryDetail.getDescription().equals(plmGlossaryEntryDetail.getDescription())) {
                    descriptionChanged = true;
                }
            }
        }
        if (descriptionChanged) {
            glossaryEntry.setLatest(false);
            glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
            PLMGlossaryEntry newEntry = new PLMGlossaryEntry();
            newEntry.setLatest(true);
            newEntry.setVersion(glossaryEntry.getVersion() + 1);
            newEntry = glossaryEntryRepository.save(newEntry);
            for (PLMGlossaryEntryDetails glossaryEntryDetail : glossaryEntryDetails) {
                PLMGlossaryEntryDetails newEntryDetail = new PLMGlossaryEntryDetails();
                newEntryDetail.setName(glossaryEntryDetail.getName());
                newEntryDetail.setLanguage(glossaryEntryDetail.getLanguage());
                newEntryDetail.setDescription(glossaryEntryDetail.getDescription());
                newEntryDetail.setGlossaryEntry(newEntry.getId());
                newEntryDetail.setNotes(glossaryEntryDetail.getNotes());
                newEntryDetail = glossaryEntryDetailsRepository.save(newEntryDetail);
            }
            List<PLMGlossary> glossaries = glossaryRepository.findByLatestTrueAndIsReleasedFalse();
            if (glossaries.size() > 0) {
                for (PLMGlossary plmGlossary : glossaries) {
                    PLMGlossaryEntryItem glossaryEntryItem = glossaryEntryItemRepository.findByGlossaryAndEntry(plmGlossary.getId(), glossaryEntry);
                    if (glossaryEntryItem != null) {
                        glossaryEntryItem.setEntry(newEntry);
                        glossaryEntryItem = glossaryEntryItemRepository.save(glossaryEntryItem);
                    }
                }
            }
            PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
            PLMGlossaryEntryDetails defaultDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(newEntry.getId(), glossaryLanguage);
            PLMGlossaryEntryDetails oldDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage);
            newEntry.setDefaultDetail(defaultDetail);
            newEntry = glossaryEntryRepository.save(newEntry);
            glossaryEntry.setDefaultDetail(oldDetail);
            glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
            copyAttributes(glossaryEntry, newEntry);
        } else {
            for (PLMGlossaryEntryDetails glossaryEntryDetail : glossaryEntryDetails) {
                glossaryEntryDetail = glossaryEntryDetailsRepository.save(glossaryEntryDetail);
            }
        }
        return glossaryEntry;
    }

    @Transactional
    public PLMGlossaryEntry updateEntryEdit(Integer glossaryId, PLMGlossaryEntry glossaryEntry) {
        List<PLMGlossaryEntryDetails> glossaryEntryDetails = glossaryEntry.getGlossaryEntryDetails();
        PLMGlossary glossary = null;
        for (PLMGlossaryEntryDetails entryDetail : glossaryEntryDetails) {
            if (entryDetail.getId() == null) {
                if (entryDetail.getName() != null && !entryDetail.getName().equals("")) {
                    entryDetail.setGlossaryEntry(glossaryEntry.getId());
                    entryDetail = glossaryEntryDetailsRepository.save(entryDetail);
                    PLMGlossaryEntryEdit previousEditEntry = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), entryDetail.getLanguage());
                    if (previousEditEntry != null) {
                        previousEditEntry.setLatest(true);
                        previousEditEntry = glossaryEntryEditRepository.save(previousEditEntry);
                    }
                    PLMGlossaryEntryEdit glossaryEntryEdit = new PLMGlossaryEntryEdit();
                    glossaryEntryEdit.setEntry(glossaryEntry.getId());
                    glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                    glossaryEntryEdit.setEditedDescription(entryDetail.getDescription());
                    glossaryEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                    glossaryEntryEdit.setUpdatedDate(new Date());
                    glossaryEntryEdit.setLanguage(entryDetail.getLanguage());
                    glossaryEntryEdit.setEditedNotes(entryDetail.getNotes());
                    if (previousEditEntry != null) {
                        glossaryEntryEdit.setEditVersion(previousEditEntry.getEditVersion() + 1);
                    } else {
                        glossaryEntryEdit.setEditVersion(1);
                    }
                    glossaryEntryEdit.setLatest(true);
                    if (previousEditEntry != null && previousEditEntry.getEditedDescription() != null) {
                        if (!previousEditEntry.getEditedDescription().equals(entryDetail.getDescription())) {
                            glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                        }
                    } else {
                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                    }
                }
            }
        }
        List<PLMGlossaryEntryDetails> existGlossaryEntryDetails = glossaryEntryDetailsRepository.findByGlossaryEntry(glossaryEntry.getId());
        glossary = glossaryRepository.findOne(glossaryId);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findOne(glossary.getDefaultLanguage().getId());

        /*PLMGlossaryEntryDetails existGlossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage);

        PLMGlossaryEntryEdit latestEntryEdit = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), glossaryLanguage);
*/
        for (PLMGlossaryEntryDetails entryDetail : glossaryEntryDetails) {
            if (entryDetail.getName() != null && !entryDetail.getName().equals("")) {
                for (PLMGlossaryEntryDetails existEntryDetail : existGlossaryEntryDetails) {
                    if (existEntryDetail.getLanguage().getId().equals(entryDetail.getLanguage().getId())) {
                        if (existEntryDetail.getDescription() != null) {
                            if (!entryDetail.getDescription().equals(existEntryDetail.getDescription())) {
                                PLMGlossaryEntryEdit previousEditEntry = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), entryDetail.getLanguage());
                                if (previousEditEntry != null) {
                                    previousEditEntry.setLatest(false);
                                    previousEditEntry = glossaryEntryEditRepository.save(previousEditEntry);
                                }
                                PLMGlossaryEntryEdit glossaryEntryEdit = new PLMGlossaryEntryEdit();
                                glossaryEntryEdit.setEntry(glossaryEntry.getId());
                                glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                                glossaryEntryEdit.setEditedDescription(entryDetail.getDescription());
                                glossaryEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                                glossaryEntryEdit.setUpdatedDate(new Date());
                                glossaryEntryEdit.setLanguage(entryDetail.getLanguage());
                                glossaryEntryEdit.setEditedNotes(entryDetail.getNotes());
                                if (previousEditEntry != null) {
                                    glossaryEntryEdit.setEditVersion(previousEditEntry.getEditVersion() + 1);
                                } else {
                                    glossaryEntryEdit.setEditVersion(1);
                                }
                                glossaryEntryEdit.setLatest(true);
                                if (previousEditEntry != null && previousEditEntry.getEditedDescription() != null) {
                                    if (!previousEditEntry.getEditedDescription().equals(entryDetail.getDescription())) {
                                        glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                    }
                                } else {
                                    glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                }

                            }
                        } else {
                            PLMGlossaryEntryEdit previousEditEntry = glossaryEntryEditRepository.findByEntryAndLanguageAndLatestTrue(glossaryEntry.getId(), entryDetail.getLanguage());
                            PLMGlossaryEntryEdit glossaryEntryEdit = new PLMGlossaryEntryEdit();
                            glossaryEntryEdit.setEntry(glossaryEntry.getId());
                            glossaryEntryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
                            glossaryEntryEdit.setEditedDescription(entryDetail.getDescription());
                            glossaryEntryEdit.setStatus(GlossaryEntryEditStatus.NONE);
                            glossaryEntryEdit.setUpdatedDate(new Date());
                            glossaryEntryEdit.setLanguage(entryDetail.getLanguage());
                            glossaryEntryEdit.setEditedNotes(entryDetail.getNotes());
                            if (previousEditEntry != null) {
                                glossaryEntryEdit.setEditVersion(previousEditEntry.getEditVersion() + 1);
                            } else {
                                glossaryEntryEdit.setEditVersion(1);
                            }
                            glossaryEntryEdit.setLatest(true);
                            if (previousEditEntry != null && previousEditEntry.getEditedDescription() != null) {
                                if (!previousEditEntry.getEditedDescription().equals(entryDetail.getDescription())) {
                                    glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                    previousEditEntry.setLatest(false);
                                    previousEditEntry = glossaryEntryEditRepository.save(previousEditEntry);
                                }
                            } else {
                                if (previousEditEntry != null) {
                                    glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                    previousEditEntry.setLatest(false);
                                    previousEditEntry = glossaryEntryEditRepository.save(previousEditEntry);
                                } else {
                                    glossaryEntryEdit = glossaryEntryEditRepository.save(glossaryEntryEdit);
                                }
                            }
                        }
                    }
                }
            }
        }
        return glossaryEntry;
    }

/*	private void copyAttributes(PLMGlossaryEntry oldEntry, PLMGlossaryEntry newEntry) {
		List<ObjectAttribute> oldGlossaryAttributes = objectAttributeRepository.findByObjectId(oldEntry.getId());
		if (oldGlossaryAttributes.size() > 0) {
			for (ObjectAttribute attr : oldGlossaryAttributes) {
				ObjectAttribute objectAttribute1 = new ObjectAttribute();
				objectAttribute1.setId(new ObjectAttributeId(newEntry.getId(), attr.getId().getAttributeDef()));
				objectAttribute1.setStringValue(attr.getStringValue());
				objectAttribute1.setLongTextValue(attr.getLongTextValue());
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

    private void copyAttributes(PLMGlossaryEntry oldEntry, PLMGlossaryEntry newEntry) {
        List<ObjectAttribute> oldRequirementAttributes = objectAttributeRepository.findByObjectId(oldEntry.getId());
        if (oldRequirementAttributes.size() > 0) {
            for (ObjectAttribute objectAttribute : oldRequirementAttributes) {
                ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findOne(objectAttribute.getId().getAttributeDef());
                ObjectAttribute objectAttribute1 = (ObjectAttribute) Utils.cloneObject(objectAttribute, ObjectAttribute.class);
                objectAttribute1.setId(new ObjectAttributeId(newEntry.getId(), objectAttribute.getId().getAttributeDef()));
                if (objectAttribute1.getAttachmentValues().length > 0) {
                    List<AttributeAttachment> attachments = attributeAttachmentService.getMultipleAttributeAttachments(Arrays.asList(objectAttribute1.getAttachmentValues()));
                    List<Integer> integers = new ArrayList<>();
                    for (AttributeAttachment attachment : attachments) {
                        AttributeAttachment attachment1 = new AttributeAttachment();
                        attachment1.setObjectId(newEntry.getId());
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

    @Override
    @Transactional
    public void delete(Integer id) {
        PLMGlossaryEntry plmGlossaryEntry = glossaryEntryRepository.findOne(id);
        glossaryEntryRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PLMGlossaryEntry get(Integer id) {
        return glossaryEntryRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PLMGlossaryEntry> getAll() {
        return glossaryEntryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossaryEntry> findAll(Pageable pageable) {
        return glossaryEntryRepository.findAll(pageable);
    }

    @Transactional
    public PLMGlossaryEntryItem createGlossaryEntryItem(PLMGlossaryEntryItem entryItem) {
        return glossaryEntryItemRepository.save(entryItem);
    }

    @Transactional
    public PLMGlossaryEntryItem updateGlossaryEntryItem(PLMGlossaryEntryItem entryItem) {
        return glossaryEntryItemRepository.save(entryItem);
    }

    @Transactional(readOnly = true)
    public PLMGlossaryEntryItem getGlossaryEntryItem(Integer id) {
        return glossaryEntryItemRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryEntryItem> getAllGlossaryEntryItem() {
        return glossaryEntryItemRepository.findAll();
    }

    @Transactional
    public PLMGlossaryEntryHistory createGlossaryEntryHistory(PLMGlossaryEntryHistory entryHistory) {
        return glossaryEntryHistoryRepository.save(entryHistory);
    }

    @Transactional
    public PLMGlossaryEntryHistory updateGlossaryEntryHistory(PLMGlossaryEntryHistory entryHistory) {
        return glossaryEntryHistoryRepository.save(entryHistory);
    }

    @Transactional(readOnly = true)
    public PLMGlossaryEntryHistory getGlossaryEntryHistory(Integer id) {
        return glossaryEntryHistoryRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryEntryHistory> getAllGlossaryEntryHistory() {
        return glossaryEntryHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossaryEntry> getEntries(Integer glossaryId, Pageable pageable) {
        List<PLMGlossaryEntry> glossaryEntryList = new ArrayList<>();
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossaryId);
        Page<PLMGlossaryEntry> glossaryEntries = glossaryEntryRepository.findByLatestTrueOrderByCreatedDateAsc(pageable);

        /*if (glossaryEntries.getContent().size() > 0) {
            if (glossaryEntryItems.size() > 0) {
                for (PLMGlossaryEntry glossaryEntry : glossaryEntries.getContent()) {
                    Boolean entryExist = false;
                    for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
                        if (glossaryEntry.getName().equals(glossaryEntryItem.getEntry().getName())) {
                            entryExist = true;
                        }
                    }
                    if (!entryExist) {
                        glossaryEntryList.add(glossaryEntry);
                    }
                }

            } else {
                glossaryEntryList.addAll(glossaryEntries.getContent());
            }
        }*/
        Page<PLMGlossaryEntry> entries = new PageImpl<PLMGlossaryEntry>(glossaryEntryList, pageable, glossaryEntryList.size());
        return entries;
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossaryEntry> getEntriesByLanguage(Integer glossary, String language, Pageable pageable) {
        List<PLMGlossaryEntry> glossaryEntryList = new ArrayList<>();
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        List<PLMGlossaryEntryItem> glossaryEntryItems = glossaryEntryItemRepository.findByGlossary(glossary);
        for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
            glossaryEntryItem.getEntry().setDefaultDetail(glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntryItem.getEntry().getId(), glossaryLanguage));
        }
        Page<PLMGlossaryEntry> glossaryEntries = glossaryEntryRepository.findByLatestTrueOrderByCreatedDateAsc(pageable);
        for (PLMGlossaryEntry glossaryEntry : glossaryEntries) {
            glossaryEntry.setDefaultDetail(glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage));
        }
        if (glossaryEntries.getContent().size() > 0) {
            if (glossaryEntryItems.size() > 0) {
                for (PLMGlossaryEntry glossaryEntry : glossaryEntries.getContent()) {
                    Boolean entryExist = false;
                    for (PLMGlossaryEntryItem glossaryEntryItem : glossaryEntryItems) {
                        if (glossaryEntry.getDefaultDetail().getName().equals(glossaryEntryItem.getEntry().getDefaultDetail().getName())) {
                            entryExist = true;
                        }
                    }
                    if (!entryExist) {
                        glossaryEntryList.add(glossaryEntry);
                    }
                }

            } else {
                glossaryEntryList.addAll(glossaryEntries.getContent());
            }
        }
        Page<PLMGlossaryEntry> entries = new PageImpl<PLMGlossaryEntry>(glossaryEntryList, pageable, glossaryEntryList.size());
        return entries;
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryEntry> getEntryVersions(Integer entryId) {
        PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findOne(entryId);
//        List<PLMGlossaryEntry> glossaryEntries = glossaryEntryRepository.findByNameEqualsIgnoreCaseOrderByCreatedDateDesc(glossaryEntry.getName());
        List<PLMGlossaryEntry> glossaryEntries = new ArrayList<>();
        return glossaryEntries;
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryEntry> getEntryVersionsByLanguage(Integer entry, String language) {
        PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findOne(entry);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        List<PLMGlossaryEntry> glossaryEntries = new ArrayList<>();
        PLMGlossaryEntryDetails glossaryEntryDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage);
        List<PLMGlossaryEntryDetails> glossaryEntryDetails = glossaryEntryDetailsRepository.findByNameOrderById(glossaryEntryDetail.getName());
        for (PLMGlossaryEntryDetails entryDetail : glossaryEntryDetails) {
            PLMGlossaryEntry entry1 = glossaryEntryRepository.findOne(entryDetail.getGlossaryEntry());
            entry1.setDefaultDetail(entryDetail);
            entry1.getGlossaryEntryDetails().addAll(glossaryEntryDetailsRepository.findByGlossaryEntry(entryDetail.getGlossaryEntry()));
            glossaryEntries.add(entry1);
        }

        /*if (glossaryEntries.size() > 0) {
            Collections.sort(glossaryEntries, new Comparator<PLMGlossaryEntry>() {
                public int compare(final PLMGlossaryEntry object1, final PLMGlossaryEntry object2) {
                    return object1.getCreatedDate().compareTo(object2.getCreatedDate());
                }
            });
        }*/
        return glossaryEntries;
    }

    @Transactional(readOnly = true)
    public Page<PLMGlossaryEntry> freeTextSearch(Pageable pageable, GlossaryEntryCriteria criteria) {
        Predicate predicate = glossaryEntryPredicateBuilder.build(criteria, QPLMGlossaryEntry.pLMGlossaryEntry);
        return glossaryEntryRepository.findAll(predicate, pageable);
    }

    @Transactional(readOnly = true)
    public List<PLMGlossaryEntryEdit> getEntryEditsByLanguage(Integer entry, String language) {
        PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findOne(entry);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        List<PLMGlossaryEntryEdit> glossaryEntryEdits = new ArrayList<>();
        glossaryEntryEdits = glossaryEntryEditRepository.findByEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage.getId());
        return glossaryEntryEdits;
    }

    @Transactional
    public PLMGlossaryEntryEdit acceptEntryEditChange(Integer entryId, PLMGlossaryEntryEdit glossaryEntryEdit) {
        List<PLMGlossaryEntryEdit> glossaryEntryEdits = glossaryEntryEditRepository.findByEntryAndEditVersionOrderByUpdatedDateAsc(entryId, glossaryEntryEdit.getEditVersion());
        for (PLMGlossaryEntryEdit entryEdit : glossaryEntryEdits) {
            entryEdit.setStatus(GlossaryEntryEditStatus.ACCEPTED);
            entryEdit.setAcceptedDate(new Date());
            entryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
            entryEdit = glossaryEntryEditRepository.save(entryEdit);
        }
        glossaryEntryEdit = glossaryEntryEditRepository.findOne(glossaryEntryEdit.getId());
        return glossaryEntryEdit;
    }

    @Transactional
    public PLMGlossaryEntryEdit rejectEntryEditChange(Integer entryId, PLMGlossaryEntryEdit glossaryEntryEdit) {
        List<PLMGlossaryEntryEdit> glossaryEntryEdits = glossaryEntryEditRepository.findByEntryAndEditVersionOrderByUpdatedDateAsc(entryId, glossaryEntryEdit.getEditVersion());
        for (PLMGlossaryEntryEdit entryEdit : glossaryEntryEdits) {
            entryEdit.setStatus(GlossaryEntryEditStatus.REJECTED);
            entryEdit.setAcceptedDate(new Date());
            entryEdit.setPerson(sessionWrapper.getSession().getLogin().getPerson());
            entryEdit = glossaryEntryEditRepository.save(entryEdit);
        }
        glossaryEntryEdit = glossaryEntryEditRepository.findOne(glossaryEntryEdit.getId());
        return glossaryEntryEdit;
    }

    @Transactional
    public PLMGlossaryEntry approveEntryEdits(Integer glossaryId, PLMGlossaryEntry glossaryEntry) {
        PLMGlossary glossary = glossaryRepository.findOne(glossaryId);
        PLMGlossaryLanguages defaultLanguage = glossaryLanguagesRepository.findOne(glossary.getDefaultLanguage().getId());
        PLMGlossaryEntryEdit lastAcceptedEntryEdit = glossaryEntryEditRepository.findByEntryAndLanguageAndStatusOrderByAcceptedDateDesc(glossaryEntry.getId(), defaultLanguage, GlossaryEntryEditStatus.ACCEPTED).get(0);
        List<PLMGlossaryEntryEdit> glossaryEntryEdits = glossaryEntryEditRepository.findByEntryAndEditVersion(glossaryEntry.getId(), lastAcceptedEntryEdit.getEditVersion());
        glossaryEntry.setLatest(false);
        glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
        PLMGlossaryEntry newEntry = new PLMGlossaryEntry();
        newEntry.setLatest(true);
        newEntry.setVersion(glossaryEntry.getVersion() + 1);
        newEntry = glossaryEntryRepository.save(newEntry);
        List<PLMGlossaryEntryDetails> glossaryEntryDetails = glossaryEntryDetailsRepository.findByGlossaryEntry(glossaryEntry.getId());
        for (PLMGlossaryEntryEdit glossaryEntryEdit : glossaryEntryEdits) {
            for (PLMGlossaryEntryDetails glossaryEntryDetail : glossaryEntryDetails) {
                if (glossaryEntryDetail.getLanguage().getId().equals(glossaryEntryEdit.getLanguage().getId())) {
                    PLMGlossaryEntryDetails newEntryDetail = new PLMGlossaryEntryDetails();
                    newEntryDetail.setName(glossaryEntryDetail.getName());
                    newEntryDetail.setLanguage(glossaryEntryEdit.getLanguage());
                    newEntryDetail.setDescription(glossaryEntryEdit.getEditedDescription());
                    newEntryDetail.setGlossaryEntry(newEntry.getId());
                    newEntryDetail.setNotes(glossaryEntryEdit.getEditedNotes());
                    newEntryDetail = glossaryEntryDetailsRepository.save(newEntryDetail);
                }
            }
        }
        List<PLMGlossary> glossaries = glossaryRepository.findByLatestTrueAndIsReleasedFalse();
        if (glossaries.size() > 0) {
            for (PLMGlossary plmGlossary : glossaries) {
                PLMGlossaryEntryItem glossaryEntryItem = glossaryEntryItemRepository.findByGlossaryAndEntry(plmGlossary.getId(), glossaryEntry);
                if (glossaryEntryItem != null) {
                    glossaryEntryItem.setEntry(newEntry);
                    glossaryEntryItem = glossaryEntryItemRepository.save(glossaryEntryItem);
                }
            }
        }
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByDefaultLanguageTrue();
        PLMGlossaryEntryDetails defaultDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(newEntry.getId(), glossaryLanguage);
        PLMGlossaryEntryDetails oldDetail = glossaryEntryDetailsRepository.findByGlossaryEntryAndLanguage(glossaryEntry.getId(), glossaryLanguage);
        newEntry.setDefaultDetail(defaultDetail);
        newEntry.setName(defaultDetail.getName());
        newEntry.setDescription(defaultDetail.getDescription());
        newEntry.setDefaultName(defaultDetail.getName());
        newEntry.setDefaultDescription(defaultDetail.getDescription());
        newEntry = glossaryEntryRepository.save(newEntry);
        glossaryEntry.setDefaultDetail(oldDetail);
        glossaryEntry = glossaryEntryRepository.save(glossaryEntry);
        copyAttributes(glossaryEntry, newEntry);
        return glossaryEntry;
    }

    @Transactional(readOnly = true)
    public PLMGlossaryEntryEdit getLastAcceptedEntryEdit(Integer entryId, String language) {
        PLMGlossaryEntry glossaryEntry = glossaryEntryRepository.findOne(entryId);
        PLMGlossaryLanguages glossaryLanguage = glossaryLanguagesRepository.findByLanguage(language);
        PLMGlossaryEntryEdit lastAcceptedEntryEdit = null;
        List<PLMGlossaryEntryEdit> glossaryEntryEdits = glossaryEntryEditRepository.
                findByEntryAndLanguageAndStatusOrderByAcceptedDateDesc(glossaryEntry.getId(), glossaryLanguage, GlossaryEntryEditStatus.ACCEPTED);
        if ((glossaryEntryEdits.size() > 0)) {
            lastAcceptedEntryEdit = glossaryEntryEdits.get(0);
        }
        return lastAcceptedEntryEdit;
    }
}
