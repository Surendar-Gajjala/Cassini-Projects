package com.cassinisys.pdm.controller;

import com.cassinisys.pdm.model.PDMCommit;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.service.PDMCommitService;
import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pdm/commits")
public class PDMCommitController extends BaseController {
    @Autowired
    private PDMCommitService commitService;

    @Autowired
    private PDMFileRepository fileRepository;

    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(method = RequestMethod.POST)
    public PDMCommit create(@RequestBody PDMCommit commit) {
        return commitService.create(commit);
    }

    @RequestMapping(value = "/{commitId}", method = RequestMethod.PUT)
    public PDMCommit update(@RequestBody PDMCommit commit) {
        return commitService.update(commit);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<PDMCommit> getAll() {
        return commitService.getAll();
    }

    @RequestMapping(value = "/paged", method = RequestMethod.GET)
    public Page<PDMCommit> getAllPaged(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        return commitService.getAllPaged(pageable);
    }

    @RequestMapping(value = "/{commitId}", method = RequestMethod.GET)
    public PDMCommit get(@PathVariable("commitId") Integer commitId) {
        return commitService.get(commitId);
    }

    @RequestMapping(value = "/{commitId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("commitId") Integer commitId) {
        commitService.delete(commitId);
    }

    @RequestMapping(value = "/{commitId}/files", method = RequestMethod.GET)
    public List<PDMFile> getFiles(@PathVariable("commitId") Integer commitId) {
        return fileRepository.findAllByCommitOrderByCreatedDateDesc(commitId);
    }
}
