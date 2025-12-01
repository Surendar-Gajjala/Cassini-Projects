package com.cassinisys.erp.service.filesystem;

import com.cassinisys.erp.config.AutowiredLogger;
import com.cassinisys.erp.config.TenantManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
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

    public void initialize() throws FileSystemException{
        String s = env.getProperty(FS_ROOT_PROPERTY);
        if(s == null) {
            s = System.getProperty(FS_ROOT_PROPERTY);
        }

        if(s != null) {
            fsRoot = new File(s);
            if(!fsRoot.exists()) {
                String msg = String.format("Filesystem service failed to initialize. " +
                        "Filesystem root %s does not exist.", fsRoot.getAbsolutePath());
                LOGGER.error(msg);
                throw new FileSystemException(msg);
            }
        }
        else {
            String msg = String.format("Filesystem service failed to initialize. %s property not set.", FS_ROOT_PROPERTY);
            LOGGER.error(msg);
            throw new FileSystemException(msg);
        }
    }

    public File getRoot() {
        return fsRoot;
    }

    public File getTenantRoot(String tenant) throws FileSystemException {
        if(fsRoot == null) {
            String msg = "Filesystem service was not initialized";
            LOGGER.error(msg);
            throw new FileSystemException(msg);
        }

        return new File(fsRoot, tenant);
    }

    public File getCurrentTenantRoot() throws FileSystemException {
        return getTenantRoot(TenantManager.get().getTenantId());
    }

    public File getTenantFile(String tenant, String filePath) throws FileSystemException {
        File root = getTenantRoot(tenant);
        if(root != null) {
            Path path = Paths.get(root.getAbsolutePath(), filePath);
            if(!path.toFile().exists()) {
                String msg = String.format("File %s does not exist in the tenant file system", filePath);
                LOGGER.error(msg);
                throw new FileSystemException(msg);
            }

            return path.toFile();
        }

        return null;
    }

    public File getCurrentTenantFile(String filePath) throws FileSystemException {
        return getTenantFile(TenantManager.get().getTenantId(), filePath);
    }

}
