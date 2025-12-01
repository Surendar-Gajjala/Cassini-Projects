/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cassinisys.plm.service.plm;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.plm.model.cm.PLMChangeFile;
import com.cassinisys.plm.model.mes.MESBOPFile;
import com.cassinisys.plm.model.mes.MESMBOMFile;
import com.cassinisys.plm.model.mes.MESObjectFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerFile;
import com.cassinisys.plm.model.mfr.PLMManufacturerPartFile;
import com.cassinisys.plm.model.mfr.PLMMfrPartInspectionReport;
import com.cassinisys.plm.model.mfr.PLMSupplierFile;
import com.cassinisys.plm.model.mro.MROObjectFile;
import com.cassinisys.plm.model.pgc.PGCObjectFile;
import com.cassinisys.plm.model.plm.PLMDocument;
import com.cassinisys.plm.model.plm.PLMFile;
import com.cassinisys.plm.model.plm.PLMItemFile;
import com.cassinisys.plm.model.plm.PLMNprFile;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMActivityFile;
import com.cassinisys.plm.model.pm.PLMProgramFile;
import com.cassinisys.plm.model.pm.PLMProjectFile;
import com.cassinisys.plm.model.pm.PLMTaskFile;
import com.cassinisys.plm.model.pqm.*;
import com.cassinisys.plm.model.req.PLMRequirementDocumentFile;
import com.cassinisys.plm.model.req.PLMRequirementFile;
import com.cassinisys.plm.model.rm.PLMGlossaryFile;
import com.cassinisys.plm.model.rm.RmObjectFile;
import com.cassinisys.plm.repo.cm.ChangeFileRepository;
import com.cassinisys.plm.repo.mes.BOPFileRepository;
import com.cassinisys.plm.repo.mes.MBOMFileRepository;
import com.cassinisys.plm.repo.mes.MESObjectFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerFileRepository;
import com.cassinisys.plm.repo.mfr.ManufacturerPartFileRepository;
import com.cassinisys.plm.repo.mfr.MfrPartInspectionReportRepository;
import com.cassinisys.plm.repo.mfr.SupplierFileRepository;
import com.cassinisys.plm.repo.mro.MROObjectFileRepository;
import com.cassinisys.plm.repo.pgc.PGCObjectFileRepository;
import com.cassinisys.plm.repo.plm.FileRepository;
import com.cassinisys.plm.repo.plm.ItemFileRepository;
import com.cassinisys.plm.repo.plm.NprFileRepository;
import com.cassinisys.plm.repo.pm.*;
import com.cassinisys.plm.repo.pqm.*;
import com.cassinisys.plm.repo.req.PLMRequirementDocumentFileRepository;
import com.cassinisys.plm.repo.req.PLMRequirementFileRepository;
import com.cassinisys.plm.repo.rm.GlossaryFileRepository;
import com.cassinisys.plm.repo.rm.RmObjectFileRepository;
import com.cassinisys.plm.service.UtilService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.*;

/**
 * @author suresh cassini
 */
@Component
public class FileHelpers {

    static final int BUFFER = 2048;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ItemFileRepository itemFileRepository;
    @Autowired
    private ProjectFileRepository projectFileRepository;
    @Autowired
    private ActivityFileRepository activityFileRepository;
    @Autowired
    private TaskFileRepository taskFileRepository;
    @Autowired
    private ChangeFileRepository changeFileRepository;
    @Autowired
    private ManufacturerFileRepository manufacturerFileRepository;
    @Autowired
    private ManufacturerPartFileRepository manufacturerPartFileRepository;
    @Autowired
    private RmObjectFileRepository rmObjectFileRepository;
    @Autowired
    private GlossaryFileRepository glossaryFileRepository;
    @Autowired
    private InspectionPlanFileRepository inspectionPlanFileRepository;
    @Autowired
    private InspectionFileRepository inspectionFileRepository;
    @Autowired
    private ProblemReportFileRepository problemReportFileRepository;
    @Autowired
    private NCRFileRepository ncrFileRepository;
    @Autowired
    private QCRFileRepository qcrFileRepository;
    @Autowired
    private MESObjectFileRepository mesObjectFileRepository;
    @Autowired
    private PPAPChecklistRepository ppapChecklistRepository;
    @Autowired
    private MfrPartInspectionReportRepository mfrPartInspectionReportRepository;
    @Autowired
    private MROObjectFileRepository mroObjectFileRepository;
    @Autowired
    private PGCObjectFileRepository pgcObjectFileRepository;
    @Autowired
    private SupplierAuditFileRepository supplierAuditFileRepository;
    @Autowired
    private ProgramFileRepository programFileRepository;
    @Autowired
    private SupplierFileRepository supplierFileRepository;
    @Autowired
    private CustomerFileRepository customerFileRepository;
    @Autowired
    private NprFileRepository nprFileRepository;
    @Autowired
    private UtilService utilService;
    @Autowired
    private PLMRequirementDocumentFileRepository requirementDocumentFileRepository;
    @Autowired
    private PLMRequirementFileRepository requirementFileRepository;
    @Autowired
    private PLMDocumentRepository plmDocumentRepository;
    @Autowired
    private PLMObjectDocumentRepository objectDocumentRepository;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private BOPFileRepository bopFileRepository;
    @Autowired
    private MBOMFileRepository mbomFileRepository;

    /**
     * Creates a zipfile.
     *
     * @param zipFilename Name of the zip
     * @param files       to include in the zip. Paths are not removed.
     * @return the checksum of the zipfile
     * @throws FileNotFoundException
     */
    public long Zip(String zipFilename, String[] files, String type, Integer id) throws FileNotFoundException, IOException {
        return ZipFile(zipFilename, files, false, type, id);
    }

    /**
     * Creates a zipfile.
     *
     * @param zipFilename Name of the zip
     * @param files       to include in the zip. Paths are not removed.
     * @param includePath include Pathname in files
     * @return the checksum of the zipfile
     * @throws FileNotFoundException
     */
    public long ZipFile(String zipFilename, String[] files, Boolean includePath, String type, Integer id) throws FileNotFoundException, IOException {
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(zipFilename);
        CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
        //out.setMethod(ZipOutputStream.DEFLATED);
        byte data[] = new byte[BUFFER];
        // get a list of files from current directory
        for (int i = 0; i < files.length; i++) {
            generateFileList(data, out, "", files[i], type, id);

        }
        out.close();
        return checksum.getChecksum().getValue();
    }

