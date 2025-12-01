package com.cassinisys.test.service;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.test.model.TestFile;
import com.cassinisys.test.model.TestRun;
import com.cassinisys.test.model.TestRunFile;
import com.cassinisys.test.repo.RunFileRepository;
import com.cassinisys.test.repo.RunRepository;
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
 * Created by CassiniSystems on 11-09-2018.
 */
@Service
public class TestFileService implements CrudService<TestFile, Integer> {

    @Autowired
    private RunFileRepository runFileRepository;
    @Autowired
    private TestFileRepository testFileRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private RunRepository runRepository;

    /* ------- Create test file ------*/
    @Override
    @Transactional
    public TestFile create(TestFile testFile) {
        return testFile = testFileRepository.save(testFile);
    }

    /*Update test file*/
    @Override
    @Transactional
    public TestFile update(TestFile testFile) {
        return testFile = testFileRepository.save(testFile);
    }

    /* ------ Delete test file ------*/
    @Override
    @Transactional
    public void delete(Integer id) {
        TestFile testFile = testFileRepository.findOne(id);

    }

    /* ------ Get testrun file based on testRunId ------*/
    public List<TestRunFile> findByTestRun(Integer runCase) {
        return runFileRepository.findByTestRunAndLatestTrueOrderByModifiedDateDesc(runCase);
    }

    /*------ Get test file -----*/
    @Override
    public TestFile get(Integer id) {
        return testFileRepository.findOne(id);
    }

    /* ------- Get all test files -------*/
    @Override
    public List<TestFile> getAll() {
        return testFileRepository.findAll();
    }

    /* -------- Upload test files ------*/
    @Transactional
    public List<TestFile> uploadItemFiles(Integer itemId, Map<String, MultipartFile> fileMap) {
        List<TestFile> uploaded = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();
        try {
            for (MultipartFile file : fileMap.values()) {
                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");
                TestRunFile itemFile = runFileRepository.findByTestRunAndNameAndLatestTrue(itemId, name);
                Integer version = 1;
                if (itemFile != null) {
                    itemFile.setLatest(false);
                    Integer oldVersion = itemFile.getVersion();
                    version = oldVersion + 1;
                    testFileRepository.save(itemFile);
                }
                itemFile = new TestRunFile();
                itemFile.setName(name);
                itemFile.setCreatedBy(login.getPerson().getId());
                itemFile.setModifiedBy(login.getPerson().getId());
                itemFile.setSize(file.getSize());
                itemFile.setVersion(version);
                itemFile.setTestRun(itemId);
                itemFile = testFileRepository.save(itemFile);

                String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
                        "filesystem" + File.separator + itemFile.getTestRun();
                File fDir = new File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                String path = dir + File.separator + itemFile.getId();
                saveDocumentToDisk(file, path);

                uploaded.add(itemFile);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return uploaded;
    }

    /* ------ file saved in Disk ------*/
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

    /* ----------Get run file -------*/
    public File getRunFile(Integer runId, Integer fileId) {
        TestFile testFile = testFileRepository.findOne(fileId);
        if (testFile == null) {
            throw new ResourceNotFoundException();
        }
        TestRun testRun = runRepository.findOne(runId);
        if (testRun == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + runId + File.separator + fileId;
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }

    /* ------- Delete file  ------*/
    public void deleteFile(Integer fileId) {
        testFileRepository.delete(fileId);
    }


}
