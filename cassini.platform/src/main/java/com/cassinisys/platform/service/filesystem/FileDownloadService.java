package com.cassinisys.platform.service.filesystem;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by reddy on 12/4/15.
 */
@Service
public class FileDownloadService {
    @Value("classpath:mime.types.txt")
    private Resource reportsResource;

    private MimetypesFileTypeMap mimeTypes = null;

    @Transactional
    public void writeFileContentToResponse(HttpServletResponse response, String fileName, File file) {
        response.setContentType(getMimeType(fileName));
        try {
            String deCodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            response.setHeader("Content-disposition","attachment; filename=" + deCodedFileName);
        } catch (UnsupportedEncodingException e) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        }

        try {
            OutputStream out = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getMimeType(String fileName) {
        try {
            if(mimeTypes == null) {
                mimeTypes = new MimetypesFileTypeMap(reportsResource.getInputStream());
            }

            return mimeTypes.getContentType(fileName);
        } catch (IOException e) {
           return null;
        }
    }
}
