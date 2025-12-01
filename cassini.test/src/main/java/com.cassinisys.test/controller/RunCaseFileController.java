package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.test.model.RunCaseFile;
import com.cassinisys.test.model.TestFile;
import com.cassinisys.test.service.RunCaseFileService;
import com.cassinisys.test.service.TestFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by CassiniSystems on 20-09-2018.
 */
@RestController
@RequestMapping("test/run/case/{caseId}/files")
public class RunCaseFileController extends BaseController {
    @Autowired
    private RunCaseFileService runCaseFileService;
    @Autowired
    private TestFileService testFileService;
    @Autowired
    private FileDownloadService fileDownloadService;

    /* ------ Create and update test runCase files ------*/
    @RequestMapping(method = RequestMethod.POST)
    public List<TestFile> uploadRunCaseFiles(@PathVariable("caseId") Integer caseId,
                                             MultipartHttpServletRequest request) {
        return runCaseFileService.uploadRunCaseFiles(caseId, request.getFileMap());
    }

    /*------ Get test runCase files ------*/
    @RequestMapping(value = "/testRunCaseFiles", method = RequestMethod.GET)
    public List<RunCaseFile> getItemFiles(@PathVariable("caseId") Integer caseId) {
        return runCaseFileService.findByTestRun(caseId);
    }

    /* ----- Download file ------*/
    @RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("caseId") Integer caseId, @PathVariable("fileId") Integer fileId,
                             HttpServletResponse response) {
        TestFile testFile = testFileService.get(fileId);
        File file = runCaseFileService.getRunFile(caseId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, testFile.getName(), file);
        }
    }

    /*----- Delete testRun file based on fileId ------*/
    @RequestMapping(value = "/deleteFile/{fileId}", method = RequestMethod.DELETE)
    public void deleteFiles(@PathVariable("fileId") Integer fileId) {
        runCaseFileService.deleteFile(fileId);
    }
}
