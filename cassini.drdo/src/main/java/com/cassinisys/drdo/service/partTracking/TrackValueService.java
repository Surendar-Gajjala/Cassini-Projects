package com.cassinisys.drdo.service.partTracking;

import com.cassinisys.drdo.model.partTracking.PartTrackingItems;
import com.cassinisys.drdo.model.partTracking.TrackValue;
import com.cassinisys.drdo.repo.bom.BomRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingItemsRepository;
import com.cassinisys.drdo.repo.partTracking.PartTrackingStepsRepository;
import com.cassinisys.drdo.repo.partTracking.TrackValueRepository;
import com.cassinisys.platform.service.col.AttachmentService;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.security.SessionWrapper;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by Nageshreddy on 08-10-2018.
 */

@Service
public class TrackValueService implements CrudService<TrackValue, Integer> {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Map fileMap = new HashMap();
    static SecureRandom rnd = new SecureRandom();
    @Autowired
    Configuration fmConfiguration;
    @Autowired
    private TrackValueRepository trackValueRepository;
    @Autowired
    private PartTrackingItemsRepository partTrackingItemsRepository;
    @Autowired
    private PersonService personservice;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private BomRepository bomRepository;

    @Autowired
    private PartTrackingStepsRepository partTrackingStepsRepository;

    @Override
    @Transactional(readOnly = false)
    public TrackValue create(TrackValue trackValue) {
        return trackValueRepository.save(trackValue);
    }

    @Override
    @Transactional(readOnly = false)
    public TrackValue update(TrackValue trackValue) {
        return trackValueRepository.save(trackValue);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(Integer integer) {
        trackValueRepository.delete(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackValue get(Integer integer) {
        return trackValueRepository.findOne(integer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackValue> getAll() {
        return trackValueRepository.findAll();
    }

    private String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @Transactional(readOnly = true)
    public List<TrackValue> getByPartTrackingId(List<Integer> partTrackings) {
        List<TrackValue> trackValues = trackValueRepository.findByPartTrackingInOrderByPartTrackingAsc(partTrackings);
        return trackValues;
    }

    @Transactional(readOnly = true)
    public String getExportHtml(List<TrackValue> partTrackings, HttpServletResponse response) {

        Map<String, Object> model = new HashMap<>();
        for (TrackValue trackValue : partTrackings) {
            if (trackValue.getPartTracking() != null) {
                PartTrackingItems trackValuesItem = partTrackingItemsRepository.getOne(trackValue.getPartTracking());
                trackValuesItem.setPartTrackingStepValue(partTrackingStepsRepository.getOne(trackValuesItem.getPartTrackingStep()).getDescription());
                trackValue.setPartTrackingItems(trackValuesItem);
            }
            if (trackValue.getCreatedBy() != null)
                trackValue.setCheckedPersonName(personservice.get(trackValue.getCreatedBy()).getFirstName());
            if (trackValue.getAttachment() != null) {
                trackValue.setAttachmentName(attachmentService.get(trackValue.getAttachment()).getName());
            } else {
                trackValue.setAttachmentName("");
            }
            if (trackValue.getComment() == null)
                trackValue.setComment("");
        }

        model.put("partTrackings", partTrackings);
        model.put("loginPerson", sessionWrapper.getSession().getLogin().getPerson().getFirstName());
        model.put("section", bomRepository.getOne(partTrackings.get(0).getPartTrackingItems().getItem()).getItem());
        model.put("currentDate", new Date());
        String templatePath = "partTrackingClearance/partTrackingReport.html";
        String exportHtmlData = getContentFromTemplate(templatePath, model);
        return exportHtmlData;

    }

    @Transactional(readOnly = true)
    public String getContentFromTemplate(String templatePath, Map<String, Object> model) {
        StringBuffer content = new StringBuffer();

        try {

            fmConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/");
            content.append(FreeMarkerTemplateUtils
                    .processTemplateIntoString(fmConfiguration.getTemplate(templatePath), model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    @Transactional(readOnly = true)
    public String generateReport(Integer bomId, HttpServletResponse response) {

        InputStream is = null;
        String fileId = null;
        Set<Integer> integers = new HashSet();
        List<PartTrackingItems> partTrackingItems = partTrackingItemsRepository.findByItemOrderBySerialNo(bomId);
        for (PartTrackingItems trackValuesItem : partTrackingItems) {
            integers.add(trackValuesItem.getId());
        }

        List<TrackValue> partTrackings = trackValueRepository.findByPartTrackingInOrderByPartTrackingAsc(new ArrayList<>(integers));

        fileId = randomString(8) + ".html";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + randomString(8) + "\"");
            response.setContentType("text/html");

            String htmlResponse = getExportHtml(partTrackings, response);
            response.setContentLength(htmlResponse.getBytes().length);

            is = new ByteArrayInputStream(htmlResponse.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        fileMap.put(fileId, is);
        return fileId;

    }

    public void downloadExportFile(String fileId, HttpServletResponse response) {
        try {
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileId, "UTF-8"));
        } catch (UnsupportedEncodingException var51) {
            response.setHeader("Content-disposition", "attachment; filename=" + fileId);
        }

        try {
            ServletOutputStream var5 = response.getOutputStream();
            IOUtils.copy((InputStream) fileMap.get(fileId), var5);
            var5.flush();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}
