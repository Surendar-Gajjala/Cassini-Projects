package com.cassinisys.platform.service.col;

import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Location;
import com.cassinisys.platform.model.col.Media;
import com.cassinisys.platform.model.col.MediaType;
import com.cassinisys.platform.repo.col.LocationRepository;
import com.cassinisys.platform.repo.col.MediaRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by swapna on 23/11/18.
 */
@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Value("classpath:mime.types.txt")
    private Resource reportsResource;

    private MimetypesFileTypeMap mimeTypes = null;

    @Transactional
    public List<Media> uploadMedia(Integer objectId, MultipartHttpServletRequest request, Location location) throws Exception {
        List<Media> projectMedias = new ArrayList<>();
        Map<String, MultipartFile> map = request.getFileMap();
        List<MultipartFile> files = new ArrayList<>(map.values());
        Location uploadLocation = null;

        if (location != null && (location.getLatitude() != null && location.getLongitude() != null)) {
            uploadLocation = new Location();
            uploadLocation.setLatitude(location.getLatitude());
            uploadLocation.setLongitude(location.getLongitude());
            uploadLocation.setUploadFrom(location.getUploadFrom());

            uploadLocation = locationRepository.save(uploadLocation);
        }

        if (files.size() > 0) {
            for (MultipartFile file : files) {
                String fname = file.getOriginalFilename();
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                try {
                    byte[] picData = file.getBytes();

                    Media media = new Media();

                    media.setFileName(fname);
                    media.setData(picData);
                    media.setExtension(extension);
                    media.setObjectId(objectId);
                    if (location != null && location.getDescription() != null) {
                        media.setDescription(location.getDescription());
                    }
                    if (extension.equalsIgnoreCase("JPEG") || extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("JPG") || extension.equalsIgnoreCase("PNG") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("GIF") || extension.equalsIgnoreCase("gif")) {
                        media.setMediaType(MediaType.IMAGE);
                    } else if (extension.equalsIgnoreCase("mp4")) {
                        media.setMediaType(MediaType.VIDEO);
                    }

                    if (uploadLocation != null) {
                        media.setLocation(uploadLocation);
                    }

                    mediaRepository.save(media);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return projectMedias;
    }

    @Transactional(readOnly = true)
    public List<Media> getMediaByObjectId(Integer objectId) {
        return mediaRepository.findByObjectIdOrderByCreatedDateDesc(objectId);
    }

    @Transactional(readOnly = true)
    public void getMediaById(Integer mediaId, HttpServletResponse response) {
        Media media = mediaRepository.findOne(mediaId);
        if (media != null) {
            response.setContentType(getMimeType(media.getFileName()));
            try {
                response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(media.getFileName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                response.setHeader("Content-disposition", "attachment; filename=" + media.getFileName());
            }

            try {
                InputStream is = new ByteArrayInputStream(media.getData());
                OutputStream out = response.getOutputStream();
                IOUtils.copy(is, response.getOutputStream());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getMimeType(String fileName) {
        try {
            if (mimeTypes == null) {
                mimeTypes = new MimetypesFileTypeMap(reportsResource.getInputStream());
            }

            return mimeTypes.getContentType(fileName);
        } catch (IOException e) {
            return null;
        }
    }

    public void deleteMedia(Integer mediaId) {
        mediaRepository.delete(mediaId);
    }

    @Transactional(readOnly = true)
    public void previewFile(Integer id, HttpServletResponse response) throws Exception {
        Media media = mediaRepository.findOne(id);
        if (media != null) {
            InputStream is = new ByteArrayInputStream(media.getData());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
