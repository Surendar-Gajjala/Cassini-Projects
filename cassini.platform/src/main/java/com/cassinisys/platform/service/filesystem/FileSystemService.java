package com.cassinisys.platform.service.filesystem;

import com.cassinisys.platform.config.AutowiredLogger;
import com.cassinisys.platform.config.TenantManager;
import com.cassinisys.platform.exceptions.CassiniException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by reddy on 10/17/15.
 */
@Service
@PropertySource({"classpath:application.properties"})
public class FileSystemService {
    private String FS_ROOT_PROPERTY = "cassini.fs.root";

    @AutowiredLogger
    private Logger LOGGER;

    @Autowired
    private Environment env;

    private File fsRoot;

    public FileSystemService() {

    }

    @Transactional
    public void initialize() throws FileSystemException {
        String s = env.getProperty(FS_ROOT_PROPERTY);
        if (s == null) {
            s = System.getProperty(FS_ROOT_PROPERTY);
        }

        if (s != null) {
            fsRoot = new File(s);
            if (!fsRoot.exists()) {
                String msg = String.format("Filesystem service failed to initialize. " +
                        "Filesystem root %s does not exist.", fsRoot.getAbsolutePath());
                LOGGER.error(msg);
                throw new FileSystemException(msg);
            }
        } else {
            String msg = String.format("Filesystem service failed to initialize. %s property not set.", FS_ROOT_PROPERTY);
            LOGGER.error(msg);
            throw new FileSystemException(msg);
        }
    }

    @Transactional
    public File getRoot() {
        return fsRoot;
    }

    @Transactional
    public File getTenantRoot(String tenant) throws FileSystemException {
        if (fsRoot == null) {
            String msg = "Filesystem service was not initialized";
            LOGGER.error(msg);
            throw new FileSystemException(msg);
        }

        return new File(fsRoot, tenant);
    }

    @Transactional
    public File getCurrentTenantRoot() throws FileSystemException {
        return getTenantRoot(TenantManager.get().getTenantId());
    }

    @Transactional
    public File getTenantFolder(String tenant, String folderPath) throws FileSystemException {
        File root = getTenantRoot(tenant);
        if (root != null) {
            Path path = Paths.get(root.getAbsolutePath(), folderPath);
            if (path.toFile().exists() && path.toFile().isDirectory()) {
                return path.toFile();
            }
        }

        return null;
    }

    @Transactional
    public File getCurrentTenantFolder(String folderPath) throws FileSystemException {
        return getTenantFolder(TenantManager.get().getTenantId(), folderPath);
    }

    @Transactional
    public File getTenantFile(String tenant, String filePath) throws FileSystemException {
        File root = getTenantRoot(tenant);
        if (root != null) {
            Path path = Paths.get(root.getAbsolutePath(), filePath);
            if (path.toFile().exists()) {
                return path.toFile();
            }
        }

        return null;
    }

    @Transactional
    public File getCurrentTenantFile(String filePath) throws FileSystemException {
        return getTenantFile(TenantManager.get().getTenantId(), filePath);
    }

    public void saveDocumentToDisk(MultipartFile multipartFile, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
        } catch (IOException e) {
            throw new CassiniException("Failed to save object file to disk. REASON: " + e.getMessage());
        }
    }

    public void deleteDocumentFromDisk(Integer fileId, String path) {
        try {
            path = path + File.separator + fileId.toString();
            File file = new File(path);
            System.gc();
            Thread.sleep(1000);
            FileUtils.deleteQuietly(file);
        } catch (Exception e) {
            throw new CassiniException("Failed to delete file " + path + " REASON: " + e.getMessage());
        }
    }

    public void deleteDocumentFromDiskFolder(Integer fileId, String path) {
        try {
            File file = new File(path);
            System.gc();
            Thread.sleep(1000);
            FileUtils.deleteQuietly(file);
        } catch (Exception e) {
            throw new CassiniException("Failed to delete file " + path + " REASON: " + e.getMessage());
        }
    }

    public File getObjectFilePath(Integer objectId, Integer fileId) {
        String path = getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + objectId;
        path = path + File.separator + fileId;

        return new File(path);
    }

    public File saveObjectFileToDisk(Integer objectId, Integer fileId, MultipartFile multipartFile) {
        String path = getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator + objectId;
        File fDir = new File(path);
        if (!fDir.exists()) {
            fDir.mkdirs();
        }

        path = path + File.separator + fileId;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(file));
            return file;
        } catch (IOException e) {
            throw new CassiniException("Failed to save file to disk. REASON: " + e.getMessage());
        }
    }

    public String getFileSystemPath() {
        String path = getCurrentTenantRoot() + File.separator +
                "filesystem" + File.separator;
        return path;
    }
}

