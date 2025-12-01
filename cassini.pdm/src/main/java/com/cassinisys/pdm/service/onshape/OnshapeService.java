package com.cassinisys.pdm.service.onshape;

import com.cassinisys.pdm.model.ObjectType;
import com.cassinisys.pdm.model.PDMFile;
import com.cassinisys.pdm.model.PDMFolder;
import com.cassinisys.pdm.model.PDMVault;
import com.cassinisys.pdm.model.onshape.*;
import com.cassinisys.pdm.repo.FolderRepository;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.repo.VaultRepository;
import com.cassinisys.pdm.service.PDMFileService;
import com.cassinisys.pdm.service.onshape.requests.*;
import com.cassinisys.pdm.service.onshape.responses.FoldersCreateFolderResponse;
import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectAttributeId;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.ObjectAttributeRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.ObjectAttributeService;
import com.cassinisys.platform.service.core.ObjectTypeAttributeService;
import com.cassinisys.platform.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onshape.api.Onshape;
import com.onshape.api.exceptions.OnshapeException;
import com.onshape.api.requests.*;
import com.onshape.api.responses.*;
import com.onshape.api.types.Blob;
import com.onshape.api.types.WV;
import com.onshape.api.types.WVM;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class OnshapeService {
    public static String ONSHAPE_VAULT_ID_ATT = "Onshape Vault ID";
    public static String ONSHAPE_FOLDER_ID_ATT = "Onshape Folder ID";
    public static String ONSHAPE_DOCUMENT_ID_ATT = "Onshape Document ID";
    public static String ONSHAPE_WORKSPACE_ID_ATT = "Onshape Workspace ID";
    public static String ONSHAPE_ELEMENT_ID_ATT = "Onshape Element ID";
    public static String ONSHAPE_VERSION_ID_ATT = "Onshape Version ID";
    public static String PART_REFERENCES_ATT = "Part References";

    private Onshape onshape = new Onshape();

    @Autowired
    private ObjectAttributeService objectAttributeService;
    @Autowired
    private ObjectAttributeRepository objectAttributeRepository;
    @Autowired
    private ObjectTypeAttributeService objectTypeAttributeService;

    @Autowired
    private ObjectTypeAttributeRepository objectTypeAttributeRepository;

    @Autowired
    private PDMFileService pdmFileService;

    @Autowired
    private PDMFileRepository pdmFileRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private VaultRepository vaultRepository;

    @PostConstruct
    private void postConstruct() {
        String accessKey = "GsM28rxnnz0jAi7yjWQOJe5A";
        String secretKey = "uAtM2RLzir8ZrK1WfuA5Wm2YocBGAy1zahxjCCduNf9HpJsR";
        onshape.setAPICredentials(accessKey, secretKey);
    }


    private void updateOnshapeIds(final PDMFile pdmFile, final String tid) {
        final String tenantId = TenantManager.get().getTenantId();

        try {
            Thread t = new Thread(() -> {
                TenantManager.get().setTenantId(tenantId);
                checkTranslationStatusAndGetOnshapeIds(pdmFile, tid);
            });
            t.start();
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkTranslationStatusAndGetOnshapeIds(final PDMFile pdmFile, final String tid) {
        try {
            TranslationsGetTranslationResponse response = onshape.translations().getTranslation().call(tid);
            if(response.getRequestState().equalsIgnoreCase("ACTIVE")) {
                Thread.sleep(10000);
                checkTranslationStatusAndGetOnshapeIds(pdmFile, tid);
            }
            else if(response.getRequestState().equalsIgnoreCase("DONE")) {
                String[] ids = response.getResultElementIds();
                if(ids.length == 1) {
                    String did = response.getResultDocumentId();
                    String wid = response.getResultWorkspaceId();
                    String eid = ids[0];

                    MetadataGetPartListMetadataResponse metadataResponse = onshape.metadata().getPartListMetadata()
                            .call(did, WVM.Workspace, wid, eid);

                    MetadataGetPartListMetadataResponseItems[] items = metadataResponse.getItems();

                    if(items.length > 0) {
                        MetadataGetPartListMetadataResponseItems partItem = items[0];

                        String newName = pdmFile.getName();
                        newName = newName.trim();
                        newName = newName.substring(0, newName.lastIndexOf('.'));
                        MetadataGetPartMetadataResponse mRes = onshape.metadata().getPartMetadata().
                                call(partItem.partId, did, WVM.Workspace, wid, eid);

                        saveOnshapeProperties(pdmFile, newName, partItem.partId, did, wid, eid, mRes.getHref());

                        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_DOCUMENT_ID_ATT,
                                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                        ObjectAttribute attValue = new ObjectAttribute();
                        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                        attValue.setStringValue(did);
                        objectAttributeService.createAttribute(attValue);

                        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_WORKSPACE_ID_ATT,
                                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                        attValue = new ObjectAttribute();
                        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                        attValue.setStringValue(wid);
                        objectAttributeService.createAttribute(attValue);

                        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_ELEMENT_ID_ATT,
                                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                        attValue = new ObjectAttribute();
                        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                        attValue.setStringValue(eid);
                        objectAttributeService.createAttribute(attValue);

                        DocumentsCreateVersionResponse versionResponse = onshape.documents()
                                .createVersion()
                                .documentId(did)
                                .description("Initial import from Cassini")
                                .name("V1")
                                .call(response.getResultDocumentId());
                        String versionId = null;

                        if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                            versionId = versionResponse.getVersion().id;
                        }
                        else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                            versionId = (String) versionResponse.getOtherProperties().get("id");
                        }
                        if(versionId != null) {
                            oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VERSION_ID_ATT,
                                    com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                            attValue = new ObjectAttribute();
                            attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                            attValue.setStringValue(versionId);
                            objectAttributeService.createAttribute(attValue);
                        }

                        deleteEmptyElements(did, wid, eid);

                        translateDrawingFileIfExists(pdmFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void translateDrawingFileIfExists(PDMFile pdmFile) {
        Integer folder = pdmFile.getFolder();
        String name = pdmFile.getName();
        name = name.replace(".SLDPRT", ".DXF");
        PDMFile dxfFile = pdmFileRepository.findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(folder, name);
        if(dxfFile != null) {
            translateFile(dxfFile);
        }
    }

    private void saveOnshapeProperties(PDMFile pdmFile, String name, String partId, String did, String wid, String eid, String href) {
        try {
            CustomMetadataUpdatePartMetadataRequestItemsProperties props1 = new CustomMetadataUpdatePartMetadataRequestItemsProperties();
            props1.propertyId = "57f3fb8efa3416c06701d60d";
            props1.value = name;

            CustomMetadataUpdatePartMetadataRequestItemsProperties props2 = new CustomMetadataUpdatePartMetadataRequestItemsProperties();
            props2.propertyId = "57f3fb8efa3416c06701d60f";
            props2.value = getFileAttribute(pdmFile,"Part Number");

            CustomMetadataUpdatePartMetadataRequestItemsProperties props3 = new CustomMetadataUpdatePartMetadataRequestItemsProperties();
            props3.propertyId = "57f3fb8efa3416c06701d60e";
            props3.value = getFileAttribute(pdmFile,"Part Description");

            CustomMetadataUpdatePartMetadataRequestItems reqItems = new CustomMetadataUpdatePartMetadataRequestItems();
            reqItems.href = href;
            reqItems.properties = new CustomMetadataUpdatePartMetadataRequestItemsProperties[]{props1, props2, props3};

            CustomMetadataUpdatePartMetadataRequest req = new CustomMetadataUpdatePartMetadataRequest(onshape);
            req.items(new CustomMetadataUpdatePartMetadataRequestItems[]{reqItems});
            MetadataUpdatePartMetadataResponse metaUpdateRes = req
                    .call(partId, did, WV.Workspace, wid, eid);
        } catch (OnshapeException e) {
            e.printStackTrace();
        }

    }

    private void deleteEmptyElements(String did, String wid, String elemToKeep) {
        try {
            DocumentsGetElementListResponse response = onshape.documents().getElementList().call(did, WVM.Workspace, wid);
            DocumentsGetElementListResponseElements[] elements = response.getElements();
            for (DocumentsGetElementListResponseElements element : elements) {
                String eid = element.id;
                if(!eid.equalsIgnoreCase(elemToKeep)) {
                    onshape.elements().deleteElement().call(did, wid, eid);
                }
            }
        } catch (OnshapeException e) {
            e.printStackTrace();
        }
    }

    private String getFileAttribute(PDMFile pdmFile, String attName) {
        String value = "";

        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(attName,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFile.getId(), oAtt.getId());
            if(attValue != null) {
                value = attValue.getStringValue();
            }
        }

        return value;
    }

    private void saveOnshapeIds(PDMFile pdmFile, OnshapeIds onshapeIds) {
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_DOCUMENT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        ObjectAttribute attValue = new ObjectAttribute();
        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
        attValue.setStringValue(onshapeIds.getDocumentId());
        objectAttributeService.createAttribute(attValue);

        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_WORKSPACE_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        attValue = new ObjectAttribute();
        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
        attValue.setStringValue(onshapeIds.getWorkspaceId());
        objectAttributeService.createAttribute(attValue);

        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_ELEMENT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        attValue = new ObjectAttribute();
        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
        attValue.setStringValue(onshapeIds.getElementId());
        objectAttributeService.createAttribute(attValue);

        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VERSION_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        attValue = new ObjectAttribute();
        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
        attValue.setStringValue(onshapeIds.getVersionId());
        objectAttributeService.createAttribute(attValue);
    }

    public void savePartReferences(PDMFile assyFile, MultipartFile multipartFile) {
        checkAndCreateOnshapeAttributes();

        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(PART_REFERENCES_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        if(oAtt != null) {
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(multipartFile.getInputStream(), writer, StandardCharsets.UTF_8);

                ObjectAttribute attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(assyFile.getId(), oAtt.getId()));
                attValue.setStringValue(writer.toString());
                objectAttributeService.createAttribute(attValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPartReferences(PDMFile sldasmFile) {
        String json = null;
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(PART_REFERENCES_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(sldasmFile.getId(), oAtt.getId());
            json = attValue.getStringValue();
        }

        return json;
    }

    private OnshapeIds checkAndCreatePart(Component component) {
        OnshapeIds onshapeIds = null;
        PDMFile pdmFile = pdmFileRepository.findOne(component.getCassiniId());//pdmFileRepository.findByVaultAndNameIgnoreCase(sldasmFile.getVault(), component.getName());
        if(pdmFile != null) {
            onshapeIds = getOnshapeIds(pdmFile);
            if(onshapeIds == null) {
                onshapeIds = translateFile(pdmFile);
            }
        }
        return onshapeIds;
    }

    private OnshapeIds translateAssemblyFile(PDMFile sldasmFile) throws Exception {
        OnshapeIds onshapeIds = getOnshapeIds(sldasmFile);
        if(onshapeIds == null) {
            String json = getPartReferences(sldasmFile);

            ObjectMapper mapper = new ObjectMapper();
            //OnshapeData onshapeData = mapper.readValue(json, OnshapeData.class);
            Assembly assembly = mapper.readValue(json, Assembly.class);
            //Assembly assembly = onshapeData.getRootAssembly();

            onshapeIds = checkAndCreateDocument(sldasmFile);
            //First create the onshape assembly
            String name = assembly.getName().substring(0, assembly.getName().lastIndexOf('.'));
            AssembliesCreateAssemblyResponse createAssemblyResponse = onshape.assemblies().createAssembly()
                    .name(name)
                    .call(onshapeIds.getDocumentId(), onshapeIds.getWorkspaceId());
            String assemblyId = createAssemblyResponse.getId();
            onshapeIds.setElementId(assemblyId);

            DocumentsCreateVersionResponse versionResponse = onshape.documents()
                    .createVersion()
                    .documentId(onshapeIds.getDocumentId())
                    .description("Initial import from Cassini")
                    .name("Preliminary")
                    .call(onshapeIds.getDocumentId());

            String versionId  = null;
            if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                versionId = versionResponse.getVersion().id;
            }
            else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                versionId = (String) versionResponse.getOtherProperties().get("id");
            }
            if(versionId != null) {
                onshapeIds.setVersionId(versionId);
            }

            //Now create the instances
            List<Component> components = assembly.getChildren();

            List<AssembliesInsertTransformedInstancesRequestTransformGroups> tGroupsList = new ArrayList<>();

            for (int i = 0; i < components.size(); i++) {
                Component comp = components.get(i);
                OnshapeIds compIds = checkAndCreatePart(comp);
                System.out.println("Adding " + comp.getName());

                if (compIds.getElementId() == null) {
                    System.out.println("ERROR: " + comp.getName() + " doesn't have a corresponding element");
                } else {
                    String compName = comp.getName();
                    compName = compName.toLowerCase();
                    if(compName.endsWith(".sldprt")) {
                        MetadataGetPartListMetadataResponse metadataResponse = onshape.metadata().getPartListMetadata()
                                .call(compIds.getDocumentId(), WVM.Workspace, compIds.getWorkspaceId(), compIds.getElementId());

                        MetadataGetPartListMetadataResponseItems[] items = metadataResponse.getItems();
                        List<AssembliesInsertTransformedInstancesRequestTransformGroupsInstances> instList = new ArrayList<>();
                        for (int j = 0; j < items.length; j++) {
                            if (items[j].partType.equalsIgnoreCase("solid") ||
                                    items[j].partType.equalsIgnoreCase("composite")) {
                                AssembliesInsertTransformedInstancesRequestTransformGroupsInstances instance = AssembliesInsertTransformedInstancesRequestTransformGroupsInstances.builder()
                                        .documentId(compIds.getDocumentId())
                                        .elementId(compIds.getElementId())
                                        .partId(items[j].partId)
                                        .versionId(compIds.getVersionId())
                                        .isWholePartStudio(false)
                                        .build();
                                instList.add(instance);
                            }
                        }

                        AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[] instances = new AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[instList.size()];
                        for (int j = 0; j < instances.length; j++) {
                            instances[j] = instList.get(j);
                        }

                        AssembliesInsertTransformedInstancesRequestTransformGroups group = AssembliesInsertTransformedInstancesRequestTransformGroups.builder()
                                .transform(comp.convertToOnshape())
                                .instances(instances)
                                .build();
                        tGroupsList.add(group);
                    }
                    else if(compName.endsWith(".sldasm")) {
                        AssembliesInsertTransformedInstancesRequestTransformGroupsInstances instance = AssembliesInsertTransformedInstancesRequestTransformGroupsInstances.builder()
                                .documentId(compIds.getDocumentId())
                                .elementId(compIds.getElementId())
                                .partId("JFD")
                                .versionId(compIds.getVersionId())
                                .isWholePartStudio(false)
                                .isAssembly(true)
                                .build();
                        AssembliesInsertTransformedInstancesRequestTransformGroups group = AssembliesInsertTransformedInstancesRequestTransformGroups.builder()
                                .transform(comp.convertToOnshape())
                                .instances(new AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[]{instance})
                                .build();
                        tGroupsList.add(group);
                    }
                }
            }

            if (tGroupsList.size() > 0) {
                AssembliesInsertTransformedInstancesRequestTransformGroups[] tGroups = new AssembliesInsertTransformedInstancesRequestTransformGroups[tGroupsList.size()];
                for (int i = 0; i < tGroupsList.size(); i++) {
                    tGroups[i] = tGroupsList.get(i);
                }

                try {
                    AssembliesInsertTransformedInstancesResponse response = onshape.assemblies().insertTransformedInstances()
                            .transformGroups(tGroups)
                            .call(onshapeIds.getDocumentId(), onshapeIds.getWorkspaceId(), assemblyId);
                } catch (OnshapeException e) {
                    e.printStackTrace();
                }
            }

            versionResponse = onshape.documents()
                    .createVersion()
                    .documentId(onshapeIds.getDocumentId())
                    .description("After adding instances")
                    .name("V1")
                    .call(onshapeIds.getDocumentId());

            versionId  = null;
            if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                versionId = versionResponse.getVersion().id;
            }
            else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                versionId = (String) versionResponse.getOtherProperties().get("id");
            }
            if(versionId != null) {
                onshapeIds.setVersionId(versionId);
            }

            deleteEmptyElements(onshapeIds.getDocumentId(), onshapeIds.getWorkspaceId(), onshapeIds.getElementId());

            saveOnshapeIds(sldasmFile, onshapeIds);
        }

        return onshapeIds;
    }


    private void visitPaths(File dir, Map<String, String> paths) {
        File[] children = dir.listFiles();
        if(children != null) {
            for (File child : children) {
                if(child.isDirectory()) {
                    visitPaths(child, paths);
                }
                else {
                    paths.put(child.getName(), child.getPath());
                }
            }
        }
    }

    public List<IdMap> getFileToOnshapeIdMap(Integer[] ids) {
        List<IdMap> maps = new ArrayList<>();

        for(Integer id : ids) {
            IdMap idMap = new IdMap();
            idMap.setFileId(id);
            OnshapeIds onshapeIds = getOnshapeIds(pdmFileService.get(id));
            idMap.setOnshapeIds(onshapeIds);
            maps.add(idMap);
        }

        return maps;
    }

    private OnshapeIds checkAndCreateDocument(PDMFile pdmFile) {
        OnshapeIds onshapeIds = null;
        try {

            String docName = pdmFile.getName();
            docName = docName.trim();
            if(!docName.toLowerCase().endsWith(".dxf")) {
                docName = docName.substring(0, docName.lastIndexOf('.'));
            }

            PDMFolder pdmFolder = folderRepository.findOne(pdmFile.getFolder());
            String parentId = createOnshapeFolder(pdmFolder);
            CustomDocumentCreateRequest request = new CustomDocumentCreateRequest(onshape);
            request.setOwnerId("58b5c529b9bece0ef9e9e55c");
            request.setOwnerType(1);
            request.setName(docName);
            request.setParentId(parentId);

            DocumentsCreateDocumentResponse response = request.call();
            onshapeIds = new OnshapeIds();
            onshapeIds.setDocumentId(response.getId());
            onshapeIds.setWorkspaceId(response.getDefaultWorkspace().id);
        } catch (OnshapeException e) {
            e.printStackTrace();
        }

        return onshapeIds;
    }

    @Transactional(readOnly = false)
    public OnshapeIds translateFile(PDMFile pdmFile) {
        checkAndCreateOnshapeAttributes();
        String name = pdmFile.getName();
        try {
            if(name.toLowerCase().endsWith(".sldprt")) {
                return translatePartFile(pdmFile);
            }
            else if(name.toLowerCase().endsWith(".sldasm")) {
                //return translateAssemblyFile(pdmFile);
                return processAssembly(pdmFile);
            }
            else if(name.toLowerCase().endsWith(".dxf")) {
                return translateDrawings(pdmFile);
            }
            else {
                throw new CassiniException("Unsupported file format.");
            }
        } catch (Exception e) {
            throw new CassiniException(e.getMessage());
        }
    }

    private OnshapeIds processAssembly(PDMFile pdmFile) {
        System.out.println();
        System.out.println("***** TRANSLATION STARTED *****");
        System.out.println();

        List<PDMFile> partFiles = new ArrayList<>();

        System.out.println("Collecting untranslated part files");
        Map<Integer, PDMFile> fileLookup = new HashMap<>();
        visitHierarchy(pdmFile, partFiles, fileLookup);

        List<PDMFile> untranslatedPartFiles = new ArrayList<>();
        Map<Integer, PDMFile> map = new HashMap<>();
        for(PDMFile partFile : partFiles) {
            OnshapeIds onshapeIds = getOnshapeIds(partFile);
            if(onshapeIds == null && !map.containsKey(partFile.getId())) {
                untranslatedPartFiles.add(partFile);
                map.put(partFile.getId(), partFile);
            }
        }

        System.out.println("Uploading files for translations");
        List<String> translationIds = new ArrayList<>();
        Map<PDMFile, String> mapTranslations = new LinkedHashMap<>();
        int counter = 1;
        int total = untranslatedPartFiles.size();
        for(PDMFile untranslatedFile : untranslatedPartFiles) {
            OnshapeIds ids = checkAndCreateDocument(untranslatedFile);

            File sldprtFile = pdmFileService.getFile(untranslatedFile.getId());
            System.out.println("(" + counter + " of " + total + ") Uploading '" + untranslatedFile.getPath() + "'");
            String contentDispString = ContentDisposition.type("attachment")
                    .fileName(untranslatedFile.getName())
                    .modificationDate(new Date(sldprtFile.lastModified()))
                    .size(sldprtFile.length())
                    .build()
                    .toString();

            try {
                String formatName = "ONSHAPE";
                TranslationsCreateTranslationResponse response = onshape.translations()
                        .createTranslation()
                        .createComposite(true)
                        .file(new Blob(new FileInputStream(sldprtFile), contentDispString))
                        .yAxisIsUp(Boolean.FALSE)
                        .storeInDocument(true)
                        .formatName(formatName)
                        .flattenAssemblies(Boolean.FALSE)
                        .call(ids.getDocumentId(), ids.getWorkspaceId());

                String transId = response.getId();
                translationIds.add(transId);
                mapTranslations.put(untranslatedFile, transId);
                counter++;
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        final Map<String, WDE> translatedIds = new LinkedHashMap<>();

        if(translationIds.size() > 0) {
            System.out.println("Checking for status for all translations");
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
            ScheduledFuture<?> scheduledFuture = ses.scheduleAtFixedRate(checkTranslationStatus(translatedIds, translationIds),
                    0, 1, TimeUnit.MINUTES);

            while (true) {
                try {
                    Thread.sleep(10 * 1000);
                    if (translatedIds.size() == translationIds.size()) {
                        scheduledFuture.cancel(true);
                        ses.shutdown();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("No files to translate");
        }

        System.out.println("Post processing translated files");
        postProcessAfterTranslation(mapTranslations, translatedIds);


        Map<Integer, PDMFile> assembliesLookup = new HashMap<>();
        System.out.println("Processing main assembly hierarchy");
        OnshapeIds onshapeIds = processAssemblyHierarchy(pdmFile, assembliesLookup);

        System.out.println();
        System.out.println("***** TRANSLATION COMPLETED SUCCESSFULLY *****");
        System.out.println();

        return onshapeIds;
    }

    private OnshapeIds processAssemblyHierarchy(PDMFile sldasmFile, Map<Integer, PDMFile> assembliesLookup) {

        OnshapeIds parentOnshapeIds = getOnshapeIds(sldasmFile);
        if(parentOnshapeIds == null) {
            String json = getPartReferences(sldasmFile);

            ObjectMapper mapper = new ObjectMapper();
            try {
                Assembly assembly = mapper.readValue(json, Assembly.class);

                parentOnshapeIds = checkAndCreateDocument(sldasmFile);

                System.out.println("Creating onshape assembly document for the assembly - " + sldasmFile.getName());

                //First create the onshape assembly
                String name = assembly.getName().substring(0, assembly.getName().lastIndexOf('.'));
                AssembliesCreateAssemblyResponse createAssemblyResponse = onshape.assemblies().createAssembly()
                        .name(name)
                        .call(parentOnshapeIds.getDocumentId(), parentOnshapeIds.getWorkspaceId());
                String assemblyId = createAssemblyResponse.getId();
                parentOnshapeIds.setElementId(assemblyId);

                System.out.println("  Creating document version");
                DocumentsCreateVersionResponse versionResponse = onshape.documents()
                        .createVersion()
                        .documentId(parentOnshapeIds.getDocumentId())
                        .description("Initial import from Cassini")
                        .name("Preliminary")
                        .call(parentOnshapeIds.getDocumentId());

                String versionId  = null;
                if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                    versionId = versionResponse.getVersion().id;
                }
                else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                    versionId = (String) versionResponse.getOtherProperties().get("id");
                }
                if(versionId != null) {
                    parentOnshapeIds.setVersionId(versionId);
                }

                //Now create the instances
                List<Component> components = assembly.getChildren();
                List<AssembliesInsertTransformedInstancesRequestTransformGroups> tGroupsList = new ArrayList<>();

                System.out.println("  Adding instances to the assembly - " + sldasmFile.getName());
                for (int i = 0; i < components.size(); i++) {
                    Component comp = components.get(i);
                    PDMFile childFile = pdmFileRepository.findOne(comp.getCassiniId());

                    String compName = comp.getName();
                    compName = compName.toLowerCase();
                    if(compName.endsWith(".sldasm")) {

                        if(!assembliesLookup.containsKey(childFile.getId())) {
                            assembliesLookup.put(childFile.getId(), childFile);
                            processAssemblyHierarchy(childFile, assembliesLookup);
                        }

                        OnshapeIds compIds = getOnshapeIds(childFile);
                        if(compIds == null) continue;

                        AssembliesInsertTransformedInstancesRequestTransformGroupsInstances instance = AssembliesInsertTransformedInstancesRequestTransformGroupsInstances.builder()
                                .documentId(compIds.getDocumentId())
                                .elementId(compIds.getElementId())
                                .partId("JFD")
                                .versionId(compIds.getVersionId())
                                .isWholePartStudio(false)
                                .isAssembly(true)
                                .build();
                        AssembliesInsertTransformedInstancesRequestTransformGroups group = AssembliesInsertTransformedInstancesRequestTransformGroups.builder()
                                .transform(comp.convertToOnshape())
                                .instances(new AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[]{instance})
                                .build();
                        tGroupsList.add(group);
                    }
                    else if(compName.endsWith(".sldprt")){
                        OnshapeIds compIds = getOnshapeIds(childFile);
                        if(compIds == null) continue;

                        MetadataGetPartListMetadataResponse metadataResponse = onshape.metadata().getPartListMetadata()
                                .call(compIds.getDocumentId(), WVM.Workspace, compIds.getWorkspaceId(), compIds.getElementId());

                        MetadataGetPartListMetadataResponseItems[] items = metadataResponse.getItems();
                        List<AssembliesInsertTransformedInstancesRequestTransformGroupsInstances> instList = new ArrayList<>();
                        for (int j = 0; j < items.length; j++) {
                            if (items[j].partType.equalsIgnoreCase("solid") ||
                                    items[j].partType.equalsIgnoreCase("composite")) {
                                AssembliesInsertTransformedInstancesRequestTransformGroupsInstances instance = AssembliesInsertTransformedInstancesRequestTransformGroupsInstances.builder()
                                        .documentId(compIds.getDocumentId())
                                        .elementId(compIds.getElementId())
                                        .partId(items[j].partId)
                                        .versionId(compIds.getVersionId())
                                        .isWholePartStudio(false)
                                        .build();
                                instList.add(instance);
                            }
                        }

                        AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[] instances = new AssembliesInsertTransformedInstancesRequestTransformGroupsInstances[instList.size()];
                        for (int j = 0; j < instances.length; j++) {
                            instances[j] = instList.get(j);
                        }

                        AssembliesInsertTransformedInstancesRequestTransformGroups group = AssembliesInsertTransformedInstancesRequestTransformGroups.builder()
                                .transform(comp.convertToOnshape())
                                .instances(instances)
                                .build();
                        tGroupsList.add(group);
                    }
                }

                if (tGroupsList.size() > 0) {
                    System.out.println("  Inserting instances into assembly");
                    AssembliesInsertTransformedInstancesRequestTransformGroups[] tGroups = new AssembliesInsertTransformedInstancesRequestTransformGroups[tGroupsList.size()];
                    for (int i = 0; i < tGroupsList.size(); i++) {
                        tGroups[i] = tGroupsList.get(i);
                    }

                    try {
                        AssembliesInsertTransformedInstancesResponse response = onshape.assemblies().insertTransformedInstances()
                                .transformGroups(tGroups)
                                .call(parentOnshapeIds.getDocumentId(), parentOnshapeIds.getWorkspaceId(), assemblyId);
                    } catch (OnshapeException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("  Creating new version after inserting instances");
                versionResponse = onshape.documents()
                        .createVersion()
                        .documentId(parentOnshapeIds.getDocumentId())
                        .description("After adding instances")
                        .name("V1")
                        .call(parentOnshapeIds.getDocumentId());

                versionId  = null;
                if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                    versionId = versionResponse.getVersion().id;
                }
                else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                    versionId = (String) versionResponse.getOtherProperties().get("id");
                }
                if(versionId != null) {
                    parentOnshapeIds.setVersionId(versionId);
                }

                System.out.println("  Deleting any empty elements in onshape document");
                deleteEmptyElements(parentOnshapeIds.getDocumentId(), parentOnshapeIds.getWorkspaceId(), parentOnshapeIds.getElementId());

                System.out.println("  Saving onshape ids in Cassini");
                saveOnshapeIds(sldasmFile, parentOnshapeIds);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return parentOnshapeIds;
    }

    private void postProcessAfterTranslation(Map<PDMFile, String> mapTranslations, Map<String, WDE> translatedIds) {
        List<PDMFile> pdmFiles = new ArrayList<>(mapTranslations.keySet());
        int counter = 0;
        for(PDMFile pdmFile : pdmFiles) {
            String transId = mapTranslations.get(pdmFile);
            WDE wde = translatedIds.get(transId);

            if(wde == null) continue;

            String wid = wde.workspace;
            String did = wde.document;
            String eid = wde.element;

            try {
                counter++;
                System.out.println("(" + counter + " of " + pdmFiles.size() + ") Getting parts list metadata for - " + pdmFile.getName());
                MetadataGetPartListMetadataResponse metadataResponse = onshape.metadata().getPartListMetadata()
                        .call(did, WVM.Workspace, wid, eid);

                MetadataGetPartListMetadataResponseItems[] items = metadataResponse.getItems();

                if(items.length > 0) {
                    MetadataGetPartListMetadataResponseItems partItem = items[0];

                    String newName = pdmFile.getName();
                    newName = newName.trim();
                    newName = newName.substring(0, newName.lastIndexOf('.'));
                    MetadataGetPartMetadataResponse mRes = onshape.metadata().getPartMetadata().
                            call(partItem.partId, did, WVM.Workspace, wid, eid);

                    System.out.println("  Saving cassini properties in onshape");
                    saveOnshapeProperties(pdmFile, newName, partItem.partId, did, wid, eid, mRes.getHref());

                    System.out.println("  Saving onshape IDs to Cassini");
                    ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_DOCUMENT_ID_ATT,
                            com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                    ObjectAttribute attValue = new ObjectAttribute();
                    attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                    attValue.setStringValue(did);
                    objectAttributeService.createAttribute(attValue);

                    oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_WORKSPACE_ID_ATT,
                            com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                    attValue = new ObjectAttribute();
                    attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                    attValue.setStringValue(wid);
                    objectAttributeService.createAttribute(attValue);

                    oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_ELEMENT_ID_ATT,
                            com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                    attValue = new ObjectAttribute();
                    attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                    attValue.setStringValue(eid);
                    objectAttributeService.createAttribute(attValue);

                    System.out.println("  Creating new version after adding properties");
                    DocumentsCreateVersionResponse versionResponse = onshape.documents()
                            .createVersion()
                            .documentId(did)
                            .description("Initial import from Cassini")
                            .name("V1")
                            .call(did);
                    String versionId = null;

                    if(versionResponse.getVersion() != null && versionResponse.getVersion().id != null) {
                        versionId = versionResponse.getVersion().id;
                    }
                    else if(versionResponse.getOtherProperties() != null && versionResponse.getOtherProperties().get("id") != null) {
                        versionId = (String) versionResponse.getOtherProperties().get("id");
                    }
                    if(versionId != null) {
                        oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VERSION_ID_ATT,
                                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                        attValue = new ObjectAttribute();
                        attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                        attValue.setStringValue(versionId);
                        objectAttributeService.createAttribute(attValue);
                    }

                    System.out.println("  Deleting any empty elements");
                    deleteEmptyElements(did, wid, eid);

                    System.out.println("  Translating any related drawings");
                    translateDrawingFileIfExists(pdmFile);
                }
            } catch (OnshapeException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable checkTranslationStatus(Map<String, WDE> translatedIds, List<String> translationIds) {
        Runnable runnable = () -> {
            try {
                for(String transId : translationIds) {
                    TranslationsGetTranslationResponse response = onshape.translations().getTranslation().call(transId);
                    if (response.getRequestState().equalsIgnoreCase("DONE")) {
                        String[] ids = response.getResultElementIds();
                        if(ids.length == 1) {
                            String did = response.getResultDocumentId();
                            String wid = response.getResultWorkspaceId();
                            String eid = ids[0];

                            translatedIds.put(transId, new WDE(wid, did, eid));
                        }
                        else {
                            translatedIds.put(transId, null);
                        }
                    }
                    else if (response.getRequestState().equalsIgnoreCase("FAILED")) {
                        translatedIds.put(transId, null);
                    }
                }
            } catch (OnshapeException e) {
                e.printStackTrace();
            }
        };

        return runnable;
    }

    private void visitHierarchy(PDMFile pdmFile, List<PDMFile> partFiles, Map<Integer, PDMFile> fileLookup) {
        OnshapeIds onshapeIds = getOnshapeIds(pdmFile);
        if(onshapeIds == null) {
            String json = getPartReferences(pdmFile);

            ObjectMapper mapper = new ObjectMapper();
            try {
                Assembly assembly = mapper.readValue(json, Assembly.class);

                //Now create the instances
                List<Component> components = assembly.getChildren();

                for (int i = 0; i < components.size(); i++) {
                    Component comp = components.get(i);
                    PDMFile childFile = pdmFileRepository.findOne(comp.getCassiniId());

                    if(childFile == null || fileLookup.containsKey(childFile.getId())) continue;

                    fileLookup.put(childFile.getId(), childFile);

                    String compName = comp.getName();
                    compName = compName.toLowerCase();
                    if(compName.endsWith(".sldprt")) {
                        partFiles.add(childFile);
                    }
                    else if(compName.endsWith(".sldasm")) {
                        visitHierarchy(childFile, partFiles, fileLookup);
                    }
                }

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private OnshapeIds translateDrawings(PDMFile pdmFile) {
        OnshapeIds ids = getOnshapeIds(pdmFile);
        if(ids == null) {
            ids = checkAndCreateDocument(pdmFile);
            File file = pdmFileService.getFile(pdmFile.getId());

            try {
                String contentDispString = ContentDisposition.type("attachment")
                        .fileName(pdmFile.getName())
                        .modificationDate(new Date(file.lastModified()))
                        .size(file.length())
                        .build()
                        .toString();

                BlobElementsUploadFileCreateElementResponse response = onshape.blobElements()
                        .uploadFileCreateElement()
                        .yAxisIsUp(false)
                        .translate(true)
                        .flattenAssemblies(false)
                        .createDrawingIfPossible(true)
                        .file(new Blob(new FileInputStream(file), contentDispString))
                        .call(ids.getDocumentId(), ids.getWorkspaceId());

                String did = ids.getDocumentId();
                String wid = ids.getWorkspaceId();
                String eid = response.getId();

                ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_DOCUMENT_ID_ATT,
                        com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                ObjectAttribute attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                attValue.setStringValue(did);
                objectAttributeService.createAttribute(attValue);

                oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_WORKSPACE_ID_ATT,
                        com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                attValue.setStringValue(wid);
                objectAttributeService.createAttribute(attValue);

                oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_ELEMENT_ID_ATT,
                        com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(pdmFile.getId(), oAtt.getId()));
                attValue.setStringValue(eid);
                objectAttributeService.createAttribute(attValue);

            } catch (IOException | OnshapeException e) {
                e.printStackTrace();
            }
        }

        deleteEmptyElements(ids.getDocumentId(), ids.getWorkspaceId(), ids.getElementId());
        return ids;
    }

    private OnshapeIds translatePartFile(PDMFile pdmFile) {
        OnshapeIds ids = getOnshapeIds(pdmFile);
        if(ids == null) {
            ids = checkAndCreateDocument(pdmFile);

            File sldprtFile = pdmFileService.getFile(pdmFile.getId());
            System.out.println("Uploading " + sldprtFile.getPath());
            String contentDispString = ContentDisposition.type("attachment")
                    .fileName(pdmFile.getName())
                    .modificationDate(new Date(sldprtFile.lastModified()))
                    .size(sldprtFile.length())
                    .build()
                    .toString();

            try {
                String formatName = "ONSHAPE";
                TranslationsCreateTranslationResponse response = onshape.translations()
                        .createTranslation()
                        .createComposite(true)
                        .file(new Blob(new FileInputStream(sldprtFile), contentDispString))
                        .yAxisIsUp(Boolean.FALSE)
                        .storeInDocument(true)
                        .formatName(formatName)
                        .flattenAssemblies(Boolean.FALSE)
                        .call(ids.getDocumentId(), ids.getWorkspaceId());

                updateOnshapeIds(pdmFile, response.getId());
                ids = getOnshapeIds(pdmFile);
            } catch (Exception e) {
                throw new CassiniException(e.getMessage());
            }
        }

        return ids;
    }

    private void checkAndCreateOnshapeAttributes() {
        String[] attNames = {ONSHAPE_VAULT_ID_ATT, ONSHAPE_FOLDER_ID_ATT, ONSHAPE_DOCUMENT_ID_ATT,
                ONSHAPE_WORKSPACE_ID_ATT, ONSHAPE_ELEMENT_ID_ATT, ONSHAPE_VERSION_ID_ATT, PART_REFERENCES_ATT};
        for (String attName : attNames) {
            ObjectTypeAttribute oAtt = null;
            if(attName.equalsIgnoreCase(ONSHAPE_VAULT_ID_ATT)) {
                oAtt = objectTypeAttributeRepository.findByNameAndObjectType(attName, com.cassinisys.platform.model.core.ObjectType.valueOf("VAULT"));
                if(oAtt == null) {
                    oAtt = new ObjectTypeAttribute();
                }
                oAtt.setObjectType(ObjectType.VAULT);
            }
            else if(attName.equalsIgnoreCase(ONSHAPE_FOLDER_ID_ATT)) {
                oAtt = objectTypeAttributeRepository.findByNameAndObjectType(attName, com.cassinisys.platform.model.core.ObjectType.valueOf("FOLDER"));
                if(oAtt == null) {
                    oAtt = new ObjectTypeAttribute();
                }
                oAtt.setObjectType(ObjectType.FOLDER);
            }
            else {
                oAtt = objectTypeAttributeRepository.findByNameAndObjectType(attName, com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
                if(oAtt == null) {
                    oAtt = new ObjectTypeAttribute();
                }
                oAtt.setObjectType(ObjectType.FILE);
            }
            if(oAtt != null) {
                oAtt.setName(attName);
                oAtt.setDataType(DataType.STRING);
                objectTypeAttributeService.create(oAtt);
            }
        }
    }

    private OnshapeIds getOnshapeIds(PDMFile pdmFile) {
        OnshapeIds ids = null;

        ObjectTypeAttribute oAttDoc = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_DOCUMENT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        ObjectTypeAttribute oAttWork = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_WORKSPACE_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        ObjectTypeAttribute oAttElem = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_ELEMENT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        ObjectTypeAttribute oAttVer = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VERSION_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));

        if(oAttDoc != null && oAttWork != null && oAttElem != null && oAttVer != null) {
            try {
                ObjectAttribute attValueDoc = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFile.getId(), oAttDoc.getId());
                ObjectAttribute attValueWork = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFile.getId(), oAttWork.getId());
                ObjectAttribute attValueElem = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFile.getId(), oAttElem.getId());
                ObjectAttribute attValueVer = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFile.getId(), oAttVer.getId());

                if(attValueDoc != null && attValueWork != null && attValueElem != null) {
                    ids = new OnshapeIds();
                    ids.setDocumentId(attValueDoc.getStringValue());
                    ids.setWorkspaceId(attValueWork.getStringValue());
                    ids.setElementId(attValueElem.getStringValue());
                    if(attValueVer != null) {
                        ids.setVersionId(attValueVer.getStringValue());
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return ids;
    }

    private String createOnshapeFolder(PDMFolder pdmFolder) {
        String folderId = null;
        checkAndCreateOnshapeAttributes();

        folderId = getOnshapeFolderIdAtt((pdmFolder));
        if(folderId == null) {
            String parentFolderId = null;
            String path = pdmFolder.getIdPath();
            String paths[] = path.split("/");
            for(int i=0; i<paths.length; i++) {
                if(i == 0) {
                    parentFolderId = checkAndCreateVaultFolder(Integer.parseInt(paths[i]));
                }
                else {
                    parentFolderId = checkAndCreateFolder(parentFolderId, Integer.parseInt(paths[i]));
                }
            }

            folderId = parentFolderId;
        }

        return folderId;
    }

    private String checkAndCreateFolder(String parentFolder, Integer pdmFolderId) {
        PDMFolder pdmFolder = folderRepository.findOne(pdmFolderId);
        String folderId = getOnshapeFolderIdAtt(pdmFolder);
        if(folderId == null) {
            folderId = createOnshapeFolder(parentFolder, pdmFolder.getName());
            setOnshapeFolderIdAtt(pdmFolder, folderId);
        }

        return folderId;
    }

    private String checkAndCreateVaultFolder(Integer id) {
        PDMVault pdmVault = vaultRepository.findOne(id);
        String folderId = getOnshapeVaultFolderIdAtt(pdmVault);
        if(folderId == null) {
            folderId = createOnshapeFolder(null, pdmVault.getName());
            setOnshapeVaultFolderIdAtt(pdmVault, folderId);
        }

        return folderId;
    }


    private String getOnshapeFolderIdAtt(PDMFolder pdmFolder) {
        String folderId = null;
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_FOLDER_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FOLDER"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFolder.getId(), oAtt.getId());
            if(attValue != null) {
                folderId = attValue.getStringValue();
            }
        }

        return folderId;
    }

    private String getOnshapeVaultFolderIdAtt(PDMVault pdmVault) {
        String folderId = null;
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VAULT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("VAULT"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmVault.getId(), oAtt.getId());
            if(attValue != null) {
                folderId = attValue.getStringValue();
            }
        }

        return folderId;
    }

    private void setOnshapeVaultFolderIdAtt(PDMVault pdmVault, String onshapeId) {
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_VAULT_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("VAULT"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmVault.getId(), oAtt.getId());
            if(attValue == null) {
                attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(pdmVault.getId(), oAtt.getId()));
                attValue.setStringValue(onshapeId);
                objectAttributeService.createAttribute(attValue);
            }
            else {
                attValue.setStringValue(onshapeId);
                objectAttributeRepository.save(attValue);
            }
        }
    }

    private void setOnshapeFolderIdAtt(PDMFolder pdmFolder, String onshapeId) {
        ObjectTypeAttribute oAtt = objectTypeAttributeRepository.findByNameAndObjectType(ONSHAPE_FOLDER_ID_ATT,
                com.cassinisys.platform.model.core.ObjectType.valueOf("FOLDER"));
        if(oAtt != null) {
            ObjectAttribute attValue = objectAttributeRepository.findByObjectIdAndAttributeDefId(pdmFolder.getId(), oAtt.getId());
            if(attValue == null) {
                attValue = new ObjectAttribute();
                attValue.setId(new ObjectAttributeId(pdmFolder.getId(), oAtt.getId()));
                attValue.setStringValue(onshapeId);
                objectAttributeService.createAttribute(attValue);
            }
            else {
                attValue.setStringValue(onshapeId);
                objectAttributeRepository.save(attValue);
            }
        }
    }

    private String createOnshapeFolder(String parentId, String name) {
        String folderId = null;
        FoldersCreateFolderRequest request = new FoldersCreateFolderRequest(onshape);
        request.setOwnerType("1");
        request.setOwnerId("58b5c529b9bece0ef9e9e55c");
        request.setName(name);
        request.setParentId(parentId);
        try {
            FoldersCreateFolderResponse response = request.call();
            folderId = response.getId();
        } catch (OnshapeException e) {
            e.printStackTrace();
        }

        return folderId;
    }

    public List<ObjectAttribute> saveFileProperties(@PathVariable Integer id, @RequestBody List<FileProperty> properties) {
        List<ObjectAttribute> attributes = new ArrayList<>();
        for(FileProperty prop : properties) {
            attributes.add(checkAndCreateFileAttribute(id, prop));
        }
        return attributes;
    }

    private ObjectAttribute checkAndCreateFileAttribute(Integer id, FileProperty property) {
        ObjectTypeAttribute objectTypeAttribute = objectTypeAttributeRepository.findByNameAndObjectType(property.getName(),
                com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
        if(objectTypeAttribute == null) {
            objectTypeAttribute = new ObjectTypeAttribute();
            objectTypeAttribute.setName(property.getName());
            objectTypeAttribute.setObjectType(com.cassinisys.platform.model.core.ObjectType.valueOf("FILE"));
            switch (property.getDataType()) {
                case Text:
                    objectTypeAttribute.setDataType(DataType.STRING);
                    break;
                case Number:
                    objectTypeAttribute.setDataType(DataType.DOUBLE);
                    break;
                case Boolean:
                    objectTypeAttribute.setDataType(DataType.BOOLEAN);
                    break;
                default:
                    objectTypeAttribute.setDataType(DataType.STRING);
                    break;
            }

            objectTypeAttribute = objectTypeAttributeRepository.save(objectTypeAttribute);

        }

        ObjectAttribute objectAttribute = new ObjectAttribute();
        objectAttribute.setId(new ObjectAttributeId(id, objectTypeAttribute.getId()));
        switch (property.getDataType()) {
            case Text:
                objectAttribute.setStringValue(property.getTextValue());
                break;
            case Number:
                objectAttribute.setDoubleValue(property.getNumberValue());
                break;
            case Boolean:
                objectAttribute.setBooleanValue(property.getBoolValue());
                break;
        }
        objectAttribute = objectAttributeRepository.save(objectAttribute);
        return objectAttribute;
    }

    @Data
    @AllArgsConstructor
    class WDE {
        private String workspace;
        private String document;
        private String element;
    }

}
