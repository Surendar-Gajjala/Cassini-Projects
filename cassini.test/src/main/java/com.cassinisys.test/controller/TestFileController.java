package com.cassinisys.test.controller;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.service.filesystem.FileDownloadService;
import com.cassinisys.test.model.TestFile;
import com.cassinisys.test.model.TestRunFile;
import com.cassinisys.test.service.TestFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * Created by CassiniSystems on 10-09-2018.
 */
@RestController
@RequestMapping("test/run/{runId}/files")
public class TestFileController extends BaseController {
    @Autowired
    private TestFileService testFileService;
    @Autowired
    private FileDownloadService fileDownloadService;

    /* ------ Upload testRun files ------*/
    @RequestMapping(method = RequestMethod.POST)
    public List<TestFile> uploadItemFiles(@PathVariable("runId") Integer runId,
                                          MultipartHttpServletRequest request) {
        return testFileService.uploadItemFiles(runId, request.getFileMap());
    }

    /*------- Update testRunFiles ------*/
    @RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
    public TestFile updateItemFile(@PathVariable("fileId") Integer fileId, @RequestBody TestFile file) {
        return testFileService.update(file);
    }

    /* ------ Get all testRun files ------*/
    @RequestMapping(value = "/runFiles", method = RequestMethod.GET)
    public List<TestRunFile> getItemFiles(@PathVariable("runId") Integer runCaseId) {
        return testFileService.findByTestRun(runCaseId);
    }

    /* ------- Download testRun file -----*/
    @RequestMapping(value = "/{fileId}/download", method = RequestMethod.GET)
    public void downloadFile(@PathVariable("runId") Integer runId, @PathVariable("fileId") Integer fileId,
                             HttpServletResponse response) {
        TestFile testFile = testFileService.get(fileId);
        File file = testFileService.getRunFile(runId, fileId);
        if (file != null) {
            fileDownloadService.writeFileContentToResponse(response, testFile.getName(), file);
        }
    }

    /* ------ Delete testRunFile -----*/
    @RequestMapping(value = "/deleteFile/{fileId}", method = RequestMethod.DELETE)
    public void deleteFiles(@PathVariable("fileId") Integer fileId) {
        testFileService.deleteFile(fileId);
    }

}
