package com.cassinisys.plm.controller.rm;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.filtering.GlossaryEntryCriteria;
import com.cassinisys.plm.model.rm.PLMGlossaryEntry;
import com.cassinisys.plm.model.rm.PLMGlossaryEntryEdit;
import com.cassinisys.plm.model.rm.PLMGlossaryEntryHistory;
import com.cassinisys.plm.model.rm.PLMGlossaryEntryItem;
import com.cassinisys.plm.service.rm.GlossaryEntryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by GSR on 19-06-2018.
 */
@RestController
@RequestMapping("/rm/glossaryEntries")
@Api(tags = "PLM.RM",description = "Requirement Related")
public class GlossaryEntryController extends BaseController {

    @Autowired
    private GlossaryEntryService glossaryEntryService;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PLMGlossaryEntry crete(@RequestBody PLMGlossaryEntry glossaryEntry) {
        return glossaryEntryService.create(glossaryEntry);
    }

    @RequestMapping(value = "/glossary/{glossaryId}", method = RequestMethod.POST)
    public PLMGlossaryEntry createEntryAndAddToGlossary(@PathVariable("glossaryId") Integer glossaryId, @RequestBody PLMGlossaryEntry glossaryEntry) {
        return glossaryEntryService.createEntryAndAddToGlossary(glossaryId, glossaryEntry);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PLMGlossaryEntry update(@PathVariable("id") Integer id,
                                   @RequestBody PLMGlossaryEntry glossaryEntry) {
        return glossaryEntryService.update(glossaryEntry);
    }

    @RequestMapping(value = "/{glossaryId}/edit/{entryId}", method = RequestMethod.PUT)
    public PLMGlossaryEntry updateEntryEdit(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("entryId") Integer entryId,
                                            @RequestBody PLMGlossaryEntry glossaryEntry) {
        return glossaryEntryService.updateEntryEdit(glossaryId, glossaryEntry);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        glossaryEntryService.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PLMGlossaryEntry get(@PathVariable("id") Integer id) {
        return glossaryEntryService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PLMGlossaryEntry> getAll() {
        return glossaryEntryService.getAll();
    }

    @RequestMapping(value = "/getAllGlossaryEntries", method = RequestMethod.GET)
    public Page<PLMGlossaryEntry> getAllGlossaryEntries(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return glossaryEntryService.findAll(pageable);
    }

    @RequestMapping(value = "/glossaryEntryItem", method = RequestMethod.POST)
    public PLMGlossaryEntryItem createGlossaryRevisionHistory(@RequestBody PLMGlossaryEntryItem revisionHistory) {
        return glossaryEntryService.createGlossaryEntryItem(revisionHistory);
    }

    @RequestMapping(value = "/glossaryEntryItem/{id}", method = RequestMethod.PUT)
    public PLMGlossaryEntryItem updateGlossaryRevisionHistory(@PathVariable("id") Integer id,
                                                              @RequestBody PLMGlossaryEntryItem revisionHistory) {
        return glossaryEntryService.updateGlossaryEntryItem(revisionHistory);
    }

    @RequestMapping(value = "/glossaryEntryItem/{id}", method = RequestMethod.GET)
    public PLMGlossaryEntryItem getGlossaryRevisionHistory(@PathVariable("id") Integer id) {
        return glossaryEntryService.getGlossaryEntryItem(id);
    }

    @RequestMapping(value = "/glossaryEntryItem/all", method = RequestMethod.GET)
    public List<PLMGlossaryEntryItem> getAllGlossaryRevisionHistory() {
        return glossaryEntryService.getAllGlossaryEntryItem();
    }

    @RequestMapping(value = "/glossaryEntryHistory", method = RequestMethod.POST)
    public PLMGlossaryEntryHistory createGlossaryRevisionStatusHistory(@RequestBody PLMGlossaryEntryHistory revisionStatusHistory) {
        return glossaryEntryService.createGlossaryEntryHistory(revisionStatusHistory);
    }

    @RequestMapping(value = "/glossaryEntryHistory/{id}", method = RequestMethod.PUT)
    public PLMGlossaryEntryHistory updateGlossaryRevisionStatusHistory(@PathVariable("id") Integer id,
                                                                       @RequestBody PLMGlossaryEntryHistory revisionStatusHistory) {
        return glossaryEntryService.updateGlossaryEntryHistory(revisionStatusHistory);
    }

    @RequestMapping(value = "/glossaryEntryHistory/{id}", method = RequestMethod.GET)
    public PLMGlossaryEntryHistory getGlossaryRevisionStatusHistory(@PathVariable("id") Integer id) {
        return glossaryEntryService.getGlossaryEntryHistory(id);
    }

    @RequestMapping(value = "/glossaryEntryHistory/all", method = RequestMethod.GET)
    public List<PLMGlossaryEntryHistory> getAllGlossaryRevisionStatusHistory() {
        return glossaryEntryService.getAllGlossaryEntryHistory();
    }

    @RequestMapping(value = "/getEntries/{glossaryId}", method = RequestMethod.GET)
    public Page<PLMGlossaryEntry> getEntries(@PathVariable("glossaryId") Integer glossaryId, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return glossaryEntryService.getEntries(glossaryId, pageable);
    }

    @RequestMapping(value = "/getEntries/{glossaryId}/language/{language}", method = RequestMethod.GET)
    public Page<PLMGlossaryEntry> getEntriesByLanguage(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("language") String language, PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return glossaryEntryService.getEntriesByLanguage(glossaryId, language, pageable);
    }

    @RequestMapping(value = "/{entry}/versions", method = RequestMethod.GET)
    public List<PLMGlossaryEntry> getEntryVersions(@PathVariable("entry") Integer entry) {
        return glossaryEntryService.getEntryVersions(entry);
    }

    @RequestMapping(value = "/{entry}/versions/language/{language}", method = RequestMethod.GET)
    public List<PLMGlossaryEntry> getEntryVersionsByLanguage(@PathVariable("entry") Integer entry, @PathVariable("language") String language) {
        return glossaryEntryService.getEntryVersionsByLanguage(entry, language);
    }

    @RequestMapping(value = "/{entry}/edits/language/{language}", method = RequestMethod.GET)
    public List<PLMGlossaryEntryEdit> getEntryEditsByLanguage(@PathVariable("entry") Integer entry, @PathVariable("language") String language) {
        return glossaryEntryService.getEntryEditsByLanguage(entry, language);
    }

    @RequestMapping(value = "/{entry}/edit/accept/{editId}", method = RequestMethod.PUT)
    public PLMGlossaryEntryEdit acceptEntryEditChange(@PathVariable("entry") Integer entry, @PathVariable("editId") Integer editId,
                                                      @RequestBody PLMGlossaryEntryEdit glossaryEntryEdit) {
        return glossaryEntryService.acceptEntryEditChange(entry, glossaryEntryEdit);
    }

    @RequestMapping(value = "/{entry}/edit/reject/{editId}", method = RequestMethod.PUT)
    public PLMGlossaryEntryEdit rejectEntryEditChange(@PathVariable("entry") Integer entry, @PathVariable("editId") Integer editId,
                                                      @RequestBody PLMGlossaryEntryEdit glossaryEntryEdit) {
        return glossaryEntryService.rejectEntryEditChange(entry, glossaryEntryEdit);
    }

    @RequestMapping(value = "/{glossaryId}/edit/approve/{entryId}", method = RequestMethod.PUT)
    public PLMGlossaryEntry approveEntryEdits(@PathVariable("glossaryId") Integer glossaryId, @PathVariable("entryId") Integer entryId,
                                              @RequestBody PLMGlossaryEntry entry) {
        return glossaryEntryService.approveEntryEdits(glossaryId, entry);
    }

    @RequestMapping(value = "/{entryId}/edit/lastAccepted/{language}", method = RequestMethod.GET)
    public PLMGlossaryEntryEdit getLastAcceptedEntryEdit(@PathVariable("entryId") Integer entryId, @PathVariable("language") String language) {
        return glossaryEntryService.getLastAcceptedEntryEdit(entryId, language);
    }

    @RequestMapping(value = "/freeTextSearch", method = RequestMethod.GET)
    public Page<PLMGlossaryEntry> freeTextSearch(PageRequest pageRequest, GlossaryEntryCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Page<PLMGlossaryEntry> glossaryEntries = glossaryEntryService.freeTextSearch(pageable, criteria);
        return glossaryEntries;
    }
}
