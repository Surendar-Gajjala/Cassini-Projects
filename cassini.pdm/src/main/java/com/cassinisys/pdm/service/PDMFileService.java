package com.cassinisys.pdm.service;

import com.cassinisys.pdm.filtering.FileCriteria;
import com.cassinisys.pdm.filtering.FilePredicateBuilder;
import com.cassinisys.pdm.model.*;
import com.cassinisys.pdm.model.QPDMFile;
import com.cassinisys.pdm.repo.CommitRepository;
import com.cassinisys.pdm.repo.FileAttributeRepository;
import com.cassinisys.pdm.repo.FolderRepository;
import com.cassinisys.pdm.repo.PDMFileRepository;
import com.cassinisys.pdm.service.onshape.OnshapeService;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.core.ObjectTypeAttributeRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.ObjectTypeAttributeService;
import com.cassinisys.platform.service.filesystem.FileSystemService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by subramanyamreddy on 14-Feb-17.
 */
@Service
@Transactional
public class PDMFileService implements CrudService<PDMFile, Integer> {

	@Autowired
	private PDMFileRepository fileRepository;

	@Autowired
	private FolderRepository folderRepository;

	@Autowired
	private SessionWrapper sessionWrapper;

	@Autowired
	private FileSystemService fileSystemService;

	@Autowired
	private FileAttributeRepository fileAttributeRepository;

	@Autowired
	private FilePredicateBuilder filePredicateBuilder;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private CommitRepository commitRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private OnshapeService onshapeService;

	@Autowired
    private ObjectTypeAttributeService objectTypeAttributeService;

	@Autowired
	private ObjectTypeAttributeRepository objectTypeAttributeRepository;

	@Override
	public PDMFile create(PDMFile pdmFile) {
		checkNotNull(pdmFile);

		pdmFile = fileRepository.save(pdmFile);
		PDMFolder folder = null;
		if (pdmFile.getFolder() != null) {
			folder = folderRepository.getOne(pdmFile.getFolder());
		}
		if (folder != null) {
			pdmFile.setPath(folder.getPath() + "/" + pdmFile.getName());
			pdmFile.setIdPath(folder.getIdPath() + "/" + pdmFile.getId());
		}

		pdmFile = fileRepository.save(pdmFile);
		return pdmFile;
	}

	public PDMFileAttribute createAttribute(PDMFileAttribute fileAttribute) {
		checkNotNull(fileAttribute);
		fileAttribute.setId(null);
		return fileAttributeRepository.save(fileAttribute);
	}

	@Override
	public PDMFile update(PDMFile pdmFile) {
		checkNotNull(pdmFile);
		return fileRepository.save(pdmFile);
	}

	public PDMFileAttribute updateAttribute(PDMFileAttribute fileAttribute) {
		checkNotNull(fileAttribute);
		return fileAttributeRepository.save(fileAttribute);
	}

	@Override
	public void delete(Integer id) {
		checkNotNull(id);
		PDMFile pdmFile = fileRepository.findOne(id);
		if (pdmFile == null) {
			throw new ResourceNotFoundException();
		}
		fileRepository.delete(id);
	}

	@Override
	public PDMFile get(Integer id) {
		checkNotNull(id);
		return fileRepository.findOne(id);
	}

	@Override
	public List<PDMFile> getAll() {
		return fileRepository.findAll();
	}

	public List<PDMFile> getByFolder(Integer folderId) {
		return fileRepository.findByFolderAndLatestTrue(folderId);
	}

	public Page<PDMFile> findAll(Pageable pageable) {
		return fileRepository.findAll(pageable);
	}

	public List<PDMFile> uploadFiles(Integer commit, Integer folderId, Map<String, MultipartFile> fileMap) {

		checkNotNull(folderId);
		checkNotNull(fileMap);
		PDMFolder pdmFolder = folderRepository.findOne(folderId);
		if (pdmFolder == null) {
			throw new ResourceNotFoundException();
		}
		List<PDMFile> uploaded = new ArrayList<>();
		for (MultipartFile file : fileMap.values()) {
			String name = file.getOriginalFilename();

            if(name.toLowerCase().endsWith(".sldprt") || name.toLowerCase().endsWith(".sldasm")) {
                PDMFile exists = fileRepository.findByVaultAndNameIgnoreCase(pdmFolder.getVault(), name);
                if(exists != null) {
                    uploaded.add(exists);
                    continue;
                }
            }

			PDMFile pdmFile = fileRepository.findByFolderAndNameAndLatestTrueOrderByModifiedDateDesc(folderId, file.getOriginalFilename());
			Integer version = 1;
			if (pdmFile != null) {
				pdmFile.setLatest(false);
				Integer oldVersion = pdmFile.getVersion();
				version = oldVersion + 1;
				fileRepository.save(pdmFile);
			}

			pdmFile = new PDMFile();
			pdmFile.setName(file.getOriginalFilename());
			pdmFile.setFolder(folderId);
			pdmFile.setVault(pdmFolder.getVault());
			pdmFile.setCommit(commit);
			pdmFile.setVersion(version);
			pdmFile.setSize(file.getSize());
			pdmFile.setPath(pdmFolder.getPath() + "/" + pdmFile.getName());
			pdmFile = fileRepository.save(pdmFile);
            pdmFile.setIdPath(pdmFolder.getId() + "/" + pdmFile.getId());
            pdmFile = fileRepository.save(pdmFile);


			String dir = fileSystemService.getCurrentTenantRoot() + File.separator +
					"filesystem" + File.separator + pdmFolder.getId();
			File fDir = new File(dir);
			if (!fDir.exists()) {
				fDir.mkdirs();
			}

			String path = dir + File.separator + pdmFile.getId();
			File savedFile = saveDocumentToDisk(file, path);
			uploaded.add(pdmFile);
		}

		return uploaded;
	}



