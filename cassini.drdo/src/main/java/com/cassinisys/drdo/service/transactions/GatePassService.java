package com.cassinisys.drdo.service.transactions;

import com.cassinisys.drdo.filtering.GatePassCriteria;
import com.cassinisys.drdo.filtering.GatePassPredicateBuilder;
import com.cassinisys.drdo.model.DRDOObjectType;
import com.cassinisys.drdo.model.bom.File;
import com.cassinisys.drdo.model.transactions.GatePass;
import com.cassinisys.drdo.model.transactions.Inward;
import com.cassinisys.drdo.model.transactions.QGatePass;
import com.cassinisys.drdo.repo.bom.FileRepository;
import com.cassinisys.drdo.repo.transactions.GatePassRepository;
import com.cassinisys.drdo.repo.transactions.InwardRepository;
import com.cassinisys.drdo.service.DRDOUpdatesService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Nageshreddy on 27-10-2018.
 */
@Service
public class GatePassService implements CrudService<GatePass, Integer> {

    @Autowired
    private GatePassRepository gatePassRepository;

    @Autowired
    private GatePassPredicateBuilder gatePassPredicateBuilder;

    @Autowired
    private FileSystemService fileSystemService;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private InwardRepository inwardRepository;

    @Autowired
    private DRDOUpdatesService drdoUpdatesService;

    @Override
    @Transactional(readOnly = false)
    public GatePass create(GatePass gatePass) {
        return gatePassRepository.save(gatePass);
    }

    @Override
    @Transactional(readOnly = false)
    public GatePass update(GatePass gatePass) {
        return gatePassRepository.save(gatePass);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer integer) {

        List<Inward> inwards = inwardRepository.getInwardsByGatePass(integer);

        if (inwards.size() == 0) {
            gatePassRepository.delete(integer);
        } else {
            throw new CassiniException("Gate Pass has Inwards. We cannot delete this Gate Pass");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GatePass get(Integer integer) {
        return gatePassRepository.findOne(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GatePass> getAll() {
        return gatePassRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<GatePass> getGatePassByFreeTextSearch(GatePassCriteria criteria) {
        Pageable pageable = new org.springframework.data.domain.PageRequest(0,
                100, Sort.Direction.DESC, "createdDate");
        Predicate predicate = gatePassPredicateBuilder.build(criteria, QGatePass.gatePass1);

        Page<GatePass> gatePasses = gatePassRepository.findAll(predicate, pageable);
        List<GatePass> gatePasses1 = gatePasses.getContent();
        return gatePasses1;

    }

    @Transactional(readOnly = true)
    public Page<GatePass> getAllGatePasses(Pageable pageable, GatePassCriteria gatePassCriteria) {

        Predicate predicate = gatePassPredicateBuilder.build(gatePassCriteria, QGatePass.gatePass1);

        Page<GatePass> gatePasses = gatePassRepository.findAll(predicate, pageable);

        gatePasses.getContent().forEach(gatePass -> {
            gatePass.setInwards(inwardRepository.getInwardsByGatePass(gatePass.getId()).size());
        });

        return gatePasses;
    }

    @Transactional
    public GatePass uploadGatePass(String gatePassNumber, String gatePassDate, Map<String, MultipartFile> fileMap) {
        List<File> uploaded = new ArrayList<>();
        Login login = sessionWrapper.getSession().getLogin();

        try {
            for (MultipartFile file : fileMap.values()) {

                String name = new String((file.getOriginalFilename()).getBytes("iso-8859-1"), "UTF-8");

                List<GatePass> existGatePass = gatePassRepository.getGatePassByName(name);

                if (existGatePass.size() > 0) {
                    throw new CassiniException(name + " Gate Pass already exist");
                }

                File file1 = new File();
                file1.setName(name);
                file1.setCreatedBy(login.getPerson().getId());
                file1.setModifiedBy(login.getPerson().getId());
                file1.setVersion(0);
                file1.setSize(file.getSize());
                file1.setObjectType(DRDOObjectType.INWARDGATEPASS);
                file1 = fileRepository.save(file1);


                String dir = fileSystemService.getCurrentTenantRoot() + java.io.File.separator +
                        "filesystem" + java.io.File.separator + file1.getId();
                java.io.File fDir = new java.io.File(dir);
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                String path = dir + java.io.File.separator + file1.getId();
                saveDocumentToDisk(file, path);

                uploaded.add(file1);

                String message = sessionWrapper.getSession().getLogin().getPerson().getFullName() + " has created new '" + file1.getName()
                        + "' gate pass";
                drdoUpdatesService.updateMessage(message, DRDOObjectType.INWARDGATEPASS);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(gatePassDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GatePass gatePass = new GatePass();
        gatePass.setGatePass(uploaded.get(0));
        gatePass.setGatePassNumber(gatePassNumber);
        gatePass.setGatePassDate(date);

        gatePass = gatePassRepository.save(gatePass);

        return gatePass;
    }

    @Transactional
    protected void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            java.io.File file = new java.io.File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException();
        }
    }

    public java.io.File getGatePassFile(Integer gatePassId, Integer fileId) {
        checkNotNull(gatePassId);
        checkNotNull(fileId);
        GatePass gatePass = gatePassRepository.findOne(gatePassId);
        if (gatePass == null) {
            throw new ResourceNotFoundException();
        }
        File gatePassFile = fileRepository.findOne(fileId);
        if (gatePassFile == null) {
            throw new ResourceNotFoundException();
        }

        String path = fileSystemService.getCurrentTenantRoot() + java.io.File.separator +
                "filesystem" + java.io.File.separator + fileId + java.io.File.separator + fileId;
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return file;
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