    public void generateFileList(byte data[], ZipOutputStream out, String sourceFolder, String file, String type, Integer id) throws IOException {
        // add file only
        File node = new File(file);
//        if (sourceFolder == "" || sourceFolder == null)
        sourceFolder = node.getAbsolutePath().substring(0, node.getAbsolutePath().lastIndexOf(File.separator));
        if (node.isFile()) {
            //System.out.println("Adding: "+file);
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            String zipfname = file.substring(sourceFolder.length() + 1, file.length());
            Integer fileId = Integer.parseInt(zipfname);
            if (type.equals("ITEM")) {
                PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
                if (plmItemFile != null && plmItemFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmItemFile.getId(), type);
                    if (plmItemFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmItemFile != null && plmItemFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmItemFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PROJECT")) {
                PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
                if (plmProjectFile != null && plmProjectFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmProjectFile.getId(), type);
                    if (plmProjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmProjectFile != null && plmProjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmProjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("ACTIVITY")) {
                PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
                if (plmActivityFile != null && plmActivityFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmActivityFile.getId(), type);
                    if (plmActivityFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmActivityFile != null && plmActivityFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmActivityFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("TASKS")) {
                PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
                if (plmTaskFile != null && plmTaskFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmTaskFile.getId(), type);
                    if (plmTaskFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmTaskFile != null && plmTaskFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmTaskFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("CHANGE")) {
                PLMChangeFile plmChangeFile = changeFileRepository.findOne(fileId);
                if (plmChangeFile != null && plmChangeFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmChangeFile.getId(), type);
                    if (plmChangeFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmChangeFile != null && plmChangeFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmChangeFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("MFR")) {
                PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
                if (plmManufacturerFile != null && plmManufacturerFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmManufacturerFile.getId(), type);
                    if (plmManufacturerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmManufacturerFile != null && plmManufacturerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmManufacturerFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("SUPPLIER")) {
                PLMSupplierFile plmManufacturerFile = supplierFileRepository.findOne(fileId);
                if (plmManufacturerFile != null && plmManufacturerFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmManufacturerFile.getId(), type);
                    if (plmManufacturerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmManufacturerFile != null && plmManufacturerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmManufacturerFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PLMNPR")) {
                PLMNprFile nprFile = nprFileRepository.findOne(fileId);
                if (nprFile != null && nprFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(nprFile.getId(), type);
                    if (nprFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (nprFile != null && nprFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(nprFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("CUSTOMER")) {
                PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
                if (customerFile != null && customerFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(customerFile.getId(), type);
                    if (customerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (customerFile != null && customerFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(customerFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("MFRPART")) {
                PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
                if (plmManufacturerPartFile != null && plmManufacturerPartFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmManufacturerPartFile.getId(), type);
                    if (plmManufacturerPartFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmManufacturerPartFile != null && plmManufacturerPartFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmManufacturerPartFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("RM")) {
                RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
                if (rmObjectFile != null && rmObjectFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(rmObjectFile.getId(), type);
                    if (rmObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (rmObjectFile != null && rmObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(rmObjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("GLOSSARY")) {
                PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
                if (plmGlossaryFile != null && plmGlossaryFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(plmGlossaryFile.getId(), type);
                    if (plmGlossaryFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (plmGlossaryFile != null && plmGlossaryFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(plmGlossaryFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("INSPECTIONPLAN")) {
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
                if (inspectionPlanFile != null && inspectionPlanFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(inspectionPlanFile.getId(), type);
                    if (inspectionPlanFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (inspectionPlanFile != null && inspectionPlanFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(inspectionPlanFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("ITEMINSPECTION")) {
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
                if (inspectionFile != null && inspectionFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(inspectionFile.getId(), type);
                    if (inspectionFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (inspectionFile != null && inspectionFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(inspectionFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PROBLEMREPORT")) {
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
                if (problemReportFile != null && problemReportFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(problemReportFile.getId(), type);
                    if (problemReportFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (problemReportFile != null && problemReportFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(problemReportFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("NCR")) {
                PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
                if (pqmncrFile != null && pqmncrFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(pqmncrFile.getId(), type);
                    if (pqmncrFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (pqmncrFile != null && pqmncrFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(pqmncrFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("QCR")) {
                PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
                if (pqmqcrFile != null && pqmqcrFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(pqmqcrFile.getId(), type);
                    if (pqmqcrFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (pqmqcrFile != null && pqmqcrFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(pqmqcrFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("MESOBJECT")) {
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
                if (mesObjectFile != null && mesObjectFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(mesObjectFile.getId(), type);
                    if (mesObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (mesObjectFile != null && mesObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(mesObjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PPAPCHECKLIST")) {
                PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
                if (pqmppapChecklist != null && pqmppapChecklist.getParentFile() != null) {
                    String path = getParentFileSystemPath(pqmppapChecklist.getId(), type);
                    if (pqmppapChecklist.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (pqmppapChecklist != null && pqmppapChecklist.getLatest()) {
                        ZipEntry entry = new ZipEntry(pqmppapChecklist.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("MFRPARTINSPECTIONREPORT")) {
                PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
                if (mfrPartInspectionReport != null && mfrPartInspectionReport.getParentFile() != null) {
                    String path = getParentFileSystemPath(mfrPartInspectionReport.getId(), type);
                    if (mfrPartInspectionReport.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (mfrPartInspectionReport != null && mfrPartInspectionReport.getLatest()) {
                        ZipEntry entry = new ZipEntry(mfrPartInspectionReport.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("DOCUMENT")) {
                PLMDocument document = plmDocumentRepository.findOne(fileId);
                if (document != null && document.getParentFile() != null) {
                    String path = getParentFileSystemPath1(document.getId(), type, id);
                    if (document.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (document != null && document.getLatest()) {
                        ZipEntry entry = new ZipEntry(document.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("MROOBJECT")) {
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
                if (mroObjectFile != null && mroObjectFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(mroObjectFile.getId(), type);
                    if (mroObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (mroObjectFile != null && mroObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(mroObjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PGCOBJECT")) {
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
                if (pgcObjectFile != null && pgcObjectFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(pgcObjectFile.getId(), type);
                    if (pgcObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (pgcObjectFile != null && pgcObjectFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(pgcObjectFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("SUPPLIERAUDIT")) {
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
                if (supplierAuditFile != null && supplierAuditFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(supplierAuditFile.getId(), type);
                    if (supplierAuditFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (supplierAuditFile != null && supplierAuditFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(supplierAuditFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("PROGRAM")) {
                PLMProgramFile programFile = programFileRepository.findOne(fileId);
                if (programFile != null && programFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(programFile.getId(), type);
                    if (programFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (programFile != null && programFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(programFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("REQUIREMENTDOCUMENT")) {
                PLMRequirementDocumentFile requirementFile = requirementDocumentFileRepository.findOne(fileId);
                if (requirementFile != null && requirementFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(requirementFile.getId(), type);
                    if (requirementFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (requirementFile != null && requirementFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(requirementFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("REQUIREMENT")) {
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileId);
                if (requirementFile != null && requirementFile.getParentFile() != null) {
                    String path = getParentFileSystemPath(requirementFile.getId(), type);
                    if (requirementFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(path);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                } else {
                    if (requirementFile != null && requirementFile.getLatest()) {
                        ZipEntry entry = new ZipEntry(requirementFile.getName());
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER)) != -1) {
                            out.write(data, 0, count);
                        }
                        origin.close();
                    }
                }
            } else if (type.equals("SHAREDOBJECT")) {
                PLMItemFile itemFile = itemFileRepository.findOne(fileId);
                PLMChangeFile changeFile = changeFileRepository.findOne(fileId);
                PLMProjectFile projectFile = projectFileRepository.findOne(fileId);
                PLMActivityFile activityFile = activityFileRepository.findOne(fileId);
                PLMTaskFile taskFile = taskFileRepository.findOne(fileId);
                PLMProgramFile programFile = programFileRepository.findOne(fileId);
                PLMManufacturerFile manufacturerFile = manufacturerFileRepository.findOne(fileId);
                PLMManufacturerPartFile manufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
                PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
                PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
                PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
                PQMNCRFile ncrFile = ncrFileRepository.findOne(fileId);
                PQMQCRFile qcrFile = qcrFileRepository.findOne(fileId);
                MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
                MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
                PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
                PLMSupplierFile plmSupplierFile = supplierFileRepository.findOne(fileId);
                MESBOPFile mesBopFile = bopFileRepository.findOne(fileId);
                PLMRequirementDocumentFile requirementDocumentFile = requirementDocumentFileRepository.findOne(fileId);
                PLMRequirementFile requirementFile = requirementFileRepository.findOne(fileId);
                PQMCustomerFile customerFile = customerFileRepository.findOne(fileId);
                PLMNprFile plmNprFile = nprFileRepository.findOne(fileId);
                PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
                PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
                MESMBOMFile mbomFile = mbomFileRepository.findOne(fileId);


                if (itemFile != null) {
                    if (itemFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(itemFile.getId(), PLMObjectType.ITEMREVISION.toString());
                        if (itemFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (itemFile != null && itemFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(itemFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }
                } else if (changeFile != null) {
                    if (changeFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(changeFile.getId(), PLMObjectType.CHANGE.toString());
                        if (changeFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (changeFile != null && changeFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(changeFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (projectFile != null) {
                    if (projectFile.getParentFile() != null) {
                        String path = getParentFileSystemPath1(projectFile.getId(), PLMObjectType.PROJECT.toString(),id);
                        if (projectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (projectFile != null && projectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(projectFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }
                } else if (activityFile != null) {
                    if (activityFile.getParentFile() != null) {
                        String path = getParentFileSystemPath1(activityFile.getId(), PLMObjectType.PROJECTACTIVITY.toString(),id);
                        if (activityFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (activityFile != null && activityFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(activityFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }
                } else if (taskFile != null) {
                    if (taskFile.getParentFile() != null) {
                        String path = getParentFileSystemPath1(taskFile.getId(), PLMObjectType.PROJECTTASK.toString(),id);
                        if (taskFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (taskFile != null && taskFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(taskFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (programFile != null) {
                    if (programFile.getParentFile() != null) {
                        String path = getParentFileSystemPath1(programFile.getId(), PLMObjectType.PROGRAM.toString(),id);
                        if (programFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (programFile != null && programFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(programFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (manufacturerFile != null) {
                    if (manufacturerFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(manufacturerFile.getId(), PLMObjectType.MANUFACTURER.toString());
                        if (manufacturerFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (manufacturerFile != null && manufacturerFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(manufacturerFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (manufacturerPartFile != null) {
                    if (manufacturerPartFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(manufacturerPartFile.getId(), PLMObjectType.MANUFACTURERPART.toString());
                        if (manufacturerPartFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (manufacturerPartFile != null && manufacturerPartFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(manufacturerPartFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (inspectionPlanFile != null) {
                    if (inspectionPlanFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(inspectionPlanFile.getId(), PLMObjectType.INSPECTIONPLAN.toString());
                        if (inspectionPlanFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (inspectionPlanFile != null && inspectionPlanFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(inspectionPlanFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (inspectionFile != null) {
                    if (inspectionFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(inspectionFile.getId(), PLMObjectType.INSPECTION.toString());
                        if (inspectionFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (inspectionFile != null && inspectionFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(inspectionFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (problemReportFile != null) {
                    if (problemReportFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(problemReportFile.getId(), PLMObjectType.PROBLEMREPORT.toString());
                        if (problemReportFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (problemReportFile != null && problemReportFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(problemReportFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (ncrFile != null) {
                    if (ncrFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(ncrFile.getId(), PLMObjectType.NCR.toString());
                        if (ncrFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (ncrFile != null && ncrFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(ncrFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (qcrFile != null) {
                    if (qcrFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(qcrFile.getId(), PLMObjectType.QCR.toString());
                        if (qcrFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (qcrFile != null && qcrFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(qcrFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (mesObjectFile != null) {
                    if (mesObjectFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(mesObjectFile.getId(), PLMObjectType.MESOBJECT.toString());
                        if (mesObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (mesObjectFile != null && mesObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(mesObjectFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (mroObjectFile != null) {
                    if (mroObjectFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(mroObjectFile.getId(), PLMObjectType.MROOBJECT.toString());
                        if (mroObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (mroObjectFile != null && mroObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(mroObjectFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (pgcObjectFile != null) {
                    if (pgcObjectFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(pgcObjectFile.getId(), PLMObjectType.PGCOBJECT.toString());
                        if (pgcObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (pgcObjectFile != null && pgcObjectFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(pgcObjectFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (plmSupplierFile != null) {
                    if (plmSupplierFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(plmSupplierFile.getId(), PLMObjectType.SUPPLIER.toString());
                        if (plmSupplierFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (plmSupplierFile != null && plmSupplierFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(plmSupplierFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (mesBopFile != null) {
                    if (mesBopFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(mesBopFile.getId(), PLMObjectType.BOP.toString());
                        if (mesBopFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (mesBopFile != null && mesBopFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(mesBopFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (requirementDocumentFile != null) {
                    if (requirementDocumentFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(requirementDocumentFile.getId(), PLMObjectType.REQUIREMENTDOCUMENT.toString());
                        if (requirementDocumentFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (requirementDocumentFile != null && requirementDocumentFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(requirementDocumentFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (requirementFile != null) {
                    if (requirementFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(requirementFile.getId(), PLMObjectType.REQUIREMENT.toString());
                        if (requirementFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (requirementFile != null && requirementFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(requirementFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (customerFile != null) {
                    if (customerFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(customerFile.getId(), PLMObjectType.CUSTOMER.toString());
                        if (customerFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (customerFile != null && customerFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(customerFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (plmNprFile != null) {
                    if (plmNprFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(plmNprFile.getId(), PLMObjectType.PLMNPR.toString());
                        if (plmNprFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (plmNprFile != null && plmNprFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(plmNprFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (supplierAuditFile != null) {
                    if (supplierAuditFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(supplierAuditFile.getId(), PLMObjectType.SUPPLIERAUDIT.toString());
                        if (supplierAuditFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (supplierAuditFile != null && supplierAuditFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(supplierAuditFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (plmDocument != null) {
                    if (plmDocument.getParentFile() != null) {
                        String path = getParentFileSystemPath1(plmDocument.getId(), PLMObjectType.DOCUMENT.toString(), id);
                        if (plmDocument.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (plmDocument != null && plmDocument.getLatest()) {
                            ZipEntry entry = new ZipEntry(plmDocument.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                } else if (mbomFile != null) {
                    if (mbomFile.getParentFile() != null) {
                        String path = getParentFileSystemPath(mbomFile.getId(), PLMObjectType.MBOM.toString());
                        if (mbomFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(path);
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    } else {
                        if (mbomFile != null && mbomFile.getLatest()) {
                            ZipEntry entry = new ZipEntry(mbomFile.getName());
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                                out.write(data, 0, count);
                            }
                            origin.close();
                        }
                    }

                }
            }

        }
        if (node.isDirectory()) {
            File[] subNote = node.listFiles();
            /*String folderPath = node.getAbsolutePath().substring(node.getAbsolutePath().lastIndexOf(File.separator) + 1, node.getAbsolutePath().length());
            List<PLMObjectDocument> objectDocuments = objectDocumentRepository.findByFolder(Integer.parseInt(folderPath));
            if (subNote != null && subNote.length > 0 && objectDocuments.size() > 0) {
                List<File> files = new ArrayList<>();
                for (File filename : subNote) {
                    files.add(filename);
                }
                objectDocuments.forEach(plmObjectDocument -> {
                    File documentFile = getDocumentFile(plmObjectDocument.getDocument().getId());
                    files.add(documentFile);
                });
                File[] intArray = new File[files.size()];
                intArray = files.toArray(intArray);
                subNote = intArray;
            }*/

            for (File filename : subNote) {
                generateFileList(data, out, sourceFolder, filename.getAbsolutePath(), type, id);
            }
            if (subNote.length == 0) {
                String zipfname = file.substring(sourceFolder.length() + 1, file.length());
                Integer fileId = Integer.parseInt(zipfname);
                String path = null;
                if (type.equals("ITEM")) {
                    PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
                    if (plmItemFile != null) {
                        if (plmItemFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmItemFile.getId(), "ITEM") + File.separator;
                        } else {
                            path = plmItemFile.getName() + File.separator;
                        }
                    }

                } else if (type.equals("PROJECT")) {
                    PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
                    if (plmProjectFile != null) {
                        if (plmProjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmProjectFile.getId(), "PROJECT") + File.separator;
                        } else {
                            path = plmProjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("SUPPLIER")) {
                    PLMSupplierFile supplierFile = supplierFileRepository.findOne(fileId);
                    if (supplierFile != null) {
                        if (supplierFile.getParentFile() != null) {
                            path = getParentFileSystemPath(supplierFile.getId(), "SUPPLIER") + File.separator;
                        } else {
                            path = supplierFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("ACTIVITY")) {
                    PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
                    if (plmActivityFile != null) {
                        if (plmActivityFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmActivityFile.getId(), "ACTIVITY") + File.separator;
                        } else {
                            path = plmActivityFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("TASKS")) {
                    PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
                    if (plmTaskFile != null) {
                        if (plmTaskFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmTaskFile.getId(), "TASKS") + File.separator;
                        } else {
                            path = plmTaskFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("CHANGE")) {
                    PLMChangeFile plmChangeFile = changeFileRepository.findOne(fileId);
                    if (plmChangeFile != null) {
                        if (plmChangeFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmChangeFile.getId(), "CHANGE") + File.separator;
                        } else {
                            path = plmChangeFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("MFR")) {
                    PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
                    if (plmManufacturerFile != null) {
                        if (plmManufacturerFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmManufacturerFile.getId(), "MFR") + File.separator;
                        } else {
                            path = plmManufacturerFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("MFRPART")) {
                    PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
                    if (plmManufacturerPartFile != null) {
                        if (plmManufacturerPartFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmManufacturerPartFile.getId(), "MFRPART") + File.separator;
                        } else {
                            path = plmManufacturerPartFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("RM")) {
                    RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
                    if (rmObjectFile != null) {
                        if (rmObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(rmObjectFile.getId(), "RM") + File.separator;
                        } else {
                            path = rmObjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("GLOSSARY")) {
                    PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
                    if (plmGlossaryFile != null) {
                        if (plmGlossaryFile.getParentFile() != null) {
                            path = getParentFileSystemPath(plmGlossaryFile.getId(), "GLOSSARY") + File.separator;
                        } else {
                            path = plmGlossaryFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("INSPECTIONPLAN")) {
                    PQMInspectionPlanFile planFile = inspectionPlanFileRepository.findOne(fileId);
                    if (planFile != null) {
                        if (planFile.getParentFile() != null) {
                            path = getParentFileSystemPath(planFile.getId(), "INSPECTIONPLAN") + File.separator;
                        } else {
                            path = planFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("ITEMINSPECTION")) {
                    PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
                    if (inspectionFile != null) {
                        if (inspectionFile.getParentFile() != null) {
                            path = getParentFileSystemPath(inspectionFile.getId(), "ITEMINSPECTION") + File.separator;
                        } else {
                            path = inspectionFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("PROBLEMREPORT")) {
                    PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
                    if (problemReportFile != null) {
                        if (problemReportFile.getParentFile() != null) {
                            path = getParentFileSystemPath(problemReportFile.getId(), "PROBLEMREPORT") + File.separator;
                        } else {
                            path = problemReportFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("NCR")) {
                    PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
                    if (pqmncrFile != null) {
                        if (pqmncrFile.getParentFile() != null) {
                            path = getParentFileSystemPath(pqmncrFile.getId(), "NCR") + File.separator;
                        } else {
                            path = pqmncrFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("QCR")) {
                    PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
                    if (pqmqcrFile != null) {
                        if (pqmqcrFile.getParentFile() != null) {
                            path = getParentFileSystemPath(pqmqcrFile.getId(), "QCR") + File.separator;
                        } else {
                            path = pqmqcrFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("MESOBJECT")) {
                    MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
                    if (mesObjectFile != null) {
                        if (mesObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(mesObjectFile.getId(), "MESOBJECT") + File.separator;
                        } else {
                            path = mesObjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("PPAPCHECKLIST")) {
                    PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
                    if (pqmppapChecklist != null) {
                        if (pqmppapChecklist.getParentFile() != null) {
                            path = getParentFileSystemPath(pqmppapChecklist.getId(), "PPAPCHECKLIST") + File.separator;
                        } else {
                            path = pqmppapChecklist.getName() + File.separator;
                        }
                    }
                } else if (type.equals("MFRPARTINSPECTIONREPORT")) {
                    PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
                    if (mfrPartInspectionReport != null) {
                        if (mfrPartInspectionReport.getParentFile() != null) {
                            path = getParentFileSystemPath(mfrPartInspectionReport.getId(), "MFRPARTINSPECTIONREPORT") + File.separator;
                        } else {
                            path = mfrPartInspectionReport.getName() + File.separator;
                        }
                    }
                } else if (type.equals("DOCUMENT")) {
                    PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
                    if (plmDocument != null) {
                        if (plmDocument.getParentFile() != null) {
                            path = getParentFileSystemPath(plmDocument.getId(), "DOCUMENT") + File.separator;
                        } else {
                            path = plmDocument.getName() + File.separator;
                        }
                    }
                } else if (type.equals("MROOBJECT")) {
                    MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
                    if (mroObjectFile != null) {
                        if (mroObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(mroObjectFile.getId(), "MROOBJECT") + File.separator;
                        } else {
                            path = mroObjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("PGCOBJECT")) {
                    PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
                    if (pgcObjectFile != null) {
                        if (pgcObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(pgcObjectFile.getId(), "PGCOBJECT") + File.separator;
                        } else {
                            path = pgcObjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("SUPPLIERAUDIT")) {
                    PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
                    if (supplierAuditFile != null) {
                        if (supplierAuditFile.getParentFile() != null) {
                            path = getParentFileSystemPath(supplierAuditFile.getId(), "SUPPLIERAUDIT") + File.separator;
                        } else {
                            path = supplierAuditFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("PROGRAM")) {
                    PLMProgramFile programFile = programFileRepository.findOne(fileId);
                    if (programFile != null) {
                        if (programFile.getParentFile() != null) {
                            path = getParentFileSystemPath(programFile.getId(), "PROGRAM") + File.separator;
                        } else {
                            path = programFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("REQUIREMENT")) {
                    PLMRequirementFile pgcObjectFile = requirementFileRepository.findOne(fileId);
                    if (pgcObjectFile != null) {
                        if (pgcObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(pgcObjectFile.getId(), "REQUIREMENT") + File.separator;
                        } else {
                            path = pgcObjectFile.getName() + File.separator;
                        }
                    }
                } else if (type.equals("REQUIREMENTDOCUMENT")) {
                    PLMRequirementDocumentFile pgcObjectFile = requirementDocumentFileRepository.findOne(fileId);
                    if (pgcObjectFile != null) {
                        if (pgcObjectFile.getParentFile() != null) {
                            path = getParentFileSystemPath(pgcObjectFile.getId(), "REQUIREMENTDOCUMENT") + File.separator;
                        } else {
                            path = pgcObjectFile.getName() + File.separator;
                        }
                    }
                }
                ZipEntry entry = new ZipEntry(path);
                out.putNextEntry(entry);

                /*folderPath = node.getAbsolutePath().substring(node.getAbsolutePath().lastIndexOf(File.separator) + 1, node.getAbsolutePath().length());
                objectDocuments = objectDocumentRepository.findByFolder(Integer.parseInt(folderPath));
                if (objectDocuments.size() > 0) {
                    List<File> files = new ArrayList<>();
                    for (File filename : subNote) {
                        files.add(filename);
                    }
                    objectDocuments.forEach(plmObjectDocument -> {
                        File documentFile = getDocumentFile(plmObjectDocument.getDocument().getId());
                        files.add(documentFile);
                    });
                    File[] intArray = new File[files.size()];
                    intArray = files.toArray(intArray);
                    subNote = intArray;
                }

                for (File filename : subNote) {
                    generateFileList(data, out, sourceFolder, filename.getAbsolutePath(), type);
                }*/
            }
        }
    }

    private String getParentFileSystemPath1(Integer fileId, String type, Integer id) {
        String path = "";
        if (type.equals("DOCUMENT")) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            path = plmDocument.getName();
            if (plmDocument.getParentFile() != null) {
                path = visitParentFolder1(plmDocument.getParentFile(), path, type, id);
            }
        }else if (type.equals("PROJECT")) {
            PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
            path = plmProjectFile.getName();
            if (plmProjectFile.getParentFile() != null) {
                path = visitParentFolder1(plmProjectFile.getParentFile(), path, type,id);
            }
        }else if (type.equals("PROJECTACTIVITY")) {
            PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
            path = plmActivityFile.getName();
            if (plmActivityFile.getParentFile() != null) {
                path = visitParentFolder1(plmActivityFile.getParentFile(), path, type,id);
            }
        } else if (type.equals("PROJECTTASK")) {
            PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
            path = plmTaskFile.getName();
            if (plmTaskFile.getParentFile() != null) {
                path = visitParentFolder1(plmTaskFile.getParentFile(), path, type,id);
            }
        }else if (type.equals("PROGRAM")) {
            PLMProgramFile programFile = programFileRepository.findOne(fileId);
            path = programFile.getName();
            if (programFile.getParentFile() != null) {
                path = visitParentFolder1(programFile.getParentFile(), path, type,id);
            }
        }
        return path;
    }

    private String visitParentFolder1(Integer fileId, String path, String type, Integer id) {
        if (type.equals("DOCUMENT")) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            if (plmDocument.getParentFile() != null) {
                path = plmDocument.getName() + File.separator + path;
                if (!id.equals(0) && plmDocument.getId().equals(id)) {
                    return path;
                } else {
                    path = visitParentFolder1(plmDocument.getParentFile(), path, type, id);
                }
            } else {
                path = plmDocument.getName() + File.separator + path;
                return path;
            }
        }else if (type.equals("PROJECT")) {
            PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
            if (plmProjectFile.getParentFile() != null) {
                path = plmProjectFile.getName() + File.separator + path;
                if (!id.equals(0) && plmProjectFile.getId().equals(id)) {
                    return path;
                } else {
                    path = visitParentFolder1(plmProjectFile.getParentFile(), path, type, id);
                }
            } else {
                path = plmProjectFile.getName() + File.separator + path;
                return path;
            }
        }else if (type.equals("PROJECTACTIVITY")) {
            PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
            if (plmActivityFile.getParentFile() != null) {
                path = plmActivityFile.getName() + File.separator + path;
                if (!id.equals(0) && plmActivityFile.getId().equals(id)) {
                    return path;
                } else {
                    path = visitParentFolder1(plmActivityFile.getParentFile(), path, type, id);
                }
            } else {
                path = plmActivityFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PROJECTTASK")) {
            PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
            if (plmTaskFile.getParentFile() != null) {
                path = plmTaskFile.getName() + File.separator + path;
                if (!id.equals(0) && plmTaskFile.getId().equals(id)) {
                    return path;
                } else {
                    path = visitParentFolder1(plmTaskFile.getParentFile(), path, type, id);
                }
            } else {
                path = plmTaskFile.getName() + File.separator + path;
                return path;
            }
        }else if (type.equals("PROGRAM")) {
            PLMProgramFile programFile = programFileRepository.findOne(fileId);
            if (programFile.getParentFile() != null) {
                path = programFile.getName() + File.separator + path;
                if (!id.equals(0) && programFile.getId().equals(id)) {
                    return path;
                } else {
                    path = visitParentFolder1(programFile.getParentFile(), path, type, id);
                }
            } else {
                path = programFile.getName() + File.separator + path;
                return path;
            }
        }
        return path;
    }


    private String getParentFileSystemPath(Integer fileId, String type) {
        String path = "";
        if (type.equals("ITEM")) {
            PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);
            path = plmItemFile.getName();
            if (plmItemFile.getParentFile() != null) {
                path = visitParentFolder(plmItemFile.getParentFile(), path, type);
            }
        } else if (type.equals("PROJECT")) {
            PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
            path = plmProjectFile.getName();
            if (plmProjectFile.getParentFile() != null) {
                path = visitParentFolder(plmProjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("ACTIVITY")) {
            PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
            path = plmActivityFile.getName();
            if (plmActivityFile.getParentFile() != null) {
                path = visitParentFolder(plmActivityFile.getParentFile(), path, type);
            }
        } else if (type.equals("TASKS")) {
            PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
            path = plmTaskFile.getName();
            if (plmTaskFile.getParentFile() != null) {
                path = visitParentFolder(plmTaskFile.getParentFile(), path, type);
            }
        } else if (type.equals("CHANGE")) {
            PLMChangeFile plmChangeFile = changeFileRepository.findOne(fileId);
            path = plmChangeFile.getName();
            if (plmChangeFile.getParentFile() != null) {
                path = visitParentFolder(plmChangeFile.getParentFile(), path, type);
            }
        } else if (type.equals("MFR")) {
            PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
            path = plmManufacturerFile.getName();
            if (plmManufacturerFile.getParentFile() != null) {
                path = visitParentFolder(plmManufacturerFile.getParentFile(), path, type);
            }
        } else if (type.equals("MFRPART")) {
            PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
            path = plmManufacturerPartFile.getName();
            if (plmManufacturerPartFile.getParentFile() != null) {
                path = visitParentFolder(plmManufacturerPartFile.getParentFile(), path, type);
            }
        } else if (type.equals("RM")) {
            RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
            path = rmObjectFile.getName();
            if (rmObjectFile.getParentFile() != null) {
                path = visitParentFolder(rmObjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("GLOSSARY")) {
            PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
            path = plmGlossaryFile.getName();
            if (plmGlossaryFile.getParentFile() != null) {
                path = visitParentFolder(plmGlossaryFile.getParentFile(), path, type);
            }
        } else if (type.equals("INSPECTIONPLAN")) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
            path = inspectionPlanFile.getName();
            if (inspectionPlanFile.getParentFile() != null) {
                path = visitParentFolder(inspectionPlanFile.getParentFile(), path, type);
            }
        } else if (type.equals("ITEMINSPECTION")) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
            path = inspectionFile.getName();
            if (inspectionFile.getParentFile() != null) {
                path = visitParentFolder(inspectionFile.getParentFile(), path, type);
            }
        } else if (type.equals("PROBLEMREPORT")) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            path = problemReportFile.getName();
            if (problemReportFile.getParentFile() != null) {
                path = visitParentFolder(problemReportFile.getParentFile(), path, type);
            }
        } else if (type.equals("NCR")) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
            path = pqmncrFile.getName();
            if (pqmncrFile.getParentFile() != null) {
                path = visitParentFolder(pqmncrFile.getParentFile(), path, type);
            }
        } else if (type.equals("QCR")) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
            path = pqmqcrFile.getName();
            if (pqmqcrFile.getParentFile() != null) {
                path = visitParentFolder(pqmqcrFile.getParentFile(), path, type);
            }
        } else if (type.equals("MESOBJECT")) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            path = mesObjectFile.getName();
            if (mesObjectFile.getParentFile() != null) {
                path = visitParentFolder(mesObjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("PPAPCHECKLIST")) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            path = pqmppapChecklist.getName();
            if (pqmppapChecklist.getParentFile() != null) {
                path = visitParentFolder(pqmppapChecklist.getParentFile(), path, type);
            }
        } else if (type.equals("MFRPARTINSPECTIONREPORT")) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            path = mfrPartInspectionReport.getName();
            if (mfrPartInspectionReport.getParentFile() != null) {
                path = visitParentFolder(mfrPartInspectionReport.getParentFile(), path, type);
            }
        } else if (type.equals("DOCUMENT")) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            path = plmDocument.getName();
            if (plmDocument.getParentFile() != null) {
                path = visitParentFolder(plmDocument.getParentFile(), path, type);
            }
        } else if (type.equals("MROOBJECT")) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            path = mroObjectFile.getName();
            if (mroObjectFile.getParentFile() != null) {
                path = visitParentFolder(mroObjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("PGCOBJECT")) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            path = pgcObjectFile.getName();
            if (pgcObjectFile.getParentFile() != null) {
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("SUPPLIERAUDIT")) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            path = supplierAuditFile.getName();
            if (supplierAuditFile.getParentFile() != null) {
                path = visitParentFolder(supplierAuditFile.getParentFile(), path, type);
            }
        } else if (type.equals("PROGRAM")) {
            PLMProgramFile programFile = programFileRepository.findOne(fileId);
            path = programFile.getName();
            if (programFile.getParentFile() != null) {
                path = visitParentFolder(programFile.getParentFile(), path, type);
            }
        } else if (type.equals("REQUIREMENT")) {
            PLMRequirementFile pgcObjectFile = requirementFileRepository.findOne(fileId);
            path = pgcObjectFile.getName();
            if (pgcObjectFile.getParentFile() != null) {
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            }
        } else if (type.equals("REQUIREMENTDOCUMENT")) {
            PLMRequirementDocumentFile pgcObjectFile = requirementDocumentFileRepository.findOne(fileId);
            path = pgcObjectFile.getName();
            if (pgcObjectFile.getParentFile() != null) {
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            }
        }
        return path;
    }

    private String getDocumentParentFileSystemPathName(Integer fileId) {
        String path = "";
        PLMDocument document = plmDocumentRepository.findOne(fileId);
        path = document.getName();
        if (document.getParentFile() != null) {
            path = visitDocumentParentFolderSystemPath(document.getParentFile(), path);
        }
        return path;
    }

    private String visitDocumentParentFolderSystemPath(Integer fileId, String path) {
        PLMDocument document = plmDocumentRepository.findOne(fileId);

        if (document.getParentFile() != null) {
            path = document.getName() + File.separator + path;
            path = visitDocumentParentFolderSystemPath(document.getParentFile(), path);
        } else {
            path = document.getName() + File.separator + path;
            return path;
        }
        return path;
    }

    private String visitParentFolder(Integer fileId, String path, String type) {
        if (type.equals("ITEM")) {
            PLMItemFile plmItemFile = itemFileRepository.findOne(fileId);

            if (plmItemFile.getParentFile() != null) {
                path = plmItemFile.getName() + File.separator + path;
                path = visitParentFolder(plmItemFile.getParentFile(), path, type);
            } else {
                path = plmItemFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PROJECT")) {
            PLMProjectFile plmProjectFile = projectFileRepository.findOne(fileId);
            if (plmProjectFile.getParentFile() != null) {
                path = plmProjectFile.getName() + File.separator + path;
                path = visitParentFolder(plmProjectFile.getParentFile(), path, type);
            } else {
                path = plmProjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("ACTIVITY")) {
            PLMActivityFile plmActivityFile = activityFileRepository.findOne(fileId);
            if (plmActivityFile.getParentFile() != null) {
                path = plmActivityFile.getName() + File.separator + path;
                path = visitParentFolder(plmActivityFile.getParentFile(), path, type);
            } else {
                path = plmActivityFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("TASKS")) {
            PLMTaskFile plmTaskFile = taskFileRepository.findOne(fileId);
            if (plmTaskFile.getParentFile() != null) {
                path = plmTaskFile.getName() + File.separator + path;
                path = visitParentFolder(plmTaskFile.getParentFile(), path, type);
            } else {
                path = plmTaskFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("CHANGE")) {
            PLMChangeFile plmChangeFile = changeFileRepository.findOne(fileId);
            if (plmChangeFile.getParentFile() != null) {
                path = plmChangeFile.getName() + File.separator + path;
                path = visitParentFolder(plmChangeFile.getParentFile(), path, type);
            } else {
                path = plmChangeFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("MFR")) {
            PLMManufacturerFile plmManufacturerFile = manufacturerFileRepository.findOne(fileId);
            if (plmManufacturerFile.getParentFile() != null) {
                path = plmManufacturerFile.getName() + File.separator + path;
                path = visitParentFolder(plmManufacturerFile.getParentFile(), path, type);
            } else {
                path = plmManufacturerFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("MFRPART")) {
            PLMManufacturerPartFile plmManufacturerPartFile = manufacturerPartFileRepository.findOne(fileId);
            if (plmManufacturerPartFile.getParentFile() != null) {
                path = plmManufacturerPartFile.getName() + File.separator + path;
                path = visitParentFolder(plmManufacturerPartFile.getParentFile(), path, type);
            } else {
                path = plmManufacturerPartFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("RM")) {
            RmObjectFile rmObjectFile = rmObjectFileRepository.findOne(fileId);
            if (rmObjectFile.getParentFile() != null) {
                path = rmObjectFile.getName() + File.separator + path;
                path = visitParentFolder(rmObjectFile.getParentFile(), path, type);
            } else {
                path = rmObjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("GLOSSARY")) {
            PLMGlossaryFile plmGlossaryFile = glossaryFileRepository.findOne(fileId);
            if (plmGlossaryFile.getParentFile() != null) {
                path = plmGlossaryFile.getName() + File.separator + path;
                path = visitParentFolder(plmGlossaryFile.getParentFile(), path, type);
            } else {
                path = plmGlossaryFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("INSPECTIONPLAN")) {
            PQMInspectionPlanFile inspectionPlanFile = inspectionPlanFileRepository.findOne(fileId);
            if (inspectionPlanFile.getParentFile() != null) {
                path = inspectionPlanFile.getName() + File.separator + path;
                path = visitParentFolder(inspectionPlanFile.getParentFile(), path, type);
            } else {
                path = inspectionPlanFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("ITEMINSPECTION")) {
            PQMInspectionFile inspectionFile = inspectionFileRepository.findOne(fileId);
            if (inspectionFile.getParentFile() != null) {
                path = inspectionFile.getName() + File.separator + path;
                path = visitParentFolder(inspectionFile.getParentFile(), path, type);
            } else {
                path = inspectionFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PROBLEMREPORT")) {
            PQMProblemReportFile problemReportFile = problemReportFileRepository.findOne(fileId);
            if (problemReportFile.getParentFile() != null) {
                path = problemReportFile.getName() + File.separator + path;
                path = visitParentFolder(problemReportFile.getParentFile(), path, type);
            } else {
                path = problemReportFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("NCR")) {
            PQMNCRFile pqmncrFile = ncrFileRepository.findOne(fileId);
            if (pqmncrFile.getParentFile() != null) {
                path = pqmncrFile.getName() + File.separator + path;
                path = visitParentFolder(pqmncrFile.getParentFile(), path, type);
            } else {
                path = pqmncrFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("QCR")) {
            PQMQCRFile pqmqcrFile = qcrFileRepository.findOne(fileId);
            if (pqmqcrFile.getParentFile() != null) {
                path = pqmqcrFile.getName() + File.separator + path;
                path = visitParentFolder(pqmqcrFile.getParentFile(), path, type);
            } else {
                path = pqmqcrFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("MESOBJECT")) {
            MESObjectFile mesObjectFile = mesObjectFileRepository.findOne(fileId);
            if (mesObjectFile.getParentFile() != null) {
                path = mesObjectFile.getName() + File.separator + path;
                path = visitParentFolder(mesObjectFile.getParentFile(), path, type);
            } else {
                path = mesObjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PPAPCHECKLIST")) {
            PQMPPAPChecklist pqmppapChecklist = ppapChecklistRepository.findOne(fileId);
            if (pqmppapChecklist.getParentFile() != null) {
                path = pqmppapChecklist.getName() + File.separator + path;
                path = visitParentFolder(pqmppapChecklist.getParentFile(), path, type);
            } else {
                path = pqmppapChecklist.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("MFRPARTINSPECTIONREPORT")) {
            PLMMfrPartInspectionReport mfrPartInspectionReport = mfrPartInspectionReportRepository.findOne(fileId);
            if (mfrPartInspectionReport.getParentFile() != null) {
                path = mfrPartInspectionReport.getName() + File.separator + path;
                path = visitParentFolder(mfrPartInspectionReport.getParentFile(), path, type);
            } else {
                path = mfrPartInspectionReport.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("DOCUMENT")) {
            PLMDocument plmDocument = plmDocumentRepository.findOne(fileId);
            if (plmDocument.getParentFile() != null) {
                path = plmDocument.getName() + File.separator + path;
                // condition needed  id != 0 && plmDocumet.id == id
                path = visitParentFolder(plmDocument.getParentFile(), path, type);
            } else {
                path = plmDocument.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("MROOBJECT")) {
            MROObjectFile mroObjectFile = mroObjectFileRepository.findOne(fileId);
            if (mroObjectFile.getParentFile() != null) {
                path = mroObjectFile.getName() + File.separator + path;
                path = visitParentFolder(mroObjectFile.getParentFile(), path, type);
            } else {
                path = mroObjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PGCOBJECT")) {
            PGCObjectFile pgcObjectFile = pgcObjectFileRepository.findOne(fileId);
            if (pgcObjectFile.getParentFile() != null) {
                path = pgcObjectFile.getName() + File.separator + path;
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            } else {
                path = pgcObjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("SUPPLIERAUDIT")) {
            PQMSupplierAuditFile supplierAuditFile = supplierAuditFileRepository.findOne(fileId);
            if (supplierAuditFile.getParentFile() != null) {
                path = supplierAuditFile.getName() + File.separator + path;
                path = visitParentFolder(supplierAuditFile.getParentFile(), path, type);
            } else {
                path = supplierAuditFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("PROGRAM")) {
            PLMProgramFile programFile = programFileRepository.findOne(fileId);
            if (programFile.getParentFile() != null) {
                path = programFile.getName() + File.separator + path;
                path = visitParentFolder(programFile.getParentFile(), path, type);
            } else {
                path = programFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("REQUIREMENT")) {
            PLMRequirementFile pgcObjectFile = requirementFileRepository.findOne(fileId);
            if (pgcObjectFile.getParentFile() != null) {
                path = pgcObjectFile.getName() + File.separator + path;
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            } else {
                path = pgcObjectFile.getName() + File.separator + path;
                return path;
            }
        } else if (type.equals("REQUIREMENTDOCUMENT")) {
            PLMRequirementDocumentFile pgcObjectFile = requirementDocumentFileRepository.findOne(fileId);
            if (pgcObjectFile.getParentFile() != null) {
                path = pgcObjectFile.getName() + File.separator + path;
                path = visitParentFolder(pgcObjectFile.getParentFile(), path, type);
            } else {
                path = pgcObjectFile.getName() + File.separator + path;
                return path;
            }
        }
        return path;
    }


    public String getDocumentParentFileSystemPath(Integer fileId) {
        String path = "";
        PLMFile file = plmDocumentRepository.findOne(fileId);
        path = File.separator + fileId + "";
        if (file.getParentFile() != null) {
            path = visitDocumentParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId();
        }
        return path;
    }


    private String visitDocumentParentFolder(Integer fileId, String path) {
        PLMFile file = plmDocumentRepository.findOne(fileId);
        if (file.getParentFile() != null) {
            path = File.separator + file.getId() + path;
            path = visitDocumentParentFolder(file.getParentFile(), path);
        } else {
            path = File.separator + file.getId() + path;
            return path;
        }
        return path;
    }

    private File getDocumentFile(Integer fileId) {
        PLMFile plmFile = fileRepository.findOne(fileId);
        if (plmFile == null) {
            throw new ResourceNotFoundException();
        }
        String path = fileSystemService.getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + "documents" + getDocumentParentFileSystemPath(fileId);
        File file = new File(path);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }
}   