	public void sendMail(Person person, PDMCommit commit, List<PDMFile> files) {
		String email = person.getEmail();
		if (email != null) {
			Person committedBy = personRepository.findOne(commit.getCreatedBy());

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attr.getRequest();
			StringBuffer url = request.getRequestURL();
			String uri = request.getRequestURI();
			String host = url.substring(0, url.indexOf(uri));
			Map<String, Object> model = new HashMap<>();
			model.put("host", host);
			model.put("cssIncludes", "");
			model.put("files", files);
			model.put("commit", commit);
			model.put("committedBy", committedBy);
			Mail mail = new Mail();
			mail.setMailTo(email);
			mail.setMailSubject("Cassini.PDM Commit");
			mail.setTemplatePath("commitsDateDetails.html");
			mail.setModel(model);
			this.mailService.sendEmail(mail);
		}
	}

	private String getCss(String host) {
		String css = "";

		String[] urls = {
				host + "/app/assets/bower_components/bootstrap/dist/css/bootstrap.min.css",
				host + "/app/assets/template/css/bootstrap-override.css",
				host + "/app/assets/template/css/style.default.css",
				host + "/app/assets/template/css/style.katniss.css"
		};

		try {
			for (String url : urls) {
				css += IOUtils.toString(new URL(url), (Charset) null);
				css += "\n\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return css;
	}

	protected File saveDocumentToDisk(MultipartFile multipartFile, String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
			return file;
		} catch (IOException e) {
			throw new CassiniException();
		}
	}

	@Transactional(readOnly = true)
	public File getFolderFile(Integer folderId, Integer fileId) {
		checkNotNull(folderId);
		checkNotNull(fileId);

		PDMFolder pdmFolder = folderRepository.findOne(folderId);
		if (pdmFolder == null) {
			throw new ResourceNotFoundException();
		}
		PDMFile pdmFile = fileRepository.findOne(fileId);
		if (pdmFile == null) {
			throw new ResourceNotFoundException();
		}

		String path = fileSystemService.getCurrentTenantRoot() + File.separator +
				"filesystem" + File.separator + folderId + File.separator + fileId;
		File file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			throw new ResourceNotFoundException();
		}
	}

    @Transactional(readOnly = true)
    public File getFile(Integer fileId) {
        checkNotNull(fileId);

        PDMFile pdmFile = fileRepository.findOne(fileId);
        if (pdmFile == null) {
            throw new ResourceNotFoundException();
        }

        return getFolderFile(pdmFile.getFolder(), fileId);
    }

	public PDMFile moveFile(Integer fileId, Integer toFolder, PDMFile pdmFile) throws Exception {
		checkNotNull(fileId);
		checkNotNull(toFolder);
		checkNotNull(pdmFile);

		PDMFile file = fileRepository.findOne(fileId);
		PDMFolder sourceFolder = folderRepository.findOne(file.getFolder());
		String path = sourceFolder.getPath();
		Path source = FileSystems.getDefault().getPath(path);

		PDMFolder destinationFolder = folderRepository.findOne(toFolder);
		String path1 = destinationFolder.getPath() + "/" + destinationFolder.getName();

		Path dest = FileSystems.getDefault().getPath(path1);
		Files.move(source, dest, StandardCopyOption.REPLACE_EXISTING);
		return fileRepository.save(pdmFile);
	}

	public List<PDMFile> getAllFileVersions(Integer folderId, String name) {
		List<PDMFile> fileVersionHistories = fileRepository.findAllByFolderAndNameOrderByTimeStampDesc(folderId, name);
		return fileVersionHistories;
	}

	public Page<PDMFile> freeTextSearch(Pageable pageable, FileCriteria fileCriteria) {
		Predicate predicate = filePredicateBuilder.build(fileCriteria, QPDMFile.pDMFile);
		Page<PDMFile> pdmFiles = fileRepository.findAll(predicate, pageable);

		return pdmFiles;
	}

	public Page<PDMFile> freeTextSearchAll(Pageable pageable, FileCriteria fileCriteria) {
		Predicate predicate = filePredicateBuilder.getFreeTextSearchPredicateForAll(fileCriteria, QPDMFile.pDMFile);
		Page<PDMFile> pdmFiles = fileRepository.findAll(predicate, pageable);

		return pdmFiles;
	}

	public PDMFolder getParentFolder(PDMFile pdmFile) {
		Integer parentId = pdmFile.getFolder();
		return folderRepository.findOne(parentId);
	}

	public Boolean setThumbnail(Integer fileId, MultipartHttpServletRequest request) {
		PDMFile pdmFile = fileRepository.findOne(fileId);
		if(pdmFile != null) {
			Map<String, MultipartFile> filesMap = request.getFileMap();
			List<MultipartFile> files = new ArrayList<>(filesMap.values());
			if (files.size() > 0) {
				MultipartFile file = files.get(0);
				try {
					pdmFile.setThumbnail(file.getBytes());
					pdmFile = fileRepository.save(pdmFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}

	public void getThumbnail(Integer fileId, HttpServletResponse response) {
		PDMFile pdmFile = fileRepository.findOne(fileId);
		if(pdmFile != null) {
			InputStream is = new ByteArrayInputStream(pdmFile.getThumbnail());
			try {
				response.setContentType("image/png");
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			throw new CassiniException("File does not exist");
		}
	}
}
