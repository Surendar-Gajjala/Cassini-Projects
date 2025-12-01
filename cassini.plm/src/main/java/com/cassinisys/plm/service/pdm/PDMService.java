package com.cassinisys.plm.service.pdm;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.repo.col.AttachmentRepository;
import com.cassinisys.platform.service.col.AttachmentService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.cassinisys.plm.filtering.PDMAssemblyPredicateBuilder;
import com.cassinisys.plm.filtering.PDMDrawingPredicateBuilder;
import com.cassinisys.plm.filtering.PDMObjectCriteria;
import com.cassinisys.plm.filtering.PDMPartPredicateBuilder;
import com.cassinisys.plm.model.pdm.*;
import com.cassinisys.plm.model.pdm.dto.AssemblyDTO;
import com.cassinisys.plm.model.pdm.dto.ComponentDTO;
import com.cassinisys.plm.model.pdm.dto.PartDTO;
import com.cassinisys.plm.model.plm.*;
import com.cassinisys.plm.repo.pdm.*;
import com.cassinisys.plm.repo.plm.BomRepository;
import com.cassinisys.plm.repo.plm.ItemRepository;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.service.UtilService;
import com.cassinisys.plm.service.plm.BomService;
import com.cassinisys.plm.service.plm.ItemService;
import com.cassinisys.plm.service.plm.ItemTypeService;
import com.itextpdf.text.DocumentException;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class PDMService {
    @AutowiredLogger
    private Logger LOGGER;

    @Autowired
    private PDMObjectRepository pdmObjectRepository;

    @Autowired
    private PDMObjectFileRepository pdmObjectFileRepository;

    @Autowired
    private PDMRevisionMasterRepository pdmRevisionMasterRepository;

    @Autowired
    private PDMAssemblyRepository pdmAssemblyRepository;

    @Autowired
    private PDMPartRepository pdmPartRepository;

    @Autowired
    private PDMDrawingRepository pdmDrawingRepository;

    @Autowired
    private PDMConfigurationRepository pdmConfigurationRepository;

    @Autowired
    private PDMAssemblyConfigurationRepository pdmAssemblyConfigurationRepository;

    @Autowired
    private PDMPartConfigurationRepository pdmPartConfigurationRepository;

    @Autowired
    private PDMDrawingConfigurationRepository pdmDrawingConfigurationRepository;

    @Autowired
    private PDMBOMOccurrenceRepository pdmBOMOccurrenceRepository;

    @Autowired
    private PDMAssemblyBOMOccurrenceRepository pdmAssemblyBOMOccurrenceRepository;

    @Autowired
    private PDMPartBOMOccurrenceRepository pdmPartBOMOccurrenceRepository;

    @Autowired
    private PDMRevisionedObjectRepository pdmRevisionedObjectRepository;

    @Autowired
    private PDMFileVersionRepository pdmFileVersionRepository;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PDMAssemblyPredicateBuilder pdmAssemblyPredicateBuilder;
    @Autowired
    private PDMPartPredicateBuilder pdmPartPredicateBuilder;
    @Autowired
    private PDMDrawingPredicateBuilder pdmDrawingPredicateBuilder;

    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private BomService bomService;
    @Autowired
    private BomRepository bomRepository;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private PDMThumbnailRepository pdmThumbnailRepository;
    @Autowired
    private PDMDrawingTemplateRepository pdmDrawingTemplateRepository;
    @Autowired
    private PDMService pdmService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private UtilService utilService;


    public PDMRevisionMaster createRevisionMaster(PDMRevisionMaster revisionMaster) {
        return pdmRevisionMasterRepository.save(revisionMaster);
    }

    public PDMRevisionMaster updateRevisionMaster(PDMRevisionMaster revisionMaster) {
        return pdmRevisionMasterRepository.save(revisionMaster);
    }

    public PDMRevisionMaster getRevisionMaster(Integer id) {
        return pdmRevisionMasterRepository.findOne(id);
    }

    @PreAuthorize("hasPermission(#assembly,'create')")
    public PDMAssembly createAssembly(PDMAssembly assembly) {
        assembly = pdmAssemblyRepository.save(assembly);
        return assembly;
    }

    @PreAuthorize("hasPermission(#assembly.id ,'edit')")
    public PDMAssembly updateAssembly(PDMAssembly assembly) {
        assembly = pdmAssemblyRepository.save(assembly);
        return assembly;
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMAssembly getAssembly(Integer id) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(id);
        if (pdmAssembly != null) {
            pdmAssembly.getMaster().setPlmItem(itemRepository.findByDesignObject(pdmAssembly.getMaster().getId()));
            pdmAssembly.setPlmItemRevision(itemRevisionRepository.findByDesignObject(pdmAssembly.getId()));
        }
        return pdmAssembly;
    }

    @PreAuthorize("hasPermission(#drawing,'create')")
    public PDMDrawing createDrawing(PDMDrawing drawing) {
        drawing = pdmDrawingRepository.save(drawing);
        return drawing;
    }

    @PreAuthorize("hasPermission(#drawing.id ,'edit')")
    public PDMDrawing updateDrawing(PDMDrawing drawing) {
        drawing = pdmDrawingRepository.save(drawing);
        return drawing;
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMDrawing getDrawing(Integer id) {
        PDMDrawing drawing = pdmDrawingRepository.findOne(id);
        if (drawing != null) {
            drawing.getMaster().setPlmItem(itemRepository.findByDesignObject(drawing.getMaster().getId()));
            drawing.setPlmItemRevision(itemRevisionRepository.findByDesignObject(drawing.getId()));
        }
        return drawing;
    }

    public PDMRevisionedObject updateRevisionedObject(Integer id, PDMRevisionedObject revObject) {
        return pdmRevisionedObjectRepository.save(revObject);
    }

    public PDMRevisionedObject getRevisionedObject(Integer id) {
        PDMRevisionedObject revObject = pdmRevisionedObjectRepository.findOne(id);
        if (revObject != null) {
            revObject.getMaster().setPlmItem(itemRepository.findByDesignObject(revObject.getMaster().getId()));
            revObject.setPlmItemRevision(itemRevisionRepository.findByDesignObject(revObject.getId()));
        }
        return revObject;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMAssembly> getAssemblies(Pageable pageable) {
        return pdmAssemblyRepository.findAll(pageable);
    }

    @PreAuthorize("hasPermission(#id,'delete')")
    public void deleteAssembly(Integer id) {
        pdmAssemblyRepository.delete(id);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMAssembly> getAssemblies(List<Integer> ids) {
        return pdmAssemblyRepository.findByIdIn(ids);
    }

    /* Part methods */
    @PreAuthorize("hasPermission(#part,'create')")
    public PDMPart createPart(PDMPart part) {
        part = pdmPartRepository.save(part);
        return part;
    }

    @PreAuthorize("hasPermission(#part.id ,'edit')")
    public PDMPart updatePart(PDMPart part) {
        part = pdmPartRepository.save(part);
        return part;
    }

    @PostAuthorize("hasPermission(returnObject, 'view')")
    public PDMPart getPart(Integer id) {
        PDMPart pdmPart = pdmPartRepository.findOne(id);
        if (pdmPart != null) {
            pdmPart.getMaster().setPlmItem(itemRepository.findByDesignObject(pdmPart.getMaster().getId()));
            pdmPart.setPlmItemRevision(itemRevisionRepository.findByDesignObject(pdmPart.getId()));
        }
        return pdmPart;
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public Page<PDMPart> getParts(Pageable pageable) {
        return pdmPartRepository.findAll(pageable);
    }

    @PreAuthorize("hasPermission(#id,'delete')")
    public void deletePart(Integer id) {
        pdmPartRepository.delete(id);
    }

    @PostFilter("hasPermission(filterObject,'view')")
    public List<PDMPart> getParts(List<Integer> ids) {
        return pdmPartRepository.findByIdIn(ids);
    }


    /* Assembly Configuration methods */
    public PDMAssemblyConfiguration createAssemblyConfiguration(PDMAssemblyConfiguration assemblyConfiguration) {
        assemblyConfiguration = pdmAssemblyConfigurationRepository.save(assemblyConfiguration);
        return assemblyConfiguration;
    }

    public PDMAssemblyConfiguration updateAssemblyConfiguration(PDMAssemblyConfiguration assemblyConfiguration) {
        assemblyConfiguration = pdmAssemblyConfigurationRepository.save(assemblyConfiguration);
        return assemblyConfiguration;
    }

    public PDMAssemblyConfiguration getAssemblyConfiguration(Integer id) {
        return pdmAssemblyConfigurationRepository.findOne(id);
    }

    public List<PDMAssemblyConfiguration> getAssemblyConfigurations(Integer id) {
        return pdmAssemblyConfigurationRepository.findByAssembly(id);
    }

    public void deleteAssemblyConfiguration(Integer id) {
        pdmAssemblyConfigurationRepository.delete(id);
    }

    public List<PDMAssemblyConfiguration> getAssemblyConfigurations(List<Integer> ids) {
        return pdmAssemblyConfigurationRepository.findByIdIn(ids);
    }

    /* Part Configuration methods */
    public PDMPartConfiguration createPartConfiguration(PDMPartConfiguration partConfiguration) {
        partConfiguration = pdmPartConfigurationRepository.save(partConfiguration);
        return partConfiguration;
    }

    public PDMPartConfiguration updatePartConfiguration(PDMPartConfiguration partConfiguration) {
        partConfiguration = pdmPartConfigurationRepository.save(partConfiguration);
        return partConfiguration;
    }

    public PDMPartConfiguration getPartConfiguration(Integer id) {
        return pdmPartConfigurationRepository.findOne(id);
    }

    public List<PDMPartConfiguration> getPartConfigurations(Integer id) {
        return pdmPartConfigurationRepository.findByPart(id);
    }

    public void deletePartConfiguration(Integer id) {
        pdmPartConfigurationRepository.delete(id);
    }

    public List<PDMAssemblyConfiguration> getPartConfigurations(List<Integer> ids) {
        return pdmAssemblyConfigurationRepository.findByIdIn(ids);
    }

    /* Drawing Configuration methods */
    public PDMDrawingConfiguration createDrawinglyConfiguration(PDMDrawingConfiguration drawingConfiguration) {
        drawingConfiguration = pdmDrawingConfigurationRepository.save(drawingConfiguration);
        return drawingConfiguration;
    }

    public PDMDrawingConfiguration updateDrawingConfiguration(PDMDrawingConfiguration drawingConfiguration) {
        drawingConfiguration = pdmDrawingConfigurationRepository.save(drawingConfiguration);
        return drawingConfiguration;
    }

    public PDMDrawingConfiguration getDrawingConfiguration(Integer id) {
        return pdmDrawingConfigurationRepository.findOne(id);
    }

    public Page<PDMDrawingConfiguration> getDrawingConfigurations(Pageable pageable) {
        return pdmDrawingConfigurationRepository.findAll(pageable);
    }

    public void deleteDrawingConfiguration(Integer id) {
        pdmDrawingConfigurationRepository.delete(id);
    }

    public List<PDMDrawingConfiguration> getDrawingConfigurations(List<Integer> ids) {
        return pdmDrawingConfigurationRepository.findByIdIn(ids);
    }

    /* Assembly BOM Occurrence methods */
    public PDMBOMOccurrence createBOMOccurrence(PDMBOMOccurrence bomOccurrence) {
        bomOccurrence = pdmBOMOccurrenceRepository.save(bomOccurrence);
        return bomOccurrence;
    }

    public PDMBOMOccurrence updateBOMOccurrence(PDMBOMOccurrence bomOccurrence) {
        bomOccurrence = pdmBOMOccurrenceRepository.save(bomOccurrence);
        return bomOccurrence;
    }

    public PDMBOMOccurrence getBOMOccurrence(Integer id) {
        return pdmBOMOccurrenceRepository.findOne(id);
    }

    public void deleteBOMOccurrence(Integer id) {
        pdmBOMOccurrenceRepository.delete(id);
    }

    public List<PDMBOMOccurrence> getBOMOccurrences(List<Integer> ids) {
        return pdmBOMOccurrenceRepository.findByIdIn(ids);
    }

    /* Get BOM Occurrences by parent */
    public List<PDMBOMOccurrence> getBOMOccurrencesByParent(Integer configId) {
        return pdmBOMOccurrenceRepository.findByParent(configId);
    }

    public void addBomOccurrences(Integer assyId, AssemblyDTO assemblyDTO) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(assyId);
        PDMAssemblyConfiguration assemblyConfiguration = pdmAssemblyConfigurationRepository.findOne(pdmAssembly.getDefaultConfiguration());
        List<PDMBOMOccurrence> bomOccurrences = pdmBOMOccurrenceRepository.findByParent(assemblyConfiguration.getId());
        pdmBOMOccurrenceRepository.delete(bomOccurrences);

        List<ComponentDTO> children = assemblyDTO.getChildren();
        for (ComponentDTO child : children) {
            PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(child.getVersion());
            if (child instanceof AssemblyDTO) {
                AssemblyDTO assDto = (AssemblyDTO) child;
                PDMAssembly childAssembly = pdmAssemblyRepository.findOne(pdmFileVersion.getAttachedTo());

                PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(childAssembly.getDefaultConfiguration());

                PDMAssemblyBOMOccurrence assemblyBOMOccurrence = new PDMAssemblyBOMOccurrence();
                assemblyBOMOccurrence.setAssembly(config.getId());
                assemblyBOMOccurrence.setParent(assemblyConfiguration.getId());
                assemblyBOMOccurrence.setQuantity(child.getQuantity());
                pdmAssemblyBOMOccurrenceRepository.save(assemblyBOMOccurrence);

                addBomOccurrences(childAssembly.getId(), assDto);
            } else {
                PDMPart childPart = pdmPartRepository.findOne(pdmFileVersion.getAttachedTo());
                PDMPartConfiguration config = pdmPartConfigurationRepository.findOne(childPart.getDefaultConfiguration());

                PDMPartBOMOccurrence partBomOccurrence = new PDMPartBOMOccurrence();
                partBomOccurrence.setPart(config.getId());
                partBomOccurrence.setParent(assemblyConfiguration.getId());
                partBomOccurrence.setQuantity(child.getQuantity());
                pdmPartBOMOccurrenceRepository.save(partBomOccurrence);
            }
        }
    }

    public void addBomOccurrences(Integer assyId, List<ComponentDTO> children) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(assyId);
        PDMAssemblyConfiguration assemblyConfiguration = pdmAssemblyConfigurationRepository.findOne(pdmAssembly.getDefaultConfiguration());
        List<PDMBOMOccurrence> bomOccurrences = pdmBOMOccurrenceRepository.findByParent(assemblyConfiguration.getId());
        pdmBOMOccurrenceRepository.delete(bomOccurrences);

        for (ComponentDTO child : children) {
            PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findOne(child.getVersion());
            if (child instanceof AssemblyDTO) {
                PDMAssembly childAssembly = pdmAssemblyRepository.findOne(pdmFileVersion.getAttachedTo());
                PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(childAssembly.getDefaultConfiguration());

                PDMAssemblyBOMOccurrence assemblyBOMOccurrence = new PDMAssemblyBOMOccurrence();
                assemblyBOMOccurrence.setAssembly(config.getId());
                assemblyBOMOccurrence.setParent(assemblyConfiguration.getId());
                assemblyBOMOccurrence.setQuantity(child.getQuantity());
                pdmAssemblyBOMOccurrenceRepository.save(assemblyBOMOccurrence);
            } else {
                PDMPart childPart = pdmPartRepository.findOne(pdmFileVersion.getAttachedTo());
                PDMPartConfiguration config = pdmPartConfigurationRepository.findOne(childPart.getDefaultConfiguration());

                PDMPartBOMOccurrence partBomOccurrence = new PDMPartBOMOccurrence();
                partBomOccurrence.setPart(config.getId());
                partBomOccurrence.setParent(assemblyConfiguration.getId());
                partBomOccurrence.setQuantity(child.getQuantity());
                pdmPartBOMOccurrenceRepository.save(partBomOccurrence);
            }
        }
    }


    @Transactional(readOnly = true)
    public AssemblyDTO getAssemblyBom(Integer assemblyId) {
        AssemblyDTO assemblyDTO = new AssemblyDTO();

        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(assemblyId);
        if (pdmAssembly != null) {
            assemblyDTO.setAssembly(pdmAssembly);
            List<PDMRevisionedObject> listCollector = new ArrayList<>();
            listCollector.add(pdmAssembly);
            collectBomOccurrences(assemblyDTO, pdmAssembly, listCollector);

            List<Integer> masterIds = new ArrayList<>();
            for (PDMRevisionedObject revObj : listCollector) {
                masterIds.add(revObj.getMaster().getId());
            }

            List<PLMItem> plmItems = itemRepository.findByDesignObjectIn(masterIds);

            Map<Integer, PLMItem> mapItems = new HashMap<>();
            for (PLMItem plmItem : plmItems) {
                mapItems.put(plmItem.getId(), plmItem);
            }

            for (PDMRevisionedObject revObj : listCollector) {
                PLMItem plmItem = mapItems.get(revObj.getMaster().getItemObject());
                if (plmItem != null) {
                    revObj.getMaster().setPlmItem(plmItem);
                }
            }
        }

        return assemblyDTO;
    }

    private void collectBomOccurrences(AssemblyDTO assemblyDTO, PDMAssembly pdmAssembly, List<PDMRevisionedObject> listCollector) {
        Integer config = pdmAssembly.getDefaultConfiguration();
        List<PDMBOMOccurrence> occurrences = pdmBOMOccurrenceRepository.findByParentOrderById(config);
        for (PDMBOMOccurrence occurrence : occurrences) {
            if (occurrence instanceof PDMAssemblyBOMOccurrence) {
                PDMAssemblyBOMOccurrence assOcc = (PDMAssemblyBOMOccurrence) occurrence;
                PDMAssemblyConfiguration childAssyConfig = pdmAssemblyConfigurationRepository.findOne(assOcc.getAssembly());
                PDMAssembly childAssy = pdmAssemblyRepository.findOne(childAssyConfig.getAssembly());

                AssemblyDTO childAssyDTO = new AssemblyDTO();
                childAssyDTO.setId(occurrence.getId());
                childAssyDTO.setAssembly(childAssy);
                childAssyDTO.setQuantity(assOcc.getQuantity());

                collectBomOccurrences(childAssyDTO, childAssy, listCollector);
                assemblyDTO.getChildren().add(childAssyDTO);

                listCollector.add(childAssy);

            } else if (occurrence instanceof PDMPartBOMOccurrence) {
                PDMPartBOMOccurrence partOcc = (PDMPartBOMOccurrence) occurrence;
                PDMPartConfiguration childPartConfig = pdmPartConfigurationRepository.findOne(partOcc.getPart());
                PDMPart childPart = pdmPartRepository.findOne(childPartConfig.getPart());

                PartDTO childPartDTO = new PartDTO();
                childPartDTO.setId(occurrence.getId());
                childPartDTO.setPart(childPart);
                childPartDTO.setQuantity(partOcc.getQuantity());
                assemblyDTO.getChildren().add(childPartDTO);

                listCollector.add(childPart);
            }
        }
    }

    public void assignPartNumbersToAssembly(Integer assemblyId) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(assemblyId);
        if (pdmAssembly != null) {
            Integer itemId = pdmAssembly.getMaster().getItemObject();
            if (itemId == null) {
                PLMItemType itemType = itemTypeService.getItemTypeName("Product");
                if (itemType != null) {
                    PLMItem plmItem = new PLMItem();
                    plmItem.setItemType(itemType);
                    plmItem.setItemName(pdmAssembly.getName());
                    plmItem.setDescription(pdmAssembly.getDescription());
                    plmItem.setMakeOrBuy(MakeOrBuy.MAKE);
                    plmItem.setDesignObject(pdmAssembly.getMaster().getId());
                    plmItem = itemService.create(plmItem);

                    PLMItemRevision plmItemRevision = itemService.getItemRevision(plmItem.getLatestRevision());
                    plmItemRevision.setDesignObject(pdmAssembly.getId());
                    plmItemRevision = itemService.updateRevision(plmItemRevision);

                    PDMRevisionMaster master = pdmAssembly.getMaster();
                    master.setItemObject(plmItem.getId());
                    pdmRevisionMasterRepository.save(master);

                    pdmAssembly.setItemObject(plmItemRevision.getId());
                    pdmAssemblyRepository.save(pdmAssembly);

                    assignPartNumbersToChildren(pdmAssembly);
                }
            }
        }
    }

    private void assignPartNumbersToChildren(PDMAssembly pdmAssembly) {
        Integer config = pdmAssembly.getDefaultConfiguration();
        List<PDMBOMOccurrence> occurrences = pdmBOMOccurrenceRepository.findByParentOrderById(config);
        for (PDMBOMOccurrence occurrence : occurrences) {
            if (occurrence instanceof PDMAssemblyBOMOccurrence) {
                PDMAssemblyBOMOccurrence assOcc = (PDMAssemblyBOMOccurrence) occurrence;
                PDMAssemblyConfiguration childAssyConfig = pdmAssemblyConfigurationRepository.findOne(assOcc.getAssembly());
                PDMAssembly childAssy = pdmAssemblyRepository.findOne(childAssyConfig.getAssembly());
                assignPartNumbersToAssembly(childAssy.getId());
            } else if (occurrence instanceof PDMPartBOMOccurrence) {
                PDMPartBOMOccurrence partOcc = (PDMPartBOMOccurrence) occurrence;
                PDMPartConfiguration childPartConfig = pdmPartConfigurationRepository.findOne(partOcc.getPart());
                PDMPart childPart = pdmPartRepository.findOne(childPartConfig.getPart());

                assignPartNumbersToPart(childPart);
            }
        }
    }

    private void assignPartNumbersToPart(PDMPart pdmPart) {
        Integer itemId = pdmPart.getMaster().getItemObject();
        if (itemId == null) {
            PLMItemType itemType = itemTypeService.getItemTypeName("Part");
            if (itemType != null) {
                PLMItem plmItem = new PLMItem();
                plmItem.setItemType(itemType);
                plmItem.setItemName(pdmPart.getName());
                plmItem.setDescription(pdmPart.getDescription());
                plmItem.setMakeOrBuy(MakeOrBuy.BUY);
                plmItem.setDesignObject(pdmPart.getMaster().getId());
                plmItem = itemService.create(plmItem);

                PLMItemRevision plmItemRevision = itemService.getItemRevision(plmItem.getLatestRevision());
                plmItemRevision.setDesignObject(pdmPart.getId());
                plmItemRevision = itemService.updateRevision(plmItemRevision);

                PDMRevisionMaster master = pdmPart.getMaster();
                master.setItemObject(plmItem.getId());
                pdmRevisionMasterRepository.save(master);

                pdmPart.setItemObject(plmItemRevision.getId());
                pdmPartRepository.save(pdmPart);
            }
        }
    }

    public List<PLMItem> getItemsFromDesignObjects(List<Integer> ids) {
        List<PDMRevisionedObject> revObjs = pdmRevisionedObjectRepository.findByIdIn(ids);
        List<Integer> masterIds = new ArrayList<>();
        for (PDMRevisionedObject revObj : revObjs) {
            masterIds.add(revObj.getMaster().getId());
        }

        return itemRepository.findByDesignObjectIn(masterIds);
    }

    @Transactional(readOnly = true)
    public void getThumbnail(Integer id, HttpServletResponse response) {
        PDMFileVersion pdmFileVersion = pdmFileVersionRepository.findByAttachedTo(id);
        PDMObjectThumbnail pdmThumbnail = pdmThumbnailRepository.findOne(pdmFileVersion.getId());
        if (pdmFileVersion != null && pdmThumbnail != null) {
            InputStream is = new ByteArrayInputStream(pdmThumbnail.getThumbnail());
            try {
                response.setContentType("image/png");
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new CassiniException("File does not exist");
        }
    }

    @Transactional(readOnly = true)
    public void getAssemblyDrawingPDF(Integer id, HttpServletResponse response) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(id);
        if (pdmAssembly != null && pdmAssembly.getDrawingRevision() != null) {
            PDMDrawing pdmDrawing = pdmAssembly.getDrawingRevision();
            if (pdmDrawing.getPdfFile() != null) {
                Attachment attachment = attachmentService.get(pdmDrawing.getPdfFile());
                File file = attachmentService.getAttachmentFile(attachment.getObjectId(), attachment);

                if (file != null) {
                    try {
                        response.setContentType("application/pdf");
                        try {
                            String e = URLDecoder.decode(attachment.getName(), "UTF-8");
                            response.setHeader("Content-disposition", "inline; filename=" + e);
                        } catch (UnsupportedEncodingException var6) {
                            response.setHeader("Content-disposition", "inline; filename=" + attachment.getName());
                        }
                        ServletOutputStream out = response.getOutputStream();
                        IOUtils.copy(new FileInputStream(file), out);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public void downloadPartDrawingPDF(int id, HttpServletResponse response) {
        PDMPart pdmPart = pdmPartRepository.findOne(id);
        if (pdmPart != null && pdmPart.getDrawingRevision() != null) {
            PDMDrawing pdmDrawing = pdmPart.getDrawingRevision();
            generateWatermarkedDrawing(pdmDrawing, response);
        }
    }

    @Transactional(readOnly = true)
    public void downloadAssemblyDrawingPDF(int id, HttpServletResponse response) {
        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(id);
        if (pdmAssembly != null && pdmAssembly.getDrawingRevision() != null) {
            PDMDrawing pdmDrawing = pdmAssembly.getDrawingRevision();
            generateWatermarkedDrawing(pdmDrawing, response);
        }
    }

    private void generateWatermarkedDrawing(PDMDrawing pdmDrawing, HttpServletResponse response) {
        if (pdmDrawing.getPdfFile() != null) {
            Attachment attachment = attachmentService.get(pdmDrawing.getPdfFile());
            File file = attachmentService.getAttachmentFile(attachment.getObjectId(), attachment);

            if (file != null) {
                String user = sessionWrapper.getSession().getLogin().getPerson().getFullName();
                String date = new SimpleDateFormat(CustomDateSerializer.DATEFORMAT).format(new Date());
                try {
                    utilService.writeFileContentToResponse(response, '"' + file.getName() + '"', file,user,date);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Transactional(readOnly = true)
    public void getPartDrawingPDF(Integer id, HttpServletResponse response) {
        PDMPart pdmPart = pdmPartRepository.findOne(id);
        if (pdmPart != null && pdmPart.getDrawingRevision() != null) {
            PDMDrawing pdmDrawing = pdmPart.getDrawingRevision();
            if (pdmDrawing.getPdfFile() != null) {
                Attachment attachment = attachmentService.get(pdmDrawing.getPdfFile());
                File file = attachmentService.getAttachmentFile(attachment.getObjectId(), attachment);

                if (file != null) {
                    try {
                        response.setContentType("application/pdf");
                        try {
                            String e = URLDecoder.decode(attachment.getName(), "UTF-8");
                            response.setHeader("Content-disposition", "inline; filename=" + e);
                        } catch (UnsupportedEncodingException var6) {
                            response.setHeader("Content-disposition", "inline; filename=" + attachment.getName());
                        }
                        ServletOutputStream out = response.getOutputStream();
                        IOUtils.copy(new FileInputStream(file), out);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new ResourceNotFoundException();
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public PDMFileVersion getFileVersionForRevisionedObject(Integer id) {
        return pdmFileVersionRepository.findByAttachedTo(id);
    }


    @Transactional(readOnly = true)
    public List<PDMRevisionedObject> getAssemblyChildren(Integer assemblyId) {
        List<PDMRevisionedObject> children = new ArrayList<>();

        PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(assemblyId);
        PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(pdmAssembly.getDefaultConfiguration());
        List<PDMBOMOccurrence> occs = pdmBOMOccurrenceRepository.findByParent(config.getId());
        for (PDMBOMOccurrence occ : occs) {
            if (occ instanceof PDMAssemblyBOMOccurrence) {
                Integer id = ((PDMAssemblyBOMOccurrence) occ).getAssembly();
                PDMAssemblyConfiguration parentConfig = pdmAssemblyConfigurationRepository.findOne(id);
                PDMAssembly childAssembly = pdmAssemblyRepository.findOne(parentConfig.getAssembly());
                childAssembly.setFileVersion(getFileVersionForRevisionedObject(childAssembly.getId()));
                children.add(childAssembly);
            } else if (occ instanceof PDMPartBOMOccurrence) {
                Integer id = ((PDMPartBOMOccurrence) occ).getPart();
                PDMPartConfiguration parentConfig = pdmPartConfigurationRepository.findOne(id);
                PDMPart pdmPart = pdmPartRepository.findOne(parentConfig.getPart());
                pdmPart.setFileVersion(getFileVersionForRevisionedObject(pdmPart.getId()));
                children.add(pdmPart);
            }
        }

        return children;
    }

    @Transactional(readOnly = true)
    public List<PDMRevisionedObject> getWhereUsed(Integer id) {
        List<PDMRevisionedObject> whereUsed = new ArrayList<>();

        PDMRevisionedObject revObject = pdmRevisionedObjectRepository.findOne(id);
        if (revObject instanceof PDMAssembly) {
            PDMAssembly pdmAssembly = (PDMAssembly) revObject;
            List<PDMAssemblyBOMOccurrence> occs = pdmAssemblyBOMOccurrenceRepository.findByAssembly(pdmAssembly.getDefaultConfiguration());
            for (PDMAssemblyBOMOccurrence occ : occs) {
                PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(occ.getParent());
                pdmAssembly = pdmAssemblyRepository.findOne(config.getAssembly());
                pdmAssembly.setFileVersion(getFileVersionForRevisionedObject(pdmAssembly.getId()));
                whereUsed.add(pdmAssembly);
            }
        } else if (revObject instanceof PDMPart) {
            PDMPart pdmPart = (PDMPart) revObject;
            List<PDMPartBOMOccurrence> occs = pdmPartBOMOccurrenceRepository.findByPart(pdmPart.getDefaultConfiguration());
            for (PDMPartBOMOccurrence occ : occs) {
                PDMAssemblyConfiguration config = pdmAssemblyConfigurationRepository.findOne(occ.getParent());
                PDMAssembly pdmAssembly = pdmAssemblyRepository.findOne(config.getAssembly());
                pdmAssembly.setFileVersion(getFileVersionForRevisionedObject(pdmAssembly.getId()));
                whereUsed.add(pdmAssembly);
            }
        }

        return whereUsed;
    }

    @Transactional(readOnly = true)
    public Page<PDMAssembly> getAssembliesByPageable(Pageable pageable, PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmAssemblyPredicateBuilder.build(objectCriteria, QPDMAssembly.pDMAssembly);
        Page<PDMAssembly> assemblies = pdmAssemblyRepository.findAll(predicate, pageable);
        return assemblies;
    }

    @Transactional(readOnly = true)
    public Page<PDMPart> getPartsByPageable(Pageable pageable, PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmPartPredicateBuilder.build(objectCriteria, QPDMPart.pDMPart);
        Page<PDMPart> parts = pdmPartRepository.findAll(predicate, pageable);
        return parts;
    }

    @Transactional(readOnly = true)
    public Page<PDMDrawing> getDrawingsByPageable(Pageable pageable, PDMObjectCriteria objectCriteria) {
        Predicate predicate = pdmDrawingPredicateBuilder.build(objectCriteria, QPDMDrawing.pDMDrawing);
        Page<PDMDrawing> parts = pdmDrawingRepository.findAll(predicate, pageable);
        return parts;
    }

    @Transactional
    public PLMItemRevision createEBOM(Integer revisionedId) {
        PDMRevisionedObject revObject = pdmRevisionedObjectRepository.findOne(revisionedId);
        if (revObject != null && revObject.getItemObject() != null) {
            PLMItemRevision plmItemRevision = itemRevisionRepository.findOne(revObject.getItemObject());
            if (plmItemRevision != null && revObject instanceof PDMAssembly) {
                PDMAssembly pdmAssembly = (PDMAssembly) revObject;
                Integer config = pdmAssembly.getDefaultConfiguration();
                List<PDMBOMOccurrence> occurrences = pdmBOMOccurrenceRepository.findByParentOrderById(config);

                List<PLMBom> bomItems = new ArrayList<>();

                for (PDMBOMOccurrence occurrence : occurrences) {
                    PLMItem bomPlmItem = null;

                    if (occurrence instanceof PDMAssemblyBOMOccurrence) {
                        PDMAssemblyBOMOccurrence assOcc = (PDMAssemblyBOMOccurrence) occurrence;
                        PDMAssemblyConfiguration childAssyConfig = pdmAssemblyConfigurationRepository.findOne(assOcc.getAssembly());
                        PDMAssembly childAssy = pdmAssemblyRepository.findOne(childAssyConfig.getAssembly());

                        PLMItemRevision assyItem = createEBOM(childAssy.getId());
                        if (assyItem != null) {
                            bomPlmItem = itemRepository.findOne(assyItem.getItemMaster());
                        }
                    } else if (occurrence instanceof PDMPartBOMOccurrence) {
                        PDMPartBOMOccurrence partOcc = (PDMPartBOMOccurrence) occurrence;
                        PDMPartConfiguration childPartConfig = pdmPartConfigurationRepository.findOne(partOcc.getPart());
                        PDMPart childPart = pdmPartRepository.findOne(childPartConfig.getPart());

                        PLMItemRevision childItemRev = itemRevisionRepository.findOne(childPart.getItemObject());
                        if (childItemRev != null) {
                            bomPlmItem = itemRepository.findOne(childItemRev.getItemMaster());
                        }
                    }

                    if (bomPlmItem != null) {
                        PLMBom bomItem = new PLMBom();
                        bomItem.setItem(bomPlmItem);
                        bomItem.setParent(plmItemRevision);
                        bomItem.setQuantity(occurrence.getQuantity());
                        bomItems.add(bomItem);
                    }
                }

                if (bomItems.size() > 0) {
                    bomService.create(plmItemRevision.getId(), bomItems);
                }

                return plmItemRevision;
            }
        }

        return null;
    }

    @Transactional
    public PDMDrawingTemplate createDrawingTemplate(PDMDrawingTemplate drawingTemplate) {
        drawingTemplate = pdmDrawingTemplateRepository.save(drawingTemplate);
        return drawingTemplate;
    }

    @Transactional
    public PDMDrawingTemplate updateDrawingTemplate(Integer id, PDMDrawingTemplate drawingTemplate) {
        drawingTemplate = pdmDrawingTemplateRepository.save(drawingTemplate);
        return drawingTemplate;
    }

    @Transactional
    public void deleteDrawingTemplate(Integer id) {
        pdmDrawingTemplateRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public PDMDrawingTemplate getDrawingTemplate(Integer id) {
        return pdmDrawingTemplateRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<PDMDrawingTemplate> getDrawingTemplates() {
        List<PDMDrawingTemplate> drawingTemplates = pdmDrawingTemplateRepository.findAllByOrderByCreatedDateDesc();
        drawingTemplates.forEach(drawingTemplate -> {
            if (drawingTemplate.getTemplateFile() != null) {
                Attachment attachment = attachmentRepository.findOne(drawingTemplate.getTemplateFile());
                drawingTemplate.setAttachmentId(attachment.getId());
                drawingTemplate.setAttachmentName(attachment.getName());
            }
        });
        return drawingTemplates;
    }

}
