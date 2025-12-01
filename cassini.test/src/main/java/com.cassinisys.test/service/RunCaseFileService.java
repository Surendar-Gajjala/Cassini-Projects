package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.test.model.RunCase;
import com.cassinisys.test.model.RunCaseFile;
import com.cassinisys.test.model.TestFile;
import com.cassinisys.test.repo.RunCaseFileRepository;
import com.cassinisys.test.repo.RunCaseRepository;
import com.cassinisys.test.repo.TestFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CassiniSystems on 20-09-2018.
 */
@Service
public class RunCaseFileService implements CrudService<RunCaseFile, Integer> {

    @Autowired
    private RunCaseFileRepository runCaseFileRepository;
    @Autowired
    private TestFileRepository testFileRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private RunCaseRepository runCaseRepository;

    /*------- Create runCaseFile ------*/
    @Override
    @Transactional
    public RunCaseFile create(RunCaseFile testFile) {
        return testFile = runCaseFileRepository.save(testFile);
    }

    /* ----- Update runcaseFile ------*/
    @Override
    @Transactional
    public RunCaseFile update(RunCaseFile testFile) {
        return testFile = runCaseFileRepository.save(testFile);
    }

    /*------ Delete runCaseFile -----*/
    @Override
    @Transactional
    public void delete(Integer id) {
        TestFile testFile = testFileRepository.findOne(id);
    }

    /*------- Get all runCaseFiles based on runCaseId ------*/
    public List<RunCaseFile> findByTestRun(Integer runCase) {
        return runCaseFileRepository.findByTestRunCaseAndLatestTrueOrderByModifiedDateDesc(runCase);
    }

    /* -------  Get runCase file based on runCaseId  ------*/
    @Override
    public RunCaseFile get(Integer id) {
        return runCaseFileRepository.findOne(id);
    }

    /*  ------- Get all runCase files  ------*/
    @Override
    public List<RunCaseFile> getAll() {
        return runCaseFileRepository.findAll();
    }

    /* ------ Upload runCaseFiles based on runCaseId ------*/
    @Transactional
    public List<TestFile> uploadRunCaseFiles(Integer caseId, Map<String, MultipartFile> fileMap) {
        List<TestFile> uploaded = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                RunCaseFile caseFile = runCaseFileRepository.findByTestRunCaseAndNameAndLatestTrue(caseId, name);
                Integer version = 1;
                if (caseFile != null) {
                    caseFile.setLatest(false);
                    Integer oldVersion = caseFile.getVersion();
                    version = oldVersion + 1;
                    testFileRepository.save(caseFile);
                }

                caseFile = new RunCaseFile();
                caseFile.setName(name);
                caseFile.setCreatedBy(login.getPerson().getId());
                caseFile.setModifiedBy(login.getPerson().getId());
                caseFile.setSize(file.getSize());
                caseFile.setVersion(version);
                caseFile.setTestRunCase(caseId);
                caseFile = testFileRepository.save(caseFile);
                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + caseFile.getTestRunCase();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }
                String path = dir + File.separator + caseFile.getId();
                saveDocumentToDisk(file, path);
                uploaded.add(caseFile);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uploaded;
    }

    /* ------ Save files to Disk -----*/
    @Transactional
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

    /* ------- Get RunCase file ------*/
    public File getRunFile(Integer caseId, Integer fileId) {
        TestFile testFile = testFileRepository.findOne(fileId);
        if (testFile == null) {
            throw new ResourceNotFoundException();
        }
        RunCase runCaseFile = runCaseRepository.findOne(caseId);
        if (runCaseFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + caseId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /* ------ Delete file -----*/
    public void deleteFile(Integer fileId) {
        testFileRepository.delete(fileId);
    }

}
